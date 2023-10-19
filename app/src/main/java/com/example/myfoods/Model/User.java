package com.example.myfoods.Model;

public class User {
    private String Name;
    private String Password;
    private String Phone;
    private String IsStaff;
    private String SecureCode;

    public User(){

    }

    public User(String name, String password, String phone, String secureCode) {
        this.Name = name;
        Password = password;
        Phone = phone;
        IsStaff = "false";
        this.SecureCode = secureCode;
    }

    public String getSecureCode() {
        return SecureCode;
    }

    public void setSecureCode(String secureCode) {
        SecureCode = secureCode;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getIsStaff() {
        return IsStaff;
    }

    public void setIsStaff(String isStaff) {
        IsStaff = isStaff;
    }
}
