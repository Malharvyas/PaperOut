package com.example.paperout;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.ClientError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.relex.circleindicator.CircleIndicator;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;


public class PremiumFragment extends Fragment implements PaymentResultListener {

    String did="";
    String url = "";
    String sr_id = "",username = "",useremail = "",usermobile="";
    TextView p1plan_name,p1plan_price,p1_duration,p1plan_desc;
    TextView p2plan_name,p2plan_price,p2_duration,p2plan_desc;
    TextView planstart,planend,referral,sumbit;
    Button p1_buy,p2_buy;
    List<PlanModel> planlist ;

    public PremiumFragment() {
        // Required empty public constructor
    }

    public static PremiumFragment newInstance(String param1, String param2) {
        PremiumFragment fragment = new PremiumFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        planlist = new ArrayList<>();

        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("userpref", Context.MODE_PRIVATE);
        did = sharedPreferences.getString("district_id","0");
        sr_id = sharedPreferences.getString("sr_id","0");

        getplandetails(sr_id);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_premium, container, false);
        p1plan_name = v.findViewById(R.id.p1plan_name);
        p1plan_price = v.findViewById(R.id.p1plan_price);
        p1_duration = v.findViewById(R.id.p1_duration);
        p1plan_desc = v.findViewById(R.id.p1_plan_description);

        p2plan_name = v.findViewById(R.id.p2plan_name);
        p2plan_price = v.findViewById(R.id.p2plan_price);
        p2_duration = v.findViewById(R.id.p2_duration);
        p2plan_desc = v.findViewById(R.id.p2_plan_description);

        p1_buy = v.findViewById(R.id.p1_buy);
        p2_buy = v.findViewById(R.id.p2_buy);

        p1_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("planids", Context.MODE_PRIVATE);
                String selected = sharedPreferences.getString("plan1","NA");

