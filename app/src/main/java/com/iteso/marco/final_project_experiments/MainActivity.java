package com.iteso.marco.final_project_experiments;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.database.DataSetObserver;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class MainActivity extends ActionBarActivity implements WifiP2pManager.PeerListListener
{
    private WifiP2pManager wifiP2pManager;
    private Channel channel;
    private BroadcastReceiver receiver;
    private IntentFilter intentFilter;
    private ListView listView;
    private Button btnConnect;
    private Collection<WifiP2pDevice> deviceCollection;
    private TextView txtContent;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        wifiP2pManager = (WifiP2pManager)getSystemService(WIFI_P2P_SERVICE);
        channel = wifiP2pManager.initialize(this, getMainLooper(), null);
        receiver = new WifiP2PBroadcastReceiver(wifiP2pManager, channel, this);

        intentFilter = new IntentFilter();
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

        wifiP2pManager.discoverPeers(channel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess()
            {
                //Toast.makeText(getBaseContext(), "Peer discovery successful!", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(int reason)
            {
                //Toast.makeText(getBaseContext(), "Peer discovery failed. Error code " + reason, Toast.LENGTH_LONG).show();
            }
        });

        listView = (ListView)findViewById(R.id.listDevices);
        btnConnect = (Button)findViewById(R.id.btnConnect);
        txtContent = (TextView)findViewById(R.id.txtContent);

        btnConnect.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                connectToPeer();
            }
        });
    }

    private void connectToPeer()
    {
        final WifiP2pDevice[] devices = deviceCollection.toArray(new WifiP2pDevice[deviceCollection.size()]);
        WifiP2pConfig config = new WifiP2pConfig();
        final int port = 8888;


        config.deviceAddress = devices[0].deviceAddress;
        wifiP2pManager.connect(channel, config, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess()
            {
                try
                {
                    //new DataServerAsyncTask().execute(devices[0]);
                    Toast.makeText(getBaseContext(), "Successfully connected to device", Toast.LENGTH_LONG).show();
                    Socket socket = new Socket();
                    socket.bind(null);
                    //socket.connect(new InetSocketAddress(host, port), 500);
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int reason)
            {
                Log.e("Failed to connect to peer " + devices[0].deviceAddress, ". Reason: " + reason);
                Toast.makeText(getBaseContext(), "Failed to connect to device", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void transferData()
    {

    }

    @Override
    protected void onResume()
    {
        super.onResume();
        registerReceiver(receiver, intentFilter);
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        unregisterReceiver(receiver);
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPeersAvailable(WifiP2pDeviceList peers)
    {
        List<String> devices = new ArrayList<String>();
        String [] data;
        deviceCollection = peers.getDeviceList();

        for(WifiP2pDevice device : peers.getDeviceList())
        {
            devices.add(device.deviceName);
            Toast.makeText(this, "Device: " + device.deviceName, Toast.LENGTH_LONG).show();
        }

        data = devices.toArray(new String[devices.size()]);
        listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, data));
    }
}
