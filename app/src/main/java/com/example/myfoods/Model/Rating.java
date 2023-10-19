package com.example.myfoods.Model;

public class Rating {
    private String userPhone;
    private String foodID;
    private String rateValue;
    private String comment;

    public Rating() {
    }

    public Rating(String userPhone, String foodID, String rateValue, String comment) {
        this.userPhone = userPhone;
        this.foodID = foodID;
        this.rateValue = rateValue;
        this.comment = comment;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getFoodID() {
        return foodID;
    }

    public void setFoodID(String foodID) {
        this.foodID = foodID;
    }

    public String getRateValue() {
        return rateValue;
    }

    public void setRateValue(String rateValue) {
        this.rateValue = rateValue;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
