package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btn = findViewById(R.id.startButton);

        EditText time_text = findViewById(R.id.time_text);
        EditText single_text = findViewById(R.id.single_text);
        EditText judge_text = findViewById(R.id.judge_text);
        EditText mutichoice_text = findViewById(R.id.mutichoice_text);


        Map<String, String> userInfo = Save.getUserInfo(this);
        String DB_PATH = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/databases/";
        String DB_NAME = "question.db";
        //应用启动时，判断数据库是否存在，不存在则将提前打包好的数据库文件复制到数据库目录下
        //数据库目录不存在时，创建数据库目录
        if ((new File(DB_PATH + DB_NAME).exists()) == false) {
            File dir = new File(DB_PATH);
            if (!dir.exists()) {
                dir.mkdir();
            }
        }
        //定义输入输出流，用于复制文件
        try {
            InputStream is = getBaseContext().getAssets().open(DB_NAME);
            OutputStream os = new FileOutputStream(DB_PATH + DB_NAME);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
            //刷新输出流，关闭输入输出流
            os.flush();
            os.close();
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 保存
        if (userInfo != null) {
            time_text.setText(userInfo.get("time"));
            single_text.setText(userInfo.get("single"));
            judge_text.setText(userInfo.get("judge"));
            mutichoice_text.setText(userInfo.get("mutichoice"));
        }

        btn.setOnClickListener(v -> {
            String time = time_text.getText().toString();
            String single = single_text.getText().toString();
            String judge = judge_text.getText().toString();
            String mutichoice = mutichoice_text.getText().toString();

            int timeNum = Integer.parseInt(time);
            int singleNum = Integer.parseInt(single);
            int judgeNum = Integer.parseInt(judge);
            int mutichoiceNum = Integer.parseInt(mutichoice);

            Save.saveUserInfo(this, time, single, judge, mutichoice);
            Intent intent = new Intent(MainActivity.this, ExamActivity.class);
            intent.putExtra("time", timeNum);
            intent.putExtra("single", singleNum);
            intent.putExtra("judge", judgeNum);
            intent.putExtra("mutichoice", mutichoiceNum);
            startActivity(intent);
        });
    }
}
