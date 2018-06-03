package com.example.kasunchinthaka.mapcodex;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UpdateActivity extends AppCompatActivity {
    String item3;
    String updated_ID,device_ID,createdat,CREATED;
    EditText Uedit1,Uedit2,Uedit3,Uedit4,Uedit5;
    String u1,u2,u3,u4,u5;
    String currentDateTimeString;
    String format;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        Toast.makeText(this, "Here", Toast.LENGTH_SHORT).show();
        Bundle bundle = getIntent().getExtras();
        item3 = bundle.getString("chartData2");
        try {
            JSONObject obj = new JSONObject(item3);
            updated_ID = obj.getString("#ID");
            device_ID = obj.getString("#DEVICE_ID");
            CREATED = obj.getString("#CREATED");

            Toast.makeText(this, updated_ID, Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        format = s.format(new Date());
        Uedit5 = (EditText)findViewById(R.id.u5);
        Uedit5.setText(format);
        u5 = format;
    }

    public void updateme(View view){


        Uedit1 = (EditText)findViewById(R.id.u1);
        u1 = Uedit1.getText().toString();

        Uedit2 = (EditText)findViewById(R.id.u2);
        u2 = Uedit2.getText().toString();

        Uedit3 = (EditText)findViewById(R.id.u3);
        u3 = Uedit3.getText().toString();

        Uedit4 = (EditText)findViewById(R.id.u4);
        u4 = Uedit4.getText().toString();

        Uedit5 = (EditText)findViewById(R.id.u5);
        u5 = format;


        createdat= "0";
        UpdateActivity.BackGround bb = new UpdateActivity.BackGround();
        Log.i("fuck",updated_ID+" "+device_ID+" "+u1+" "+u2+" "+u3+" "+u4+" "+CREATED+" "+u5);
        Log.i("u1",u1);
        Log.i("u2",u2);
        Log.i("u3",u3);
        Log.i("u4",u4);
        Log.i("u5",u5);

        bb.execute(updated_ID,device_ID,u1,u2,u3,u4,CREATED,u5);
    }








    class BackGround extends AsyncTask<String, String, String> {



        private ProgressDialog dialog = new ProgressDialog(UpdateActivity.this);

        /** progress dialog to show user that the backup is processing. */
        /** application context. */
        @Override
        protected void onPreExecute() {
            this.dialog.setMessage("Please wait");
            this.dialog.show();
        }


        @Override
        protected String doInBackground(String... params) {
            String id = params[0];
//            String password = params[1];
            String data="";
            int tmp;

            try {
                URL url = new URL("http://yummy.projects.mrt.ac.lk:8086/phpset/chuka.php");
                Log.i("gggggggggggggg",u1);
                String urlParams = "id="+updated_ID+"&device_id="+device_ID+"&humidity="+u1+"&water_level="+u2+"&temprature="+u3+"&Device_status="+u4+"&created_at="+createdat+"&updated_at="+u5;
                Toast.makeText(UpdateActivity.this,updated_ID+" "+device_ID+" "+u1+" "+u2+" "+u3+" "+u4+" "+createdat+" "+u5, Toast.LENGTH_SHORT).show();
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                OutputStream os = httpURLConnection.getOutputStream();
                os.write(urlParams.getBytes());
                os.flush();
                os.close();

                InputStream is = httpURLConnection.getInputStream();
                while((tmp=is.read())!=-1){
                    data+= (char)tmp;
                }

                is.close();
                httpURLConnection.disconnect();

                return data;
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return "Exception: "+e.getMessage();
            } catch (IOException e) {
                e.printStackTrace();
                return "Exception: "+e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String result) {

            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }
    }

    public void onBackPressed() {

        finish();
        Intent intent = new Intent(this, SecondActivity.class);
        startActivity(intent);
    }
}
