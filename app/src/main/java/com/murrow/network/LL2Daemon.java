package com.murrow.network;

import android.util.Log;

import com.murrow.support.Factory;
import com.murrow.support.NetworkConstants;
import com.murrow.support.UIManager;
import com.murrow.support.Utilities;

/**
 * Created by Corbin Murrow on 2/16/2016.
 */

public class LL2Daemon
{
    private LL1Daemon ll1Daemon;
    private UIManager uiManager;
    private ARPDaemon arpDaemon;
    private LRPDaemon lrpDaemon;

    private Integer localLL2PAddr;

    public LL2Daemon()
    {
        setLocalLL2PAddr(Integer.valueOf(NetworkConstants.MY_LL2P_ADDR, 16));
    }

    public void getObjectReferences(Factory factory)
    {
        ll1Daemon = factory.getLL1Daemon();
        uiManager = factory.getUIManager();
        arpDaemon = factory.getARPDaemon();
        lrpDaemon = factory.getLRPDaemon();
    }

    public void setLocalLL2PAddr(Integer addr)
    {
        localLL2PAddr = addr;
    }

    public void sendLL2PFrame(LL2P frame)
    {
        ll1Daemon.sendLL2PFrame(frame);
    }

    public void sendLL2PFrame(byte[] payload, Integer dstAddr, Integer type)
    {
        LL2P frame = new LL2P(Integer.toHexString(dstAddr), Integer.toHexString(localLL2PAddr), Integer.toHexString(type), Utilities.bytesToString(payload));
        sendLL2PFrame(frame);
    }

    public void sendLL2PEchoRequest(Integer dstAddr, String payload)
    {

        LL2P frame = new LL2P(Integer.toHexString(dstAddr), Integer.toHexString(localLL2PAddr), NetworkConstants.TYPE_ECHO_REQUEST, payload);
        sendLL2PFrame(frame);
        uiManager.raiseToast("Sending echo request to " + frame.getDstAddrHexString());
    }

    public void receiveLL2PFrame(LL2P frame)
    {
        Log.i("LL2 Daemon:", "Received frame from " + frame.getSrcAddrHexString() + " of type " + frame.getTypeHexString());

        if (true) //if isValidFrame(frame)
        {
            if (frame.getDstAddr() == localLL2PAddr)
            {
                switch (frame.getTypeHexString())
                {
                    case NetworkConstants.TYPE_LL3P: break;
                    case NetworkConstants.TYPE_ARP: break;
                    case NetworkConstants.TYPE_LRP: lrpDaemon.receiveNewLRP(frame.getPayloadBytes(), frame.getSrcAddr()); break;
                    case NetworkConstants.TYPE_ECHO_REQUEST: sendEchoReply(frame); break;
                    case NetworkConstants.TYPE_ECHO_REPLY: uiManager.raiseToast("Received echo reply from " + frame.getSrcAddrHexString() + "!"); break;
                    case NetworkConstants.TYPE_ARP_UPDATE: arpDaemon.addOrUpdate(frame.getSrcAddr(), Integer.valueOf(frame.getPayloadString(), 16)); sendARPReply(frame.getSrcAddr()); break;
                    case NetworkConstants.TYPE_ARP_REPLY: uiManager.raiseToast("Received ARP reply from " + frame.getSrcAddrHexString() + "!"); break;
                    default: uiManager.raiseToast("Frame typed incorrectly: " + frame.getTypeHexString()); break;
                }

                uiManager.updateLL2PDisplay(frame);
            } else
            {
                uiManager.raiseToast("Received frame for someone else (" + frame.getDstAddrHexString() + ")");
            }
        } else
            uiManager.raiseToast("Invalid Frame, CRC error");
    }

    public void receiveLL2PFrame(byte[] data)
    {
        receiveLL2PFrame(new LL2P(data));
    }

    public void sendARPUpdate(Integer LL2PAddr)
    {
        LL2P frame = new LL2P(Integer.toHexString(LL2PAddr), Integer.toHexString(localLL2PAddr), NetworkConstants.TYPE_ARP_UPDATE, NetworkConstants.MY_LL3P_ADDR);
        sendLL2PFrame(frame);
    }

    public void sendARPReply(Integer LL2PAddr)
    {
        LL2P frame = new LL2P(Integer.toHexString(LL2PAddr), Integer.toHexString(localLL2PAddr), NetworkConstants.TYPE_ARP_REPLY, NetworkConstants.MY_LL3P_ADDR);
        sendLL2PFrame(frame);
        uiManager.raiseToast("Sending ARP reply to " + frame.getDstAddrHexString());
    }

    /*
    Void sendARPReply(Integer LL2PAddress)
    Any time a Layer 2 Daemon receives an “ARP Update” frame it will be able to use the LL3P address in the ARP Update’s payload and the source LL2P Address.  It can use them both to notify the ARP Daemon that this address pair should either be added or have its timeout value updated to the current time.  The Layer 2 Daemon must then send an ARP reply to the original sender informing the sender of the receiver’s LL3P address. The ARP Reply is a standard LL2P Frame with type 0x8007 and this router’s LL3P address as the payload.
    */

    /* -------- PRIVATE METHODS -------- */

    private void sendEchoReply(LL2P frame)
    {
        LL2P reply = new LL2P(frame.getSrcAddrHexString(), Integer.toHexString(localLL2PAddr), NetworkConstants.TYPE_ECHO_REPLY, frame.getPayloadString());
        uiManager.updateLL2PDisplay(frame);
        uiManager.raiseToast("Sending echo reply to " + reply.getDstAddrHexString());
        ll1Daemon.sendLL2PFrame(reply);
    }

    private boolean isValidFrame(LL2P frame)
    {
        LL2P checkFrame = new LL2P(frame.getDstAddrHexString(), frame.getSrcAddrHexString(), frame.getTypeHexString(), frame.getPayloadString());

        if (checkFrame.getCRC() == frame.getCRC())
        {
            return true;
        }

        return false;
    }
}
