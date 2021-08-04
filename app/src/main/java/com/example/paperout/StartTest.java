package com.example.paperout;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class StartTest extends AppCompatActivity implements TestAdapter2.onClickListener {

    TextView timer;
    String tid,t_time,hrt_time,min_time;
    int hr,millis,min;
    long mtimeleftinnMillis,time_taken;
    private CountDownTimer countDownTimer;
    private boolean mTimerRunning;
    RecyclerView showtest;
    String url = "";
    List<Test2> testlist;
    List<String> questions,answers;
    List<Answer> multianswer;
    LinearLayoutManager linearLayoutManager;
    RecyclerView tests;
    RecyclerView.Adapter adapter;
    DividerItemDecoration dividerItemDecoration;
    Button submit;
    RadioGroup group;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_test);

        timer = findViewById(R.id.timer);
        showtest = findViewById(R.id.show_test_recycler2);

        Intent i = getIntent();
        tid = i.getExtras().getString("tid");
        t_time = i.getExtras().getString("t_time");



        char c = '\0';
        StringBuffer hrtime = new StringBuffer();
        for(int j = 0; j < 2; j++)
        {
            c = t_time.charAt(j);
            if(c == ':')
            {
                continue;
            }
            else
            {
                hrtime = hrtime.append(c);
            }
        }
        hrt_time = String.valueOf(hrtime);
        hr = Integer.parseInt(hrt_time);
        millis = hr * 60 * 60 * 1000;
        //for minutes
        StringBuffer mintime = new StringBuffer();
        for(int j = 3; j < 5; j++)
        {
            c = t_time.charAt(j);
            if(c == ':')
            {
                continue;
            }
            else
            {
                mintime = mintime.append(c);
            }
        }
        min_time = String.valueOf(mintime);
        min = Integer.parseInt(min_time);
        millis = millis+(min * 60  * 1000);
        mtimeleftinnMillis = millis;

        countDownTimer = new CountDownTimer(mtimeleftinnMillis,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mtimeleftinnMillis = millisUntilFinished;
                updateText();
            }

            @Override
            public void onFinish() {
                mTimerRunning = false;
                onClicked(testlist.size()-1);
            }
        }.start();

        questions = new ArrayList<>();
        answers = new ArrayList<>();
        testlist = new ArrayList<>();
        multianswer = new ArrayList<>();
        adapter = new TestAdapter2(getApplicationContext(),testlist,this);
        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        tests = findViewById(R.id.show_test_recycler2);
        dividerItemDecoration = new DividerItemDecoration(tests.getContext(),linearLayoutManager.getOrientation());
        tests.setHasFixedSize(true);
        tests.setLayoutManager(linearLayoutManager);
        tests.addItemDecoration(dividerItemDecoration);
        tests.setAdapter(adapter);
        showquestions(tid);
    }


    private void showquestions(String tid) {
        BaseUrl b = new BaseUrl();
        url = b.url;
        url = url.concat("hrcet/index.php/api/Test/quetion_set");
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
                                JSONArray pdfsarray = json.getJSONArray("data");
                                for(int i = 0; i < pdfsarray.length(); i++ )
                                {
                                    JSONObject imgobj = pdfsarray.getJSONObject(i);
                                    Test2 tes = new Test2();
                                    String tqid = imgobj.getString("tqid");
                                    String question =imgobj.getString("question");
                                    String mcq1  = imgobj.getString("mcq1");
                                    String mcq2  = imgobj.getString("mcq2");
                                    String mcq3  = imgobj.getString("mcq3");
                                    String mcq4  = imgobj.getString("mcq4");
                                    String multiselect = imgobj.getString("multiselect");
                                    String tid = imgobj.getString("tid");

                                        tes.setTid(tid);
                                        tes.setTqid(tqid);
                                        tes.setQuestion(question);
                                        tes.setMcq1(mcq1);
                                        tes.setMcq2(mcq2);
                                        tes.setMcq3(mcq3);
                                        tes.setMcq4(mcq4);
                                        tes.setMultiselect(multiselect);
                                        testlist.add(tes);
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
                params.put("tid", tid);
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

    private void updateText() {
        int hours = (int) (mtimeleftinnMillis / 1000) / 60 / 60;
        int minute = (int) (mtimeleftinnMillis / 1000) / 60 % 60 ;
        int seconds = (int) (mtimeleftinnMillis / 1000)  % 60;

        String timeleft = String.format(Locale.getDefault(),"%02d:%02d:%02d",hours,minute,seconds);
        timer.setText(timeleft);
    }

    @Override
    public void onClicked(int position) {

        for(int i = 0; i < testlist.size(); i++)
        {
            SharedPreferences ans = getSharedPreferences("answers"+i, Context.MODE_PRIVATE);
            Test2 t = testlist.get(i);
            if(t.getMultiselect().equals("1"))
            {
                String op1 = ans.getString("ch1"+i,"null");
                String op2 = ans.getString("ch2"+i,"null");
                String op3 = ans.getString("ch3"+i,"null");
                String op4 = ans.getString("ch4"+i,"null");
                Answer a = new Answer();
                if(op1 != "null")
                {
                    a.setOp1(op1);
                }
                if(op2 != "null")
                {
                    a.setOp2(op2);
                }
                if(op3 != "null")
                {
                    a.setOp3(op3);
                }
                if(op4 != "null")
                {
                    a.setOp4(op4);
                }
                multianswer.add(a);
                answers.add("multiple");
            }
            else
            {
                String answer = ans.getString(""+i,"0");
                answers.add(answer);
            }
        }
        for(int i = 0 ; i < testlist.size(); i++)
        {
            Test2 t = testlist.get(i);
            String ques = t.getTqid();
            questions.add(ques);
        }
        BaseUrl b = new BaseUrl();
        url = b.url;
        url = url.concat("hrcet/index.php/api/Test/answer");
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
                                if(stat.equals("true")) {
                                    String msg = json.getString("message");
//                                    Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
                                    new AlertDialog.Builder(getApplicationContext(),R.style.Theme_AppCompat_Light_Dialog)
                                            .setTitle("Test Status")
                                            .setMessage("Test has been successfully submitted!")
                                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    startActivity(new Intent(getApplicationContext(),Dashboard.class));
                                                }
                                            }).show();
//                                    JSONArray tarray = json.getJSONArray("data");
//                                        TsetData tdata = new TsetData();
//                                        JSONObject tobj = tarray.getJSONObject(0);
//                                        String totalans = tobj.getString("total_answer");
//                                        String trueans = tobj.getString("true_answer");
//                                        String falseans = tobj.getString("false_answer");
//                                        String attempt = tobj.getString("attempt_time");
//                                        tdata.setTotal_answer(totalans);
//                                        tdata.setTrue_answer(trueans);
//                                        tdata.setFalse_answer(falseans);
//                                        tdata.setAttempt_time(attempt);
//                                        Toast.makeText(getApplicationContext(),"True : "+trueans,Toast.LENGTH_SHORT).show();
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
                SharedPreferences get = getSharedPreferences("userpref",Context.MODE_PRIVATE);
                String sr_id = get.getString("sr_id","0");
                time_taken = millis-mtimeleftinnMillis;
                time_taken = time_taken/1000;
                int hour = (int) time_taken / 60 / 60;
                int min = (int) time_taken / 60 ;
                int sec = (int) time_taken;
                Map<String, String> params = new HashMap<String, String>();
                Test2 t1 = testlist.get(0);
                params.put("tid",t1.getTid());
                params.put("sr_id",sr_id);
                params.put("attempt_time",""+hour+":"+min+":"+sec);
                String op1,op2,op3,op4;
                int pos = 0;
                for(int i = 0;i < testlist.size(); i++)
                {
                    String que = questions.get(i);
                    params.put("tqid["+i+"]",que);
                }
                for(int i = 0 ; i < testlist.size(); i++)
                {
                    if(answers.get(i).equals("multiple"))
                    {

                        StringBuffer sb = new StringBuffer();
                        Answer a = multianswer.get(pos);
                        if(a.getOp1() != null )
                        {
                            op1 = a.getOp1();
                            sb.append(op1+",");
                        }
                        if(a.getOp2() != null )
                        {
                            op2 = a.getOp2();
                            sb.append(op2+",");
                        }
                        if(a.getOp3() != null )
                        {
                            op3 = a.getOp3();
                            sb.append(op3+",");
                        }
                        if(a.getOp4() != null)
                        {
                            op4 = a.getOp4();
                            sb.append(op4+",");
                        }
                        sb.setLength(sb.length()-1);
//                        Toast.makeText(getApplicationContext(),""+sb,Toast.LENGTH_SHORT).show();
                        String ans = String.valueOf(sb);
                        params.put("answer["+i+"]",ans);
                    }
                    else
                    {
                        String ans = answers.get(i);
                        params.put("answer["+i+"]",ans);
                    }
                    pos++;
                }
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

//    @Override
//    public void onDownload(int position) {
//
//    }
}
