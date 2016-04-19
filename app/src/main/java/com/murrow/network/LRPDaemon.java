package com.murrow.network;

import android.app.Activity;
import android.util.Log;

import com.murrow.support.Factory;
import com.murrow.support.NetworkConstants;
import com.murrow.support.UIManager;

import java.util.ArrayList;

/**
 * Created by Corbin Murrow on 3/6/2016.
 */
public class LRPDaemon implements Runnable
{
    private ARPDaemon arpDaemon;
    private RouteTable routeTable;
    private ForwardingTable forwardingTable;
    private UIManager uiManager;
    private Activity parentActivity;
    private LL2Daemon ll2Daemon;
    private Activity activity;

    public LRPDaemon()
    {
        routeTable = new RouteTable();
        forwardingTable = new ForwardingTable();
    }

    public RouteTable getRouteTable()
    {
        return routeTable;
    }

    public ForwardingTable getForwardingTable()
    {
        return forwardingTable;
    }

    public ArrayList<RouteTableEntry> getRoutingTableAsList()
    {
        return routeTable.getRoutes();
    }

    public ArrayList<RouteTableEntry> getForwardingList()
    {
        return forwardingTable.getRoutes();
    }

    public void getObjectReferences(Factory factory)
    {
        arpDaemon = factory.getARPDaemon();
        uiManager = factory.getUIManager();
        parentActivity = factory.getParentActivity();
        ll2Daemon = factory.getLL2Daemon();
        activity = factory.getParentActivity();

        routeTable.getObjectReferences(factory);
        forwardingTable.getObjectReferences(factory);
    }

    public void receiveNewLRP(byte[] lrpPacket, Integer srcAddr)
    {
        LRP lrp = new LRP(lrpPacket);

        arpDaemon.addOrUpdate(srcAddr, lrp.getSourceAddr());

        for(NetworkDistancePair ndp : lrp.getPairList())
        {
            routeTable.addOrUpdateEntry(lrp.getSourceAddr(), ndp.getNetwork(), ndp.getDistance() + 1, lrp.getSourceAddr());
        }

        forwardingTable.reset();
        forwardingTable.addRouteList(getRoutingTableAsList());

        uiManager.updateForwardingTable();
        uiManager.updateRoutingTable();
    }

    @Override
    public void run()
    {
        Log.i("LRP Daemon", "Begin Scheduled LRP Runnable");
        ArrayList<ARPTableEntry> adjacentRouters = new ArrayList<>();
        adjacentRouters.addAll(arpDaemon.getARPTable().getTableEntries());

        for (ARPTableEntry arp : adjacentRouters)
        {
            routeTable.addOrUpdateEntry(Integer.valueOf(NetworkConstants.MY_LL3P_ADDR, 16), arp.getNetwork(), 1, arp.getLL3PAddr());
        }

        forwardingTable.reset();
        forwardingTable.addRouteList(getRoutingTableAsList());

        activity.runOnUiThread(new Runnable()
        {
            public void run()
            {
                uiManager.updateRoutingTable();
                uiManager.updateForwardingTable();
            }
        });

        for (ARPTableEntry arp : adjacentRouters)
        {
            LRP packet = new LRP();
            packet.setSourceAddr(Integer.valueOf(NetworkConstants.MY_LL3P_ADDR, 16));
            packet.setPairList(forwardingTable, arp.getLL3PAddr());

            if (packet.getRouteCount() > 0)
            {
                ll2Daemon.sendLL2PFrame(packet.getBytes(), arp.getLL2PAddr(), Integer.valueOf(NetworkConstants.TYPE_LRP, 16));
                Log.i("LRP Daemon", "Sending LRP info to " + Integer.toHexString(arp.getLL2PAddr()));
            }
        }
        Log.i("LRP Daemon", "End of Scheduled LRP Runnable");
    }
}
