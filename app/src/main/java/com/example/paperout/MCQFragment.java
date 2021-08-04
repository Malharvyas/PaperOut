package com.example.paperout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MCQFragment extends Fragment implements TestAdapter.onClickListener{

    String url = "";
    String sr_id = "";
    List<Test> testList;
    LinearLayoutManager linearLayoutManager;
    RecyclerView tests;
    RecyclerView.Adapter adapter;
    DividerItemDecoration dividerItemDecoration;

    public MCQFragment() {
        // Required empty public constructor
    }


    public static MCQFragment newInstance(String param1, String param2) {
        MCQFragment fragment = new MCQFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("userpref", Context.MODE_PRIVATE);
        sr_id = sharedPreferences.getString("sr_id","0");

        testList = new ArrayList<>();
        adapter = new TestAdapter(getContext(),testList,this);
        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        getTest(sr_id);

    }

    private void getTest(String sr_id) {
        BaseUrl b = new BaseUrl();
        url = b.url;
        url = url.concat("hrcet/index.php/api/Test/test");
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
                                JSONArray testarray = json.getJSONArray("data");
                                for(int i = 0; i < testarray.length(); i++ )
                                {
                                    JSONObject imgobj = testarray.getJSONObject(i);
                                    Test test = new Test();
                                    String testname = imgobj.getString("testname");
                                    String testdate =imgobj.getString("test_date");
                                    String testid = imgobj.getString("tid");
                                    String teststartime = imgobj.getString("test_start_time");
                                    String testendtime = imgobj.getString("test_end_time");
                                    String testtime = imgobj.getString("total_attempt_time");
                                    String testattempt = imgobj.getString("attempt_test");
                                    test.setTid(testid);
                                    test.setTestname(testname);
                                    test.setTest_date(testdate);
                                    test.setTest_start_time(teststartime);
                                    test.setTest_end_time(testendtime);
                                    test.setAttempt_test(testattempt);
                                    test.setTotal_attempt_time(testtime);
                                    testList.add(test);

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_m_c_q, container, false);
        tests = view.findViewById(R.id.show_test_recycler);
        dividerItemDecoration = new DividerItemDecoration(tests.getContext(),linearLayoutManager.getOrientation());
        tests.setHasFixedSize(true);
        tests.setLayoutManager(linearLayoutManager);
        tests.addItemDecoration(dividerItemDecoration);
        tests.setAdapter(adapter);
        return view;
    }

    @Override
    public void onClicked(int position) {
        Test t = testList.get(position);

        String attempt = t.getAttempt_test();
//        Toast.makeText(getContext(),attempt,Toast.LENGTH_SHORT).show();
        if(attempt.equals("1"))
        {
            Toast.makeText(getContext(),"You have already given the test!!",Toast.LENGTH_SHORT).show();
        }
        else
        {
            Intent i = new Intent(getContext(),TestDetails.class);
            i.putExtra("tid",String.valueOf(t.getTid()));
            i.putExtra("tstart",String.valueOf(t.getTest_start_time()));
            i.putExtra("tend",String.valueOf(t.getTest_end_time()));
            i.putExtra("t_time",String.valueOf(t.getTotal_attempt_time()));
            startActivity(i);
        }

    }

    @Override
    public void onDownload(int position) {

    }
}