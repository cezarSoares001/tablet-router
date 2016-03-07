package com.murrow.network;

import android.app.Activity;

import com.murrow.support.Factory;
import com.murrow.support.NetworkConstants;
import com.murrow.support.UIManager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by Corbin Murrow on 3/4/2016.
 */
public class RouteTable
{
    UIManager uiManager;
    Activity activity;
    TreeSet<RouteTableEntry> table;

    public RouteTable()
    {
        table = new TreeSet<>();
    }

    public void getObjectReferences(Factory factory)
    {
        uiManager = factory.getUIManager();
        activity = factory.getParentActivity();
    }

    public void addEntry(Integer srcAddr, Integer network, Integer distance, Integer nextHop)
    {
        table.add(new RouteTableEntry(srcAddr, new NetworkDistancePair(network, distance), nextHop));
    }

    public ArrayList<RouteTableEntry> getRoutes()
    {
        ArrayList<RouteTableEntry> list = new ArrayList<>();
        list.addAll(table);
        return list;
    }

    public void removeOldRoutes()
    {
        Iterator<RouteTableEntry> it = table.iterator();
        boolean found = false;
        RouteTableEntry tmp;

        while (it.hasNext() && !found)
        {
            tmp = it.next();
            if (tmp.getCurrentAge() > NetworkConstants.ROUTE_TIMEOUT)
            {
                found = true;
                table.remove(tmp);
            }
        }

        // if something was found, call the function again to see if there are more things. If not, return.
        if (!found)
            return;
        else
            removeOldRoutes();
    }

    public void removeEntry(Integer srcAddr, Integer network)
    {
        Iterator<RouteTableEntry> it = table.iterator();
        boolean found = false;
        RouteTableEntry tmp;

        while (it.hasNext() && !found)
        {
            tmp = it.next();
            if (tmp.getNetworkDistancePair().getNetwork().equals(network) && tmp.getSourceLL3P().equals(srcAddr))
            {
                found = true;
                table.remove(tmp);
            }
        }
    }
}
