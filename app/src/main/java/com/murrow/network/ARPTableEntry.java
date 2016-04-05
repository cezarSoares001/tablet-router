package com.murrow.network;

import com.murrow.support.NetworkConstants;
import com.murrow.support.Utilities;

import java.util.Calendar;

/**
 * Created by Corbin Murrow on 2/24/2016.
 */
public class ARPTableEntry implements Comparable<ARPTableEntry>
{
    private Integer LL2PAddr;
    private Integer LL3PAddr;
    private long lastTouch;


    public ARPTableEntry(Integer LL2PAddr, Integer LL3PAddr)
    {
        this.LL2PAddr = LL2PAddr;
        this.LL3PAddr = LL3PAddr;

        updateTime();
    }

    public long getCurrentAge()
    {
        return Calendar.getInstance().getTimeInMillis()/1000 - lastTouch;
    }

    public void updateTime()
    {
        lastTouch = Calendar.getInstance().getTimeInMillis()/1000;
    }

    public Integer getLL2PAddr()
    {
        return LL2PAddr;
    }

    public Integer getLL3PAddr()
    {
        return LL3PAddr;
    }

    public Integer getNetwork()
    {
        String addr = Utilities.padHex(Integer.toHexString(LL3PAddr), NetworkConstants.LL3P_ADDR_LENGTH);
        return Integer.valueOf(addr.substring(0,2), 16);
    }

    @Override
    public int compareTo(ARPTableEntry another)
    {
        return LL2PAddr.compareTo(another.getLL2PAddr());
    }
}
