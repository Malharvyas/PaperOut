package com.example.paperout;

public class TsetData {
    String total_answer;
    String true_answer;
    String false_answer;
    String attempt_time;

    public TsetData() {
    }

    public TsetData(String total_answer, String true_answer, String false_answer, String attempt_time) {
        this.total_answer = total_answer;
        this.true_answer = true_answer;
        this.false_answer = false_answer;
        this.attempt_time = attempt_time;
    }

    public String getTotal_answer() {
        return total_answer;
    }

    public void setTotal_answer(String total_answer) {
        this.total_answer = total_answer;
    }

    public String getTrue_answer() {
        return true_answer;
    }

    public void setTrue_answer(String true_answer) {
        this.true_answer = true_answer;
    }

    public String getFalse_answer() {
        return false_answer;
    }

    public void setFalse_answer(String false_answer) {
        this.false_answer = false_answer;
    }

    public String getAttempt_time() {
        return attempt_time;
    }

    public void setAttempt_time(String attempt_time) {
        this.attempt_time = attempt_time;
    }
}
