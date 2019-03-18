package com.hechuangwu.server.service;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

/**
 * Created by cwh on 2019/3/18.
 * 功能:
 */
public class MessengerServer extends Service {
    private static final int MSG_RECEIVER = 1001;
    private static final int MSG_REPLY= 1002;
    private static final String MSG_RECEIVER_DATA="msg_receiver_data";
    private static final String MSG_REPLY_DATA="msg_reply_data";
    private static Messenger mMessenger = new Messenger( new MessengerHandler() );
    private static class MessengerHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage( msg );
            switch (msg.what){
                case MSG_RECEIVER:
                    Bundle data = msg.getData();
                    String msgData = data.getString( MSG_RECEIVER_DATA );
                    Log.i( "data", "handleMessage: >>>"+msgData );
                    Messenger replyTo = msg.replyTo;
                    Message message = Message.obtain();
                    message.what = MSG_REPLY;
                    Bundle bundle = new Bundle();
                    bundle.putString( MSG_REPLY_DATA,"hello client ,i receiver" );
                    message.setData( bundle );
                    try {
                        replyTo.send( message );
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }


    @Override
    public IBinder onBind(Intent intent) {
        return mMessenger.getBinder();
    }
}
