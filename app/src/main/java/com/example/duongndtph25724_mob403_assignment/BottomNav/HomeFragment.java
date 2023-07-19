package com.example.duongndtph25724_mob403_assignment.BottomNav;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.duongndtph25724_mob403_assignment.R;
import com.example.duongndtph25724_mob403_assignment.Adapter.HomeAdapter;
import com.example.duongndtph25724_mob403_assignment.model.Products;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeFragment extends Fragment {

    private static final int REQUESTS_CODE_FOLDER = 123;
    private static final int RESULT_OK = -1;
    Bitmap imagess;
    FirebaseStorage storage;
    StorageReference storageRef;
    RecyclerView recyclerView ; RecyclerView recyclerView1 ; RecyclerView recyclerView2 ;
    List<Products> productList = new ArrayList<>();
    List<Products> aoList = new ArrayList<>();
    List<Products> quanList = new ArrayList<>();
    List<Products> muList = new ArrayList<>();
    HomeAdapter adapter = new HomeAdapter(getActivity(), productList);
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String userId = sharedPreferences.getString("userId", "");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        getProductsFromApi();
        FloatingActionButton floatingActionButton = v.findViewById(R.id.addproduct);
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        recyclerView = v.findViewById(R.id.ao);
        recyclerView1 = v.findViewById(R.id.quan);
        recyclerView2 = v.findViewById(R.id.mu);



//        LinearLayoutManager manager = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
//        recyclerView.setLayoutManager(manager);
//        recyclerView.setAdapter(adapter);



        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddProductDialog();
            }
        });
        return v;

    }


    public static String generateID() {
        String timestamp = String.valueOf(System.currentTimeMillis());
        String randomNumber = generateRandomNumber();
        return timestamp + randomNumber;
    }

    private static String generateRandomNumber() {
        Random random = new Random();
        int randomNumber = random.nextInt(90) + 10;
        return String.valueOf(randomNumber);
    }
    private void getProductsFromApi() {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://testh0stmysql.000webhostapp.com/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        addProducts api = retrofit.create(addProducts.class);

        Call<List<Products>> call = api.getProducts();
        call.enqueue(new Callback<List<Products>>() {
            @Override
            public void onResponse(Call<List<Products>> call, Response<List<Products>> response) {
                if (response.isSuccessful()) {

                    productList = response.body();
                    for (Products product : productList) {
                        String productType = product.getProducttype();
                        Log.d("hehe", "onCreateView: "+productType);
                        if (productType.equalsIgnoreCase("áo")) {
                            aoList.add(product);
                        } else if (productType.equalsIgnoreCase("quần")) {
                            quanList.add(product);
                        } else if (productType.equalsIgnoreCase("mũ")) {
                            muList.add(product);
                        }

                        HomeAdapter aoAdapter = new HomeAdapter(getActivity(), aoList);
                        LinearLayoutManager manager1 = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
                        recyclerView.setLayoutManager(manager1);
                        recyclerView.setAdapter(aoAdapter);

                        HomeAdapter quanAdapter = new HomeAdapter(getActivity(), quanList);
                        LinearLayoutManager manager2 = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
                        recyclerView1.setLayoutManager(manager2);
                        recyclerView1.setAdapter(quanAdapter);

                        HomeAdapter muAdapter = new HomeAdapter(getActivity(), muList);
                        LinearLayoutManager manager3 = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
                        recyclerView2.setLayoutManager(manager3);
                        recyclerView2.setAdapter(muAdapter);
                    }
                    // Cập nhật danh sách sản phẩm trong Adapter
                    adapter.setProductList(productList);
                    Log.d("Hello", "onResponse: "+productList.get(0).getProductname());

                    // Thông báo cho Adapter biết dữ liệu đã thay đổi
                    adapter.notifyDataSetChanged();
                } else {
                    // Xử lý khi không thành công
                    Toast.makeText(getActivity(), "Lỗi khi lấy danh sách sản phẩm từ API", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Products>> call, Throwable t) {
                // Xử lý khi gặp lỗi
                Toast.makeText(getActivity(), "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void showAddProductDialog() {
        String id = generateID();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_product, null);
        builder.setView(dialogView);

        EditText productNameEditText = dialogView.findViewById(R.id.editTextProductName);
        Spinner productTypeSpinner = dialogView.findViewById(R.id.spinnerProductType);
        EditText priceEditText = dialogView.findViewById(R.id.editTextPrice);
        Button productImageView = dialogView.findViewById(R.id.imageViewProduct);
        Spinner sizeSpinner = dialogView.findViewById(R.id.spinnerSize);
        EditText mountEditText = dialogView.findViewById(R.id.editTextMount);


        ArrayAdapter<CharSequence> productTypeAdapter = ArrayAdapter.createFromResource(requireContext(),
                R.array.product_types, android.R.layout.simple_spinner_item);
        productTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        productTypeSpinner.setAdapter(productTypeAdapter);

        ArrayAdapter<CharSequence> sizeAdapter = ArrayAdapter.createFromResource(requireContext(),
                R.array.product_sizes, android.R.layout.simple_spinner_item);
        sizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sizeSpinner.setAdapter(sizeAdapter);

        Map<String, Object> productdata = new HashMap<>();
        productImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUESTS_CODE_FOLDER);
            }
        });


        builder.setPositiveButton("Thêm sản phẩm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String productname = productNameEditText.getText().toString();
                String producttype = productTypeSpinner.getSelectedItem().toString();
                double price = Double.parseDouble(priceEditText.getText().toString());
                String size = sizeSpinner.getSelectedItem().toString();
                int mount = Integer.parseInt(mountEditText.getText().toString());

                if (id.isEmpty() || productname.isEmpty() || producttype.isEmpty() || size.isEmpty() || mount <= 0 || price <= 0) {
                    Toast.makeText(getActivity(), "Nhập đủ dữ liệu", Toast.LENGTH_SHORT).show();
                } else {
                    productdata.put("id", id);
                    productdata.put("productname", productname);
                    productdata.put("producttype", producttype);
                    productdata.put("price", price);
                    productdata.put("size", size);
                    productdata.put("mount", mount);

                    if (imagess != null) {
                        StorageReference cvRef = storageRef.child("images/" + id + "productImg");
                        ByteArrayOutputStream cvStream = new ByteArrayOutputStream();
                        imagess.compress(Bitmap.CompressFormat.JPEG, 100, cvStream);
                        byte[] cvBytes = cvStream.toByteArray();
                        UploadTask uploadTask = cvRef.putBytes(cvBytes);
                        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                cvRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        productdata.put("productimage", uri.toString());
                                        addproducts(productdata);
                                    }
                                });
                            }
                        });
                    }
                }

                dialog.dismiss();
            }
        });

        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void addproducts(Map<String, Object> productdata) {
        String id = (String) productdata.get("id");
        String productname = (String) productdata.get("productname");
        String producttype = (String) productdata.get("producttype");
        String productimage = (String) productdata.get("productimage");
        double price = (double) productdata.get("price");
        String size = (String) productdata.get("size");
        int mount = (int) productdata.get("mount");

        addproduct_api.api.add_products(id, productname, producttype, productimage, price, size, mount).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String jsonString = response.body().string();
                        // Xử lý chuỗi JSON jsonString theo ý muốn
                        Toast.makeText(getActivity(), "Thêm sản phẩm thành công", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    // Xử lý khi không thành công
                    Toast.makeText(getActivity(), "Lỗi khi thêm sản phẩm", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                String errorMessage = "Lỗi: " + t.getMessage();
                Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUESTS_CODE_FOLDER && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            try {
                InputStream inputStream = getActivity().getContentResolver().openInputStream(uri);
                imagess = BitmapFactory.decodeStream(inputStream);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}