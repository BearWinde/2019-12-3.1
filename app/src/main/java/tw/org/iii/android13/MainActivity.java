package tw.org.iii.android13;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    private WebView webView;
    private EditText n;
    private LocationManager loc;
    MyListener mylistener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    123);
        }else{
            init();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            init();
        }else{
            finish();
        }
    }

    private  void init(){
        loc = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        n = findViewById(R.id.n);
        webView = findViewById(R.id.webView);
        initWebView();

    }

    @Override
    protected void onStop() {
        super.onStop();
        loc.removeUpdates(mylistener);
    }

    @Override
    protected void onStart() {
        super.onStart();
         mylistener = new MyListener();
        loc.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,0,0,mylistener);
    }
    public class MyListener implements LocationListener {
        @Override
        public void onLocationChanged(Location location) {
            double lat=location.getLatitude();
           double lng =location.getLongitude();
           Log.v("brad",lat+"x"+lng);
           webView.loadUrl("javascript:moveTo("+lat+","+lng+")");

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    }

    @SuppressLint("JavascriptInterface")
    private void initWebView(){
        WebViewClient webViewClient = new WebViewClient();
        webView.setWebViewClient(webViewClient);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        webView.addJavascriptInterface(new MyJs(), "brad");
        webView.loadUrl("file:///android_asset/map.html");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
            Log.v("brad","keyCode"+keyCode);
            if(keyCode ==4 && webView.canGoBack()){
                webView.goBack();
                return  true;
            }
            return super.onKeyDown(keyCode,event);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.v("brad","backPress");
    }

    public void test1(View view){

        webView.loadUrl("javascript:test2(" + n.getText().toString() + ")");
    }

    public class MyJs{

        public void callFromJS(String urname){
            n.setText(urname);
        }
    }

}
