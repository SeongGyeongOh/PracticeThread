package com.gyeong.practicethread;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView startView;
    ImageView pauseView;
    TextView textView;

    boolean loopFlag = true;
    boolean isFirst = true;
    boolean isRun;

    MyThread thread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startView = (ImageView) findViewById(R.id.main_startBtn);
        pauseView = (ImageView) findViewById(R.id.main_pauseBtn);
        textView = (TextView) findViewById(R.id.main_textView);

        startView.setOnClickListener(this);
        pauseView.setOnClickListener(this);

        thread = new MyThread();
    }

    @Override
    public void onClick(View v) {
        if (v == startView) {
            if (isFirst) {
                isFirst = false;
                isRun = true;
                thread.start();
            } else {
                isRun = true;
            }
        } else if (v == pauseView) {
            isRun = false;
        }
    }

    Handler handler=new Handler(Looper.myLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if(msg.what==1){
                textView.setText(String.valueOf(msg.arg1));
            }else if(msg.what==2){
                textView.setText((String)msg.obj);
            }
        }
    };

    class MyThread extends Thread{
        @Override
        public void run() {
            super.run();
            try {
                int count=10;
                while(loopFlag){
                    Thread.sleep(1000);
                    if(isRun){
                        count--;
                        Message message=new Message();
                        message.what=1;
                        message.arg1=count;
                        handler.sendMessage(message);
                        if(count==0){
                            message=new Message();
                            message.what=2;
                            message.obj="Finish!!";
                            handler.sendMessage(message);

                            loopFlag=false;
                        }
                    }
                }
            }catch (Exception e){

            }
        }
    }


    
}
