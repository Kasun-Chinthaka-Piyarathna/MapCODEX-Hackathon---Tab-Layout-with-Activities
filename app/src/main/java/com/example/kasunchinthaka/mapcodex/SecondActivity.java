package com.example.kasunchinthaka.mapcodex;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

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
import java.util.Set;

public class SecondActivity extends AppCompatActivity {

    private ListView lv;


    ArrayList<HashMap<String, String>> contactList;

    Animation fade_in, fade_out;
    ViewFlipper viewFlipper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fff);


            viewFlipper = (ViewFlipper) this.findViewById(R.id.bckgrndViewFlipper1);
            fade_in = AnimationUtils.loadAnimation(this,
                    android.R.anim.fade_in);
            fade_out = AnimationUtils.loadAnimation(this,
                    android.R.anim.fade_out);
            viewFlipper.setInAnimation(fade_in);
            viewFlipper.setOutAnimation(fade_out);
            //sets auto flipping
            viewFlipper.setAutoStart(true);
            viewFlipper.setFlipInterval(5000);
            viewFlipper.startFlipping();



        new HttpAsyncTask().execute("http://yummy.projects.mrt.ac.lk:8086/phpset/tank.php");

        contactList = new ArrayList<>();

        lv = (ListView) findViewById(R.id.list);

    }
    public void moredata(View view){
        finish();
        View xview= (View) view.getParent();
        TextView tv = (TextView) xview.findViewById(R.id.t1);
        Intent intent = new Intent(this,tankActivity.class);
        for (HashMap map: contactList) {
            if(map.get("TABK_NAME").equals(tv.getText())){
                JSONObject obj = new JSONObject();
                Set<String> set = map.keySet();
                for(String key : set){
                    try {
                        obj.put(key,map.get(key));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                intent.putExtra("PASSINGDATA",obj.toString());
                break;
            }
        }
        startActivity(intent);
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

        private ProgressDialog dialog = new ProgressDialog(SecondActivity.this);

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
//            Toast.makeText(getBaseContext(), "Received!", Toast.LENGTH_LONG).show();

            try {


                JSONArray jsonArr = new JSONArray(result);


                for (int i = 0; i < jsonArr.length(); i++) {
                    JSONObject jsonObj = jsonArr.getJSONObject(i);

                    String device_id = jsonObj.getString("device_id");
                    String tank_name = jsonObj.getString("tank_name");
                    String longi = jsonObj.getString("longi");
                    String lati = jsonObj.getString("lati");
                    String MaxWater = jsonObj.getString("MaxWater");
                    String MinWater = jsonObj.getString("MinWater");
                    String AvgRain = jsonObj.getString("AvgRain");
                    String Capacity = jsonObj.getString("Capacity");
                    String occupancy = jsonObj.getString("occupancy");
                    String AvgArea = jsonObj.getString("AvgArea");
                    String created_at = jsonObj.getString("created_at");
                    String updated_at = jsonObj.getString("updated_at");



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
                    contactList.add(contact);


                }


                ListAdapter adapter = new SimpleAdapter(SecondActivity.this, contactList,
                        R.layout.list_item, new String[]{ "TABK_NAME","MAXWATER","MINWATER","AVGRAIN","CAPACITY","OCCUPANCY","AVGAREA"},
                        new int[]{R.id.t1, R.id.t2,R.id.t3,R.id.t4,R.id.t5,R.id.t6,R.id.t7});

                if (dialog.isShowing()) {
                    dialog.dismiss();
                }



                lv.setAdapter(adapter);



            } catch (JSONException e) {
                Toast.makeText(getBaseContext(),"NOT FOUND", Toast.LENGTH_LONG).show();
            }
        }
    }
}
