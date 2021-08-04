package com.example.paperout;

public class PlanModel {
    public String plan_id;
    public String plan_name;
    public String plan_type;
    public String plan_price;
    public String created_date;
    public String modify_date;
    public String description;
    public String is_active;

    public PlanModel() {
    }

    public PlanModel(String plan_id, String plan_name, String plan_type, String plan_price, String created_date, String modify_date, String description, String is_active) {
        this.plan_id = plan_id;
        this.plan_name = plan_name;
        this.plan_type = plan_type;
        this.plan_price = plan_price;
        this.created_date = created_date;
        this.modify_date = modify_date;
        this.description = description;
        this.is_active = is_active;
    }

    public String getPlan_id() {
        return plan_id;
    }

    public void setPlan_id(String plan_id) {
        this.plan_id = plan_id;
    }

    public String getPlan_name() {
        return plan_name;
    }

    public void setPlan_name(String plan_name) {
        this.plan_name = plan_name;
    }

    public String getPlan_type() {
        return plan_type;
    }

    public void setPlan_type(String plan_type) {
        this.plan_type = plan_type;
    }

    public String getPlan_price() {
        return plan_price;
    }

    public void setPlan_price(String plan_price) {
        this.plan_price = plan_price;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIs_active() {
        return is_active;
    }

    public void setIs_active(String is_active) {
        this.is_active = is_active;
    }
}
