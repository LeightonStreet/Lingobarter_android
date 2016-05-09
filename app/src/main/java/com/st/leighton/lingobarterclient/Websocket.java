package com.st.leighton.lingobarterclient;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

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
                String feedback;

                switch (status) {
                    case 200:
                        token = jsonResult.getJSONObject("response").getString("auth_token");
                        name = jsonResult.getJSONObject("response").getString("name");
                        username = jsonResult.getJSONObject("response").getString("username");
                        userid = jsonResult.getJSONObject("response").getString("user_id");

                        feedback = "Succeed";
                        break;

                    case 403:
                        feedback = "NeedConfirm";
                        break;

                    case 406:
                        feedback = "InvalidPassword";
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
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
