package com.example.paperout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.ClientError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {

    TextView regtext;
    Button login;
    EditText email,pass;
    String uemail,upass,res = "";
    String url = "";
    ImageView logo;
    private static final int PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        regtext = findViewById(R.id.regtext);
        login = findViewById(R.id.login);
        email = findViewById(R.id.useremail2);
        pass = findViewById(R.id.userpass3);
        logo = findViewById(R.id.logo);

        getlogo();

        regtext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(i);
                finish();
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uemail = email.getText().toString();
                upass = pass.getText().toString();
                checkcredentials(uemail,upass);
            }
        });

        if (Build.VERSION.SDK_INT >= 23)
        {
            if (checkPermission())
            {
                // Code for above or equal 23 API Oriented Device
                // Your Permission granted already .Do next code
            } else {
                requestPermission(); // Code for permission
            }
        }
    }

    private void getlogo() {
        BaseUrl b = new BaseUrl();
        url = b.url;
        url = url.concat("hrcet/index.php/api/Logo/logo");
        RequestQueue volleyRequestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        BaseUrl b = new BaseUrl();
                        url = b.url;
                        if(response != null) {
                            JSONObject json = null;

                            try {
                                json = new JSONObject(String.valueOf(response));
                                Boolean status = json.getBoolean("status");
                                String stat = status.toString();
                                if(stat.equals("true"))
                                {
                                    String msg = json.getString("message");
                                    JSONArray data = json.getJSONArray("data");
                                    JSONObject data1 = data.getJSONObject(0);
                                    String image = data1.getString("logo_image");
                                    Picasso.get().load(image).fit().into(logo);
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                BaseUrl b = new BaseUrl();
                url = b.url;
                if(error instanceof ClientError)
                {
                    try{
                        String responsebody = new String(error.networkResponse.data,"utf-8");
                        JSONObject data = new JSONObject(responsebody);
                        Boolean status = data.getBoolean("status");
                        String stat = status.toString();
                        if(stat.equals("false"))
                        {
                            String msg = data.getString("message");
                            Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_LONG).show();
                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }

            }
        }){
            @Override
            public Map<String,String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                String credentials = "HRCET:.r*fm5D%d-Re45-)";
                String auth = "Basic "
                        + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                headers.put("Authorization", auth);
                headers.put("x-api-key","HRCETCRACKER@123");
//                headers.put("Content-Type", "application/form-data");
                return headers;
            }
        };
        volleyRequestQueue.add(stringRequest);
    }

    private void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(Login.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(Login.this, "Write External Storage permission allows us to do store images. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(Login.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }
    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(Login.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("value", "Permission Granted, Now you can use local drive .");
                } else {
                    Log.e("value", "Permission Denied, You cannot use local drive .");
                }
                break;
        }
    }

    private void checkcredentials(String uemail, String upass) {
        BaseUrl b = new BaseUrl();
        url = b.url;
        url = url.concat("hrcet/index.php/api/Register/login");
        RequestQueue volleyRequestQueue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        BaseUrl b = new BaseUrl();
                        url = b.url;
                        if(response != null) {
                            JSONObject json = null;

                            try {
                                json = new JSONObject(String.valueOf(response));
                                Boolean status = json.getBoolean("status");
                                String stat = status.toString();
                                if(stat.equals("true"))
                                {
                                    String msg = json.getString("message");
                                    JSONArray data = json.getJSONArray("data");
                                    JSONObject data1 = data.getJSONObject(0);
                                    String dis_id = data1.getString("district_id");
                                    String sr_id = data1.getString("sr_id");
                                    String uname = data1.getString("fname");
                                    String email = data1.getString("email");
                                    String mobile = data1.getString("mobile");
                                    Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_LONG).show();
                                    Intent i = new Intent(getApplicationContext(),Dashboard.class);
                                    SharedPreferences sharedPreferences = getSharedPreferences("userpref", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("district_id",dis_id);
                                    editor.putString("sr_id",sr_id);
                                    editor.putString("uname",uname);
                                    editor.putString("email",email);
                                    editor.putString("mobile",mobile);
                                    editor.apply();
                                    SharedPreferences logincredentials = getSharedPreferences("userlogin", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor1 = logincredentials.edit();
                                    editor1.putString("logged","true");
                                    editor1.apply();
                                    startActivity(i);
                                    finish();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                BaseUrl b = new BaseUrl();
                url = b.url;
                if(error instanceof ClientError)
                {
                try{
                    String responsebody = new String(error.networkResponse.data,"utf-8");
                    JSONObject data = new JSONObject(responsebody);
                    Boolean status = data.getBoolean("status");
                    String stat = status.toString();
                    if(stat.equals("false"))
                    {
                        String msg = data.getString("message");
                        Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_LONG).show();
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                }

            }
        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("email", uemail);
                params.put("password",upass);
                return params;
            }
            @Override
            public Map<String,String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                String credentials = "HRCET:.r*fm5D%d-Re45-)";
                String auth = "Basic "
                        + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                headers.put("Authorization", auth);
                headers.put("x-api-key","HRCETCRACKER@123");
//                headers.put("Content-Type", "application/form-data");
                return headers;
            }
        };
        volleyRequestQueue.add(stringRequest);
    }
}