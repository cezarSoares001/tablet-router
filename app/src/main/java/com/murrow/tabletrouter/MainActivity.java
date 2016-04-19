package com.murrow.tabletrouter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.murrow.network.*;
import com.murrow.support.*;

public class MainActivity extends AppCompatActivity
{

    private Factory factory;
    private UIManager uiManager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        factory = new Factory(this);
        uiManager = factory.getUIManager();
        uiManager.raiseToast("All done!");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id)
        {
            case R.id.optShowIPAddr: uiManager.raiseToast(NetworkConstants.IP_ADDRESS); break;
            case R.id.optSendLL2PFrame: factory.getLL1Daemon().sendLL2PFrame(); break;
            case R.id.optOpenMessenger: uiManager.openMessengerWindow(); break;

            default: break;
        }

        return super.onOptionsItemSelected(item);
    }
}
