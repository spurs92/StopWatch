package com.spurs.stopwatch;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView textView;

    MyThread myThread;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView=(TextView)findViewById(R.id.text_num);
    }

    public void clickBtn1(View v){

        if(myThread==null){
            myThread=new MyThread();
            myThread.start();
        }else {
            myThread.resumeThread();
        }

    }

    public void clickBtn2(View v){
        if(myThread!=null){
            myThread.stopThread();
            myThread=null;
        }

    }

    public void clickBtn3(View v){
        if(myThread!=null){
            myThread.pauseThread();
        }

    }

    class MyThread extends Thread{

        boolean isRun=true;
        boolean isWait=false;

        int min,sec,millis;

        @Override
        public void run() {
            while (isRun){
                millis++;
                if(millis>=100){
                    millis=0;
                    sec++;
                    if(sec>=60){
                        sec=0;
                        min++;
                    }
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String s= String.format("%02d:%02d:%02d", min, sec, millis);
                        textView.setText(s);
                    }
                });

                try {
                    MyThread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if(isWait){
                    synchronized (this){
                        try {
                            this.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }

            }//while
        }//run()

        void stopThread(){
            isRun=false;
        }

        void pauseThread(){
            isWait= true;
        }

        void resumeThread(){
            isWait= false;

            synchronized (this) {
                this.notify();
            }
        }
    }

}
