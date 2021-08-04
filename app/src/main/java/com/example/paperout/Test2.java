package com.example.paperout;

public class Test2 {
    String tid;
    String tqid;
    String question;
    String mcq1;
    String mcq2;
    String mcq3;
    String mcq4;
    String multiselect;

    public Test2() {
    }

    public Test2(String tqid, String question, String mcq1, String mcq2, String mcq3, String mcq4, String multiselect,String tid) {
        this.tqid = tqid;
        this.question = question;
        this.mcq1 = mcq1;
        this.mcq2 = mcq2;
        this.mcq3 = mcq3;
        this.mcq4 = mcq4;
        this.multiselect = multiselect;
        this.tid = tid;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getTqid() {
        return tqid;
    }

    public void setTqid(String tqid) {
        this.tqid = tqid;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getMcq1() {
        return mcq1;
    }

    public void setMcq1(String mcq1) {
        this.mcq1 = mcq1;
    }

    public String getMcq2() {
        return mcq2;
    }

    public void setMcq2(String mcq2) {
        this.mcq2 = mcq2;
    }

    public String getMcq3() {
        return mcq3;
    }

    public void setMcq3(String mcq3) {
        this.mcq3 = mcq3;
    }

    public String getMcq4() {
        return mcq4;
    }

    public void setMcq4(String mcq4) {
        this.mcq4 = mcq4;
    }

    public String getMultiselect() {
        return multiselect;
    }

    public void setMultiselect(String multiselect) {
        this.multiselect = multiselect;
    }
}
