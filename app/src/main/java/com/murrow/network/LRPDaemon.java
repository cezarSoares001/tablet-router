package com.murrow.network;

import android.app.Activity;

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

    public LRPDaemon()
    {
        routeTable = new RouteTable();
        forwardingTable = new ForwardingTable();

        routeTable.addOrUpdateEntry(0x0C01, 0x0A, 0x01, 0x0A00);
        routeTable.addOrUpdateEntry(0x0D01, 0x0A, 0x02, 0x0B00);
        routeTable.addOrUpdateEntry(0x0E01, 0x0A, 0x04, 0x0D00);
        routeTable.addOrUpdateEntry(0x0B01, 0x0A, 0x05, 0x0E00);
        routeTable.addOrUpdateEntry(0x0E01, 0x0A, 0x06, 0x0F00);
        forwardingTable.addOrUpdateEntry(0x0C01, 0x0A, 0x01, 0x0B00);
        forwardingTable.addOrUpdateEntry(0x0E01, 0x0A, 0x02, 0x0B00);
        forwardingTable.addOrUpdateEntry(0x0F01, 0x0A, 0x04, 0x0D00);
        forwardingTable.addOrUpdateEntry(0x0B01, 0x0A, 0x05, 0x0E00);
        forwardingTable.addOrUpdateEntry(0x0A01, 0x0A, 0x06, 0x0F00);
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

        forwardingTable.addRouteList(getRoutingTableAsList());

        uiManager.updateForwardingTable();
        uiManager.updateRoutingTable();
    }

    @Override
    public void run()
    {
        ArrayList<ARPTableEntry> adjacentRouters = new ArrayList<>();
        adjacentRouters.addAll(arpDaemon.getARPTable().getTableEntries());

        for (ARPTableEntry arp : adjacentRouters)
        {
            routeTable.addOrUpdateEntry(Integer.valueOf(NetworkConstants.MY_LL3P_ADDR, 16), arp.getLL3PAddr(), 1, arp.getLL3PAddr());
        }

        forwardingTable.addRouteList(getRoutingTableAsList());

        uiManager.updateRoutingTable();
        uiManager.updateForwardingTable();

        for (ARPTableEntry arp : adjacentRouters)
        {
            LRP packet = new LRP();
            packet.setSourceAddr(Integer.valueOf(NetworkConstants.MY_LL3P_ADDR, 16));
            packet.setPairList(forwardingTable, arp.getLL3PAddr());

            ll2Daemon.sendLL2PFrame(packet.getBytes(), arp.getLL2PAddr(), Integer.valueOf(NetworkConstants.TYPE_LRP));
        }
    }
}
