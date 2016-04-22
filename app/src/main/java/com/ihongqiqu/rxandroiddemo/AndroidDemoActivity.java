package com.ihongqiqu.rxandroiddemo;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class AndroidDemoActivity extends AppCompatActivity {

    private static final String TAG = "Demo";

    private android.widget.Button btnshowimage;
    private android.widget.ImageView ivshow;
    private Button btnaddoperation;
    private android.widget.TextView tvsum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_android_demo);
        this.tvsum = (TextView) findViewById(R.id.tv_sum);
        this.btnaddoperation = (Button) findViewById(R.id.btn_add_operation);
        this.ivshow = (ImageView) findViewById(R.id.iv_show);
        this.btnshowimage = (Button) findViewById(R.id.btn_show_image);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_show_image:
                clickShowImage();
                break;
            case R.id.btn_add_operation:
                clickAddOperation();
                break;
            default:

                break;
        }
    }

    private void clickAddOperation() {
        Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                int sum = 0;
                for (int i = 0; i < 100 ; i++) {
                    sum += (i + 1);
                }
                subscriber.onNext(sum);
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        tvsum.setText("sum = " + integer);

                    }
                });
    }

    private void clickShowImage() {
        Log.d(TAG, "clickShowImage()");
        final int resId = R.mipmap.ic_launcher;
        Observable.create(new Observable.OnSubscribe<Drawable>() {
            @Override
            public void call(Subscriber<? super Drawable> subscriber) {
                Drawable drawable = getResources().getDrawable(resId);
                subscriber.onNext(drawable);
                subscriber.onCompleted();
            }
        }).subscribe(new Observer<Drawable>() {
            @Override
            public void onCompleted() {
                Log.d(TAG, "onCompleted()");
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(Drawable drawable) {
                Log.d(TAG, "onNext()");
                ivshow.setImageDrawable(drawable);
            }
        });
    }


}
