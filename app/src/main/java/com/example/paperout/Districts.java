package com.example.paperout;

public class Districts {
    public String did;
    public String dname;

    public Districts() {
    }

    public Districts(String did, String dname) {
        this.did = did;
        this.dname = dname;
    }

    public String getDid() {
        return did;
    }

    public void setDid(String did) {
        this.did = did;
    }

    public String getDname() {
        return dname;
    }

    public void setDname(String dname) {
        this.dname = dname;
    }
}
