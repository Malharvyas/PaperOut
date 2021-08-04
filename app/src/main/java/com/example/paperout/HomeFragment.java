package com.example.paperout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.os.Environment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.ClientError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class HomeFragment extends Fragment implements PDFAdapter.onClickListener{

    String did="";
    String url = "";
    String sr_id = "";
    List<PDF> pdflist;
    LinearLayoutManager linearLayoutManager;
    RecyclerView pdfs;
    RecyclerView.Adapter adapter;
    DividerItemDecoration dividerItemDecoration;
    ViewPager viewPager,viewtab;
    ArrayList<String> banners = new ArrayList<String>();
    MyCustomAdapter myCustomAdapter;
    TabLayout tabLayout;
    TabItem paidtab,freetab;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("userpref", Context.MODE_PRIVATE);
        did = sharedPreferences.getString("district_id","0");
        sr_id = sharedPreferences.getString("sr_id","0");

        pdflist = new ArrayList<>();
        adapter = new PDFAdapter(getContext(),pdflist,this);
        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        getPDF(sr_id);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        viewPager = v.findViewById(R.id.banner1);

        pdfs = v.findViewById(R.id.pdfrecycler2);
//        dividerItemDecoration = new DividerItemDecoration(pdfs.getContext(),linearLayoutManager.getOrientation());
        pdfs.setHasFixedSize(true);
        pdfs.setLayoutManager(linearLayoutManager);
//        pdfs.addItemDecoration(dividerItemDecoration);
        pdfs.setAdapter(adapter);

        getBanners(did);
        return v;
    }

    private void getPDF(String sr_id) {
        BaseUrl b = new BaseUrl();
        url = b.url;
        url = url.concat("hrcet/index.php/api/Pdf/pdf");
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
                                JSONArray pdfsarray = json.getJSONArray("data");
                                for(int i = 0; i < pdfsarray.length(); i++ )
                                {
                                    JSONObject imgobj = pdfsarray.getJSONObject(i);
                                    PDF pdf = new PDF();
                                    String pdfname = imgobj.getString("pdf_name");
                                    String pdfdate =imgobj.getString("created_date");
                                    String pdfurl  = imgobj.getString("pdf_url");
                                    String pdfques = imgobj.getString("total_question");
                                    String paid = imgobj.getString("paid_free");
                                    if(paid.equals("0"))
                                    {
                                        pdf.setCreated_date(pdfdate);
                                        pdf.setPdf_name(pdfname);
                                        pdf.setPdf_url(pdfurl);
                                        pdf.setTotal_question(pdfques);
                                        pdflist.add(pdf);
                                    }

                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            adapter.notifyDataSetChanged();
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
                params.put("sr_id", sr_id);
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

    private void getBanners(String did) {
        BaseUrl b = new BaseUrl();
        url = b.url;
        url =  url.concat("hrcet/index.php/api/Banner/banner");
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
                                    JSONObject imgobj = imagesarray.getJSONObject(i);
                                    String bimg = imgobj.getString("banner_img");
                                    banners.add(bimg);
                                }
                                myCustomAdapter = new MyCustomAdapter(getContext(), banners);
                                viewPager.setAdapter(myCustomAdapter);

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
                params.put("district_id", did);
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
    public void onClicked(int position) {
        PDF p = pdflist.get(position);
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(String.valueOf(p.getPdf_url())));
        startActivity(browserIntent);
    }

    @Override
    public void onDownload(int position) {
//        PDF p1 = pdflist.get(position);
//        new DownloadFile(String.valueOf(p1.getPdf_name())).execute(String.valueOf(p1.getPdf_url()));
    }

//    private class DownloadFile extends AsyncTask<String, Void, Void> {
//        String fname;
//        public DownloadFile(String s) {
//            this.fname = s;
//        }
//
//        @Override
//        protected Void doInBackground(String... strings) {
//            String fileUrl = strings[0];
//            String fileName = fname+".pdf";
//            String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
//            File folder = new File(extStorageDirectory,"Download");
//            folder.mkdir();
//
//            File pdfFile = new File(folder, fileName);
//
//            try{
//                pdfFile.createNewFile();
//            }catch (IOException e){
//                e.printStackTrace();
//            }
//            PdfDownloader.DownloadFile(fileUrl, pdfFile);
//            return null;
//        }
//        @Override
//        protected void onPostExecute(Void aVoid) {
//            super.onPostExecute(aVoid);
//            Toast.makeText(getContext(), "Download PDf successfully", Toast.LENGTH_SHORT).show();
//        }
//    }
}