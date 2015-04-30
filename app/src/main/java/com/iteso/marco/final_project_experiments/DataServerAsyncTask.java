package com.iteso.marco.final_project_experiments;

import android.net.wifi.p2p.WifiP2pDevice;
import android.os.AsyncTask;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * Created by marco on 4/28/15.
 */
public class DataServerAsyncTask extends AsyncTask<WifiP2pDevice, Void, Void>
{
    @Override
    protected Void doInBackground(WifiP2pDevice... params)
    {
        try
        {
            WifiP2pDevice remotePeer = params[0];
            ServerSocket server = new ServerSocket(8888);


        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid)
    {
        super.onPostExecute(aVoid);
    }
}
