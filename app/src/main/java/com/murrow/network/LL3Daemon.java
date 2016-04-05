package com.murrow.network;

import com.murrow.support.Factory;
import com.murrow.support.NetworkConstants;
import com.murrow.support.UIManager;

/**
 * Created by Corbin Murrow on 4/5/2016.
 */
public class LL3Daemon
{
    private ARPDaemon arpDaemon;
    private UIManager uiManager;
    private LL2Daemon ll2Daemon;
    private ForwardingTable forwardingTable;

    public LL3Daemon()
    {

    }

    public void getObjectReferences(Factory factory)
    {
        arpDaemon = factory.getARPDaemon();
        uiManager = factory.getUIManager();
        ll2Daemon = factory.getLL2Daemon();
        forwardingTable = factory.getLRPDaemon().getForwardingTable();
    }

    public void receiveLL3PPacket(byte[] data)
    {
        LL3P packet = new LL3P(data);

        if (packet.getTTL() == 255)
        {
            arpDaemon.touchARPEntry(packet.getSrcAddr());
        }

        if (packet.getDstAddrHex().equals(NetworkConstants.MY_LL3P_ADDR))
        {
            //process it
            uiManager.updateLL3PDisplay(packet);
        } else
        {
            //forward it
            uiManager.raiseToast("Received LL3P Packet for " + packet.getDstAddrHex() + ". Forwarding it on.");
        }
    }

    public void sendLL3PPacket(LL3P packet)
    {
        if (!packet.getSrcAddrHex().equals(NetworkConstants.MY_LL3P_ADDR))
            packet.decrementTTL();

        ll2Daemon.sendLL2PFrame(packet.getPacketBytes(), arpDaemon.getARPTable().getLL2PAddr(packet.getDstAddr()), Integer.valueOf(NetworkConstants.TYPE_LL3P, 16));
    }

    public void sendPayloadToLL3PDestination(Integer dstAddr, byte[] payload)
    {
        LL3P packet = new LL3P();

        packet.setDstAddr(Integer.toHexString(dstAddr));
        packet.setPayload(payload);

        sendLL3PPacket(packet);
    }

    public void ARPUpdate(Integer LL2PAddr, Integer LL3PAddr)
    {
        arpDaemon.addOrUpdate(LL2PAddr, LL3PAddr);
    }


}
