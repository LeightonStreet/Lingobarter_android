package com.st.leighton.lingobarterclient;

/**
 * Created by vicky on 5/10/16.
 */
import android.app.Application;
import com.lingobarter.socketclient.WebsocketClient;

public class MyApplication extends Application {

    public void setSocket(WebsocketClient socket){
        SocketHandler.setSocket(socket);
    }

    public WebsocketClient getSocket() {
        return SocketHandler.getSocket();
    }
}