package com.example.paperout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.ClientError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.razorpay.PaymentResultListener;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Dashboard extends AppCompatActivity implements PaymentResultListener, NavigationView.OnNavigationItemSelectedListener {

    BottomNavigationView bottomNavigationView;
    NavigationView navigationView;
    String log_status = "";
    Toolbar toolbar;
    ImageView nav_profile;
    DrawerLayout drawerLayout;
    ImageView header_logo;
    String url = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        SharedPreferences logincredentials = getSharedPreferences("userlogin", Context.MODE_PRIVATE);
        log_status = logincredentials.getString("logged","NA");
        if(log_status.equals("true"))
        {

        }
        else
        {
            Intent i = new Intent(getApplicationContext(),Login.class);
            startActivity(i);
            finish();
        }

        getheaderlogo();

        nav_profile = findViewById(R.id.nav_profile);
        bottomNavigationView = findViewById(R.id.bottom_nav);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        header_logo = findViewById(R.id.header_logo);

        navigationView.setNavigationItemSelectedListener(this);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,
                R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


        if (savedInstanceState == null) {
            HomeFragment hf = new HomeFragment();
            FragmentTransaction ft1 = getSupportFragmentManager().beginTransaction();
            ft1.replace(R.id.container1,hf);
            ft1.commit();
        }
        nav_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomNavigationView.getMenu().setGroupCheckable(0,false,true);
                ProfileFragment pf = new ProfileFragment();
                FragmentTransaction ft2 = getSupportFragmentManager().beginTransaction();
                ft2.replace(R.id.container1,pf);
                ft2.commit();
            }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                bottomNavigationView.getMenu().setGroupCheckable(0,true,true);
                switch ((item.getItemId()))
                {
                    case R.id.nav_home:
                    {
                        HomeFragment hf = new HomeFragment();
                        FragmentTransaction ft1 = getSupportFragmentManager().beginTransaction();
                        ft1.replace(R.id.container1,hf);
                        ft1.commit();
                    }
                    break;
                    case R.id.nav_plans:
                    {
//                        ProfileFragment pf = new ProfileFragment();
//                        FragmentTransaction ft2 = getSupportFragmentManager().beginTransaction();
//                        ft2.replace(R.id.container1,pf);
//                        ft2.commit();
                        PremiumFragment pf = new PremiumFragment();
                        FragmentTransaction ft4 = getSupportFragmentManager().beginTransaction();
                        ft4.replace(R.id.container1,pf);
                        ft4.commit();
                    }
                    break;
                    case R.id.mcq_test:
                    {
                        MCQFragment mf = new MCQFragment();
                        FragmentTransaction ft3 = getSupportFragmentManager().beginTransaction();
                        ft3.replace(R.id.container1,mf);
                        ft3.commit();
                    }
                    break;
                    case R.id.nav_answers:
                    {
                        PaidFragment pf = new PaidFragment();
                        FragmentTransaction ft4 = getSupportFragmentManager().beginTransaction();
                        ft4.replace(R.id.container1,pf);
                        ft4.commit();
                    }
                    break;
                }
                return true;
            }
        });
    }

    private void getheaderlogo() {
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
                                    String image = data1.getString("header_logo_image");
                                    Picasso.get().load(image).fit().into(header_logo);
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

//    @Override
//    protected void onRestart() {
//        super.onRestart();
//        Fragment frg = null;
//        frg = getSupportFragmentManager().findFragmentByTag("PremiumFragment");
//        final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//        ft.detach(frg);
//        ft.attach(frg);
//        ft.commit();
//    }

    @Override
    public void onPaymentSuccess(String s) {
        SharedPreferences sharedPreferences = getSharedPreferences("planids", Context.MODE_PRIVATE);
        String selected = sharedPreferences.getString("selected","NA");
        SharedPreferences sharedPreferences2 = getSharedPreferences("userpref", Context.MODE_PRIVATE);
        String sr_id = sharedPreferences2.getString("sr_id","0");
        BaseUrl b = new BaseUrl();
        url = b.url;
        url =  url.concat("hrcet/index.php/api/PlanActive/plan_active");
        RequestQueue volleyRequestQueue = Volley.newRequestQueue(getApplicationContext());
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
                                    Toast.makeText(getApplicationContext(),""+msg,Toast.LENGTH_SHORT).show();
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
                else
                {
                    Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_LONG).show();
                }
            }
        }){
            @Override
            protected Map<String,String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("sr_id", sr_id);
                params.put("plan_id", selected);
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
//        Toast.makeText(getApplicationContext(),""+s,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(getApplicationContext(),"Cancelled !!" +
                "",Toast.LENGTH_LONG).show();
    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.nav_logout:
            {
                SharedPreferences logincredentials = getSharedPreferences("userlogin", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor1 = logincredentials.edit();
                editor1.clear();
                editor1.apply();
                SharedPreferences sharedPreferences = getSharedPreferences("userpref", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();
                Intent i = new Intent(getApplicationContext(),Login.class);
                startActivity(i);
                finish();
//                Toast.makeText(getApplicationContext(),"Logout",Toast.LENGTH_SHORT).show();
            }
            break;
            case R.id.nav_about:
            {
                AboutUsFragment af = new AboutUsFragment();
                FragmentTransaction ft5 = getSupportFragmentManager().beginTransaction();
                ft5.replace(R.id.container1,af);
                ft5.commit();
            }
            break;
            case R.id.nav_share:
            {
                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My application name");
                    String shareMessage= "\nLet me recommend you this application\n\n";
                    shareMessage = shareMessage + "Application Link will be added here" + BuildConfig.APPLICATION_ID +"\n\n";
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    startActivity(Intent.createChooser(shareIntent, "choose one"));
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
            break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else{
            super.onBackPressed();
        }
    }
}