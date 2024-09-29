package tk.altogradesoftwares.firewall;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import static android.content.Intent.ACTION_SHUTDOWN;
import static android.net.ConnectivityManager.CONNECTIVITY_ACTION;

public class MyReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.


       /* if(intent.getAction().equals("com.example.firewall.BCAST")) {
            Toast.makeText(context, "BROADCAST RECEIVED: com.example.firewall.BCAST", Toast.LENGTH_LONG).show();
            Log.v("FIREWALL BROADCAST", "BROADCAST RECEIVED: com.example.firewall.BCAST");

        } */

     /*   if(intent.getAction().equals(CONNECTIVITY_ACTION)) {
            Toast.makeText(context, "BROADCAST RECEIVED: CONNECTIVITY_ACTION", Toast.LENGTH_LONG).show();
            Log.v("FIREWALL BROADCAST", "BROADCAST RECEIVED: CONNECTIVITY_ACTION");

        } */

        if(intent.getAction().equals(ACTION_SHUTDOWN)) {
           // Toast.makeText(context, "BROADCAST RECEIVED: ACTION_SHUTDOWN", Toast.LENGTH_LONG).show();
            Toast.makeText(context, "AG FIREWALL WARNING: Don't forget to turn off your data. That's if it is not already turned off.", Toast.LENGTH_LONG).show();
            Log.v("FIREWALL BROADCAST", "BROADCAST RECEIVED: ACTION_SHUTDOWN");

        }




    }
}
