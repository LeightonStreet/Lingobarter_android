package com.st.leighton.lingobarterclient;

import com.lingobarter.socketclient.WebsocketClient;

/**
 * Created by vicky on 5/10/16.
 */
public class SocketHandler {
    private static WebsocketClient socket;

    public static synchronized WebsocketClient getSocket(){
        return socket;
    }

    public static synchronized void setSocket(WebsocketClient socket){
        SocketHandler.socket = socket;
    }
}
