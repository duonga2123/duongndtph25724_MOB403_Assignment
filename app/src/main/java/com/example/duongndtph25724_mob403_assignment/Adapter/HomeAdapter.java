package com.example.duongndtph25724_mob403_assignment.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.duongndtph25724_mob403_assignment.BottomNav.HomeFragment;
import com.example.duongndtph25724_mob403_assignment.model.Products;
import com.example.duongndtph25724_mob403_assignment.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {
    private Context context;
    HomeFragment fragment;
    private List<Products> productList;

    public HomeAdapter(Context context, List<Products> productList, HomeFragment fragment) {
        this.context = context;
        this.productList = productList;
        this.fragment = fragment;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_products, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Products product = productList.get(position);
        holder.productNameTextView.setText(product.getProductname());
        holder.productPriceTextView.setText(""+product.getPrice());
//        Glide.with(holder.itemView.getContext())
//                .load(product.getProductimage())
//                .placeholder(R.drawable.baseline_history_24) // Ảnh placeholder hiển thị khi chờ tải ảnh
//                .error(R.drawable.baseline_history_24) // Ảnh hiển thị khi lỗi tải ảnh
//                .into(holder.productimgview);
        Picasso.get().load(product.getProductimg()).placeholder(R.drawable.ic_launcher_foreground).into(holder.productimgview);
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment.editPr(product);
            }
        });
        holder.del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment.deletePr(product);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }
    public void setProductList(List<Products> productList) {
        this.productList = productList;
        notifyDataSetChanged();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView productNameTextView,productPriceTextView;
        ImageView productimgview;
        ImageButton edit, del;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productNameTextView= itemView.findViewById(R.id.nameitem);
            productPriceTextView = itemView.findViewById(R.id.costitem);
            productimgview = itemView.findViewById(R.id.imgitem);
            edit = itemView.findViewById(R.id.editProduct);
            del = itemView.findViewById(R.id.deleteProduct);
        }
    }




}
