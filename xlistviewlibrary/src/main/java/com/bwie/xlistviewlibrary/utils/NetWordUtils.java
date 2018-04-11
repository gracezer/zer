package com.bwie.xlistviewlibrary.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * author:Created by WangZhiQiang on 2017-09-27.
 */

public class NetWordUtils {

    private static String  tag = "NetWordUtils";
    /**
     * 获取网络json
     *
     * @param urlString
     * @return
     */
    public static String getNetjson(String urlString) {

        try {
            URL url = new URL(urlString);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");//若果是get请求可以不用配置; 其他请求必须配置
            urlConnection.setConnectTimeout(8000);//设置链接超时间

            InputStream inputStream = urlConnection.getInputStream();//获取网络返回的输入流;

            //可拼接的字符串
            StringBuilder stringBuilder = new StringBuilder();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String temp = "";
            while ((temp = bufferedReader.readLine()) != null) {
                stringBuilder.append(temp);
                temp = "";
            }
            //这个是网络获取的数据
            String data = stringBuilder.toString();
            Log.e(tag, "getData: " + data);
            return data;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }


    /**
     * 获取网络图片的工具类
     *
     * @param urlString
     * @return
     */
    public static Bitmap getNetBitmap(String urlString) {

        try {
            //用URL封装链接地址;
            URL url = new URL(urlString);
            //用url打开链接
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            //联网的状态码
            int responseCode = urlConnection.getResponseCode();
            if(responseCode ==200){
                //链接上获取输入流
                InputStream inputStream = urlConnection.getInputStream();
                //把流直接转换成bitmap(系统提供的BitmapFactory)
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);//BitmapFactory是个工具类,系统提供的
                return bitmap;
            }else {
                Log.e(tag, "网络状态码:: "+responseCode );
            }




        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }


}
