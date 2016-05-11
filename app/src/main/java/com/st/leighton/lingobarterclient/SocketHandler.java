package com.st.leighton.lingobarterclient;

import com.github.nkzawa.socketio.client.Socket;

/**
 * Created by vicky on 5/10/16.
 */
public class SocketHandler {
    private static Socket socket;

    public static synchronized Socket getSocket(){
        return socket;
    }

    public static synchronized void setSocket(Socket socket){
        SocketHandler.socket = socket;
    }
}
