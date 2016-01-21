package com.murrow.support;

import android.app.Activity;
import android.net.Network;

/**
 * Created by Corbin Murrow on 1/21/2016.
 */

public class Factory
{
    private Activity parentActivity;
    private UIManager uiManager;
    private NetworkConstants constants;
    private SoundPlayer soundPlayer;

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

    private void createObjects()
    {
        uiManager = new UIManager();
        constants = new NetworkConstants(parentActivity);
        soundPlayer = new SoundPlayer(parentActivity);
    }

    private void getObjectReferences()
    {
        uiManager.getObjectReferences(this);
    }
}
