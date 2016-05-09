package com.st.leighton.lingobarterclient;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import org.json.JSONObject;

public class Websocket extends Service {
    private String token;
    private String name;
    private String userid;
    private String username;

    private static Websocket instance = null;

    @Override
    public void onCreate() {
        instance = this;
        super.onCreate();
    }

    public static Websocket getInstance()
    {
        if (instance == null) instance = new Websocket();
        return instance;
    }

    public void Login(String email, String password) {
        WebsocketClient client
                = new WebsocketClient(WebsocketClient.METHOD.Post, "/api/v1/accounts/authorize");
        client.AddHeader("content-type", "application/json");
        client.AddPayload("email", email);
        client.AddPayload("password", password);
        client.Execute();
        Boolean flag = client.Waiting();

        if (flag) {
            JSONObject jsonResult = client.getJSON();
            try {
                int status = jsonResult.getInt("status");
                String feedback = "";

                switch (status) {
                    case 200:
                        token = jsonResult.getJSONObject("response").getString("auth_token");
                        name = jsonResult.getJSONObject("response").getString("name");
                        username = jsonResult.getJSONObject("response").getString("username");
                        userid = jsonResult.getJSONObject("response").getString("user_id");

                        feedback = "Succeed";
//                        intent.putExtra(Login.LOGIN_FEEDBACK, );
//                        getInstance().sendBroadcast(intent);

                        break;

                    case 403:
                        feedback = "NeedConfirm";
//                        broadcast("android.intent.action.Login", Login.LOGIN_FEEDBACK, );
//                        Intent intent = new Intent("android.intent.action.Login").putExtra(Login.LOGIN_FEEDBACK, "InvalidUser");
//                        getInstance().sendBroadcast(intent);
//                        intent.putExtra(Login.LOGIN_FEEDBACK, "Succeed");
//                        getInstance().sendBroadcast(intent);
                        break;

                    case 406:
                        feedback = "InvalidPassword";
//                        broadcast("android.intent.action.Login", Login.LOGIN_FEEDBACK, );
//                        Intent intent = new Intent("android.intent.action.Login").putExtra(Login.LOGIN_FEEDBACK, "InvalidUser");
//                        getInstance().sendBroadcast(intent);
//                        intent.putExtra(Login.LOGIN_FEEDBACK, "Succeed");
//                        getInstance().sendBroadcast(intent);
                        break;

                    case 404:
                        feedback = "InvalidUser";
                        break;

                    default:
                        feedback = "";
                        break;
                }

                Intent intent = new Intent("android.intent.action.Login");
                intent.putExtra(Login.LOGIN_FEEDBACK, feedback);
                getInstance().sendBroadcast(intent);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void Logout() {

    }

    public void UpdatePassword(String old_password, String new_password) {

    }

    public void ResetPassword(String email) {

    }

    public void UpdateUsername(String username) {

    }

    public void ConfirmEmail(String email) {

    }

    public void Signup(String username, String email, String password) {

    }

    public void UpdateProfile() {

    }

    public void ViewSelfProfile() {

    }

    public void DeleteAccount() {

    }

    public void ViewOtherProfile() {

    }

    public void UploadAvatar() {

    }

    public void Search() {
        
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        return 0;
        return super.onStartCommand(intent, flags, startId);
//        Log.d("There", "Service started");
//        Intent i = new Intent("android.intent.action.Login").putExtra(Login.LOGIN_FEEDBACK, "Hahaha");
//        this.sendBroadcast(i);

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    DefaultHttpClient httpClient = new DefaultHttpClient();
//                    HttpHost target = new HttpHost("10.0.2.2", 8080, "http");
//                    HttpPost postRequest = new HttpPost("/api/v1/accounts/authorize");
//                    postRequest.addHeader("content-type", "application/json");
//                    JSONObject object = new JSONObject();
//
//                    object.put("email", "lingobarter.user2@gmail.com");
//                    object.put("password", "user2lingobarter");
//
//                    StringEntity entity = new StringEntity(object.toString());
//                    postRequest.setEntity(entity);
//
//                    Log.d("Hellllllo", "executing request to " + target);
//                    Log.d("Hellllllo", "----------------------------------------");
//                    HttpResponse httpResponse = httpClient.execute(target, postRequest);
//                    Log.d("Hellllllo", "----------------------------------------");
//                    HttpEntity ret = httpResponse.getEntity();
//
//                    Log.d("Hellllllo", "----------------------------------------");
//                    Log.d("Hellllllo", (httpResponse.getStatusLine()).toString());
//                    Header[] headers = httpResponse.getAllHeaders();
//                    for (int idx = 0; idx < headers.length; idx++) {
//                        Log.d("Hellllllo", headers[idx].toString());
//                    }
//                    System.out.println("----------------------------------------");
//
//                    if (entity != null) {
//                        Log.d("Entity", EntityUtils.toString(entity));
//                    }
//
//                    if (ret != null) {
//                        String temp = EntityUtils.toString(ret);
//                        Log.d("Back", temp);
//                        JSONObject result = new JSONObject(temp);
//                        Log.d("Back", result.getJSONObject("response").getString("auth_token"));
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();

//        WebsocketClient client = new WebsocketClient(WebsocketClient.METHOD.Post, "/api/v1/accounts/authorize");
//        client.AddHeader("content-type", "application/json");
//        client.AddPayload("email", "lingobarter.user2@gmail.com");
//        client.AddPayload("password", "user2lingobarter");
//        client.Execute();
//        client.Waiting();
//        Log.d("Test", client.getStatus());
//        Log.d("Test", client.getRawResult());
//        JSONObject jsonResult = client.getJSON();
//        try {
//            Log.d("Test", jsonResult.getJSONObject("response").getString("auth_token"));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
