package com.fruits.vlk.fest.presentation.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.fruits.vlk.fest.R;
import com.fruits.vlk.fest.databinding.ActivitySlotsBinding;
import com.fruits.vlk.fest.presentation.utils.Common;
import com.fruits.vlk.fest.slotsGame.imageViewScrolling.IEventEnd;
import com.fruits.vlk.fest.slotsGame.imageViewScrolling.ImageViewScrolling;

import org.jetbrains.annotations.NotNull;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.schedulers.Schedulers;
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt;
import uk.co.samuelwall.materialtaptargetprompt.extras.backgrounds.FullscreenPromptBackground;
import uk.co.samuelwall.materialtaptargetprompt.extras.focals.RectanglePromptFocal;

public class SlotsActivity extends AppCompatActivity implements IEventEnd {

    private static final String TAG = "TAG";
    private ActivitySlotsBinding binding;

    Button btn_down;
    ImageViewScrolling image1, image2, image3;
    TextView txt_score;
    ImageView mImgJackpot;

    int count_done = 0;
    public int klo;

    MediaPlayer mp;
    MediaPlayer mpSlotMachine;
    MediaPlayer mpClickButton;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySlotsBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

        initView();

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

//        klo = Common.magicChecker;
        klo = 0;

        showBalansPromt(txt_score);

        image1.setEventEnd(this);
        image2.setEventEnd(this);
        image3.setEventEnd(this);

