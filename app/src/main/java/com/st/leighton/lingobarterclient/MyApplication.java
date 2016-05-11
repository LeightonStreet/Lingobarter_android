package com.st.leighton.lingobarterclient;

/**
 * Created by vicky on 5/10/16.
 */
import android.app.Application;
import com.github.nkzawa.socketio.client.Socket;

public class MyApplication extends Application {

    private Socket socket;

    public void setSocket(Socket socket){
        this.socket = socket;
    }

    public Socket getSocket() {
        return socket;
    }
}
