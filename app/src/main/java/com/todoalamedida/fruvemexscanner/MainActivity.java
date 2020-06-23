package com.todoalamedida.fruvemexscanner;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    WebView myWebView;
    Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        myWebView = (WebView) findViewById(R.id.webview);
        btn = (Button) findViewById(R.id.recargar);
        myWebView.loadUrl("http://sistema.fruvemex.com/Cuernavaca/Control?user=captuvo");
        myWebView.setWebViewClient(new WebViewClient(){


            public void onReceivedError(WebView webView, int errorCode, String description, String failingUrl) {
                btn.bringToFront();
            }


        });
        myWebView.setWebChromeClient(new WebChromeClient());
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        CookieManager.getInstance().setAcceptCookie(true);
        myWebView.getSettings().setDomStorageEnabled(true);
        




        IntentFilter filter = new IntentFilter();
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        filter.addAction(getResources().getString(R.string.activity_intent_filter_action));
        registerReceiver(myBroadcastReceiver, filter);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                myWebView.loadUrl("http://sistema.fruvemex.com/Cuernavaca/Control?user=captuvo");

                myWebView.bringToFront();
                // Do something
            }
        });


    }


    @Override
    public void onBackPressed() {
        this.moveTaskToBack(true);
        return;
    }
    @Override
    protected void onDestroy()
    {
//        super.onDestroy();
//        unregisterReceiver(myBroadcastReceiver);
    }

    private BroadcastReceiver myBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Bundle b = intent.getExtras();
            //  This is useful for debugging to verify the format of received intents from DataWedge
            //for (String key : b.keySet())
            //{
            //    Log.v(LOG_TAG, key);
            //}
            if (action.equals(getResources().getString(R.string.activity_intent_filter_action))) {
                //  Received a barcode scan
                try {
                    displayScanResult(intent, "via Broadcast");
                } catch (Exception e) {
                    //  Catch if the UI does not exist when we receive the broadcast... this is not designed to be a production app
                }
            }
        }
    };

    private void displayScanResult(Intent initiatingIntent, String howDataReceived)
    {
        String decodedSource = initiatingIntent.getStringExtra(getResources().getString(R.string.datawedge_intent_key_source));
        String decodedData = initiatingIntent.getStringExtra(getResources().getString(R.string.datawedge_intent_key_data));
        String decodedLabelType = initiatingIntent.getStringExtra(getResources().getString(R.string.datawedge_intent_key_label_type));

        if (null == decodedSource)
        {
            decodedSource = initiatingIntent.getStringExtra(getResources().getString(R.string.datawedge_intent_key_source_legacy));
            decodedData = initiatingIntent.getStringExtra(getResources().getString(R.string.datawedge_intent_key_data_legacy));
            decodedLabelType = initiatingIntent.getStringExtra(getResources().getString(R.string.datawedge_intent_key_label_type_legacy));
        }
//        WebView myWebView = (WebView) findViewById(R.id.webview);
//        WebSettings webSettings = myWebView.getSettings();
//        webSettings.setJavaScriptEnabled(true);
        myWebView.loadUrl("javascript: (function() {document.getElementById('folio').value= '"+decodedData+"';document.getElementById('formulario').submit();}) ();" );

//        final TextView lblScanSource = (TextView) findViewById(R.id.lblScanSource);
//        final TextView lblScanData = (TextView) findViewById(R.id.lblScanData);
//        final TextView lblScanLabelType = (TextView) findViewById(R.id.lblScanDecoder);
//        lblScanSource.setText(decodedSource + " " + howDataReceived);
//        lblScanData.setText(decodedData);
//        lblScanLabelType.setText(decodedLabelType);
    }
}