        if (klo == 1) {

            // тут реализация игры для реального пользователя

            btn_down.setOnClickListener(v -> {

                btn_down.setEnabled(false);
                btn_down.setClickable(false);

                Completable.timer(2000, TimeUnit.MILLISECONDS)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new DisposableCompletableObserver() {
                            @Override
                            public void onComplete() {
                                btn_down.setEnabled(true);
                                btn_down.setClickable(true);
                                mpSlotMachine.stop();
                            }

                            @Override
                            public void onError(@NotNull Throwable e) {
                                e.printStackTrace();
                            }
                        });

                if (Common.SCORE >= 50) {

                    mpSlotMachine = MediaPlayer.create(this, R.raw.slotmachina);
                    mpSlotMachine.start();

                    // Вот тут нужно выбрать конкретную картинку
                    image1.setValueRandom(4,
                            new Random().nextInt((8 - 5) + 1) + 5);

                    image2.setValueRandom(4,
                            new Random().nextInt((8 - 5) + 1) + 5);

                    image3.setValueRandom(4,
                            new Random().nextInt((8 - 5) + 1) + 5);

                    Common.SCORE -= 50;

                    txt_score.setText("SCORE : " + Common.SCORE);

                } else {
                    Toast.makeText(this, "You not enough money", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            btn_down.setOnClickListener(v -> {

                btn_down.setEnabled(false);
                btn_down.setClickable(false);

                Completable.timer(2000, TimeUnit.MILLISECONDS)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new DisposableCompletableObserver() {
                            @Override
                            public void onComplete() {
                                btn_down.setEnabled(true);
                                btn_down.setClickable(true);
//                                mpSlotMachine.stop();
                            }

                            @Override
                            public void onError(@NotNull Throwable e) {
                                e.printStackTrace();
                            }
                        });

                if (com.fruits.vlk.fest.presentation.utils.Common.SCORE >= 50) {

                    mpSlotMachine = MediaPlayer.create(this, R.raw.slotmachina);
                    mpSlotMachine.start();

                    // Вот тут нужно выбрать конкретную картинку
                    image1.setValueRandom(new Random().nextInt(6),
                            new Random().nextInt((8 - 5) + 1) + 5);

                    image2.setValueRandom(new Random().nextInt(6),
                            new Random().nextInt((8 - 5) + 1) + 5);

                    image3.setValueRandom(new Random().nextInt(6),
                            new Random().nextInt((8 - 5) + 1) + 5);

                    Common.SCORE -= 25;

                    txt_score.setText("SCORE : " + Common.SCORE);

                } else {
                    Toast.makeText(this, "You not enough money", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @SuppressLint("SetTextI18n")
    private void showBalans(){
        binding.txtScore.setText("BALANS : " + Common.SCORE);
    }

    private void releaseMP1() {
        if (mp != null) {
            try {
                mp.release();
                mp = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: start");

        mp = MediaPlayer.create(this, R.raw.music);
        mp.setLooping(true);
        mp.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mp.release();
        mp = null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseMP1();
    }

    public void showBalansPromt(View view) {
        new MaterialTapTargetPrompt.Builder(this)
                .setTarget(view)
                .setPrimaryText("SCORE")
                .setSecondaryText("Press the spin button to win and increase your balance")
                .setPromptBackground(new FullscreenPromptBackground())
                .setPromptFocal(new RectanglePromptFocal())
                .setPromptStateChangeListener((prompt, state) -> {
                    if (state == MaterialTapTargetPrompt.STATE_FOCAL_PRESSED) {
                        showButtonPromt(btn_down);
                    } else if (state == MaterialTapTargetPrompt.STATE_NON_FOCAL_PRESSED) {
                        showButtonPromt(btn_down);
                    }
                })
                .show();
    }

    public void showButtonPromt(View view) {
        new MaterialTapTargetPrompt.Builder(this)
                .setTarget(view)
                .setPrimaryText("The twist button")
                .setSecondaryText("Each click of the spin button costs 25 points from your balance")
                .setPromptBackground(new FullscreenPromptBackground())
                .setPromptFocal(new RectanglePromptFocal())
                .show();
    }

    private void initView() {
        btn_down = findViewById(R.id.btn_down);
        image1 = findViewById(R.id.image1);
        image2 = findViewById(R.id.image2);
        image3 = findViewById(R.id.image3);
        txt_score = findViewById(R.id.txt_score);

        binding.btnMinus.setOnClickListener(v -> {
            if(GlobalStats.currentStavka <= 5) GlobalStats.currentStavka = 5;
            else GlobalStats.currentStavka -= 5;

            showCountSpin();
        });

        binding.btnPlus.setOnClickListener(view -> {

            if(GlobalStats.currentStavka >= 50) GlobalStats.currentStavka = 50;
            else GlobalStats.currentStavka += 5;

            showCountSpin();
        });

        mImgJackpot = findViewById(R.id.img_jackpot);
    }

    private void showCountSpin(){
        binding.countSpin.setText(String.valueOf(GlobalStats.currentStavka));
    }

    private void updateBalans(int multiplayer){
        Common.SCORE += GlobalStats.currentStavka * multiplayer;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void eventEnd(int result, int count) {

        if (count_done < 2) {
            count_done++;
        } else {

            mpSlotMachine.stop();

            count_done = 0;

            Log.d(TAG, "image1: " + image1.getValue());
            Log.d(TAG, "image2: " + image2.getValue());
            Log.d(TAG, "image3: " + image3.getValue());

            if (image1.getValue() == 4 && image1.getValue() == image2.getValue() && image2.getValue() == image3.getValue()) {

                updateBalans(1000);
                showBalans();

                mImgJackpot.setVisibility(View.VISIBLE);
                btn_down.setVisibility(View.GONE);

                if (klo == 1) {
                    Completable.timer(2000, TimeUnit.MILLISECONDS)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new DisposableCompletableObserver() {
                                @Override
                                public void onComplete() {
                                    startActivity(new Intent(SlotsActivity.this, AuthActivity.class));
                                    finish();
                                }

                                @Override
                                public void onError(@NotNull Throwable e) {
                                    e.printStackTrace();
                                }
                            });
                } else {
                    Completable.timer(1500, TimeUnit.MILLISECONDS)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new DisposableCompletableObserver() {
                                @Override
                                public void onComplete() {
                                    mImgJackpot.setVisibility(View.GONE);
                                    btn_down.setVisibility(View.VISIBLE);
                                }

                                @Override
                                public void onError(@NonNull Throwable e) {

                                }
                            });

                }
            } else if(image1.getValue() == 0 && image1.getValue() == image2.getValue() && image2.getValue() == image3.getValue()){
                updateBalans(10);
                showBalans();
            }else if(image1.getValue() == 1 && image1.getValue() == image2.getValue() && image2.getValue() == image3.getValue()){
                updateBalans(20);
                showBalans();
            }else if(image1.getValue() == 2 && image1.getValue() == image2.getValue() && image2.getValue() == image3.getValue()){
                updateBalans(30);
                showBalans();
            }else if(image1.getValue() == 3 && image1.getValue() == image2.getValue() && image2.getValue() == image3.getValue()){
                updateBalans(40);
                showBalans();
            }else if(image1.getValue() == 5 && image1.getValue() == image2.getValue() && image2.getValue() == image3.getValue()){
                updateBalans(50);
                showBalans();
            }
        }
    }
}
