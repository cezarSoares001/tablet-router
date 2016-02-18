package com.murrow.network;

import android.support.v4.app.INotificationSideChannel;

import com.murrow.support.NetworkConstants;
import com.murrow.support.Utilities;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by Corbin Murrow on 2/4/2016.
 */
public class AdjacencyTableEntry implements Comparable
{
    private Integer LL2PAddr;
    private String IPAddr;

    public AdjacencyTableEntry()
    {
        LL2PAddr = 0;
        IPAddr = "000.000.000.000";
    }

    public AdjacencyTableEntry(String LL2PAddr, String IPAddr)
    {
        this.LL2PAddr = Integer.valueOf(LL2PAddr, 16);
        this.IPAddr = IPAddr;
    }

    public AdjacencyTableEntry(Integer LL2PAddr, String IPAddr)
    {
        this.LL2PAddr = LL2PAddr;
        this.IPAddr = IPAddr;
    }

    public void setLL2PAddr(Integer LL2PAddr)
    {
        this.LL2PAddr = LL2PAddr;
    }

    public void setIPAddr(String IPAddr)
    {
        this.IPAddr = IPAddr;
    }

    public Integer getLL2PAddr()
    {
        return LL2PAddr;
    }

    public String getIPAddrString()
    {
        return IPAddr;
    }

    public InetAddress getIPAddr()
    {
        InetAddress addr = null;

        try
        {
            addr = InetAddress.getByName(IPAddr);
        } catch (Exception e) {}

        return addr;
    }

    @Override
    public String toString()
    {
        return "LL2P: 0x" + Utilities.padHex(Integer.toHexString(LL2PAddr), NetworkConstants.LL2P_ADDR_LENGTH) + "  IP: " + IPAddr;
    }

    @Override
    public int compareTo(Object another)
    {
        AdjacencyTableEntry tmp = (AdjacencyTableEntry)another;
        return LL2PAddr.compareTo(tmp.getLL2PAddr());
    }
}
