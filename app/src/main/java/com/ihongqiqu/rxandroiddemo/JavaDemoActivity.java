package com.ihongqiqu.rxandroiddemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class JavaDemoActivity extends AppCompatActivity {

    private static final String TAG = JavaDemoActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_java_demo);

        String[] strArr = {"1111;1122", "true;true", "3333;3344", "true", "5555;5566", "false;123;true"};

        testNormalMethod(strArr);
        testRxJavaMethod(strArr);
    }

    /**
     * [一般写法] 遍历数组并对字符串（只有数字和“true”和“false”）分割处理且指数出整数
     *
     * @param args
     */
    private void testNormalMethod(String[] args) {
        if (BuildConfig.DEBUG) Log.d(TAG, " ");
        Log.d(TAG, "======= testNormalMethod =======");
        for (int i = 0; i < args.length; i++) {
            String item = args[i];
            String[] items = item.split(";");
            for (int i1 = 0; i1 < items.length; i1++) {
                String item1 = items[i1];
                if (!"true".equals(item1) && !"false".equals(item1)) {
                    Log.d(TAG, "testNormalMethod integer : " + Integer.parseInt(item1));
                }
            }
        }
    }

    /**
     * [RxJava写法] 遍历数组并对字符串（只有数字和“true”和“false”）分割处理且指数出整数
     *
     * @param args
     */
    private void testRxJavaMethod(String[] args) {
        if (BuildConfig.DEBUG) Log.d(TAG, " ");
        Log.d(TAG, "\n======= testRxJavaMethod =======");
        Observable.from(args)
                .flatMap(new Func1<String, Observable<String>>() {
                    @Override
                    public Observable<String> call(String s) {
                        return Observable.from(s.split(";"));
                    }
                })
                .filter(new Func1<String, Boolean>() {
                    @Override
                    public Boolean call(String s) {
                        return !"true".equals(s) && !"false".equals(s);
                    }
                })
                .map(new Func1<String, Integer>() {
                    @Override
                    public Integer call(String s) {
                        return Integer.parseInt(s);
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        Log.d(TAG, "testNormalMethod integer : " + integer);
                    }
                });
    }
}
