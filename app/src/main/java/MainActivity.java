package tk.altogradesoftwares.firewall;


import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.VpnService;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class MainActivity extends Activity {


    int request_WRITE_EXTERNAL_STORAGE;

    Intent in;

    Intent balanceIsZeroIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        balanceIsZeroIntent = getIntent();


        /*********** EXPERIMENT **************/
        if(getIntent().getAction() == "FROMSELECTAPPSACTIVITY")
        {
            /***************** Toast.makeText(getApplicationContext(), "getIntent().getAction() == \"FROMSELECTAPPSACTIVITY\"", Toast.LENGTH_LONG).show(); *******************/
            Toast.makeText(getApplicationContext(), "Firewall started. To keep the firewall running in the background don't press EXIT.", Toast.LENGTH_LONG).show();
            return;
        }
        /************************************/

        startTheFirewall();


    }

    @Override
    protected void onNewIntent (Intent intent)
    {
        super.onNewIntent(intent);
       /********************** Toast.makeText(this, "onNewIntent", Toast.LENGTH_SHORT).show(); ***************************/
        balanceIsZeroIntent = intent;
        if(balanceIsZeroIntent.getAction() == "BALANCEISZERO")
        {

            startTheFirewall();
        }

        /************* EXPERIMENT **********************/
        if(intent.getAction() == "FROMSELECTAPPSACTIVITY")
        {
            /*********** Toast.makeText(getApplicationContext(), "getIntent().getAction() == \"FROMSELECTAPPSACTIVITY\"", Toast.LENGTH_LONG).show(); ************/
            Toast.makeText(getApplicationContext(), "Firewall started. To keep the firewall running in the background don't press EXIT.", Toast.LENGTH_LONG).show();


        }

        /**********************************************/
    }

    public void startTheFirewall()
    {
        Intent intent = VpnService.prepare(getApplicationContext());
        if (intent != null) {
            startActivityForResult(intent, 0);
        } else {
            onActivityResult(0, RESULT_OK, null);
        }

    }

    @Override
    protected void onActivityResult(int request, int result, Intent data)
    {
        if (result == RESULT_OK)
        {
            /*************** EXPERIMENT ***************/
            if(balanceIsZeroIntent.getAction() == "BALANCEISZERO")
            {
                Toast.makeText(this, "Your account balance is zero so the firewall has been stopped.", Toast.LENGTH_LONG).show();
                in = new Intent(getApplicationContext(), MyIntentService.class).setAction("BALANCEISZERORESTART");
                startService(in);
                return;
            }
            /*****************************************/

            in = new Intent(getApplicationContext(), MyIntentService.class);
            startService(in);
        }
    }











    public void selectApps (View v)
    {
        Intent selectAppsActivity = new Intent(this, selectApps.class);
        startActivity(selectAppsActivity);
    }

    public void rechargeAccount (View v)
    {
        Intent rechargeIntent = new Intent(this, rechargeActivity.class);
        startActivity(rechargeIntent);
    }

    public void checkBalance (View v)
    {
        SharedPreferences subscriptionInfo = getSharedPreferences("subscriptionInfoPref", 0);
        Toast.makeText(getApplicationContext(), "Account Balance: " + subscriptionInfo.getLong("accountBalance", 0) + " minute(s)", Toast.LENGTH_LONG).show();
    }

    public void terminateFirewall(View v)
    {
        Toast.makeText(getApplicationContext(), "AG Firewall is shutting down completely. Turn off your data so that you won't lose it.", Toast.LENGTH_LONG).show();
        startService(new Intent(getApplicationContext(), MyIntentService.class).setAction("STOPFIREWALL"));

        /************* EXPERIMENT ***************/
            this.finishAndRemoveTask();
        /****************************************/
    }

    public void showManual(View v)
    {
        startActivity(new Intent(this, UserManualActivity.class));
    }






}
