package com.murrow.network;

import com.murrow.support.NetworkConstants;
import com.murrow.support.Utilities;

/**
 * Created by Corbin Murrow on 3/4/2016.
 */
public class NetworkDistancePair
{
    private Integer network;
    private Integer distance;

    public NetworkDistancePair()
    {
        network = 0;
        distance = 0;
    }

    public NetworkDistancePair(Integer network, Integer distance)
    {
        this.network = network;
        this.distance = distance;
    }

    public Integer getNetwork()
    {
        return network;
    }

    public void setNetwork(Integer network)
    {
        this.network = network;
    }

    public Integer getDistance()
    {
        return distance;
    }

    public void setDistance(Integer distance)
    {
        this.distance = distance;
    }

    @Override
    public String toString()
    {
        return Utilities.padHex(Integer.toHexString(network), 2) + Utilities.padHex(Integer.toHexString(distance), 2);
    }
}
