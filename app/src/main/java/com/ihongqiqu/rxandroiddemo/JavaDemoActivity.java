package com.ihongqiqu.rxandroiddemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;

public class JavaDemoActivity extends AppCompatActivity {

    private static final String TAG = JavaDemoActivity.class.getSimpleName();

    String[] strArr = {"1111;1122", "true;true", "3333;3344", "true", "5555;5566", "false;123;true"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_java_demo);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_test_normal_method:
                testNormalMethod(strArr);
                break;
            case R.id.btn_test_rxjava_method:
                testRxJavaMethod(strArr);
                break;
            case R.id.btn_test_observer:
                testObserver();
                break;
            case R.id.btn_test_subscribe:
                testSubscribe();
                break;
            case R.id.btn_test_action:
                testAction();
                break;

            default:

                break;
        }
    }

    private void testAction() {
        if (BuildConfig.DEBUG) Log.d(TAG, " ");
        Log.d(TAG, "======= testAction =======");
        Observable observable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("Hello");
                subscriber.onNext("World");
                subscriber.onNext("!");
                subscriber.onCompleted();
            }
        });

        Action1<String> onNextAction = new Action1<String>() {
            @Override
            public void call(String s) {
                Log.d(TAG, "onNextAction call() s : " + s);
            }
        };

        Action1<Throwable> onErrorAction = new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                throwable.printStackTrace();
            }
        };

        Action0 onCompletedAction = new Action0() {
            @Override
            public void call() {
                Log.d(TAG, "onCompletedAction call()");
            }
        };

        observable.subscribe(onNextAction, onErrorAction, onCompletedAction);
        observable.subscribe(onNextAction, onErrorAction);
        observable.subscribe(onNextAction);
    }

    /**
     * 订阅Observer
     */
    private void testObserver() {
        if (BuildConfig.DEBUG) Log.d(TAG, " ");
        Log.d(TAG, "======= testObserver =======");
        Observer<String> observer = new Observer<String>() {
            @Override
            public void onCompleted() {
                Log.d(TAG, "onCompleted()");
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError()");
            }

            @Override
            public void onNext(String s) {
                Log.d(TAG, s);
            }
        };

        Observable observable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("Hello");
                subscriber.onNext("World");
                subscriber.onNext("!");
                subscriber.onCompleted();
            }
        });


        observable.subscribe(observer);

    }

    /**
     * 订阅subscriber
     */
    private void testSubscribe() {
        if (BuildConfig.DEBUG) Log.d(TAG, " ");
        Log.d(TAG, "======= testSubscribe =======");
        final Subscriber<String> subscriber = new Subscriber<String>() {
            @Override
            public void onCompleted() {
                Log.d(TAG, "onCompleted()");
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError()");
            }

            @Override
            public void onNext(String s) {
                Log.d(TAG, s);
            }
        };
        Observable observable = Observable.just("111", "222", "333");
        observable.subscribe(subscriber);
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
                // .subscribeOn(Schedulers.newThread())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        Log.d(TAG, "testNormalMethod integer : " + integer);
                    }
                });
    }
}
