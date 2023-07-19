package com.example.duongndtph25724_mob403_assignment.BottomNav;

import com.example.duongndtph25724_mob403_assignment.model.Products;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface addProducts {
    @GET("API/api_homeitem.php")
    Call<List<Products>> getProducts();
}

