package com.example.paperout;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TestDetails extends AppCompatActivity {

    String tstart,tends,tid,t_time,t_attempt;
    Button start_test2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_details);

        start_test2 = findViewById(R.id.start_test2);

        Intent i = getIntent();
        tstart = i.getExtras().getString("tstart");
        tends  = i.getExtras().getString("tend");
        tid  = i.getExtras().getString("tid");
        t_time  = i.getExtras().getString("t_time");

        start_test2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();

                int hour = now.get(Calendar.HOUR_OF_DAY); // Get hour in 24 hour format
                int minute = now.get(Calendar.MINUTE);
                int second = now.get(Calendar.SECOND);

                Date date = parseDate(hour + ":" + minute + ":" + second);
                Date dateCompareOne = parseDate(tstart);
                Date dateCompareTwo = parseDate(tends);

//                Toast.makeText(getApplicationContext(),""+dateCompareOne + "and" + dateCompareTwo,Toast.LENGTH_LONG).show();
//                Toast.makeText(getApplicationContext(),""+tstart + "and" + tends,Toast.LENGTH_LONG).show();

                if (dateCompareOne.before( date ) && dateCompareTwo.after(date)) {
                    Intent i = new Intent(getApplicationContext(),StartTest.class);
                    i.putExtra("tid",tid);
                    i.putExtra("t_time",t_time);
                    startActivity(i);
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"You cannot give test at this point of time",Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private Date parseDate(String date) {

        final String inputFormat = "HH:mm:ss";
        SimpleDateFormat inputParser = new SimpleDateFormat(inputFormat, Locale.US);
        try {
            return inputParser.parse(date);
        } catch (java.text.ParseException e) {
            return new Date(0);
        }
    }
}