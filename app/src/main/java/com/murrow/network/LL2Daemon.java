package com.murrow.network;

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

    private Integer localLL2PAddr;

    public LL2Daemon()
    {
        setLocalLL2PAddr(Integer.valueOf(NetworkConstants.MY_LL2P_ADDR, 16));
    }

    public void getObjectReferences(Factory factory)
    {
        ll1Daemon = factory.getLL1Daemon();
        uiManager = factory.getUIManager();
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
        ll1Daemon.sendLL2PFrame(frame);
    }

    public void sendLL2PEchoRequest(Integer dstAddr, String payload)
    {
        LL2P frame = new LL2P(Integer.toHexString(dstAddr), Integer.toHexString(localLL2PAddr), NetworkConstants.TYPE_ECHO_REQUEST, payload);
        ll1Daemon.sendLL2PFrame(frame);
    }

    public void receiveLL2PFrame(LL2P frame)
    {
        if (true) //if isValidFrame(frame)
        {
            if (frame.getDstAddr() == localLL2PAddr)
            {
                switch (frame.getTypeHexString())
                {
                    case NetworkConstants.TYPE_LL3P: break;
                    case NetworkConstants.TYPE_ARP: break;
                    case NetworkConstants.TYPE_LRP: break;
                    case NetworkConstants.TYPE_ECHO_REQUEST: sendEchoReply(frame); break;
                    case NetworkConstants.TYPE_ECHO_REPLY: uiManager.raiseToast("Received echo reply from " + frame.getSrcAddrHexString() + "!"); break;
                    default: uiManager.raiseToast("Frame typed incorrectly: " + frame.getTypeHexString()); break;
                }
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

    /* -------- PRIVATE METHODS -------- */

    private void sendEchoReply(LL2P frame)
    {
        LL2P reply = new LL2P(frame.getSrcAddrHexString(), Integer.toHexString(localLL2PAddr), NetworkConstants.TYPE_ECHO_REPLY, frame.getPayloadString());
        uiManager.updateLL2PDisplay(frame);
        uiManager.raiseToast("Sending Echo Reply to " + reply.getDstAddrHexString());
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
