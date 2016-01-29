package com.murrow.support;

import com.murrow.network.LL2P;
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

    //private LL2P frame; - necessary? Step 3 in "Modify App's Display" but not seeing it's use
    private TextView lblLL2PdstAddrVal;
    private TextView lblLL2PsrcAddrVal;
    private TextView lblLL2PtypeVal;
    private TextView lblLL2PcrcVal;
    private TextView lblLL2PpayloadVal;

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

    public void updateLL2PDisplay(LL2P frame)
    {
        lblLL2PdstAddrVal.setText(frame.getDstAddrHexString());
        lblLL2PsrcAddrVal.setText(frame.getSrcAddrHexString());
        lblLL2PtypeVal.setText(frame.getTypeHexString());
        lblLL2PcrcVal.setText(frame.getCRCHexString());
        lblLL2PpayloadVal.setText(frame.getPayloadString());
    }

    private void setupMainScreen()
    {
        lblLL2PdstAddrVal = (TextView) parentActivity.findViewById(R.id.lblLL2PdstAddrVal);
        lblLL2PsrcAddrVal = (TextView) parentActivity.findViewById(R.id.lblLL2PsrcAddrVal);
        lblLL2PtypeVal = (TextView) parentActivity.findViewById(R.id.lblLL2PtypeVal);
        lblLL2PcrcVal = (TextView) parentActivity.findViewById(R.id.lblLL2PcrcVal);
        lblLL2PpayloadVal = (TextView) parentActivity.findViewById(R.id.lblLL2PpayloadVal);
    }

}
