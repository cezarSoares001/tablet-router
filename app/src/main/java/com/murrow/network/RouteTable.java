package com.murrow.network;

import android.app.Activity;
import android.util.Log;

import com.murrow.support.Factory;
import com.murrow.support.NetworkConstants;
import com.murrow.support.UIManager;
import com.murrow.support.Utilities;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by Corbin Murrow on 3/4/2016.
 */
public class RouteTable implements Runnable
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

    public void addOrUpdateEntry(Integer srcAddr, Integer network, Integer distance, Integer nextHop)
    {
        Iterator<RouteTableEntry> it = table.iterator();
        RouteTableEntry tmp;

        NetworkDistancePair ndp = new NetworkDistancePair(network, distance);

        while (it.hasNext())
        {

            tmp = it.next();
            if (tmp.getSourceLL3P().equals(srcAddr) && tmp.getNetworkDistancePair().equals(ndp))
            {
                tmp.updateLastTimeTouched();
                Log.i("Routing Table", "Updated Route Table Entry " + Utilities.padHex(Integer.toHexString(tmp.getSourceLL3P()), NetworkConstants.LL3P_ADDR_LENGTH));
                return;
            }
        }

        table.add(new RouteTableEntry(srcAddr, ndp, nextHop));
        Log.i("Routing Table", "Added Route Table Entry " + Utilities.padHex(Integer.toHexString(srcAddr), NetworkConstants.LL3P_ADDR_LENGTH));

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

    @Override
    public void run()
    {
        this.removeOldRoutes();

        activity.runOnUiThread(new Runnable()
        {
            public void run()
            {
                uiManager.updateRoutingTable();
            }
        });
    }
}
