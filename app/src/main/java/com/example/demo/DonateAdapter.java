package com.example.demo;

public class DonateAdapter {
    String donateId;
    String phone;
    String effort;
    String amount;
    String inputDate;
    String donateDate;

    public DonateAdapter() {

    }

    public DonateAdapter(String donateId, String phone, String effort, String amount, String inputDate, String donateDate) {
        this.donateId = donateId;
        this.phone = phone;
        this.effort = effort;
        this.amount = amount;
        this.inputDate = inputDate;
        this.donateDate = donateDate;
    }

    public String getDonateDate() {
        return donateDate;
    }

    public void setDonateDate(String donateDate) {
        this.donateDate = donateDate;
    }

    public String getInputDate() {
        return inputDate;
    }

    public void setInputDate(String inputDate) {
        this.inputDate = inputDate;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDonateId() {
        return donateId;
    }

    public void setDonateId(String donateId) {
        this.donateId = donateId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEffort() {
        return effort;
    }

    public void setEffort(String effort) {
        this.effort = effort;
    }
}
