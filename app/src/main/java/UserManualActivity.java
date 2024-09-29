package tk.altogradesoftwares.firewall;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;

public class UserManualActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_user_manual);


         WebView.setWebContentsDebuggingEnabled(true);
        WebView webview = (WebView) findViewById(R.id.webview);




        // style="background-color: #ff6600;"
        String summary = "<html><body ><b> WARNING: </b> <br>" +
                "<div> This firewall doesn't automatically start when you turn on your phone so make sure to turn off your data before you switch off your phone. In case of an accidental shutdown, make sure to turn off your data immediately after turning on your phone. </div><br>" +
                "<div> <b> HOW TO USE THE APP: </b> <br>" +
                "1. Open the app. When you open the app it'll block all traffic whilst waiting for you to select apps. <br>" +
                "2. Select apps. <br>" +
                "3. Press start. The app doesn't keep track of previously selected apps so whenever you press start only the currently selected apps will be taken into account. You account starts getting charged after pressing start. If you don't select any apps, your account won't be charged.<br>" +
                " 4. Turn on your data. <br>" +
                "5. To keep the firewall running in the background, don't press EXIT. Pressing the home button, minimizing the app, or closing the window won't stop the firewall. To stop the firewall completely: turn off your data, open the app and then press EXIT. <br>" +
                "6. Do not start any VPN whilst this app is active. </div><br>" +
                "<div> <b>HOW TO RECHARGE YOUR ACCOUNT: </b> <br>" +
                "1. Make an Ecocash payment to +263 778 388 737. <br>" +
                "2. Open the app. <br>" +
                "3. Turn on your data. <br>" +
                "4. Select RECHARGE YOUR ACCOUNT <br>" +
                "5. Enter your Ecocash username plus the approval code you got when you made a payment to us. You might get a recharge error even though you entered correct details and this is because it might take a while for the transaction you made to be populated into our database. In that case, retry later. </div><br>" +
                "<div><i>For further assistance, you can contact us on: <br>" +
                "WhatsApp: +263 775 267 926 <br>" +
                "Telegram: +263 775 267 926 <br>" +
                "Website: www.altogradesoftwares.tk <br>" +
                "Facebook: facebook.com/AltoGradeSoftwares <br>" +
               "Twitter: twitter.com/AltoGrade</i></div></body></html>";

           webview.loadData( summary, "text/html", null);
        webview.setBackgroundColor(getResources().getColor(R.color.webViewBackground));




















    }
}
