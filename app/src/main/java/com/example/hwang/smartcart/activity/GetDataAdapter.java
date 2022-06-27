package com.example.hwang.smartcart.activity;

public class GetDataAdapter {

    public String primaryKey; // 고유번호
    public String ImageServerUrl; // 제품사진 URL
    public String ImageTitleName; // 제품명
    public String ProductPrice; // 가격
    public String Amount; // 수량


    public void setAmount(String amount) {
        Amount = amount;
    }

    public String getAmount() {
        return Amount;
    }

    public void setPrimaryKey(String primaryKey) {
        this.primaryKey = primaryKey;
    }

    public String getPrimaryKey() {
        return primaryKey;
    }

    public void setProductPrice(String productPrice) {
        ProductPrice = productPrice;
    }

    public String getProductPrice() {
        return ProductPrice;
    }

    public String getImageServerUrl() {
        return ImageServerUrl;
    }

    public void setImageServerUrl(String imageServerUrl) {
        this.ImageServerUrl = imageServerUrl;
    }

    public String getImageTitleName() {
        return ImageTitleName;
    }

    public void setImageTitleNamee(String Imagetitlename) {
        this.ImageTitleName = Imagetitlename;
    }

}