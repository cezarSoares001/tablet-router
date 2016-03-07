package com.murrow.support;

import com.murrow.network.AdjacencyTableEntry;
import com.murrow.network.LL2P;
import com.murrow.network.RouteTableEntry;
import com.murrow.tabletrouter.R;
import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Iterator;
import java.util.List;

/**
 * Created by Corbin Murrow on 1/21/2016.
 */

public class UIManager
{
    private Activity parentActivity;
    private Context context;
    private Factory factory;

    //private LL2P frame; - necessary? Step 3 in "Modify App's Display" but not seeing it's use
    private TextView lblLL2PdstAddrVal;
    private TextView lblLL2PsrcAddrVal;
    private TextView lblLL2PtypeVal;
    private TextView lblLL2PcrcVal;
    private TextView lblLL2PpayloadVal;

    private EditText etLL2PAddr;
    private EditText etIPAddr;
    private ListView lvAdjTable;
    private Button btnAddAdj;

    private EditText etPayload;

    private ListView lvRouteTable;
    private ListView lvForwardingTable;

    private List<AdjacencyTableEntry> adjacencyList;
    private ArrayAdapter<AdjacencyTableEntry> adjacencyListAdapter;

    private List<RouteTableEntry> routeList;
    private ArrayAdapter<RouteTableEntry> routeListAdapter;

    private List<RouteTableEntry> forwardingList;
    private ArrayAdapter<RouteTableEntry> forwardingListAdapter;

    public UIManager()
    {

    }

    public void getObjectReferences(Factory factory)
    {
        this.factory = factory;
        parentActivity = factory.getParentActivity();
        context = parentActivity.getBaseContext();

        adjacencyList = factory.getLL1Daemon().getAdjacencyList();
        routeList = factory.getLRPDaemon().getRouteList();
        forwardingList = factory.getLRPDaemon().getForwardingList();

        adjacencyListAdapter = new ArrayAdapter<>(parentActivity, android.R.layout.simple_list_item_1, adjacencyList);
        routeListAdapter = new ArrayAdapter<>(parentActivity, android.R.layout.simple_list_item_1, routeList);
        forwardingListAdapter = new ArrayAdapter<>(parentActivity, android.R.layout.simple_list_item_1, forwardingList);

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

        etLL2PAddr = (EditText) parentActivity.findViewById(R.id.etLL2PAddr);
        etIPAddr = (EditText) parentActivity.findViewById(R.id.etIPPAddr);
        btnAddAdj = (Button) parentActivity.findViewById(R.id.btnAddAdj);
        lvAdjTable = (ListView) parentActivity.findViewById(R.id.lvAdjTable);

        etPayload = (EditText) parentActivity.findViewById(R.id.etPayload);

        lvRouteTable = (ListView) parentActivity.findViewById(R.id.lvRouteTable);
        lvForwardingTable = (ListView) parentActivity.findViewById(R.id.lvForwardingTable);

        btnAddAdj.setOnClickListener(addAdjacency);

        lvAdjTable.setAdapter(adjacencyListAdapter);

        lvAdjTable.setOnItemClickListener(sendToLL2P);
        lvAdjTable.setOnItemLongClickListener(removeAdjacency);

        etPayload.setText("Echo Payload");

        lvRouteTable.setAdapter(routeListAdapter);
        lvForwardingTable.setAdapter(forwardingListAdapter);
    }

    private void resetRouteListAdapter()
    {
        routeList = factory.getLRPDaemon().getRouteList();
        routeListAdapter.clear();
        Iterator<RouteTableEntry> it = routeList.iterator();

        while (it.hasNext())
            routeListAdapter.add(it.next());
    }

    private void resetForwardingListAdapter()
    {
        forwardingList = factory.getLRPDaemon().getForwardingList();
        forwardingListAdapter.clear();
        Iterator<RouteTableEntry> it = forwardingList.iterator();

        while (it.hasNext())
            forwardingListAdapter.add(it.next());
    }

    private void resetAdjacencyListAdapter()
    {
        adjacencyList = factory.getLL1Daemon().getAdjacencyList();
        adjacencyListAdapter.clear();
        Iterator<AdjacencyTableEntry> it = adjacencyList.iterator();

        while (it.hasNext())
            adjacencyListAdapter.add(it.next());
    }

    private AdapterView.OnItemLongClickListener removeAdjacency = new AdapterView.OnItemLongClickListener()
    {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
        {
            AdjacencyTableEntry target = adjacencyList.get(position);
            factory.getLL1Daemon().removeAdjacency(target.getLL2PAddr());
            resetAdjacencyListAdapter();
            return false;
        }
    };

    private AdapterView.OnItemClickListener sendToLL2P = new AdapterView.OnItemClickListener()
    {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
        {
            AdjacencyTableEntry target = adjacencyList.get(position);
            factory.getLL2Daemon().sendLL2PEchoRequest(target.getLL2PAddr(), etPayload.getText().toString());
        }
    };

    private View.OnClickListener addAdjacency = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            try
            {
                Integer LL2PVal = Integer.valueOf(etLL2PAddr.getText().toString(), 16);
                String IPVal = etIPAddr.getText().toString();
                factory.getLL1Daemon().setAdjacency(LL2PVal, IPVal);
                etLL2PAddr.setText("");
                etIPAddr.setText("");
                resetAdjacencyListAdapter();
            } catch (Exception e)
            {
                raiseToast("Adjacency not added. Verify input.");
            }
        }
    };
}
