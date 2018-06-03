package com.example.kasunchinthaka.mapcodex;

/**
 * created by Kasun Chinthaka
 */

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class tankActivity extends AppCompatActivity {

    private ListView lv;
    String item;
    TextView tv, tv2, tv3, tv4, tv5, tv6, tv7, tv8, tv9, tv10, tv11, tv12, tv13, tv14, tv15;
    String DEVICE_ID, TABK_NAME, LONGI, LATI, MAXWATER, MINWATER, AVGRAIN, CAPACITY, OCCUPANCY, AVGAREA, CREATEDAT, UPDATEDAT;


    ArrayList<HashMap<String, String>> contactList2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tank);


        Bundle bundle = getIntent().getExtras();
        item = bundle.getString("PASSINGDATA");

        try {
            JSONObject obj = new JSONObject(item);
            DEVICE_ID = obj.getString("DEVICE_ID");
            TABK_NAME = obj.getString("TABK_NAME");
            LONGI = obj.getString("LONGI");
            LATI = obj.getString("LATI");
            MAXWATER = obj.getString("MAXWATER");
            MINWATER = obj.getString("MINWATER");
            AVGRAIN = obj.getString("AVGRAIN");
            CAPACITY = obj.getString("CAPACITY");
            OCCUPANCY = obj.getString("OCCUPANCY");
            AVGAREA = obj.getString("AVGAREA");
            CREATEDAT = obj.getString("CREATEDAT");
            UPDATEDAT = obj.getString("UPDATEDAT");

            tv = (TextView) findViewById(R.id.tank1);
            tv.setText(DEVICE_ID);
            tv2 = (TextView) findViewById(R.id.tank2);
            tv2.setText(TABK_NAME);
            tv3 = (TextView) findViewById(R.id.tank3);
            tv3.setText(MAXWATER);
            tv4 = (TextView) findViewById(R.id.tank4);
            tv4.setText(MINWATER);
            tv5 = (TextView) findViewById(R.id.tank5);
            tv5.setText(AVGRAIN);
            tv6 = (TextView) findViewById(R.id.tank6);
            tv6.setText(CAPACITY);
            tv7 = (TextView) findViewById(R.id.tank7);
            tv7.setText(OCCUPANCY);
            tv8 = (TextView) findViewById(R.id.tank8);
            tv8.setText(AVGAREA);
            tv9 = (TextView) findViewById(R.id.tank9);
            tv9.setText(CREATEDAT);
            tv10 = (TextView) findViewById(R.id.tank10);
            tv10.setText(UPDATEDAT);


        } catch (JSONException e) {
            e.printStackTrace();
        }


        contactList2 = new ArrayList<>();
        lv = (ListView) findViewById(R.id.list2);


        BackGround bb = new BackGround();
        bb.execute(DEVICE_ID);


    }

    public void chart(View view) {

        finish();
        View xview = (View) view.getParent();
        TextView tv = (TextView) xview.findViewById(R.id.t7);
        Intent intent = new Intent(this, ChartActivity.class);
        for (HashMap map : contactList2) {
            Toast.makeText(this, tv.getText(), Toast.LENGTH_SHORT).show();
//            Toast.makeText(this, , Toast.LENGTH_SHORT).show();

            if (map.get("#ID").equals(tv.getText())) {
                JSONObject obj = new JSONObject();
                Set<String> set = map.keySet();
                for (String key : set) {
                    try {
                        obj.put(key, map.get(key));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
//                Toast.makeText(this,obj.toString(), Toast.LENGTH_SHORT).show();
                intent.putExtra("chartData", obj.toString());
                break;
            }
        }
        startActivity(intent);
    }


    public void updatecodex(View view) {
        finish();
        View xview = (View) view.getParent();
        TextView tv = (TextView) xview.findViewById(R.id.t7);
        Intent intent = new Intent(this, UpdateActivity.class);
        for (HashMap map : contactList2) {
//            Toast.makeText(this,tv.getText(), Toast.LENGTH_SHORT).show();
//            Toast.makeText(this, , Toast.LENGTH_SHORT).show();

            if (map.get("#ID").equals(tv.getText())) {
                JSONObject obj = new JSONObject();
                Set<String> set = map.keySet();
                for (String key : set) {
                    try {
                        obj.put(key, map.get(key));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
//                Toast.makeText(this,obj.toString(), Toast.LENGTH_SHORT).show();
                intent.putExtra("chartData2", obj.toString());
                break;
            }
        }
        startActivity(intent);
    }


    class BackGround extends AsyncTask<String, String, String> {


        private ProgressDialog dialog = new ProgressDialog(tankActivity.this);

        /** progress dialog to show user that the backup is processing. */
        /**
         * application context.
         */
        @Override
        protected void onPreExecute() {
            this.dialog.setMessage("Please wait");
            this.dialog.show();
        }


        @Override
        protected String doInBackground(String... params) {
            String id = params[0];
//            String password = params[1];
            String data = "";
            int tmp;

            try {
                URL url = new URL("http://yummy.projects.mrt.ac.lk:8086/phpset/retrivedevice.php");
                String urlParams = "id=" + DEVICE_ID;

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                OutputStream os = httpURLConnection.getOutputStream();
                os.write(urlParams.getBytes());
                os.flush();
                os.close();

                InputStream is = httpURLConnection.getInputStream();
                while ((tmp = is.read()) != -1) {
                    data += (char) tmp;
                }

                is.close();
                httpURLConnection.disconnect();

                return data;
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return "Exception: " + e.getMessage();
            } catch (IOException e) {
                e.printStackTrace();
                return "Exception: " + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String result) {

            String err = null;
            try {


                JSONArray jsonArr = new JSONArray(result);


                for (int i = 0; i < jsonArr.length(); i++) {
                    JSONObject jsonObj = jsonArr.getJSONObject(i);


                    String id = jsonObj.getString("id");
                    String device_id = jsonObj.getString("device_id");
                    String humidity = jsonObj.getString("humidity");
                    String water_level = jsonObj.getString("water_level");
                    String temprature = jsonObj.getString("temprature");
                    String device_status = jsonObj.getString("Device_status");
                    String created_at = jsonObj.getString("created_at");
                    String updated_at = jsonObj.getString("updated_at");


                    HashMap<String, String> contact2 = new HashMap<>();

                    // adding each child node to HashMap key => value
                    contact2.put("#ID", id);
                    contact2.put("#DEVICE_ID", device_id);
                    contact2.put("#HUMIDITY", humidity);
                    contact2.put("#WATERLEVEL", water_level);
                    contact2.put("#TEMP", temprature);
                    contact2.put("#DEVICETSTATUS", device_status);
                    contact2.put("#CREATED", created_at);
                    contact2.put("#UPDATED", updated_at);


                    // adding contact to contact list
                    contactList2.add(contact2);

                    ListAdapter adapter = new SimpleAdapter(tankActivity.this, contactList2,
                            R.layout.list_item2, new String[]{"#HUMIDITY", "#WATERLEVEL", "#TEMP", "#DEVICETSTATUS", "#CREATED", "#UPDATED", "#ID"},
                            new int[]{R.id.t1, R.id.t2, R.id.t3, R.id.t4, R.id.t5, R.id.t6, R.id.t7});

                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }


                    lv.setAdapter(adapter);


                }


            } catch (JSONException e) {
                Toast.makeText(getBaseContext(), "NOT FOUND", Toast.LENGTH_LONG).show();
            }


        }
    }

    public void onBackPressed() {

        finish();
        Intent intent = new Intent(this, SecondActivity.class);
        startActivity(intent);
    }

}
