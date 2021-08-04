package com.example.paperout;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
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
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    Spinner distspinner;
    EditText firstname,lastname,useremail,usermobile,userpass,userpass2,username;
    String fname,lname,uemail,umobile,upass1,upass2,udist,uname;
    Button register;
    ArrayList<String> stringArrayList = new ArrayList<String>();
    String url = "";
    TextView got_to_login;
    List<Districts> districts;
    int check = 0;
    ImageView logo2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        distspinner = findViewById(R.id.distspinner);
        username = findViewById(R.id.username);
//        firstname = findViewById(R.id.firstname);
//        lastname = findViewById(R.id.lastname);
        useremail = findViewById(R.id.useremail);
        usermobile = findViewById(R.id.usermobile);
        userpass = findViewById(R.id.userpass);
        userpass2 = findViewById(R.id.userpass2);
        register = findViewById(R.id.register);
        got_to_login = findViewById(R.id.go_to_login);
        logo2 = findViewById(R.id.logo2);

        getlogo();

        register.setOnClickListener(this);
        got_to_login.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        getdistricts();
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
                                    String image = data1.getString("register_logo_image");
                                    Picasso.get().load(image).fit().into(logo2);
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

    private void getdistricts() {
        BaseUrl b = new BaseUrl();
        url = b.url;
        url = url.concat("hrcet/index.php/api/District/district") ;
        RequestQueue volleyRequestQueue = Volley.newRequestQueue(this);

        JSONObject jsonObject = new JSONObject();

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        BaseUrl b = new BaseUrl();
                        url = b.url;
                        if(response != null) {
                            JSONObject json = null;

                            try {
                                json = new JSONObject(String.valueOf(response));
                                JSONArray jsonArray=json.getJSONArray("data");
                                for(int i=0;i<jsonArray.length();i++){
                                    JSONObject jsonObject=jsonArray.getJSONObject(i);
                                    String dis = jsonObject.getString("dname");
                                    stringArrayList.add(dis);
                                }
                                ArrayAdapter<String>adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item,stringArrayList);
                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                distspinner.setAdapter(adapter);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
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

                            }
                        }
                    }
                })
        {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
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
        volleyRequestQueue.add(request);
    }

    private void senddatatoapi(String uname, String uemail, String umobile, String upass1,String dids) {
        BaseUrl b = new BaseUrl();
        url = b.url;
        url = url.concat("hrcet/index.php/api/Register/register");
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
                                    String uname1 = data1.getString("fname");
                                    String email1 = data1.getString("email");
                                    String mobile1 = data1.getString("mobile");
                                    Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_LONG).show();
                                    Intent i = new Intent(getApplicationContext(),Dashboard.class);
                                    SharedPreferences sharedPreferences = getSharedPreferences("userpref", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("district_id",dis_id);
                                    editor.putString("sr_id",sr_id);
                                    editor.putString("uname",uname1);
                                    editor.putString("email",email1);
                                    editor.putString("mobile",mobile1);
                                    editor.apply();
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
                params.put("fname",uname);
                params.put("email",uemail);
                params.put("mobile",umobile);
                params.put("district_id", String.valueOf(dids));
                params.put("password",upass1);
                return params;
            }
            @Override
            public Map<String,String> getHeaders() throws AuthFailureError{
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


    private void checkdata() {

//        if (isEmpty(firstname)) {
//            firstname.setError("Please Enter the firstname");
//        }
//        if (isEmpty(lastname)) {
//            lastname.setError("Please Enter the lastname");
//        }
        if (isEmpty(username)) {
            username.setError("Please Enter the username");
        }
        else if (isEmpty(usermobile)) {
            usermobile.setError("Please Enter the mobile no.");
        }
        else if (isEmpty(useremail)) {
            useremail.setError("Please Enter the email address");
        }
        else if (isEmpty(userpass)) {
            userpass.setError("Password cannot be empty");
        }
        else if (isEmpty(userpass2)) {
            userpass2.setError("Password cannot be empty");
        }
        else if(usermobile.length() != 10){
            usermobile.setError("Please enter a valid 10 digit mobile no.");
        }
        else if(!userpass.getText().toString().equals(userpass2.getText().toString()))
        {
            Toast.makeText(getApplicationContext(),"Password doesnot matches",Toast.LENGTH_SHORT).show();
        }
        else
        {
            check =1;
        }
    }

    boolean isEmpty(EditText text) {
        CharSequence str = text.getText().toString();
        return TextUtils.isEmpty(str);
    }

    boolean isEmail(EditText text) {
        CharSequence email = text.getText().toString();
        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.register:
            {
                checkdata();
                if(check == 1)
                {
                    uname = username.getText().toString();
                    uemail = useremail.getText().toString();
                    umobile = usermobile.getText().toString();
                    upass1 = userpass.getText().toString();
                    upass2 = userpass2.getText().toString();
                    udist = distspinner.getSelectedItem().toString();
                    getdid(udist,uname,uemail,umobile,upass1);
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Please correct the errors",Toast.LENGTH_SHORT).show();
                }

            }
            break;
            case R.id.go_to_login:
            {
                Intent i1 = new Intent(getApplicationContext(),Login.class);
                startActivity(i1);
                finish();
            }
            break;
        }
    }

    public void getdid(String udist, String uname, String uemail, String umobile, String upass1) {
        BaseUrl b = new BaseUrl();
        url = b.url;
        url = url.concat("hrcet/index.php/api/District/district") ;
        RequestQueue volleyRequestQueue = Volley.newRequestQueue(this);

        JSONObject jsonObject = new JSONObject();

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        BaseUrl b = new BaseUrl();
                        url = b.url;
                        if(response != null) {
                            JSONObject json = null;
                            districts = new ArrayList<>();
                            try {
                                json = new JSONObject(String.valueOf(response));
                                JSONArray jsonArray=json.getJSONArray("data");

                                for(int i=0;i<jsonArray.length();i++){
                                    Districts diss = new Districts();
                                    JSONObject jsonObject=jsonArray.getJSONObject(i);
                                    String dis = jsonObject.getString("dname");
                                    String disid = jsonObject.getString("did");
                                    diss.setDname(dis);
                                    diss.setDid(disid);
                                    districts.add(diss);
                                }
//                                Toast.makeText(getApplicationContext(),""+districts.size(),Toast.LENGTH_LONG).show();
                               for(int j = 0; j<districts.size(); j++ )
                               {
                                   Districts d = districts.get(j);
//                                   System.out.println(""+d.getDid());
                                   if(udist.equalsIgnoreCase(d.getDname()))
                                   {
                                       String dids = d.getDid();
                                       senddatatoapi(uname,uemail,umobile,upass1,dids);
                                   }
                               }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
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
                                }
                            }
                            catch (Exception e)
                            {

                            }
                        }
                    }
                })
        {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
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
        volleyRequestQueue.add(request);
    }


}