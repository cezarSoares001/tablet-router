package com.murrow.network;

import android.os.AsyncTask;
import android.util.Log;

import com.murrow.support.Factory;
import com.murrow.support.NetworkConstants;
import com.murrow.support.UIManager;
import com.murrow.support.Utilities;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by Corbin Murrow on 2/4/2016.
 */
public class LL1Daemon
{
    private final static int port = 49999;

    private LL2P frame;
    private UIManager uiManager;
    private LL2Daemon ll2daemon;
    private AdjacencyTable table;

    private DatagramSocket sendSocket;
    private DatagramSocket receiveSocket;

    public LL1Daemon()
    {
        table = new AdjacencyTable();

        table.addEntry(Integer.valueOf(NetworkConstants.MY_LL2P_ADDR, 16), "127.0.0.1");
        table.addEntry(Integer.valueOf("010203", 16), "192.168.1.4");

        openUDPPorts();
    }

    public void getObjectReferences(Factory factory)
    {
        uiManager = factory.getUIManager();
        frame = factory.getFrame();
        ll2daemon = factory.getLL2Daemon();

        new ListenForUDPPacket().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, receiveSocket);
    }

    public void setAdjacency(Integer LL2PAddr, String IPAddr)
    {
        table.addEntry(LL2PAddr, IPAddr);
        ll2daemon.sendARPUpdate(LL2PAddr);
    }

    public void removeAdjacency(Integer LL2PAddr)
    {
        table.removeEntry(LL2PAddr);
    }

    public AdjacencyTable getAdjacencyTable()
    {
        return table;
    }

    public List<AdjacencyTableEntry> getAdjacencyList()
    {
        List<AdjacencyTableEntry> list = new ArrayList<>();
        list.addAll(table.getTableEntries());
        return list;
    }

    public void sendLL2PFrame()
    {
        sendLL2PFrame(frame);
    }

    public void sendLL2PFrame(LL2P frame)
    {
        InetAddress IPAddr = table.getIPforMAC(frame.getDstAddr());

        if (IPAddr != null)
        {
            DatagramPacket packet = new DatagramPacket(frame.getFrameBytes(), frame.getFrameBytes().length, IPAddr, port);
            Log.i("LL1 Daemon", frame.toString());
            new SendUDPPacket().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, sendSocket, packet);
        } else
            uiManager.raiseToast("Couldn't send frame: " + frame.getDstAddrHexString() + " is not adjacent.");
    }

    public void openUDPPorts()
    {
        sendSocket = null;
        receiveSocket = null;

        try
        {
            sendSocket = new DatagramSocket();
            receiveSocket = new DatagramSocket(port);
        } catch (Exception e) {}
    }

    private class SendUDPPacket extends AsyncTask<Object, Void, Void>
    {
        @Override
        protected Void doInBackground(Object... args)
        {
            DatagramSocket socket = (DatagramSocket)args[0];
            DatagramPacket packet = (DatagramPacket)args[1];

            try
            {
                socket.send(packet);
            } catch (Exception e) {}

            return null;
        }
    }

    private class ListenForUDPPacket extends AsyncTask<DatagramSocket, Void, byte[]>
    {
        @Override
        protected byte[] doInBackground(DatagramSocket... args)
        {
            byte[] rxData = new byte[1024];
            DatagramSocket socket = args[0];

            DatagramPacket rxPacket = new DatagramPacket(rxData, rxData.length);

            try
            {
                socket.receive(rxPacket);
            } catch (Exception e) {}

            int len = rxPacket.getLength();

            return Utilities.stringToBytes(new String(rxPacket.getData()).substring(0, len));
        }

        @Override
        protected void onPostExecute(byte[] bytes)
        {
            ll2daemon.receiveLL2PFrame(bytes);
            new ListenForUDPPacket().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, receiveSocket);
        }
    }
}