package com.murrow.network;

import com.murrow.support.Factory;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by Corbin Murrow on 2/24/2016.
 */
public class Scheduler
{
    private ScheduledThreadPoolExecutor pool;
    private ARPTable arpTable;
    //private RoutingTable routes;
    //private LRPDaemon lrpDaemon;

    public Scheduler()
    {
        pool = new ScheduledThreadPoolExecutor(3);
    }

    public void getObjectReferences(Factory factory)
    {
        arpTable = factory.getARPDaemon().getARPTable();
        //routes = factory.getRoutingTable();
        //lrpDaemon = factory.getLRPDaemon();

        beginScheduledTasks();
    }

    private void beginScheduledTasks()
    {
        pool.scheduleAtFixedRate(arpTable, 10, 80, TimeUnit.SECONDS);
    }
}
