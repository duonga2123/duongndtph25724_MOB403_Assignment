package com.example.duongndtph25724_mob403_assignment.Interface;

import com.example.duongndtph25724_mob403_assignment.model.Products;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {

    Gson gson = new GsonBuilder().setDateFormat("dd-MM-yyyy").create();

    // ip may ao connect localhost: 10.0.2.2
    // genymontion: 10.0.3.2
    ApiService apiService = new Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8000/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ApiService.class);

    @GET("listProducts")
    Call<List<Products>> getProducts();

    @POST("addProducts")
    Call<List<Products>> addProducts(@Body Products products);

    @PUT("Products/{id}")
    Call<List<Products>> updateProducts(@Path("id") String id, @Body Products products);

    @DELETE("/Products/{id}")
    Call<List<Products>> deleteProducts(@Path("id") String id);

}
