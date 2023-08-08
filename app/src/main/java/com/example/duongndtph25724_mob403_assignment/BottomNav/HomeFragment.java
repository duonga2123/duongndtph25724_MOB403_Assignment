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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duongndtph25724_mob403_assignment.Interface.ApiService;
import com.example.duongndtph25724_mob403_assignment.MainActivity;
import com.example.duongndtph25724_mob403_assignment.R;
import com.example.duongndtph25724_mob403_assignment.Adapter.HomeAdapter;
import com.example.duongndtph25724_mob403_assignment.model.Products;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeFragment extends Fragment {

    private static final int REQUESTS_CODE_FOLDER = 123;

    private Uri filePath;
    private StorageReference mStorageRef;
    private static final int RESULT_OK = -1;
    Bitmap imagess;
    FirebaseStorage storage;
    StorageReference storageRef;
    RecyclerView recyclerView;
    RecyclerView recyclerView1;
    RecyclerView recyclerView2;
    List<Products> productList = new ArrayList<>();
    List<Products> aoList = new ArrayList<>();
    List<Products> quanList = new ArrayList<>();
    List<Products> muList = new ArrayList<>();
    HomeAdapter adapter = new HomeAdapter(getActivity(), productList, this);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        FloatingActionButton floatingActionButton = v.findViewById(R.id.addproduct);
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        recyclerView = v.findViewById(R.id.ao);
        recyclerView1 = v.findViewById(R.id.quan);
        recyclerView2 = v.findViewById(R.id.mu);

        callApiGetTableList();
        adapter.notifyDataSetChanged();


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

    private void callApiGetTableList() {
        // lay danh sach
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8000/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        ApiService api = retrofit.create(ApiService.class);
        Call<List<Products>> call = api.getProducts();
        call.enqueue(new Callback<List<Products>>() {
            @Override
            public void onResponse(Call<List<Products>> call, Response<List<Products>> response) {
                if (response.isSuccessful()) {
                    productList = response.body();
                    for (Products product : productList) {
                        String productType = product.getProducttype();
                        Log.d("hehe", "onCreateView: " + productType);
                        if (productType.equalsIgnoreCase("áo")) {
                            aoList.add(product);
                        } else if (productType.equalsIgnoreCase("quần")) {
                            quanList.add(product);
                        } else if (productType.equalsIgnoreCase("mũ")) {
                            muList.add(product);
                        }

                        HomeAdapter aoAdapter = new HomeAdapter(getActivity(), aoList, HomeFragment.this);
                        LinearLayoutManager manager1 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
                        recyclerView.setLayoutManager(manager1);
                        recyclerView.setAdapter(aoAdapter);
                        aoAdapter.notifyDataSetChanged();

                        HomeAdapter quanAdapter = new HomeAdapter(getActivity(), quanList, HomeFragment.this);
                        LinearLayoutManager manager2 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
                        recyclerView1.setLayoutManager(manager2);
                        recyclerView1.setAdapter(quanAdapter);
                        quanAdapter.notifyDataSetChanged();

                        HomeAdapter muAdapter = new HomeAdapter(getActivity(), muList, HomeFragment.this);
                        LinearLayoutManager manager3 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
                        recyclerView2.setLayoutManager(manager3);
                        recyclerView2.setAdapter(muAdapter);
                        muAdapter.notifyDataSetChanged();
                    }
                    if (productList != null) {
                        adapter.setProductList(productList);
                        adapter.notifyDataSetChanged();
                    }
                    // Cập nhật danh sách sản phẩm trong Adapter
                    adapter.setProductList(productList);
                    adapter.notifyDataSetChanged();
                } else {
                    // Xử lý khi không thành công
                    Toast.makeText(getActivity(), "Lỗi khi lấy danh sách sản phẩm từ API", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Products>> call, Throwable t) {
                Toast.makeText(getActivity(), "Network error" + t, Toast.LENGTH_SHORT).show();
            }

        });
    }

    private void showAddProductDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_product, null);
        builder.setView(dialogView);

        EditText productNameEditText = dialogView.findViewById(R.id.editTextProductName);
        Spinner productTypeSpinner = dialogView.findViewById(R.id.spinnerProductType);
        EditText priceEditText = dialogView.findViewById(R.id.editTextPrice);
        EditText productImgEditText = dialogView.findViewById(R.id.editTextProductImg);
        EditText mountEditText = dialogView.findViewById(R.id.editTextMount);


        ArrayAdapter<CharSequence> productTypeAdapter = ArrayAdapter.createFromResource(requireContext(),
                R.array.product_types, android.R.layout.simple_spinner_item);
        productTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        productTypeSpinner.setAdapter(productTypeAdapter);


        Map<String, Object> productdata = new HashMap<>();
//        productImageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(Intent.ACTION_PICK);
//                intent.setType("image/*");
//                startActivityForResult(intent, REQUESTS_CODE_FOLDER);
//            }
//        });


        //THEM SAN PHAM
        builder.setPositiveButton("Thêm sản phẩm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String productname = productNameEditText.getText().toString();
                String producttype = productTypeSpinner.getSelectedItem().toString();
                double price = Double.parseDouble(priceEditText.getText().toString());
                String productimg = productImgEditText.getText().toString();
                //String size = sizeSpinner.getSelectedItem().toString();
                int mount = Integer.parseInt(mountEditText.getText().toString());

                if (productname.isEmpty() || producttype.isEmpty() || mount <= 0 || price <= 0 || productimg.isEmpty()) {
                    Toast.makeText(getActivity(), "Nhập đủ dữ liệu", Toast.LENGTH_SHORT).show();
                } else {


                    addNewData(productname, price, producttype, mount, productimg);


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

    private void addNewData(String name, double price, String type, double mount, String img) {
        Products product = new Products();
        product.setProductname(name);
        product.setPrice(price);
        product.setMount(mount);
        product.setProducttype(type);
        product.setProductimg(img);
        Log.d("Listsss", product.getProductimg());
        ApiService.apiService.addProducts(product).enqueue(new Callback<List<Products>>() {
            @Override
            public void onResponse(Call<List<Products>> call, Response<List<Products>> response) {
                if (response.isSuccessful()) {
                    // Xử lý thành công
                    Toast.makeText(getActivity(), "Thêm dữ liệu thành công", Toast.LENGTH_SHORT).show();
                    adapter.notifyDataSetChanged();
                    resetFR();
                    List<Products> tableItems = response.body();
                    if (tableItems != null) {
                        adapter.setProductList(tableItems);
                        adapter.notifyDataSetChanged();
                    }
                } else {
                    // Xử lý lỗi khi thêm dữ liệu
                    Toast.makeText(getActivity(), "Lỗi khi thêm dữ liệu", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Products>> call, Throwable t) {
                Toast.makeText(getActivity(), "Lỗi mạng", Toast.LENGTH_SHORT).show();
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


    private void updateProducts(String id, String name, double price, int quantity, String type, String img) {
        Products products = new Products();
        products.setProductname(name);
        products.setPrice(price);
        products.setMount(quantity);
        products.setProducttype(type);
        products.setProductimg(img);

        Call<List<Products>> call = ApiService.apiService.updateProducts(id, products);
        call.enqueue(new Callback<List<Products>>() {
            @Override
            public void onResponse(Call<List<Products>> call, Response<List<Products>> response) {
                if (response.isSuccessful()) {
                    //products updatedProduct = response.body();
                    Toast.makeText(getActivity(), "Sửa thành công", Toast.LENGTH_SHORT).show();
                    //callApiGetTableList();
                    List<Products> tableItems = response.body();
                    if (tableItems != null) {
                        adapter.setProductList(tableItems);
                        adapter.notifyDataSetChanged();
                    }
                } else {
                    Log.d("MAIN", "Respone Fail" + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Products>> call, Throwable t) {
                Log.d("MAIN", "Respone Fail" + t.getMessage());
            }
        });
    }

    public void editPr(Products products) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_product, null);
        builder.setView(dialogView);

        EditText productNameEditText = dialogView.findViewById(R.id.editTextProductName);
        Spinner productTypeSpinner = dialogView.findViewById(R.id.spinnerProductType);
        EditText priceEditText = dialogView.findViewById(R.id.editTextPrice);
        EditText productImgView = dialogView.findViewById(R.id.editTextProductImg);
        EditText mountEditText = dialogView.findViewById(R.id.editTextMount);


        ArrayAdapter<CharSequence> productTypeAdapter = ArrayAdapter.createFromResource(requireContext(),
                R.array.product_types, android.R.layout.simple_spinner_item);
        productTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        productTypeSpinner.setAdapter(productTypeAdapter);


//        productImageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(Intent.ACTION_PICK);
//                intent.setType("image/*");
//                startActivityForResult(intent, REQUESTS_CODE_FOLDER);
//            }
//        });


        Log.d("Listsss", products.getProductimg());
//        THEM SAN PHAM
        builder.setPositiveButton("Sửa", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String productname = productNameEditText.getText().toString();
                String producttype = productTypeSpinner.getSelectedItem().toString();
                String productimg = productImgView.getText().toString();
                double price = Double.parseDouble(priceEditText.getText().toString());
                int mount = Integer.parseInt(mountEditText.getText().toString());

                if (productname.isEmpty() || producttype.isEmpty() || mount <= 0 || price <= 0 || productimg.isEmpty()) {
                    Toast.makeText(getActivity(), "Nhập đủ dữ liệu", Toast.LENGTH_SHORT).show();
                } else {

                    updateProducts(products.getId(), productname, price, mount, producttype, productimg);
                    adapter.setProductList(productList);
                    adapter.notifyDataSetChanged();

                }
                resetFR();
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

    private void deleteProduct(Products products) {
        String id = products.getId();
        Call<List<Products>> call = ApiService.apiService.deleteProducts(id);
        call.enqueue(new Callback<List<Products>>() {
            @Override
            public void onResponse(Call<List<Products>> call, Response<List<Products>> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getActivity(), "Xóa thành công", Toast.LENGTH_SHORT).show();
                    List<Products> tableItems = response.body();
                    if (tableItems != null) {
                        adapter.setProductList(tableItems);
                        adapter.notifyDataSetChanged();
                    }
                    //callApiGetTableList();
                } else {
                    Log.d("MAIN", "Respone Fail" + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Products>> call, Throwable t) {
                Log.d("MAIN", "Respone Fail" + t.getMessage());
            }
        });
    }
    public void deletePr(Products model) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Xóa sản phẩm");
        builder.setMessage("Bạn có chắc chắn muốn xóa sản phẩm này?");
        builder.setPositiveButton("Xóa", (dialog, which) -> {
            deleteProduct(model);
            resetFR();
        });
        builder.setNegativeButton("Hủy", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }



    public void resetFR(){
        startActivity(new Intent(getActivity().getApplication(), MainActivity.class));
    }



}