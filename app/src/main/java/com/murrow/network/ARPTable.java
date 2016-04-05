package com.murrow.network;

import android.util.Log;

import com.murrow.support.NetworkConstants;
import com.murrow.support.Utilities;

import java.util.Iterator;
import java.util.TreeSet;

/**
 * Created by Corbin Murrow on 2/24/2016.
 */
public class ARPTable implements Runnable
{
    private TreeSet<ARPTableEntry> table;

    public ARPTable()
    {
        table = new TreeSet<>();
    }

    public void addEntry(Integer LL2PAddr, Integer LL3PAddr)
    {
        try
        {
            table.add(new ARPTableEntry(LL2PAddr, LL3PAddr));
        } catch (Exception e)
        {
        }
    }

    public Integer getLL2PAddr(Integer LL3PAddr)
    {
        Iterator<ARPTableEntry> it = table.iterator();
        ARPTableEntry tmp;

        while (it.hasNext())
        {
            tmp = it.next();
            if (tmp.getLL2PAddr().equals(LL3PAddr))
            {
                return tmp.getLL3PAddr();
            }
        }

        return null;
    }

    public void removeLL2P(Integer LL2PAddr)
    {
        Iterator<ARPTableEntry> it = table.iterator();
        boolean found = false;
        ARPTableEntry tmp;

        while (it.hasNext() && !found)
        {
            tmp = it.next();
            if (tmp.getLL2PAddr().equals(LL2PAddr))
            {
                found = true;
                table.remove(tmp);
            }
        }
    }

    public void removeLL3P(Integer LL3PAddr)
    {
        Iterator<ARPTableEntry> it = table.iterator();
        boolean found = false;
        ARPTableEntry tmp;

        while (it.hasNext() && !found)
        {
            tmp = it.next();
            if (tmp.getLL3PAddr().equals(LL3PAddr))
            {
                found = true;
                table.remove(tmp);
            }
        }
    }

    public TreeSet<ARPTableEntry> getTableEntries()
    {
        return table;
    }

    public boolean LL2PIsInTable(Integer LL2PAddr)
    {
        Iterator<ARPTableEntry> it = table.iterator();
        ARPTableEntry tmp;

        while (it.hasNext())
        {
            tmp = it.next();
            if (tmp.getLL2PAddr().equals(LL2PAddr))
            {
                return true;
            }
        }

        return false;
    }

    public boolean LL3PIsInTable(Integer LL3PAddr)
    {
        Iterator<ARPTableEntry> it = table.iterator();
        ARPTableEntry tmp;

        while (it.hasNext())
        {
            tmp = it.next();
            if (tmp.getLL3PAddr().equals(LL3PAddr))
            {
                return true;
            }
        }

        return false;
    }

    public void expireAndRemove()
    {
        Iterator<ARPTableEntry> it = table.iterator();
        ARPTableEntry tmp;

        while (it.hasNext())
        {
            tmp = it.next();
            if (tmp.getCurrentAge() > NetworkConstants.ARP_TIMEOUT)
            {
                table.remove(tmp);
                Log.i("ARP Table", "Removed ARP Table Entry " + Utilities.padHex(Integer.toHexString(tmp.getLL2PAddr()), NetworkConstants.LL2P_ADDR_LENGTH));
            }
        }
    }

    public void addOrUpdate(Integer LL2PAddr, Integer LL3PAddr)
    {
        Iterator<ARPTableEntry> it = table.iterator();
        ARPTableEntry tmp;

        while (it.hasNext())
        {
            tmp = it.next();
            if (tmp.getLL3PAddr().equals(LL3PAddr) && tmp.getLL2PAddr().equals(LL2PAddr))
            {
                tmp.updateTime();
                Log.i("ARP Table", "Updated ARP Table Entry " + Utilities.padHex(Integer.toHexString(tmp.getLL2PAddr()), NetworkConstants.LL2P_ADDR_LENGTH));
                return;
            }
        }

        addEntry(LL2PAddr, LL3PAddr);
        Log.i("ARP Table", "Added ARP Table Entry " + Utilities.padHex(Integer.toHexString(LL2PAddr), NetworkConstants.LL2P_ADDR_LENGTH));
    }

    public void touch(Integer LL3PAddr)
    {
        Iterator<ARPTableEntry> it = table.iterator();
        ARPTableEntry tmp;

        while (it.hasNext())
        {
            tmp = it.next();
            if (tmp.getLL3PAddr().equals(LL3PAddr))
            {
                tmp.updateTime();
            }
        }
    }

    @Override
    public void run()
    {
        expireAndRemove();
    }
}
