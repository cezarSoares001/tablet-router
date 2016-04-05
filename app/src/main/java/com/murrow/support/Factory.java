package com.murrow.support;

import android.app.Activity;
import android.net.Network;

import com.murrow.network.ARPDaemon;
import com.murrow.network.LL1Daemon;
import com.murrow.network.LL2Daemon;
import com.murrow.network.LL2P;
import com.murrow.network.LL3Daemon;
import com.murrow.network.LRPDaemon;
import com.murrow.network.RouteTable;
import com.murrow.network.Scheduler;

import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * Created by Corbin Murrow on 1/21/2016.
 */

public class Factory
{
    private Activity parentActivity;
    private UIManager uiManager;
    private NetworkConstants constants;
    private SoundPlayer soundPlayer;
    private LL2P frame;
    private LL1Daemon ll1Daemon;
    private LL2Daemon ll2Daemon;
    private ARPDaemon arpDaemon;
    private Scheduler scheduler;
    private LRPDaemon lrpDaemon;
    private LL3Daemon ll3Daemon;

    public Factory(Activity callingActivity)
    {
        parentActivity = callingActivity;

        createObjects();
        getObjectReferences();
    }

    public Activity getParentActivity()
    {
        return parentActivity;
    }

    public UIManager getUIManager()
    {
        return uiManager;
    }

    public SoundPlayer getSoundPlayer()
    {
        return soundPlayer;
    }

    public LL2P getFrame()
    {
        return frame;
    }

    public LL1Daemon getLL1Daemon()
    {
        return ll1Daemon;
    }

    public LL2Daemon getLL2Daemon()
    {
        return ll2Daemon;
    }

    public ARPDaemon getARPDaemon()
    {
        return arpDaemon;
    }

    public Scheduler getScheduler()
    {
        return scheduler;
    }

    public LRPDaemon getLRPDaemon()
    {
        return lrpDaemon;
    }

    public LL3Daemon getLL3Daemon()
    {
        return ll3Daemon;
    }

    private void createObjects()
    {
        uiManager = new UIManager();
        constants = new NetworkConstants(parentActivity);
        soundPlayer = new SoundPlayer(parentActivity);
        frame = new LL2P("010203", NetworkConstants.MY_LL2P_ADDR, NetworkConstants.TYPE_ARP, "This is a test frame.");
        ll1Daemon = new LL1Daemon();
        ll2Daemon = new LL2Daemon();
        arpDaemon = new ARPDaemon();
        scheduler = new Scheduler();
        lrpDaemon = new LRPDaemon();
        ll3Daemon = new LL3Daemon();
    }

    private void getObjectReferences()
    {
        uiManager.getObjectReferences(this);
        frame.getObjectReferences(this);
        ll1Daemon.getObjectReferences(this);
        ll2Daemon.getObjectReferences(this);
        ll3Daemon.getObjectReferences(this);
        arpDaemon.getObjectReferences(this);
        scheduler.getObjectReferences(this);
        lrpDaemon.getObjectReferences(this);
    }
}
