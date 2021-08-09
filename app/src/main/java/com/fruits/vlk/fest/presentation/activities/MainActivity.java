package com.fruits.vlk.fest.presentation.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.fruits.vlk.fest.App;
import com.fruits.vlk.fest.R;
import com.fruits.vlk.fest.api.network.apiClients.ApiClientMagicChecker;
import com.fruits.vlk.fest.api.requests.checker.Response;
import com.fruits.vlk.fest.data.dao.UserDao;
import com.fruits.vlk.fest.data.database.AppDatabase;
import com.fruits.vlk.fest.data.entities.User;
import com.fruits.vlk.fest.presentation.utils.Common;
import com.fruits.vlk.fest.presentation.utils.InternetConnection;
import com.zl.reik.dilatingdotsprogressbar.DilatingDotsProgressBar;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;

public class MainActivity extends AppCompatActivity {

    private UserDao userDao;
    private Response responseBodyChecker;

    private DilatingDotsProgressBar mDilatingDotsProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

        InternetConnection.lookError(this);
        showProgressBar();

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
            startActivity(new Intent(MainActivity.this, CatalogActivity.class));
            finish();
        } else {
            ApiClientMagicChecker.getInstance()
                    .getApiServiceMagicChecker()
                    .getCheckerContent()
                    .enqueue(new Callback<Response>() {
                        @Override
                        public void onResponse(@NonNull Call<Response> call, @NonNull retrofit2.Response<Response> response) {
                            hideProgressBar();
                            responseBodyChecker = response.body();
                            Common.magicChecker = Objects.requireNonNull(responseBodyChecker).getContent();

                            Intent intent = new Intent(MainActivity.this, SlotsActivity.class);
                            startActivity(intent);
                            finish();
                        }

                        @Override
                        public void onFailure(@NonNull Call<Response> call, @NonNull Throwable t) {
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
