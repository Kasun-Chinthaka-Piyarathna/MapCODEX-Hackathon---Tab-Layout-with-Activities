package com.example.kasunchinthaka.mapcodex;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
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

public class Main2Activity extends AppCompatActivity {
    EditText edit4,edit5,edit6;
    String user,pass,email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
    }

     public void register(View view){
         edit4 = (EditText)findViewById(R.id.edit4);
         user = edit4.getText().toString();

         edit5 = (EditText)findViewById(R.id.edit5);
         pass = edit5.getText().toString();

         edit6 = (EditText)findViewById(R.id.edit6);
         email = edit6.getText().toString();

         Main2Activity.BackGround bb = new Main2Activity.BackGround();
         bb.execute(user,pass,email);

     }

    class BackGround extends AsyncTask<String, String, String> {


        private ProgressDialog dialog = new ProgressDialog(Main2Activity.this);

        /** progress dialog to show user that the backup is processing. */
        /**
         * application context.
         */
        @Override
        protected void onPreExecute() {
            this.dialog.setMessage("Please wait");
            this.dialog.show();
        }

        protected String doInBackground(String... params) {
            String user = params[0];
            String pass = params[1];
            String email = params[2];
            String data = "";
            int tmp;

            try {
                URL url = new URL("http://zah.000webhostapp.com/UserResgister.php");
                String urlParams = "UN="+user+"&PA="+pass+"&EM="+email ;

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
//            Toast.makeText(Main2Activity.this, result, Toast.LENGTH_SHORT).show();

            String err = null;
            try {


                JSONArray jsonArr = new JSONArray(result);


                for (int i = 0; i < jsonArr.length(); i++) {
                    JSONObject jsonObj = jsonArr.getJSONObject(i);


                    String username = jsonObj.getString("Username");
                    String password = jsonObj.getString("Password");
                    String Email = jsonObj.getString("Email");

                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }


                    if(username.length()>0){
                        Intent intent = new Intent(Main2Activity.this,MainActivity.class);
                        startActivity(intent);
                    }


                }


            } catch (JSONException e) {
                Toast.makeText(getBaseContext(), "NOT FOUND", Toast.LENGTH_LONG).show();
            }


        }

    }


}
