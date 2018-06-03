package com.example.kasunchinthaka.mapcodex;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

public class FirstActivity extends FragmentActivity  implements OnMapReadyCallback,GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private Marker myyy;
    String lati,longi,tank_name;


    ArrayList<HashMap<String, String>> contactList3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        new FirstActivity.HttpAsyncTask().execute("http://yummy.projects.mrt.ac.lk:8086/phpset/tank.php");

        contactList3 = new ArrayList<>();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    public boolean onMarkerClick(final Marker marker) {


        if(marker.getTitle().equals("Beira Lake")){
            Intent intent = new Intent(this,Main2Activity.class);
            startActivity(intent);
        }

        return false;

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(8.168440, 80.926304);
        mMap.addMarker(new MarkerOptions().position(new LatLng(8.072732, 79.955642)).title("Tabbowa Wewa"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(7.533116, 80.482943)).title("Warakawehera Tank"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(8.824002, 80.757038)).title("Janakapura Tank"));
//        mMap.addMarker(new MarkerOptions().position(new LatLng(8.351944, 80.385278)).title("Basawakkulama Tank"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(6.888286, 79.947317)).title("Talangama Lake"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(8.247452, 80.485487)).title("Nachchaduwa Tank"));
//        mMap.addMarker(new MarkerOptions().position(new LatLng(8.011623, 80.557734)).title("Kala Wewa"));
        myyy = mMap.addMarker(new MarkerOptions().position(new LatLng(8.168440, 80.926304)).title("Kaudulla Wewa"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(8.351263, 81.006993)).title("Marker in Sydney"));

//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        float zoomLevel = (float) 7.0; //This goes up to 21
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, zoomLevel));



    }

    public void onBackPressed(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(FirstActivity.this,Main3Activity.class);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }


    public static String GET(String url) {
        InputStream inputStream = null;
        String result = "";
        try {

            // create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // make GET request to the given URL
            HttpResponse httpResponse = httpclient.execute(new HttpGet(url));

            // receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            // convert inputstream to string
            if (inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        return result;
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }

    public boolean isConnected() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {

        private ProgressDialog dialog = new ProgressDialog(FirstActivity.this);

        /** progress dialog to show user that the backup is processing. */
        /** application context. */
        @Override
        protected void onPreExecute() {
            this.dialog.setMessage("Please wait");
            this.dialog.show();
        }



        @Override
        protected String doInBackground(String... urls) {

            return GET(urls[0]);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
//            Toast.makeText(getBaseContext(), result, Toast.LENGTH_LONG).show();

            try {


                JSONArray jsonArr = new JSONArray(result);


                for (int i = 0; i < jsonArr.length(); i++) {
                    JSONObject jsonObj = jsonArr.getJSONObject(i);

                    String device_id = jsonObj.getString("device_id");
                     tank_name = jsonObj.getString("tank_name");
                     longi = jsonObj.getString("longi");
                     lati = jsonObj.getString("lati");
                    String MaxWater = jsonObj.getString("MaxWater");
                    String MinWater = jsonObj.getString("MinWater");
                    String AvgRain = jsonObj.getString("AvgRain");
                    String Capacity = jsonObj.getString("Capacity");
                    String occupancy = jsonObj.getString("occupancy");
                    String AvgArea = jsonObj.getString("AvgArea");
                    String created_at = jsonObj.getString("created_at");
                    String updated_at = jsonObj.getString("updated_at");


                    float longii = Float.valueOf(longi);
                    float langii = Float.valueOf(lati);

                    mMap.addMarker(new MarkerOptions().position(new LatLng(longii, langii)).title(tank_name));



                    HashMap<String, String> contact = new HashMap<>();

                    // adding each child node to HashMap key => value
                    contact.put("DEVICE_ID", device_id);
                    contact.put("TABK_NAME", tank_name);
                    contact.put("LONGI", longi);
                    contact.put("LATI", lati);
                    contact.put("MAXWATER", MaxWater);
                    contact.put("MINWATER", MinWater);
                    contact.put("AVGRAIN", AvgRain);
                    contact.put("CAPACITY", Capacity);
                    contact.put("OCCUPANCY", occupancy);
                    contact.put("AVGAREA", AvgArea);
                    contact.put("CREATEDAT", created_at);
                    contact.put("UPDATEDAT", updated_at);

                    // adding contact to contact list
                    contactList3.add(contact);


                }


                if (dialog.isShowing()) {
                    dialog.dismiss();
                }



            } catch (JSONException e) {
                Toast.makeText(getBaseContext(),"NOT FOUND", Toast.LENGTH_LONG).show();
            }
        }
    }


}
