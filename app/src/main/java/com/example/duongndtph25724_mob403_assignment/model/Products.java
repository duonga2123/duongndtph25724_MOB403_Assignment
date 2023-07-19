package com.example.duongndtph25724_mob403_assignment.model;

public class Products {
    String id, productname, productimage, producttype, size;
    Number price, mount;

    public Products(String id, String productname, String productimage, String producttype, String size, Number price, Number mount) {
        this.id = id;
        this.productname = productname;
        this.productimage = productimage;
        this.producttype = producttype;
        this.size = size;
        this.price = price;
        this.mount = mount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProductname() {
        return productname;
    }

    public void setProductname(String productname) {
        this.productname = productname;
    }

    public String getProductimage() {
        return productimage;
    }

    public void setProductimage(String productimage) {
        this.productimage = productimage;
    }

    public String getProducttype() {
        return producttype;
    }

    public void setProducttype(String producttype) {
        this.producttype = producttype;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
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