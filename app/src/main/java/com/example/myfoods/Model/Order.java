package com.example.myfoods.Model;

public class Order {
    private String UserPhone;
    private String IdProduct;
    private String NameProduct;
    private String Quantity;
    private String Price;
    private String Discount;
    private String Image;

    public Order() {
    }

    public Order(String userPhone, String idProduct, String nameProduct, String quantity, String price, String discount, String image) {
        UserPhone = userPhone;
        IdProduct = idProduct;
        NameProduct = nameProduct;
        Quantity = quantity;
        Price = price;
        Discount = discount;
        Image = image;
    }

    public String getUserPhone() {
        return UserPhone;
    }

    public void setUserPhone(String userPhone) {
        UserPhone = userPhone;
    }

    public String getIdProduct() {
        return IdProduct;
    }

    public void setIdProduct(String idProduct) {
        IdProduct = idProduct;
    }

    public String getNameProduct() {
        return NameProduct;
    }

    public void setNameProduct(String nameProduct) {
        NameProduct = nameProduct;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getDiscount() {
        return Discount;
    }

    public void setDiscount(String discount) {
        Discount = discount;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }
}
