package com.murrow.support;

import android.app.Activity;
import android.net.Network;

import com.murrow.network.LL1Daemon;
import com.murrow.network.LL2Daemon;
import com.murrow.network.LL2P;

/**
 * Created by Corbin Murrow on 1/21/2016.
 */

public class Factory
{
    private Activity parentActivity;
    private UIManager uiManager;
    private SoundPlayer soundPlayer;
    private LL2P frame;
    private LL1Daemon ll1Daemon;
    private LL2Daemon ll2Daemon;

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

    private void createObjects()
    {
        uiManager = new UIManager();
        soundPlayer = new SoundPlayer(parentActivity);
        frame = new LL2P("010203", NetworkConstants.MY_LL2P_ADDR, NetworkConstants.TYPE_ARP, "This is a test frame.");
        ll1Daemon = new LL1Daemon();
        ll2Daemon = new LL2Daemon();
    }

    private void getObjectReferences()
    {
        uiManager.getObjectReferences(this);
        frame.getObjectReferences(this);
        ll1Daemon.getObjectReferences(this);
        ll2Daemon.getObjectReferences(this);
    }
}
