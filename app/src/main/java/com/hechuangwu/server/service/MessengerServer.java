package com.hechuangwu.server.service;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;

/**
 * Created by cwh on 2019/3/18.
 * 功能:
 */
public class MessengerServer extends Service {
    private static final int MSG = 1001;
    private static final String MSG_DATA="msg_data";
    private static Messenger mMessenger = new Messenger( new MessengerHandler() );
    private static class MessengerHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage( msg );
            switch (msg.what){
                case MSG:
                    Bundle data = msg.getData();
                    String msgData = data.getString( MSG_DATA );
                    Log.i( "data", "handleMessage: >>>"+msgData );
                    break;
            }
        }
    }


    @Override
    public IBinder onBind(Intent intent) {
        return mMessenger.getBinder();
    }
}
