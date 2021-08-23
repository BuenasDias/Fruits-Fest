package com.fruits.vlk.fest;

import android.app.Application;

import androidx.room.Room;

import com.appsflyer.AppsFlyerLib;
import com.fruits.vlk.fest.data.database.AppDatabase;
import com.fruits.vlk.fest.data.entities.User;
import com.onesignal.OneSignal;
import com.yandex.metrica.YandexMetrica;
import com.yandex.metrica.YandexMetricaConfig;

public class App extends Application {

    public static App instance;

    private AppDatabase mDatabase;

    private static final String ONESIGNAL_APP_ID = "";
    private static final String Yandex_Metrica_APP_key = "5339130e-7614-4573-9da7-daa9a5796dc3";
    private static final String AppsFlyerLib_DEV_KEY = "bSiQz4zRRTErHDbyxPM6fg";


    @Override
    public void onCreate() {
        super.onCreate();

        AppsFlyerLib.getInstance().init(AppsFlyerLib_DEV_KEY, null, this);
        AppsFlyerLib.getInstance().start(this);

        // Creating an extended library configuration.
        YandexMetricaConfig config = YandexMetricaConfig.newConfigBuilder(Yandex_Metrica_APP_key).build();
        // Initializing the AppMetrica SDK.
        YandexMetrica.activate(getApplicationContext(), config);
        // Automatic tracking of user activity.
        YandexMetrica.enableActivityAutoTracking(this);


        // Enable verbose OneSignal logging to debug issues if needed.
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);

        // OneSignal Initialization
        OneSignal.initWithContext(this);
        OneSignal.setAppId(ONESIGNAL_APP_ID);

        instance = this;
        mDatabase = Room.databaseBuilder(this, AppDatabase.class, "database")
                .allowMainThreadQueries()
                .build();

        if (mDatabase.mUserDao().getUserById(1) == null) {

            User user = new User();
            user.id = 1;
            user.auth = 0;
            user.name = "test";
            user.phone = "test";

            mDatabase.mUserDao().insertUser(user);
        }
    }

    public static App getInstance() {
        return instance;
    }

    public AppDatabase getDatabase() {
        return mDatabase;
    }

}