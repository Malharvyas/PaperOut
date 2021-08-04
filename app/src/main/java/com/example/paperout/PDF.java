package com.example.paperout;

import com.android.volley.toolbox.StringRequest;

public class PDF {
    public String pdf_name;
    public String pdf_url;
    public String created_date;
    public String total_question;

    public PDF(){

    }

    public PDF(String pdf_name, String pdf_url, String created_date, String total_question) {
        this.pdf_name = pdf_name;
        this.pdf_url = pdf_url;
        this.created_date = created_date;
        this.total_question = total_question;
    }

    public String getTotal_question() {
        return total_question;
    }

    public void setTotal_question(String total_question) {
        this.total_question = total_question;
    }

    public String getPdf_name() {
        return pdf_name;
    }

    public void setPdf_name(String pdf_name) {
        this.pdf_name = pdf_name;
    }

    public String getPdf_url() {
        return pdf_url;
    }

    public void setPdf_url(String pdf_url) {
        this.pdf_url = pdf_url;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }
}
