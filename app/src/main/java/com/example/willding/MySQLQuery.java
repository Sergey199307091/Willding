package com.example.willding;

import android.os.AsyncTask;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MySQLQuery {
    public String postQueryExample(String studentID,String name,String description,String points){//новая запись в таблицу MySQL
        final String[] ret = {""};
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                OkHttpClient okHttpClient = new OkHttpClient();
                RequestBody formBody = new FormBody.Builder()
                        .add("studentID", studentID)
                        .add("name", name)
                        .add("description", description)
                        .add("points",points)
                        .build();
                Request request = new Request.Builder()
                        .url("https://sergeypopov.000webhostapp.com/create.php")
                        .post(formBody)
                        .build();
                try {
                    Response response = okHttpClient.newCall(request).execute();
                    String stringResponse = response.body().string(); //в этой переменной будет ответ сервера
                    ret[0] =stringResponse;

                } catch (IOException e) {

                }
            }
            };
        Thread thread = new Thread(runnable);
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        return ret[0];
    }
    public String getAccount(String studentID){//получение очков пользователя
        final String[] point = {""};
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                OkHttpClient okHttpClient = new OkHttpClient();
                RequestBody formBody = new FormBody.Builder()
                        .add("studentID", studentID)
                        .build();
                Request request = new Request.Builder()
                        .url("https://sergeypopov.000webhostapp.com/getAccount.php")
                        .post(formBody)
                        .build();
                try {
                    Response response = okHttpClient.newCall(request).execute();
                    String stringResponse = response.body().string(); //в этой переменной будет ответ сервера
point[0] =stringResponse;
                } catch (IOException e) {
                    // Log.e(TAG, "Exception: "+Log.getStackTraceString(e));
                }
            }
            };
        Thread thread = new Thread(runnable);
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return point[0];
    }

}
