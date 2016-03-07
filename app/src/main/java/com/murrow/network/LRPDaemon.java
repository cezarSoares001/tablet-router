package com.murrow.network;

import com.murrow.support.Factory;
import java.util.ArrayList;

/**
 * Created by Corbin Murrow on 3/6/2016.
 */
public class LRPDaemon
{
    private RouteTable routeTable;
    private ForwardingTable forwardingTable;

    public LRPDaemon()
    {
        routeTable = new RouteTable();
        forwardingTable = new ForwardingTable();

        routeTable.addEntry(0x0C01, 0x0A, 0x01, 0x0A00);
        routeTable.addEntry(0x0D01, 0x0A, 0x02, 0x0B00);
        routeTable.addEntry(0x0E01, 0x0A, 0x04, 0x0D00);
        routeTable.addEntry(0x0B01, 0x0A, 0x05, 0x0E00);
        routeTable.addEntry(0x0E01, 0x0A, 0x06, 0x0F00);
        forwardingTable.addEntry(0x0C01, 0x0A, 0x01, 0x0B00);
        forwardingTable.addEntry(0x0E01, 0x0A, 0x02, 0x0B00);
        forwardingTable.addEntry(0x0F01, 0x0A, 0x04, 0x0D00);
        forwardingTable.addEntry(0x0B01, 0x0A, 0x05, 0x0E00);
        forwardingTable.addEntry(0x0A01, 0x0A, 0x06, 0x0F00);
    }

    public ArrayList<RouteTableEntry> getRouteList()
    {
        return routeTable.getRoutes();
    }

    public ArrayList<RouteTableEntry> getForwardingList()
    {
        return forwardingTable.getRoutes();
    }

    public void getObjectReferences(Factory factory)
    {
        routeTable.getObjectReferences(factory);
        forwardingTable.getObjectReferences(factory);
    }
}
