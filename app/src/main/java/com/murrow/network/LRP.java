package com.murrow.network;

import com.murrow.support.NetworkConstants;
import com.murrow.support.Utilities;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Corbin Murrow on 3/23/2016.
 */
public class LRP
{
    private Integer sourceAddr;
    private Integer lrpCounter;
    private Integer sequenceNum;
    private Integer routeCount;
    private List<NetworkDistancePair> pairList;

    public LRP()
    {
        pairList = new ArrayList<>();
    }

    public LRP(Integer srcLL3P, ForwardingTable table, Integer dstLL3P)
    {
        setPairList(table, srcLL3P); //or should this be dstLL3P?
        sourceAddr = srcLL3P;
    }

    public LRP(byte[] data)
    {
        pairList = new ArrayList<>();

        String tmpData = Utilities.bytesToString(data);

        sourceAddr = Integer.valueOf(tmpData.substring(0, 4), 16);
        sequenceNum = Integer.valueOf(tmpData.substring(4, 5), 16);
        routeCount = Integer.valueOf(tmpData.substring(5, 6), 16);

        for (int i = 0; i < routeCount; i++)
        {
            Integer net = Integer.valueOf(tmpData.substring(6+i, 7+i), 16);
            Integer dist = Integer.valueOf(tmpData.substring(7+i, 8+i), 16);
            pairList.add(new NetworkDistancePair(net, dist));
        }
    }

    public byte[] getBytes()
    {
        return Utilities.stringToBytes(toString());
    }

    public Integer getRouteCount()
    {
        return routeCount;
    }

    public Integer getSourceAddr()
    {
        return sourceAddr;
    }

    public List<NetworkDistancePair> getPairList()
    {
        return pairList;
    }

    public String getPairListAsString()
    {
        String tmp = "";

        for (NetworkDistancePair ndp : pairList)
        {
            tmp += ndp.toString();
        }

        return tmp;
    }

    public void setSourceAddr(Integer addr)
    {
        sourceAddr = addr;
    }

    public void setPairList(ForwardingTable table, Integer sourceAddr)
    {
        ArrayList<RouteTableEntry> list = table.getFIBExcludingLL3PAddr(sourceAddr);

        for(RouteTableEntry rte : list)
        {
            pairList.add(rte.getNetworkDistancePair());
        }
    }

    @Override
    public String toString()
    {
        return Utilities.padHex(Integer.toHexString(sourceAddr), NetworkConstants.LL3P_ADDR_LENGTH)
                + Integer.toHexString(sequenceNum) + Integer.toHexString(routeCount) + getPairListAsString();
    }
}

