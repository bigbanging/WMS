package com.litte.wms.manager;

import android.text.TextUtils;
import android.util.Log;

import com.litte.wms.content.IURL;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by litte on 2017/12/29.
 */

public class HttpManager {

    /**
     * 封装登陆的POST请求的方法
     * @param username 用户名
     * @param password 密码
     * @return
     */
    public static boolean loginHttpPost(String username,String password){
        boolean flag = false;
        BufferedReader reader = null;
        try {
            URL url = new URL(IURL.LOGIN_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setConnectTimeout(5000);
            connection.setDoInput(true);
            connection.setDoInput(true);
            StringBuilder sb = new StringBuilder();
            Map<String,String> params = new HashMap<>();
            params.put("username",username);
            params.put("password",password);

            sb.append("grant_type");
            sb.append("=");
            sb.append("password");
            sb.append("&");
            for (Map.Entry<String,String> entry:params.entrySet()) {
                sb.append(entry.getKey());
                sb.append("=");
                sb.append(entry.getValue());
                sb.append("&");
            }
            byte[] bytes = sb.deleteCharAt(sb.length()-1).toString().getBytes();

            connection.setRequestProperty("Content-type","application/json;charset=UTF-8");
            connection.setRequestProperty("Content-Length", String.valueOf(bytes.length));
//            connection.setRequestProperty("Authorization","bearer ");
            connection.connect();
            OutputStream os = connection.getOutputStream();
            os.write(bytes);
            os.flush();
            os.flush();
            int statusCode = connection.getResponseCode();
            if (statusCode == 200){
                InputStream is = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(is,"utf-8"));
                String line = "";
                StringBuilder builder = new StringBuilder();
                while ((line = reader.readLine())!=null){
                    builder.append(line);
                }
                String jsonStr = builder.toString();
                JSONObject jsonObject = new JSONObject(jsonStr);
                String access_token = jsonObject.getString("access_token");
                if (!TextUtils.isEmpty(access_token)){
                    flag = true;
                    Log.i("TAG", "loginHttpPost: access_token:"+access_token);
                }else {
                    Log.i("TAG", "loginHttpPost: access_token:"+access_token);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return flag;
    }
}
