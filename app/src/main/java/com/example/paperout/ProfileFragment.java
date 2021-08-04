package com.example.paperout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ProfileFragment extends Fragment {

    String did="";
    String url = "";
    String sr_id = "",username = "",useremail = "",usermobile="";
    int checkclick = 0;
    Spinner dspinner;
    ArrayList<String> dislist = new ArrayList<String>();
    List<Districts> districts;
    Button update_profile,signout;
    EditText update_username,update_email,update_mobile;
    String updatedname,updatedemail,updatedmobile,updateddistrict;


    public ProfileFragment() {
        // Required empty public constructor
    }


    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        dspinner = v.findViewById(R.id.dspinner);
        update_username = v.findViewById(R.id.update_username);
        update_email = v.findViewById(R.id.update_email);
        update_mobile = v.findViewById(R.id.update_mobile);
        update_profile = v.findViewById(R.id.update_profile);
//        signout = v.findViewById(R.id.signout);

        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("userpref", Context.MODE_PRIVATE);
        did = sharedPreferences.getString("district_id","0");
        sr_id = sharedPreferences.getString("sr_id","0");
        username = sharedPreferences.getString("uname","0");
        useremail = sharedPreferences.getString("email","0");
        usermobile = sharedPreferences.getString("mobile","0");
        update_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatedname = update_username.getText().toString();
                updatedemail = update_email.getText().toString();
                updatedmobile = update_mobile.getText().toString();
                updateddistrict = dspinner.getSelectedItem().toString();
//                update(updatedname,updatedemail,updatedmobile,did,sr_id);
                getdid(updateddistrict,updatedname,updatedemail,updatedmobile,sr_id);
            }
        });

//        signout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                signoutprocess();
//            }
//        });

        getotherdetails();
        getdistricts();
        return v;
    }

    private void getdid(String updateddistrict, String updatedname, String updatedemail, String updatedmobile, String sr_id) {
        BaseUrl b = new BaseUrl();
        url = b.url;
        url = url.concat("hrcet/index.php/api/District/district") ;
        RequestQueue volleyRequestQueue = Volley.newRequestQueue(getContext());

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
                                    if(updateddistrict.equalsIgnoreCase(d.getDname()))
                                    {
                                        String dids = d.getDid();
//                                        senddatatoapi(uname,uemail,umobile,upass1,dids);
                                        update(updatedname,updatedemail,updatedmobile,dids,sr_id);
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

//    private void signoutprocess() {
//        SharedPreferences logincredentials = this.getActivity().getSharedPreferences("userlogin", Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor1 = logincredentials.edit();
//        editor1.clear();
//        editor1.apply();
//        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("userpref", Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.clear();
//        editor.apply();
//        Intent i = new Intent(getContext(),Login.class);
//        startActivity(i);
//        getActivity().finish();
//    }

    private void update(String updatedname, String updatedemail, String updatedmobile, String did, String sr_id) {
        BaseUrl b = new BaseUrl();
        url = b.url;
        url = url.concat("hrcet/index.php/api/Register/edit_profile");
        RequestQueue volleyRequestQueue = Volley.newRequestQueue(getContext());
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
                                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences("userpref", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("uname",updatedname);
                                    editor.putString("email",updatedemail);
                                    editor.putString("mobile",updatedmobile);
                                    editor.putString("district_id",did);
                                    editor.apply();
                                    Toast.makeText(getContext(),msg,Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(getContext(),msg,Toast.LENGTH_LONG).show();
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
                params.put("sr_id", sr_id);
                params.put("fname",updatedname);
                params.put("email",updatedemail);
                params.put("mobile",updatedmobile);
                params.put("district_id",did);
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

    private void getotherdetails() {
        update_username.setText(username);
        update_email.setText(useremail);
        update_mobile.setText(usermobile);
    }

    private void getdistricts() {
        BaseUrl b = new BaseUrl();
        url = b.url;
        url = url.concat("hrcet/index.php/api/District/district") ;
        RequestQueue volleyRequestQueue = Volley.newRequestQueue(getContext());

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
                                    dislist.add(dis);
                                }
                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item,dislist);
                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                dspinner.setAdapter(adapter);
                                for(int j = 0; j<districts.size(); j++ )
                                {
                                    Districts d = districts.get(j);
                                    if(did.equalsIgnoreCase(d.getDid()))
                                    {
                                        String dids = d.getDid();
                                        int ind = Integer.parseInt(dids);
                                        ind = ind - 1;
                                        dspinner.setSelection(ind);
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
                                    Toast.makeText(getContext(),msg,Toast.LENGTH_LONG).show();
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