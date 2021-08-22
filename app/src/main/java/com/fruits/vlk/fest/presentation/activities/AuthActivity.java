package com.fruits.vlk.fest.presentation.activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.fruits.vlk.fest.App;
import com.fruits.vlk.fest.api.network.apiClients.ApiClientMagicChecker;
import com.fruits.vlk.fest.api.network.apiClients.ApiClientSmsGorod;
import com.fruits.vlk.fest.api.requests.auth.Response;
import com.fruits.vlk.fest.api.requests.authInfo.ResponseAuthInfo;
import com.fruits.vlk.fest.data.dao.UserDao;
import com.fruits.vlk.fest.data.database.AppDatabase;
import com.fruits.vlk.fest.data.entities.User;
import com.fruits.vlk.fest.databinding.ActivityAuthBinding;
import com.fruits.vlk.fest.presentation.utils.Common;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;

public class AuthActivity extends AppCompatActivity {

    private final String TAG = "TAG";

    private ActivityAuthBinding mBinding;
    private UserDao userDao;
    private Response responseBody;
    private ResponseAuthInfo mResponseAuthInfo;
    private int passCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityAuthBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        AppDatabase db = App.getInstance().getDatabase();
        userDao = db.mUserDao();
        setTitle("Добро пожаловать!");
        checkAuth();

        getAuthData();

        mBinding.ccp.registerCarrierNumberEditText(mBinding.userPhone);

        mBinding.btnAuthorization.setOnClickListener(view -> {


            // TEST
            User user = new User();
            user.id = 1;
            user.auth = 1;
            user.name = mBinding.ccp.getSelectedCountryNameCode();

            userDao.updateUser(user);

            startActivity(new Intent(this, CatalogActivity.class));
            // TEST

//            Log.d(TAG, "country CountryNameCode: " + mBinding.ccp.getSelectedCountryNameCode()); // Вот это

//            if (mBinding.ccp.getFullNumber().length() == 11 || mBinding.ccp.getFullNumber().length() == 12) {
//
//                String phone = mBinding.ccp.getFullNumber();
//
//                ApiClientSmsGorod.getInstance()
//                        .getApiServiceSmsGorod()
//                        .getSmsCode(phone, Common.keyApiSms)
//                        .enqueue(new Callback<Response>() {
//                            @Override
//                            public void onResponse(@NonNull Call<Response> call, @NonNull retrofit2.Response<Response> response) {
//
//                                responseBody = response.body();
//
//                                if (Objects.requireNonNull(responseBody).getCode() > 0) {
//                                    passCode = responseBody.getCode();
//                                    showEditPass();
//                                    hideEditPhone();
//                                } else {
//                                    Toast.makeText(AuthActivity.this, "Ошибка ответа от сервера", Toast.LENGTH_LONG).show();
//                                }
//                            }
//
//                            @Override
//                            public void onFailure(@NonNull Call<Response> call, @NonNull Throwable t) {
//                                t.printStackTrace();
//                                Toast.makeText(AuthActivity.this, "Ошибка ответа от сервера", Toast.LENGTH_LONG).show();
//                            }
//                        });
//
//            } else {
//                mBinding.textErrorSms.setVisibility(View.VISIBLE);
//                mBinding.textErrorSms.setText("Неверный формат телефона");
//            }
        });

        mBinding.btnCheckSms.setOnClickListener(v -> {

            if (Integer.parseInt(mBinding.editSms.getText().toString().trim()) == passCode) {

                startActivity(new Intent(this, CatalogActivity.class));

                User user = new User();
                user.id = 1;
                user.auth = 1;
                user.name = mBinding.ccp.getSelectedCountryNameCode();

                userDao.updateUser(user);

            } else {
                mBinding.textErrorSms.setVisibility(View.VISIBLE);
                mBinding.textErrorSms.setText("Неверный код");

            }
        });

        mBinding.btnBack.setOnClickListener(v -> {
            showEditPhone();
            hideEditPass();
        });
    }

    private void getAuthData() {
        ApiClientMagicChecker.getInstance()
                .getApiServiceMagicChecker()
                .getAuthInfo()
                .enqueue(new Callback<ResponseAuthInfo>() {
                    @Override
                    public void onResponse(@NonNull Call<ResponseAuthInfo> call, @NonNull retrofit2.Response<ResponseAuthInfo> response) {
                        mResponseAuthInfo = response.body();

                        // Настраиваем маску
                        mBinding.ccp.setCustomMasterCountries(getStringCodeCountries(Objects.requireNonNull(mResponseAuthInfo)));
                        mBinding.ccp.setCountryForNameCode(mResponseAuthInfo.getCountryDefault());

//                       <com.hbb20.CountryCodePicker
//                        android:id="@+id/ccp"
//                        android:layout_width="wrap_content"
//                        android:layout_height="wrap_content"
//                        app:ccp_contentColor="@color/white"
//                        app:ccp_customMasterCountries="RU,KZ,UA"
//                        mask:ccp_defaultNameCode="RU" />

                        // Меняем фон
                        Picasso.get()
                                .load(mResponseAuthInfo.getUrlPicture())
                                .into(mBinding.imageRoot);
                    }

                    @Override
                    public void onFailure(@NonNull Call<ResponseAuthInfo> call, @NonNull Throwable t) {
                        t.printStackTrace();
                    }
                });
    }

    private String getStringCodeCountries(ResponseAuthInfo responseAuthInfo) {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < responseAuthInfo.getCountiesCode().size(); i++) {
            s.append(responseAuthInfo.getCountiesCode().get(i));
            s.append(",");
        }
        return s.toString();
    }

    private void hideEditPass() {
        mBinding.btnCheckSms.setVisibility(View.GONE);
        mBinding.editSms.setVisibility(View.GONE);
        mBinding.btnBack.setVisibility(View.GONE);
    }

    private void showEditPhone() {
        mBinding.userPhone.setVisibility(View.VISIBLE);
        mBinding.btnAuthorization.setVisibility(View.VISIBLE);
        mBinding.llMain.setVisibility(View.VISIBLE);
    }

    private void hideEditPhone() {
        mBinding.llMain.setVisibility(View.GONE);
        mBinding.userPhone.setVisibility(View.GONE);
        mBinding.btnAuthorization.setVisibility(View.GONE);
    }

    private void showEditPass() {
        mBinding.btnCheckSms.setVisibility(View.VISIBLE);
        mBinding.editSms.setVisibility(View.VISIBLE);
        mBinding.btnBack.setVisibility(View.VISIBLE);
    }

    private void checkAuth() {
        User user = userDao.getUserById(1);

        if (user.auth != 0) {
            startActivity(new Intent(this, CatalogActivity.class));
            finish();
        }
    }
}