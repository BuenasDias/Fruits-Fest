package com.fruits.vlk.fest.presentation.activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.fruits.vlk.fest.App;
import com.fruits.vlk.fest.R;
import com.fruits.vlk.fest.api.network.apiClients.ApiClientSmsGorod;
import com.fruits.vlk.fest.api.requests.auth.Response;
import com.fruits.vlk.fest.data.dao.UserDao;
import com.fruits.vlk.fest.data.database.AppDatabase;
import com.fruits.vlk.fest.data.entities.User;
import com.fruits.vlk.fest.databinding.ActivityAuthBinding;
import com.fruits.vlk.fest.presentation.utils.Common;
import com.fruits.vlk.fest.presentation.utils.Params;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;


public class AuthActivity extends AppCompatActivity {

    private ActivityAuthBinding mBinding;
    private UserDao userDao;
    private Response responseBody;
    private int passCode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityAuthBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        AppDatabase db = App.getInstance().getDatabase();
        userDao = db.mUserDao();
        setTitle("Добро пожаловать!");
        checkAuth();

        mBinding.ccp.registerCarrierNumberEditText(mBinding.userPhone);

        // TODO доработать авторизацию

        mBinding.btnAuthorization.setOnClickListener(view -> {

            if (mBinding.ccp.getFullNumber().length() == 11 || mBinding.ccp.getFullNumber().length() == 12) {

                String phone = mBinding.ccp.getFullNumber();

                ApiClientSmsGorod.getInstance()
                        .getApiServiceSmsGorod()
                        .getSmsCode(phone, Common.keyApiSms)
                        .enqueue(new Callback<Response>() {
                            @Override
                            public void onResponse(@NonNull Call<Response> call, @NonNull retrofit2.Response<Response> response) {

                                responseBody = response.body();

                                if (Objects.requireNonNull(responseBody).getCode() > 0) {
                                    passCode = responseBody.getCode();
                                    showEditPass();
                                    hideEditPhone();
                                } else {
                                    Toast.makeText(AuthActivity.this, "Ошибка ответа от сервера", Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onFailure(@NonNull Call<Response> call, @NonNull Throwable t) {
                                t.printStackTrace();
                                Toast.makeText(AuthActivity.this, "Ошибка ответа от сервера", Toast.LENGTH_LONG).show();
                            }
                        });

            } else {
                mBinding.textErrorSms.setVisibility(View.VISIBLE);
                mBinding.textErrorSms.setText("Неверный формат телефона");
            }
        });


        mBinding.btnCheckSms.setOnClickListener(v -> {
            if (Integer.parseInt(mBinding.editSms.getText().toString().trim()) == passCode) {

                startActivity(new Intent(this, WebViewActivity.class));

                User user = new User();
                user.id = 1;
                user.auth = 1;

                userDao.updateUser(user);

            } else {
                mBinding.textErrorSms.setVisibility(View.VISIBLE);
            }
        });

        mBinding.btnBack.setOnClickListener(v -> {
            showEditPhone();
            hideEditPass();
        });
    }

    private void hideEditPass() {
        mBinding.btnCheckSms.setVisibility(View.GONE);
        mBinding.editSms.setVisibility(View.GONE);
        mBinding.btnBack.setVisibility(View.GONE);
    }

    private void showEditPhone() {
        mBinding.userPhone.setVisibility(View.VISIBLE);
        mBinding.btnAuthorization.setVisibility(View.VISIBLE);
    }

    private void hideEditPhone() {
        mBinding.userPhone.setVisibility(View.GONE);
        mBinding.btnAuthorization.setVisibility(View.GONE);
    }

    private void showEditPass() {
        mBinding.btnCheckSms.setVisibility(View.VISIBLE);
        mBinding.editSms.setVisibility(View.VISIBLE);
        mBinding.btnBack.setVisibility(View.VISIBLE);
    }

    // Generate #### number
    private int generateCode() {
        return (int) (Math.random() * 10000) + 1000;
    }

    private void checkAuth() {
        User user = userDao.getUserById(1);

        if (user.auth != 0) {
            startActivity(new Intent(this, WebViewActivity.class));
            finish();
        }
    }
}