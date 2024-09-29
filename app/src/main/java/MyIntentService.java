package tk.altogradesoftwares.firewall;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.VpnService;
import android.os.Build;
import android.os.IBinder;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;

import static android.app.PendingIntent.FLAG_UPDATE_CURRENT;
import static android.content.Intent.ACTION_TIME_TICK;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class MyIntentService extends VpnService {



    FileInputStream in;
    FileOutputStream out;
    ParcelFileDescriptor localTunnel;

    int length;

    ByteBuffer packet;
    VpnService vService;


    String contentText;

    Boolean isFirewallRunning = false;


    Boolean stopEverything = false;

    BroadcastReceiver mReceiver;

    Notification notification;

    String contentTitle;


    @Override
    public void onCreate()
    {

    }

    @Override
    public void onRevoke()
    {

        /*********** Toast.makeText(this, "onRevoke() called", Toast.LENGTH_SHORT).show(); *******************/
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId)
    {


        if(intent.getAction() == "STOPFIREWALL")
        {
           /************** Toast.makeText(this, "intent.getAction() == \"STOPFIREWALL\"", Toast.LENGTH_SHORT).show(); *****************/

            stopEverything = true;
            try{


                if(mReceiver != null)
                {
                    unregisterReceiver(mReceiver);
                }

                localTunnel.close();
                in.close();
                out.close();
                stopForeground(true);
                stopSelf();

            }catch (Exception e)
            {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            return START_NOT_STICKY;

        }

        if(!isFirewallRunning)
        {
            isFirewallRunning = true;
        }else{
            /******************** Toast.makeText(getApplicationContext(), "FIREWALL IS ALREADY RUNNING", Toast.LENGTH_SHORT).show(); ********************/
            if(intent.getStringArrayExtra("selectedApps") == null)
            {
                /******************* EXPERIMENT *****************/
                if(intent.getAction() == "BALANCEISZERORESTART")
                {
                    stopEverything = true;
                    if(mReceiver != null)
                    {
                        unregisterReceiver(mReceiver);
                        mReceiver = null;
                    }

                    try{
                        localTunnel.close();
                        in.close();
                        out.close();

                    }catch (Exception e)
                    {
                        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    stopEverything = false;

                }else{
                    return START_NOT_STICKY;
                }

                /***********************************************/


            }else{
                stopEverything = true;
                if(mReceiver != null)
                {
                    unregisterReceiver(mReceiver);
                    mReceiver = null;
                }

                try{
                    localTunnel.close();
                    in.close();
                    out.close();

                }catch (Exception e)
                {
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                stopEverything = false;
            }

        }

        if(intent.getStringArrayExtra("selectedApps") != null)
        {



            mReceiver = new BroadcastReceiver()
            {
                public void onReceive(Context context, Intent intent)
                {
                    String action = intent.getAction();

                    if (ACTION_TIME_TICK.equals(action))
                    {
                       /*********  Toast.makeText(getApplicationContext(), "ACTION_TIME_TICK", Toast.LENGTH_SHORT).show();  *********************/

                        // ALSO, MODIDY SHARED PREFERENCES.
                        SharedPreferences subscriptionInfo = getSharedPreferences("subscriptionInfoPref", 0);


                        if(subscriptionInfo.getLong("accountBalance", 0) == 0)
                        {
                            /*********** Toast.makeText(getApplicationContext(), "accountBalance == 0", Toast.LENGTH_SHORT).show(); ********************/



                            /****************** EXPERIMENT *******************************/
                            Intent mainActivityIntent = new Intent(getApplicationContext(), MainActivity.class).setFlags(FLAG_ACTIVITY_NEW_TASK).setAction("BALANCEISZERO");
                            startActivity(mainActivityIntent);
                            /***********************************************************/


                        }else{
                            SharedPreferences.Editor editor = subscriptionInfo.edit();
                            editor.putLong("accountBalance",  subscriptionInfo.getLong("accountBalance", 0) - 1);
                            editor.commit();
                        }
                    }
                }
            };

            IntentFilter filter = new IntentFilter();
            filter.addAction(ACTION_TIME_TICK);

            registerReceiver(mReceiver, filter);


            contentTitle = "AG Firewall is running.";
            contentText = "You selected " + intent.getStringArrayExtra("selectedApps").length +  " app(s).";
        }else{
            contentTitle = "AG Firewall is blocking all traffic.";
            contentText = "Select apps or press EXIT.";
        }



        /******************* EXPERIMENT *********************** */


        try{
            Intent notificationIntent = new Intent(this, MainActivity.class);

            /************ EXPERIMENT **************************************************/
            if(intent.getStringArrayExtra("selectedApps") != null)
            {
                notificationIntent.putExtra("buttonText", "STOP");

                /************ EXPERIMENT *************/
                notificationIntent.putExtra("selectedAppsToCheck", intent.getStringArrayExtra("selectedApps"));
                /*************************************/
            }


            /*****************EXPERIMENT WORKS*******************************
             TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
             stackBuilder.addParentStack(selectApps.class);
             stackBuilder.addNextIntent(notificationIntent);
             PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

             *****************************************************************/



            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, FLAG_UPDATE_CURRENT);


            /*******************************EXPERIMENT ****************************/
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            {
                NotificationChannel channel = new NotificationChannel("AGFIREWALLCHANNEL", "AGFIREWALLchannel", NotificationManager.IMPORTANCE_HIGH);
                channel.setDescription("Channel for AGFIREWALL notifications");
                NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.createNotificationChannel(channel);

                notification = new Notification.Builder(getApplicationContext(), "AGFIREWALLCHANNEL")
                        .setSmallIcon(R.drawable.notic)
                        .setOngoing(true)
                        .setContentTitle(contentTitle)
                        .setContentText(contentText)
                        .setContentIntent(pendingIntent)
                        .build();


            }else{
                notification = new Notification.Builder(getApplicationContext())
                        .setSmallIcon(R.drawable.notic)
                        .setOngoing(true)
                        .setContentTitle(contentTitle)
                        .setContentText(contentText)
                        .setContentIntent(pendingIntent)
                        .build();
            }

            /*********************************************************************/




            startForeground(1000, notification);

        }catch (Exception e)
        {
            Log.v("My Intent Service", e.getMessage());
        }

        /* ***************************************************** */

       /****************** Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show(); **************************/

        vService = this;
        new Thread(new Runnable() {
            public void run() {


                try{



                    Builder builder = new Builder();
                    builder.addAddress("10.0.0.10", 32)
                            .addRoute("0.0.0.0", 0)
                            .setMtu(1400)
                            .setSession("AG Firewall");

                    if(intent.getStringArrayExtra("selectedApps") != null)
                    {
                        for (String appPackage: intent.getStringArrayExtra("selectedApps"))
                        {
                            builder.addDisallowedApplication(appPackage);
                            Log.v("My Intent Service", appPackage);
                        }
                    }

                    builder.addDisallowedApplication("tk.altogradesoftwares.firewall");




                    localTunnel = builder.establish();

                    in = new FileInputStream(localTunnel.getFileDescriptor());
                    out = new FileOutputStream(localTunnel.getFileDescriptor());
                    packet = ByteBuffer.allocate(Short.MAX_VALUE);







                    for(;;)
                    {
                        if(stopEverything)
                        {
                            break;
                        }

                        length = in.read(packet.array());
                        if (length > 0)
                        {

                            Log.v("My Intent Service", "READ BYTES from fd: "+ length);

                        }


                    }





                }catch (Exception e)
                {
                    // Toast.makeText(ctx, e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.v("My Intent Service", "ERROR ERROR ERROR ERROR ERROR");
                    Log.v("My Intent Service", e.getMessage());
                    Log.v("My Intent Service", e.toString());


                    try{

                        localTunnel.close();

                        stopSelf();

                    }catch(Exception r)
                    {
                        Log.v("My Intent Service", e.getMessage());
                    }

                }
            }
        }).start();




        return START_NOT_STICKY;




    }

    @Override
    public IBinder onBind(Intent intent) {
        // We don't provide binding, so return null
        return null;
    }

    @Override
    public void onDestroy()
    {

        try{
            /*************************** Toast.makeText(this, "service done", Toast.LENGTH_SHORT).show(); ******************************/
            localTunnel.close();


            in.close();
            out.close();


            /************************ EXPERIMENT ******************/
            stopForeground(true);
            /******************************************************/




        }catch (Exception e)
        {
            // Toast.makeText(MyIntentService.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.v("My Intent Service", e.getMessage());
        }


    }


}
