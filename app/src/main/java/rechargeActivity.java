package tk.altogradesoftwares.firewall;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class rechargeActivity extends Activity {

    EditText editTextEcocashUsername;
    EditText editTextApprovalCode;

    LinearLayout ll;

    char[] buffer;
    int charsRead;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge);

        ll = (LinearLayout) findViewById(R.id.llRecharge);


        Toast.makeText(getApplicationContext(), "Before trying to recharge your account, make an Ecocash payment to +263 778 388 737.", Toast.LENGTH_LONG).show();


    }

    public void recharge(View v)
    {

        editTextEcocashUsername = (EditText) findViewById(R.id.ecocashUsername);
        editTextApprovalCode = (EditText) findViewById(R.id.approvalCode);


        Toast.makeText(getApplicationContext(), "Wait a moment for your response.", Toast.LENGTH_LONG).show();

        new Thread(new Runnable() {
            public void run() {
                try{

                    if(editTextEcocashUsername.getText().toString() != null && editTextApprovalCode.getText().toString() != null )
                    {
                        // URL url = new URL("http://192.168.43.202/php/myFirewallWebsite/4june2022.php");
                        URL url = new URL("http://firewall.altogradesoftwares.tk");
                        HttpURLConnection urlConnection =  (HttpURLConnection) url.openConnection();

                        urlConnection.setDoOutput(true);


                        OutputStreamWriter out =  new OutputStreamWriter(urlConnection.getOutputStream());


                        out.write("ecocashUsername=" + URLEncoder.encode(editTextEcocashUsername.getText().toString(), "UTF-8") + "&approvalCode=" + URLEncoder.encode(editTextApprovalCode.getText().toString(), "UTF-8"));

                        out.close(); // WITHOUT THIS CODE, BLANK POSTS ARE SENT. OK RESPONSES ARE ALSO RECEIVED EVEN IF THE CODE IS BUGGY
                        int response = urlConnection.getResponseCode();

                        Log.v("response::::::", "response: " + response);

                        InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                        Reader reader = new InputStreamReader(in, "UTF-8");
                        buffer = new char[1400];
                        charsRead = reader.read(buffer);



                        urlConnection.disconnect();

                        if(charsRead > 0)
                        {
                            Log.v("HTTP HTTP::::::", new String(buffer, 0, charsRead));

                            /******************** EXPERIMENT ***************************/
                            ll.post(
                                    new Runnable()
                                    {
                                        public void run()
                                        {
                                            Toast.makeText(getApplicationContext(), new String(buffer, 0, charsRead).trim(), Toast.LENGTH_LONG).show();

                                        }
                                    }
                            );

                            /*********************************************************/

                            /**************** EXPERIMENT ************************************/
                            if(new String(buffer, 0, charsRead).trim().startsWith("Recharge successful."))
                            {
                                // Toast.makeText(getApplicationContext(), "startsWith(\"Recharge successful.\")", Toast.LENGTH_LONG).show();

                                // Toast.makeText(getApplicationContext(), new String(buffer, 0, charsRead).trim().substring(1 + new String(buffer, 0, charsRead).trim().lastIndexOf("s")), Toast.LENGTH_LONG).show();

                                // ALSO MODIDY SHARED PREFERENCES
                                /************* THIS PART WORKS. DON'T DELETE ***************************/
                                SharedPreferences subscriptionInfo = getSharedPreferences("subscriptionInfoPref", 0);
                                SharedPreferences.Editor editor = subscriptionInfo.edit();
                                editor.putLong("accountBalance", Long.parseLong(new String(buffer, 0, charsRead).trim().substring(1 + new String(buffer, 0, charsRead).trim().lastIndexOf("s")).trim()) + subscriptionInfo.getLong("accountBalance", 0));
                                editor.commit();



                                Intent selectAppsActivityIntent = new Intent(getApplicationContext(), selectApps.class).putExtra("RECHARGESUCCESSFUL", "You successfully recharged " + new String(buffer, 0, charsRead).trim().substring(1 + new String(buffer, 0, charsRead).trim().lastIndexOf("s")) + " minute(s)");
                                startActivity(selectAppsActivityIntent);

                                /******************************************************/
                            }

                            /***************************************************************/







                        }



                    }



                }catch (Exception e)
                {
                    Log.v("ERROR::::::", e.getMessage());

                    final Exception ee = e;

                    /************************ EXPERIMENT **************************************/

                    ll.post(
                            new Runnable()
                            {
                                public void run()
                                {
                                    // Toast.makeText(getApplicationContext(), ee.getMessage(), Toast.LENGTH_LONG).show();
                                    Toast.makeText(getApplicationContext(), "Your data must be on inorder for you to recharge your account.", Toast.LENGTH_LONG).show();
                                }
                            }
                    );

                    /************************************************************************/

                }
            }
        }).start();
    }
}
