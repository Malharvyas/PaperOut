package com.example.paperout;

public class Test {
    public String tid;
    public String testname;
    public String test_date;
    public String test_start_time;
    public String test_end_time;
    public String attempt_test;
    public String total_attempt_time;

    public Test()
    {

    }

    public Test(String tid, String testname, String test_date, String test_start_time, String test_end_time, String attempt_test, String total_attempt_time) {
        this.tid = tid;
        this.testname = testname;
        this.test_date = test_date;
        this.test_start_time = test_start_time;
        this.test_end_time = test_end_time;
        this.attempt_test = attempt_test;
        this.total_attempt_time = total_attempt_time;
    }

    public String getTotal_attempt_time() {
        return total_attempt_time;
    }

    public void setTotal_attempt_time(String total_attempt_time) {
        this.total_attempt_time = total_attempt_time;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getTestname() {
        return testname;
    }

    public void setTestname(String testname) {
        this.testname = testname;
    }

    public String getTest_date() {
        return test_date;
    }

    public void setTest_date(String test_date) {
        this.test_date = test_date;
    }

    public String getTest_start_time() {
        return test_start_time;
    }

    public void setTest_start_time(String test_start_time) {
        this.test_start_time = test_start_time;
    }

    public String getTest_end_time() {
        return test_end_time;
    }

    public void setTest_end_time(String test_end_time) {
        this.test_end_time = test_end_time;
    }

    public String getAttempt_test() {
        return attempt_test;
    }

    public void setAttempt_test(String attempt_test) {
        this.attempt_test = attempt_test;
    }
}
