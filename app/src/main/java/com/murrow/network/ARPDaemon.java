package com.murrow.network;

import com.murrow.support.Factory;

/**
 * Created by Corbin Murrow on 2/24/2016.
 */
public class ARPDaemon
{
    private ARPTable table;
    private LL2Daemon ll2Daemon;

    public ARPDaemon()
    {
        table = new ARPTable();
    }

    public void addOrUpdate(Integer LL2PAddr, Integer LL3PAddr)
    {
        table.addOrUpdate(LL2PAddr, LL3PAddr);
    }

    public ARPTable getARPTable()
    {
        return table;
    }

    public void getObjectReferences(Factory factory)
    {
        ll2Daemon = factory.getLL2Daemon();
    }
}
