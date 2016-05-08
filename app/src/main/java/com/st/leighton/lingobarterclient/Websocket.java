package com.st.leighton.lingobarterclient;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class Websocket extends Service {
    private static Websocket instance = null;

    public Websocket() {
    }

    public static Websocket getInstance()
    {
        if (instance == null) instance = new Websocket();
        return instance;
    }

    public void Login(String email, String password) {

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
        int result = super.onStartCommand(intent, flags, startId);
        Log.d("There", "Service started");
        Intent i = new Intent("android.intent.action.Login").putExtra(Login.LOGIN_FEEDBACK, "Hahaha");
        this.sendBroadcast(i);

        return result;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
