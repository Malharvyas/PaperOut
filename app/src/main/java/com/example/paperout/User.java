package com.example.paperout;

import android.app.Application;

public class User {
    public static String sr_id;
    public String fname;
    public String lname;
    public String email;
    public String mobile;
    public String district_id;
    public String password;
    public String active;
    public String created_date;
    public String modify_date;
    public String subscription;
    public String plan_id;

    public User(String sr_id, String fname, String lname, String email, String mobile, String district_id, String password, String active, String created_date, String modify_date, String subscription, String plan_id) {
        this.sr_id = sr_id;
        this.fname = fname;
        this.lname = lname;
        this.email = email;
        this.mobile = mobile;
        this.district_id = district_id;
        this.password = password;
        this.active = active;
        this.created_date = created_date;
        this.modify_date = modify_date;
        this.subscription = subscription;
        this.plan_id = plan_id;
    }

    public User() {

    }

    public String getSr_id() {
        return sr_id;
    }

    public void setSr_id(String sr_id) {
        this.sr_id = sr_id;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getDistrict_id() {
        return district_id;
    }

    public void setDistrict_id(String district_id) {
        this.district_id = district_id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }

    public String getModify_date() {
        return modify_date;
    }

    public void setModify_date(String modify_date) {
        this.modify_date = modify_date;
    }

    public String getSubscription() {
        return subscription;
    }

    public void setSubscription(String subscription) {
        this.subscription = subscription;
    }

    public String getPlan_id() {
        return plan_id;
    }

    public void setPlan_id(String plan_id) {
        this.plan_id = plan_id;
    }
}
