package com.fruits.vlk.fest.presentation.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.fruits.vlk.fest.App;
import com.fruits.vlk.fest.R;
import com.fruits.vlk.fest.api.network.apiClients.ApiClientMagicChecker;
import com.fruits.vlk.fest.api.requests.newProducts.ProductsItem;
import com.fruits.vlk.fest.api.requests.newProducts.ResponseProducts;
import com.fruits.vlk.fest.data.entities.User;
import com.fruits.vlk.fest.databinding.ActivityCatalogBinding;
import com.fruits.vlk.fest.presentation.adapters.RecyclerCatalogItemAdapter;
import com.zl.reik.dilatingdotsprogressbar.DilatingDotsProgressBar;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CatalogActivity extends AppCompatActivity {

    private ActivityCatalogBinding mBinding;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerCatalogItemAdapter mAdapter;
    private List<ProductsItem> mItemsList;
    private ResponseProducts mResponseProducts;

    private DilatingDotsProgressBar mDilatingDotsProgressBar;
    private RelativeLayout mRelativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityCatalogBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        initView();

        setTitle("Список предложений");
        showProgressBar();

        ApiClientMagicChecker.getInstance()
                .getApiServiceMagicChecker()
                .getProducts()
                .enqueue(new Callback<ResponseProducts>() {
                    @Override
                    public void onResponse(@NonNull Call<ResponseProducts> call, @NonNull Response<ResponseProducts> response) {
                        hideProgressBar();
                        mResponseProducts = response.body();
                        updateUi(Objects.requireNonNull(mResponseProducts));
                    }

                    @Override
                    public void onFailure(@NonNull Call<ResponseProducts> call, @NonNull Throwable t) {
                        t.printStackTrace();
                    }
                });
    }

    private void updateUi(ResponseProducts responseProducts) {

        User user = App.getInstance().getDatabase().mUserDao().getUserById(1);

        for (int i = 0; i < responseProducts.getCountries().size(); i++) {
            if(responseProducts.getCountries().get(i).getCountry().equalsIgnoreCase(user.name)){
                mItemsList = responseProducts.getCountries().get(i).getProducts();
            }
        }

        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mBinding.recyclerCatalog.setLayoutManager(mLayoutManager);
        mAdapter = new RecyclerCatalogItemAdapter(mItemsList, this);
        mBinding.recyclerCatalog.setAdapter(mAdapter);
    }

    private void hideProgressBar() {
        mDilatingDotsProgressBar.hideNow();
        mRelativeLayout.setVisibility(View.GONE);
    }

    private void initView() {
        mDilatingDotsProgressBar = findViewById(R.id.progress);
        mRelativeLayout = findViewById(R.id.relative_progress_bar);
    }

    private void showProgressBar() {
        mDilatingDotsProgressBar.showNow();
        mRelativeLayout.setVisibility(View.VISIBLE);
    }
}