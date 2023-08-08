package com.example.duongndtph25724_mob403_assignment.model;

import com.google.gson.annotations.SerializedName;

public class Products {
    @SerializedName("_id")
    String id;
    String productname, productimg, producttype;
    Number price, mount;

    public Products(String id, String productname, String productimg, String producttype, Number price, Number mount) {
        this.id = id;
        this.productname = productname;
        this.productimg = productimg;
        this.producttype = producttype;
        this.price = price;
        this.mount = mount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Products() {

    }


    public String getProductname() {
        return productname;
    }

    public void setProductname(String productname) {
        this.productname = productname;
    }

    public String getProductimg() {
        return productimg;
    }

    public void setProductimg(String productimg) {
        this.productimg = productimg;
    }

    public String getProducttype() {
        return producttype;
    }

    public void setProducttype(String producttype) {
        this.producttype = producttype;
    }

    public Number getPrice() {
        return price;
    }

    public void setPrice(Number price) {
        this.price = price;
    }

    public Number getMount() {
        return mount;
    }

    public void setMount(Number mount) {
        this.mount = mount;
    }
}