                SharedPreferences sharedPreferences2 = getActivity().getSharedPreferences("planids", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor2 = sharedPreferences2.edit();
                editor2.putString("selected",selected);
                editor2.apply();

                String plan = "plan1";
                int amount = Integer.parseInt(p1plan_price.getText().toString());
                amount = amount*100;
                makepayment(plan,amount);
            }
        });

        p2_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("planids", Context.MODE_PRIVATE);
                String selected = sharedPreferences.getString("plan2","NA");

                SharedPreferences sharedPreferences2 = getActivity().getSharedPreferences("planids", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor2 = sharedPreferences2.edit();
                editor2.putString("selected",selected);
                editor2.apply();
                LayoutInflater inflater = (LayoutInflater)
                        getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);
                View popupView = inflater.inflate(R.layout.plans_popup, null);

                planstart = popupView.findViewById(R.id.plan_startdate);
                planend = popupView.findViewById(R.id.plan_enddate);
                referral = popupView.findViewById(R.id.referral);

                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                String current = sdf.format(new Date());
                planstart.setText("Start Date: "+current);
                Calendar c = Calendar.getInstance();
                try {
                    c.setTime(sdf.parse(current));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                c.add(Calendar.DATE, 365);
                SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy");
                String end = sdf1.format(c.getTime());
                planend.setText("End Date: "+end);

//                sumbit.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
////
//                        String ref = referral.getText().toString();
//                        BaseUrl b = new BaseUrl();
//                        url = b.url;
//                        url =  url.concat("hrcet/index.php/api/Referral/referral");
//                        RequestQueue volleyRequestQueue = Volley.newRequestQueue(getContext());
//                        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
//                                new Response.Listener<String>() {
//                                    @Override
//                                    public void onResponse(String response) {
//                                        BaseUrl b = new BaseUrl();
//                                        url = b.url;
//                                        if(response != null) {
//                                            JSONObject json = null;
//
//                                            try {
//                                                json = new JSONObject(String.valueOf(response));
//                                                Boolean status = json.getBoolean("status");
//                                                String stat = status.toString();
//                                                if(stat.equals("true"))
//                                                {
//                                                    String msg = json.getString("message");
//                                                    Toast.makeText(getContext(),""+msg,Toast.LENGTH_SHORT).show();
//                                                    String plan = "plan2";
//                                                    int amount = Integer.parseInt(p2plan_price.getText().toString());
//                                                    amount = amount*100;
//                                                    makepayment(plan,amount);
//                                                }
//
//                                            } catch (JSONException e) {
//                                                e.printStackTrace();
//                                            }
//                                        }
//                                    }
//                                }, new Response.ErrorListener() {
//                            @Override
//                            public void onErrorResponse(VolleyError error) {
//                                BaseUrl b = new BaseUrl();
//                                url = b.url;
//                                if(error instanceof ClientError)
//                                {
//                                    try{
//                                        String responsebody = new String(error.networkResponse.data,"utf-8");
//                                        JSONObject data = new JSONObject(responsebody);
//                                        Boolean status = data.getBoolean("status");
//                                        String stat = status.toString();
//                                        if(stat.equals("false"))
//                                        {
//                                            String msg = data.getString("message");
//                                            Toast.makeText(getContext(),msg,Toast.LENGTH_LONG).show();
//                                        }
//                                    }
//                                    catch (Exception e)
//                                    {
//                                        e.printStackTrace();
//                                    }
//                                }
//                                else
//                                {
//                                    Toast.makeText(getContext(),error.toString(),Toast.LENGTH_LONG).show();
//                                }
//                            }
//                        }){
//                            @Override
//                            protected Map<String,String> getParams() {
//                                Map<String, String> params = new HashMap<String, String>();
//                                params.put("sr_id", PremiumFragment.this.sr_id);
//                                params.put("referral_number", ref);
//                                return params;
//                            }
//
//                            @Override
//                            public Map<String,String> getHeaders() throws AuthFailureError {
//                                Map<String, String> headers = new HashMap<>();
//                                String credentials = "HRCET:.r*fm5D%d-Re45-)";
//                                String auth = "Basic "
//                                        + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
//                                headers.put("Authorization", auth);
//                                headers.put("x-api-key","HRCETCRACKER@123");
////                headers.put("Content-Type", "application/form-data");
//                                return headers;
//                            }
//                        };
//                        volleyRequestQueue.add(stringRequest);
//                    }
//                });


//                int width = LinearLayout.LayoutParams.WRAP_CONTENT;
//                int height = LinearLayout.LayoutParams.WRAP_CONTENT;
//
//                final PopupWindow popupWindow = new PopupWindow(popupView, width, height);
//                popupWindow.setBackgroundDrawable(getContext().getDrawable(R.drawable.border3 ));
//                popupWindow.setElevation(100);
//                popupWindow.setTouchable(true);
//                popupWindow.setFocusable(false);
//                popupWindow.setOutsideTouchable(false);
//                popupWindow.showAtLocation(getView(), Gravity.CENTER, 0, 0);
                referral.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if(referral.length()==10)
                        {

                        }
                        else {
                            referral.setError("Please write a valid mobile number");
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        getContext());

                // set alert_dialog.xml to alertdialog builder
                alertDialogBuilder.setView(popupView);

                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                String ref = referral.getText().toString();
                                if(ref.length()==10)
                                {
                                    String plan = "plan2";
                                    int amount = Integer.parseInt(p2plan_price.getText().toString());
                                    amount = amount*100;
                                    makepayment(plan,amount);
//                                    BaseUrl b = new BaseUrl();
//                                    url = b.url;
//                                    url =  url.concat("hrcet/index.php/api/Referral/referral");
//                                    RequestQueue volleyRequestQueue = Volley.newRequestQueue(getContext());
//                                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
//                                            new Response.Listener<String>() {
//                                                @Override
//                                                public void onResponse(String response) {
//                                                    BaseUrl b = new BaseUrl();
//                                                    url = b.url;
//                                                    if(response != null) {
//                                                        JSONObject json = null;
//
//                                                        try {
//                                                            json = new JSONObject(String.valueOf(response));
//                                                            Boolean status = json.getBoolean("status");
//                                                            String stat = status.toString();
//                                                            if(stat.equals("true"))
//                                                            {
//                                                                String msg = json.getString("message");
//                                                                Toast.makeText(getContext(),""+msg,Toast.LENGTH_SHORT).show();
//
//                                                            }
//
//                                                        } catch (JSONException e) {
//                                                            e.printStackTrace();
//                                                        }
//                                                    }
//                                                }
//                                            }, new Response.ErrorListener() {
//                                        @Override
//                                        public void onErrorResponse(VolleyError error) {
//                                            BaseUrl b = new BaseUrl();
//                                            url = b.url;
//                                            if(error instanceof ClientError)
//                                            {
//                                                try{
//                                                    String responsebody = new String(error.networkResponse.data,"utf-8");
//                                                    JSONObject data = new JSONObject(responsebody);
//                                                    Boolean status = data.getBoolean("status");
//                                                    String stat = status.toString();
//                                                    if(stat.equals("false"))
//                                                    {
//                                                        String msg = data.getString("message");
//                                                        Toast.makeText(getContext(),msg,Toast.LENGTH_LONG).show();
//                                                    }
//                                                }
//                                                catch (Exception e)
//                                                {
//                                                    e.printStackTrace();
//                                                }
//                                            }
//                                            else
//                                            {
//                                                Toast.makeText(getContext(),error.toString(),Toast.LENGTH_LONG).show();
//                                            }
//                                        }
//                                    }){
//                                        @Override
//                                        protected Map<String,String> getParams() {
//                                            Map<String, String> params = new HashMap<String, String>();
//                                            params.put("sr_id", PremiumFragment.this.sr_id);
//                                            params.put("referral_number", ref);
//                                            return params;
//                                        }
//
//                                        @Override
//                                        public Map<String,String> getHeaders() throws AuthFailureError {
//                                            Map<String, String> headers = new HashMap<>();
//                                            String credentials = "HRCET:.r*fm5D%d-Re45-)";
//                                            String auth = "Basic "
//                                                    + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
//                                            headers.put("Authorization", auth);
//                                            headers.put("x-api-key","HRCETCRACKER@123");
////                headers.put("Content-Type", "application/form-data");
//                                            return headers;
//                                        }
//                                    };
//                                    volleyRequestQueue.add(stringRequest);
                                }
                                else {
                                    Toast.makeText(getContext(),"Please enter a valid mobile number!",Toast.LENGTH_SHORT).show();
                                }

                            }
                        })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.getWindow().setBackgroundDrawable(getContext().getDrawable(R.drawable.border3));
                alertDialog.show();
            }
        });


        return v;
    }

    private void makepayment(String plan, int amount) {
        SharedPreferences userpref = getContext().getSharedPreferences("userpref", Context.MODE_PRIVATE);
        username = userpref.getString("uname","NA");
        useremail = userpref.getString("email","NA");
        usermobile = userpref.getString("mobile","NA");

        Checkout.preload(getContext());
        Checkout checkout = new Checkout();
        checkout.setKeyID("rzp_test_e2ZrK1Xqu13HIq");

        checkout.setImage(R.drawable.logo3);

        try {
            JSONObject options = new JSONObject();

            options.put("name", username);
            options.put("currency", "INR");
            options.put("amount", amount);//pass amount in currency subunits
            options.put("prefill.email", useremail);
            options.put("prefill.contact",usermobile);
            JSONObject retryObj = new JSONObject();
            retryObj.put("enabled", true);
            retryObj.put("max_count", 4);
            options.put("retry", retryObj);

            checkout.open((Activity) getContext(), options);

        } catch(Exception e) {
            Log.e("TAG", "Error in starting Razorpay Checkout", e);
        }
    }


    private void getplandetails(String sr_id) {
        BaseUrl b = new BaseUrl();
        url = b.url;
        url =  url.concat("hrcet/index.php/api/Membership/membership_plan");
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
                                JSONArray imagesarray = json.getJSONArray("data");
                                for(int i = 0; i < imagesarray.length(); i++ )
                                {
                                    PlanModel pm = new PlanModel();
                                    JSONObject imgobj = imagesarray.getJSONObject(i);
                                    String plan_id = imgobj.getString("plan_id");
                                    String plan_name = imgobj.getString("plan_name");
                                    String plan_type = imgobj.getString("plan_type");
                                    String plan_price = imgobj.getString("plan_price");
                                    String created_date = imgobj.getString("created_date");
                                    String modify_date = imgobj.getString("modify_date");
                                    String description = imgobj.getString("description");
                                    String is_active = imgobj.getString("is_active");

                                    pm.setPlan_id(plan_id);
                                    pm.setPlan_name(plan_name);
                                    pm.setPlan_price(plan_price);
                                    pm.setPlan_type(plan_type);
                                    pm.setCreated_date(created_date);
                                    pm.setModify_date(modify_date);
                                    pm.setDescription(description);
                                    pm.setIs_active(is_active);

                                    planlist.add(pm);
                                }
                                PlanModel plan1 = planlist.get(1);
                                PlanModel plan2 = planlist.get(0);

                                p1plan_name.setText(plan1.getPlan_name());
                                p1plan_price.setText(plan1.getPlan_price());
                                p1plan_desc.setText(plan1.getDescription());
                                p1_duration.setText("Duration: "+plan1.getPlan_type());
                                if(plan1.getIs_active().equals("1"))
                                {
                                    p1_buy.setText("Activated");
                                }

                                p2plan_name.setText(plan2.getPlan_name());
                                p2plan_price.setText(plan2.getPlan_price());
                                p2plan_desc.setText(plan2.getDescription());
                                p2_duration.setText("Duration: "+plan2.getPlan_type());
                                if(plan2.getIs_active().equals("1"))
                                {
                                    p2_buy.setText("Activated");
                                }

                                SharedPreferences shared = getActivity().getSharedPreferences("planids", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = shared.edit();
                                editor.putString("plan1",plan1.getPlan_id());
                                editor.putString("plan2",plan2.getPlan_id());
                                editor.apply();

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
                else
                {
                    Toast.makeText(getContext(),error.toString(),Toast.LENGTH_LONG).show();
                }
            }
        }){
            @Override
            protected Map<String,String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("sr_id", PremiumFragment.this.sr_id);
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

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                getActivity().getSupportFragmentManager().beginTransaction().detach(this).commitNow();
                getActivity().getSupportFragmentManager().beginTransaction().attach(this).commitNow();
            } else {
                getActivity().getSupportFragmentManager().beginTransaction().detach(this).attach(this).commit();
            }
        }
    }

    @Override
    public void onPaymentSuccess(String s) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("planids", Context.MODE_PRIVATE);
        String selected = sharedPreferences.getString("selected","NA");
        BaseUrl b = new BaseUrl();
        url = b.url;
        url =  url.concat("hrcet/index.php/api/PlanActive/plan_active");
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
                                    Toast.makeText(getContext(),"Payment Succesfull",Toast.LENGTH_SHORT).show();
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
                else
                {
                    Toast.makeText(getContext(),error.toString(),Toast.LENGTH_LONG).show();
                }
            }
        }){
            @Override
            protected Map<String,String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("sr_id", PremiumFragment.this.sr_id);
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
//        Toast.makeText(getContext(),"Payment Sucessfull",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(getContext(),""+s,Toast.LENGTH_LONG).show();
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("userpref", Context.MODE_PRIVATE);
//        sr_id = sharedPreferences.getString("sr_id","0");
//        getplandetails(sr_id);
//    }

}

