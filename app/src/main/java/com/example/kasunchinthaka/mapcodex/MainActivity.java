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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    EditText edit1,edit2;
    String user,pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void login(View view) {

        edit1 = (EditText)findViewById(R.id.edit1);
        user = edit1.getText().toString();

        edit2 = (EditText)findViewById(R.id.edit2);
        pass = "$2y$10$zRtib98w7sFQUGJZs2RCb.DtY.Kg0ERjGw.2v6Origu2e9VrBkBaS";




        if(user.equals("")||pass.equals("")){
            Toast.makeText(this, "Bad Try", Toast.LENGTH_SHORT).show();
            edit1.setText("");
            edit1.setError("Field Required!");
            edit2.setText("");
            edit2.setError("Field Required!");

        }else {
            if(isEmailValid(user)){


                MainActivity.BackGround bb = new MainActivity.BackGround();
                bb.execute(user,pass);

            }else {
                Toast.makeText(this, "Email Invalid!", Toast.LENGTH_SHORT).show();
                edit1.setText("");
                edit1.setError("Field Required!");
            }

        }




//
//        new HttpAsyncTask().execute("http://192.168.8.100:8080/rest/foodservice/customerSignIn?cname=" + username + "&pwd=" + password);
//

    }
    public boolean isEmailValid(String email)
    {
        String regExpn =
                "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                        +"((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                        +"([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                        +"([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(regExpn,Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);

        if(matcher.matches())
            return true;
        else
            return false;
    }


    public void reg(View view) {
        Intent intent = new Intent(this, Main2Activity.class);
        startActivity(intent);

    }


    class BackGround extends AsyncTask<String, String, String> {


        private ProgressDialog dialog = new ProgressDialog(MainActivity.this);

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
            String data = "";
            int tmp;

            try {
                URL url = new URL("http://yummy.projects.mrt.ac.lk:8086/phpset/userLogin.php");
                String urlParams = "UN="+user+"&PA="+pass;

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


                    String username = jsonObj.getString("first_name");
                    String password = jsonObj.getString("email");
                    String Email = jsonObj.getString("password");

                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }


                   if(username.length()>0){
                       Intent intent = new Intent(MainActivity.this,Main3Activity.class);
                       startActivity(intent);
                   }


                }


            } catch (JSONException e) {
                Toast.makeText(getBaseContext(), "NOT FOUND", Toast.LENGTH_LONG).show();
            }


        }

    }
}
