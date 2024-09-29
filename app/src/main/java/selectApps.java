package tk.altogradesoftwares.firewall;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.VpnService;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.graphics.Typeface.BOLD;
import static android.graphics.Typeface.NORMAL;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class selectApps extends Activity {

    Map<String, Boolean> selectedApplications;
    String [] stringArrayOfSelectedApplications = null;
    Intent in;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_apps);

        intent = getIntent();

        if(intent.getStringExtra("RECHARGESUCCESSFUL") != null)
        {
            Toast.makeText(getApplicationContext(), intent.getStringExtra("RECHARGESUCCESSFUL"), Toast.LENGTH_LONG).show();
        }


        LinearLayout ll = (LinearLayout) findViewById(R.id.ll2);


        /************************** EXPERIMENT WORKS. DON'T DELETE*********************/
        PackageManager pManager = getApplicationContext().getPackageManager();
        String[] permissions = new String[1];
        permissions[0] = "android.permission.INTERNET";
        List<PackageInfo> internetPackages = pManager.getPackagesHoldingPermissions(permissions, 0);


        /************* EXPERIMENT **************************/
        selectedApplications = new HashMap<String, Boolean>();
        /*************************************************/

        View.OnClickListener clickListener2 = new View.OnClickListener()
        {
            public void onClick(View v)
            {
                // Toast.makeText(getApplicationContext(), v.getTag().toString(), Toast.LENGTH_LONG).show();
                selectedApplications.put(v.getTag().toString(), ((CheckBox) v).isChecked());
            }
        };

        for(int i =0; i < internetPackages.size(); i++)
        {
            // Toast.makeText(getApplicationContext(), internetPackages.get(i).packageName, Toast.LENGTH_LONG).show();
            // Toast.makeText(getApplicationContext(), pManager.getApplicationLabel(internetPackages.get(i).applicationInfo), Toast.LENGTH_LONG).show();

            /********* EXPERIMENT *****************/
            if(internetPackages.get(i).packageName.equalsIgnoreCase("tk.altogradesoftwares.firewall"))
            {
                continue;
            }
            /*************************************/

            CheckBox checkBox = new CheckBox(this);
            checkBox.setText(pManager.getApplicationLabel(internetPackages.get(i).applicationInfo));
            checkBox.setTag(internetPackages.get(i).packageName);
            checkBox.setChecked(false);

            /************ EXPERIMENT *************/
            if(intent != null)
            {
                if(intent.getStringArrayExtra("selectedAppsToCheck") != null)
                {
                    for (String appPackage: intent.getStringArrayExtra("selectedAppsToCheck"))
                    {
                        if(appPackage.equalsIgnoreCase(internetPackages.get(i).packageName))
                        {
                            checkBox.setChecked(true);
                            selectedApplications.put(internetPackages.get(i).packageName, checkBox.isChecked());
                        }
                    }
                }
            }

            /*************************************/

            /********** EXPERIMENT *********************/
            checkBox.setTypeface(Typeface.defaultFromStyle(NORMAL));
            /******************************************/

            checkBox.setTextColor(getResources().getColor(R.color.selectAppsButtonColor));
            checkBox.setOnClickListener(clickListener2);
            ll.addView(checkBox, ll.getChildCount());

        }

        View.OnClickListener clickListener = new View.OnClickListener()
        {
            public void onClick(View v)
            {
                Button btn = (Button) v;

                if(btn.getText() == "START")
                {
                    /******************** EXPERIMENT ************************/
                    List<String> listofSelectedApplications = new ArrayList<String>();

                    if(selectedApplications.size() != 0)
                    {
                        for (Map.Entry<String, Boolean> packageName : selectedApplications.entrySet())
                        {
                            if(packageName.getValue() == false)
                            {
                                continue;
                            }else{
                                listofSelectedApplications.add(packageName.getKey());
                            }

                            //  Toast.makeText(getApplicationContext(), packageName.getKey() + " " + packageName.getValue() , Toast.LENGTH_LONG).show();
                        }

                        if(listofSelectedApplications.size() != 0)
                        {
                            stringArrayOfSelectedApplications = new String[listofSelectedApplications.size()];
                            System.arraycopy(listofSelectedApplications.toArray(), 0, stringArrayOfSelectedApplications, 0, listofSelectedApplications.size());

                            /* for (int i = 0; i < stringArrayOfSelectedApplications.length ; i++)
                            {
                                Toast.makeText(getApplicationContext(), stringArrayOfSelectedApplications[i], Toast.LENGTH_LONG).show();
                            } */

                            /********************* EXPERIMENT *******************************/


                            // stopTheVpn();

                            /***************EXPERIMENT ***********************/
                            SharedPreferences subscriptionInfo = getSharedPreferences("subscriptionInfoPref", 0);
                            if(subscriptionInfo.getLong("accountBalance", 0) != 0)
                            {
                                in = new Intent(getApplicationContext(), MyIntentService.class);
                                in.putExtra("selectedApps", stringArrayOfSelectedApplications);
                                startService(in);

                                Toast.makeText(getApplicationContext(), "You selected " + listofSelectedApplications.size() + " application(s)", Toast.LENGTH_LONG).show();
                                Intent mainActivityIntent = new Intent(getApplicationContext(), MainActivity.class).setAction("FROMSELECTAPPSACTIVITY");
                                startActivity(mainActivityIntent);

                                stringArrayOfSelectedApplications = null;
                            }else{
                                Toast.makeText(getApplicationContext(), "Your account balance is zero. Recharge it so that you will be able to start the firewall.", Toast.LENGTH_LONG).show();
                            }

                            /*************************************************/




                                   /*  if(stopService(new Intent(getApplicationContext(), MyIntentService.class)))
                                    {
                                        startTheFirewall();    // THIS PART  NEEDS FIXING
                                        btn.setText("STOP");
                                        stringArrayOfSelectedApplications = null;
                                        Toast.makeText(getApplicationContext(), "SUCCEEDED stopService(new Intent(....", Toast.LENGTH_LONG).show();
                                    }else{
                                        Toast.makeText(getApplicationContext(), "FAILED stopService(new Intent(....", Toast.LENGTH_LONG).show();
                                    }   */


                            /***************************************************************/



                            /* startTheFirewall();    // THIS PART  NEEDS FIXING
                            btn.setText("STOP");
                            stringArrayOfSelectedApplications = null; */

                        }else {
                            Toast.makeText(getApplicationContext(), "Select at least 1 app", Toast.LENGTH_LONG).show();
                        }





                    }else {
                        Toast.makeText(getApplicationContext(), "Select at least 1 app", Toast.LENGTH_LONG).show();
                    }

                    /*******************************************************/







                }
            }
        };

        Button btn = new Button(this);
        btn.setText("START");

        /******** EXPERIMENT ***********************/
        btn.setLayoutParams(new ViewGroup.LayoutParams(WRAP_CONTENT, WRAP_CONTENT));
        btn.setBackgroundColor(getResources().getColor(R.color.selectAppsButtonColor));
        /*****************************************/

        btn.setOnClickListener(clickListener);
        ll.addView(btn, ll.getChildCount());

        Toast.makeText(getApplicationContext(), "Select apps, scroll down to the end of the list and then press start.", Toast.LENGTH_LONG).show();

        /******** EXPERIMENT ********************/

        if(intent != null)
        {
            if(intent.getStringExtra("buttonText") != null)
            {
                btn.setText(intent.getStringExtra("buttonText"));
            }
        }
        /**************************************/





        /*************************************************************/

        /*************************** EXPERIMENT WORKS.*********************/
           /* Intent intent = new Intent();
            sendBroadcast(intent.setAction("com.example.firewall.BCAST")); */


        /***********************************************************/


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
            in = new Intent(getApplicationContext(), MyIntentService.class);

            /****************** EXPERIMENT **************************/
            if(stringArrayOfSelectedApplications != null)
            {
                in.putExtra("selectedApps", stringArrayOfSelectedApplications);
            }
            /*******************************************************/

            startService(in);
        }
    }


    public void stopTheVpn()
    {

        startService(new Intent(getApplicationContext(), MyIntentService.class).setAction("STOPFIREWALL"));
    }

}
