package com.murrow.network;

import java.net.InetAddress;
import java.util.Iterator;
import java.util.TreeSet;

/**
 * Created by Corbin Murrow on 2/4/2016.
 */
public class AdjacencyTable
{
    private TreeSet<AdjacencyTableEntry> table;

    public AdjacencyTable()
    {
        table = new TreeSet<AdjacencyTableEntry>();
    }

    public void addEntry(Integer LL2PAddr, String IPAddr)
    {
        try
        {
            table.add(new AdjacencyTableEntry(LL2PAddr, IPAddr));
        } catch (Exception e) {}
    }

    public void removeEntry(Integer LL2PAddr)
    {
        Iterator<AdjacencyTableEntry> it = table.iterator();
        boolean found = false;
        AdjacencyTableEntry tmp = null;

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

    public InetAddress getIPforMAC(Integer LL2PAddr)
    {
        Iterator<AdjacencyTableEntry> it = table.iterator();
        AdjacencyTableEntry tmp = null;

        while (it.hasNext())
        {
            tmp = it.next();
            if (tmp.getLL2PAddr().equals(LL2PAddr))
            {
                return tmp.getIPAddr();
            }
        }

        return null;
    }
}
