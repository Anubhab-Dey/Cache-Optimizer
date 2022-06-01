package com.example.test;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity{

    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button butt=(Button)findViewById(R.id.button);
        butt.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v) {
                tv=(TextView)findViewById(R.id.textbox);
                Run P = new Run();
                new Thread(P).start();
            }
        });
    }

    class Run implements Runnable{



        @Override
        public void run() {
            try {

                Process process = Runtime.getRuntime().exec("pm compile -m speed-profile -f com.android.traceur -a");

                // Reads stdout.
                // NOTE: You can write to stdin of the command using
                //       process.getOutputStream().
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(process.getInputStream()));
                int read;
                char[] buffer = new char[4096];
                StringBuffer output = new StringBuffer();
                while ((read = reader.read(buffer)) > 0) {
                    output.append(buffer, 0, read);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tv.setText(output.toString());
                        }
                    });
                }
                reader.close();
                process.waitFor();
//                while (process.waitFor()!=0){
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            tv.setText(output);
//                        }
//                    });
//                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

//    public String runAsRoot(){
//
//    }
}