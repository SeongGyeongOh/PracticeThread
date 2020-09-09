package com.gyeong.practicethread;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Random;

public class Lab16_3Activity extends AppCompatActivity {

    OneThread oneThread;

    ArrayList<String> oddDatas;
    ArrayList<String> evenDatas;

    ArrayAdapter<String> oddAdapter;
    ArrayAdapter<String> evenAdapter;

    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab16_3);

        ListView oddListView=(ListView)findViewById(R.id.lab3_list_odd);
        ListView evenListView=(ListView)findViewById(R.id.lab3_list_even);

        oddDatas=new ArrayList<>();
        evenDatas=new ArrayList<>();

        oddAdapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, oddDatas);
        evenAdapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, evenDatas);

        oddListView.setAdapter(oddAdapter);
        evenListView.setAdapter(evenAdapter);

        handler=new Handler();

        oneThread=new OneThread();
        oneThread.start();

        TwoThread twoThread=new TwoThread();
        twoThread.start();

        //HandlerThread 연습
//        HandlerThread handlerThread=new HandlerThread("handlerThread");
//        handlerThread.start();
//
//        Handler looperHandler=new Handler(handlerThread.getLooper());
//        looperHandler.post(new Runnable() {
//            @Override
//            public void run() {
//                int data=0;
//                for(int i=0;i<10;i++){
//                    SystemClock.sleep(1000);
//                    data++;
//                    evenDatas.add("even : "+data );
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            evenAdapter.notifyDataSetChanged();
//                        }
//                    });
//                }
//            }
//        });
    }






    class OneThread extends Thread{
        Handler oneHandler;

        @Override
        public void run() {
            Looper.prepare();
            oneHandler=new Handler(Looper.myLooper()){
                @Override
                public void handleMessage(@NonNull Message msg) {
                    SystemClock.sleep(1000);
                    final int data=msg.arg1;
                    if(msg.what==0){
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                evenDatas.add("even : "+data);
                                evenAdapter.notifyDataSetChanged();
                            }
                        });
                    }else if(msg.what==1){
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                oddDatas.add("odd: "+data);
                                oddAdapter.notifyDataSetChanged();
                            }
                        });
                    }
                }
            };
            Looper.loop();
        }
    }

    class TwoThread extends Thread{
        @Override
        public void run() {
            Random random=new Random();
            for(int i=0;i<10;i++){
                SystemClock.sleep(100);
                int data=random.nextInt(10  );
                Message message=new Message();
                if(data%2==0){
                    message.what=0;
                }else{
                    message.what=1;
                }
                message.arg1=data;
                message.arg2=i;
                oneThread.oneHandler.sendMessage(message);
            }
            Log.d("Practice", "two thread stop..");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        oneThread.oneHandler.getLooper().quit();
    }
}
