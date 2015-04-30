package com.iteso.marco.final_project_experiments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.net.wifi.p2p.WifiP2pManager.PeerListListener;


/**
 * Created by marco on 4/20/15.
 */
public class WifiP2PBroadcastReceiver extends BroadcastReceiver
{
    private WifiP2pManager wifiP2pManager;
    private Channel channel;
    private MainActivity activity;
    private PeerListListener peerListListener;

    public WifiP2PBroadcastReceiver(WifiP2pManager wifiP2pManager, Channel channel, MainActivity activity)
    {
        this.wifiP2pManager = wifiP2pManager;
        this.channel = channel;
        this.activity = activity;
    }

    @Override
    public void onReceive(Context context, Intent intent)
    {
        String action = intent.getAction();
        int state;

        if(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action))
        {
            state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);

            if(state == WifiP2pManager.WIFI_P2P_STATE_ENABLED)
            {
                //Toast.makeText(context, "Wifi P2P Enabled", Toast.LENGTH_LONG).show();
            }
            else
            {
                //Toast.makeText(context, "Wifi P2P Disabled", Toast.LENGTH_LONG).show();
            }
        }  else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action))
        {
            // Call WifiP2pManager.requestPeers() to get a list of current peers
            if(this.wifiP2pManager != null)
            {
                wifiP2pManager.requestPeers(getChannel(), this.activity);
            }
        } else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action))
        {
            // Respond to new connection or disconnections

        } else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action))
        {
            // Respond to this device's wifi state changing

        }
    }

    public Channel getChannel() {
        return channel;
    }
}
