package com.murrow.tabletrouter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.murrow.support.*;

public class MainActivity extends AppCompatActivity
{

    private Factory myFactory;
    private UIManager uiManager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myFactory = new Factory(this);
        uiManager = myFactory.getUIManager();

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
        if (id == R.id.menuShowIPAddr)
        {
            uiManager.raiseToast(NetworkConstants.IP_ADDRESS);
            uiManager.setLblIPAddrVal(NetworkConstants.IP_ADDRESS);
        }

        if (id == R.id.menuMakeNoise)
        {
            myFactory.getSoundPlayer().playButtonPressSound();
        }

        return super.onOptionsItemSelected(item);
    }
}
