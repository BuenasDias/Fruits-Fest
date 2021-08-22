package com.fruits.vlk.fest.presentation.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fruits.vlk.fest.R;
import com.fruits.vlk.fest.api.requests.newProducts.ProductsItem;
import com.fruits.vlk.fest.presentation.activities.WebViewActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class RecyclerCatalogItemAdapter extends RecyclerView.Adapter<RecyclerCatalogItemAdapter.ViewHolder>{

    private List<ProductsItem> mCatalogItems = new ArrayList<>();
    private Context mContext;

    public RecyclerCatalogItemAdapter(List<ProductsItem> catalogItems, Context context) {
        mCatalogItems = catalogItems;
        mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.catalog_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        ProductsItem catalogItems = mCatalogItems.get(position);

        if (position == mCatalogItems.size() - 1) {
            holder.mBtnGetMoney.setText("Написать");
            holder.mBtnGetMoney.setBackgroundResource(R.drawable.btnred1);

            holder.mBtnGetMoney.setOnClickListener(view -> {
                Intent telegram = new Intent(Intent.ACTION_VIEW , Uri.parse(catalogItems.getUrl()));
                mContext.startActivity(telegram);
            });
        } else {
            holder.mBtnGetMoney.setOnClickListener(view -> {
                Intent intent = new Intent(mContext, WebViewActivity.class);
                intent.putExtra("url", catalogItems.getUrl());
                mContext.startActivity(intent);
            });
        }

        Picasso.get()
                .load(catalogItems.getImage())
                .into(holder.mImageView);
    }

    @Override
    public int getItemCount() {
        return mCatalogItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView mImageView;
        Button mBtnGetMoney;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mImageView = itemView.findViewById(R.id.image_catalog_item);
            mBtnGetMoney = itemView.findViewById(R.id.btn_get_money);
        }


    }
}