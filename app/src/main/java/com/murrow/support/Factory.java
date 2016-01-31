package com.murrow.support;

import android.app.Activity;
import android.net.Network;

import com.murrow.network.LL2P;

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

    public LL2P getLL2PFrame()
    {
        return frame;
    }

    private void createObjects()
    {
        uiManager = new UIManager();
        constants = new NetworkConstants(parentActivity);
        soundPlayer = new SoundPlayer(parentActivity);
        frame = new LL2P(NetworkConstants.MY_LL2P_ADDR, NetworkConstants.MY_LL2P_ADDR, NetworkConstants.TYPE_LL3P, "Hello, my name is Corbin. I am writing a router that lives on top of UDP.");
        //frame = new LL2P(Utilities.stringToBytes("12345665432198987878Payload"));
    }

    private void getObjectReferences()
    {
        uiManager.getObjectReferences(this);
    }
}
