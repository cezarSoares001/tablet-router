package com.murrow.network;

import android.net.Network;

import com.murrow.support.NetworkConstants;
import com.murrow.support.Utilities;

import java.util.Calendar;

/**
 * Created by Corbin Murrow on 3/4/2016.
 */
public class RouteTableEntry implements Comparable<RouteTableEntry>
{
    private Integer sourceLL3P;
    private NetworkDistancePair ndp;
    private long lastTouch;
    private Integer nextHop;

    public RouteTableEntry(Integer sourceLL3P, NetworkDistancePair ndp, Integer nextHop)
    {
        this.sourceLL3P = sourceLL3P;
        this.ndp = ndp;
        this.nextHop = nextHop;
        updateLastTimeTouched();
    }

    public Integer getSourceLL3P()
    {
        return sourceLL3P;
    }

    public void setSourceLL3P(Integer sourceLL3P)
    {
        this.sourceLL3P = sourceLL3P;
    }

    public NetworkDistancePair getNetworkDistancePair()
    {
        return ndp;
    }

    public void setNetworkDistancePair(NetworkDistancePair ndp)
    {
        this.ndp = ndp;
    }

    public long getCurrentAge()
    {
        return Calendar.getInstance().getTimeInMillis()/1000 - lastTouch;
    }

    public void updateLastTimeTouched()
    {
        lastTouch = Calendar.getInstance().getTimeInMillis()/1000;
    }

    public Integer getNextHop()
    {
        return nextHop;
    }

    public void setNextHop(Integer nextHop)
    {
        this.nextHop = nextHop;
    }

    @Override
    public int compareTo(RouteTableEntry another)
    {
        if (ndp.getNetwork().equals(another.getNetworkDistancePair().getNetwork()))
        {
            if (ndp.getDistance().equals(another.getNetworkDistancePair().getDistance()))
                return Utilities.getNetworkFromInteger(sourceLL3P).compareTo(Utilities.getNetworkFromInteger(another.getSourceLL3P()));
            else
                return ndp.getDistance().compareTo(another.getNetworkDistancePair().getDistance());
        } else
            return ndp.getNetwork().compareTo(another.getNetworkDistancePair().getNetwork());
    }

    @Override
    public String toString()
    {
        return "SRC: " + Utilities.padHex(Integer.toHexString(sourceLL3P), NetworkConstants.LL3P_ADDR_LENGTH) + " NDP: " + ndp.toString() + " Next: " + Utilities.padHex(Integer.toHexString(nextHop), NetworkConstants.LL3P_ADDR_LENGTH);
    }
}
