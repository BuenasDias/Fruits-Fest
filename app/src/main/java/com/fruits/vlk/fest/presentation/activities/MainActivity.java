package com.fruits.vlk.fest.presentation.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.fruits.vlk.fest.App;
import com.fruits.vlk.fest.R;
import com.fruits.vlk.fest.api.network.apiClients.ApiClientMagicChecker;
import com.fruits.vlk.fest.api.requests.checker.Response;
import com.fruits.vlk.fest.data.dao.UserDao;
import com.fruits.vlk.fest.data.database.AppDatabase;
import com.fruits.vlk.fest.data.entities.User;
import com.fruits.vlk.fest.presentation.utils.InternetConnection;
import com.fruits.vlk.fest.presentation.utils.Params;
import com.zl.reik.dilatingdotsprogressbar.DilatingDotsProgressBar;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;

public class MainActivity extends AppCompatActivity {

    private int klo;
    private UserDao userDao;
    private Response responseBody;
    private com.fruits.vlk.fest.api.requests.smsGorodKey.Response responseBodyApiKey;
    private DilatingDotsProgressBar mDilatingDotsProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

        AppDatabase db = App.getInstance().getDatabase();
        userDao = db.mUserDao();

        // TEST
        User user1 = new User();
        user1.id = 1;
        user1.auth = 0;

        userDao.updateUser(user1);
        // TEST

        User user = userDao.getUserById(1);

        if(user.getAuth() == 1){
            Log.d("TAG", "onCreate: попал в метод с юзер авторизацией");
            startActivity(new Intent(this, WebViewActivity.class));
            hideProgressBar();
        } else {
            ApiClientMagicChecker.getInstance()
                    .getApiServiceMagicChecker()
                    .getApiKeySms()
                    .enqueue(new Callback<com.fruits.vlk.fest.api.requests.smsGorodKey.Response>() {
                        @Override
                        public void onResponse(@NotNull Call<com.fruits.vlk.fest.api.requests.smsGorodKey.Response> call,
                                               retrofit2.@NotNull Response<com.fruits.vlk.fest.api.requests.smsGorodKey.Response> response) {

                            responseBodyApiKey = response.body();

                            Params.keyApi = responseBodyApiKey != null ? responseBodyApiKey.getKey() : "";
                        }

                        @Override
                        public void onFailure(@NotNull Call<com.fruits.vlk.fest.api.requests.smsGorodKey.Response> call, @NotNull Throwable t) {
                            t.printStackTrace();
                        }
                    });

            showProgressBar();

            InternetConnection.lookError(this);

            ApiClientMagicChecker.getInstance()
                    .getApiServiceMagicChecker()
                    .getCheckerContent()
                    .enqueue(new Callback<Response>() {
                        @Override
                        public void onResponse(@NotNull Call<Response> call, retrofit2.@NotNull Response<Response> response) {

                            hideProgressBar();

                            responseBody = response.body();
                            klo = Objects.requireNonNull(responseBody).getContent();

                            Log.d("TAG", "Retrofit. klo = " + klo);

                            Intent intent = new Intent(MainActivity.this, SlotsActivity.class);
                            intent.putExtra("cloaka", klo);

                            startActivity(intent);
                            finish();
                        }

                        @Override
                        public void onFailure(@NotNull Call<Response> call, @NotNull Throwable t) {
                            t.printStackTrace();
                        }
                    });
        }
    }

    private void hideProgressBar() {
        mDilatingDotsProgressBar.hideNow();
    }

    private void showProgressBar() {
        mDilatingDotsProgressBar.showNow();
    }

    private void initView() {
        mDilatingDotsProgressBar = findViewById(R.id.progress);
    }
}
