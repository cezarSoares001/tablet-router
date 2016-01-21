package com.murrow.support;

import com.murrow.tabletrouter.R;
import android.app.Activity;
import android.content.Context;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Corbin Murrow on 1/21/2016.
 */

public class UIManager
{
    private Activity parentActivity;
    private Context context;
    private Factory myFactory;
    private TextView lblIPAddr;
    private TextView lblIPAddrVal;

    public UIManager()
    {

    }

    public void getObjectReferences(Factory factory)
    {
        myFactory = factory;
        parentActivity = factory.getParentActivity();
        context = parentActivity.getBaseContext();

        setupMainScreen();
    }

    public void raiseToast(String value)
    {
        Toast.makeText(context, value, Toast.LENGTH_SHORT).show();
    }

    private void setupMainScreen()
    {
        lblIPAddr = (TextView) parentActivity.findViewById(R.id.lblIPAddr);
        lblIPAddrVal = (TextView) parentActivity.findViewById(R.id.lblIPAddrVal);
    }

    public void setLblIPAddrVal(String ipAddr)
    {
        lblIPAddrVal.setText(ipAddr);
    }

}
