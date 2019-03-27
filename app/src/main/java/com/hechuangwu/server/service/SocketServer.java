package com.hechuangwu.server.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

/**
 * Created by cwh on 2019/3/26.
 * 功能:
 */
public class SocketServer extends Service {
    private boolean mIsServerDestroy;
    private String[] mMessage = {"你好","你不好","你好不好","你不好挺好","你好大家好"};
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    private ServerSocket mServerSocket = null;
    @Override
    public void onCreate() {
        new Thread( new TCPServer()).start();
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            acceptClient();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return super.onStartCommand( intent, flags, startId );
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mIsServerDestroy = true;
    }

    private class TCPServer implements Runnable{
        @Override
        public void run() {
            try {
                mServerSocket = new ServerSocket( 8688 );
                acceptClient();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void acceptClient() throws IOException {
        if(mServerSocket!=null) {
            new Thread( new Runnable() {
                @Override
                public void run() {
                    Socket client = null;
                    try {
                        client = mServerSocket.accept();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    responseClient( client );
                }
            } ).start();
        }
    }

    private void responseClient(Socket client) {
        try {
            BufferedReader bufferedReader = new BufferedReader( new InputStreamReader( client.getInputStream() ) );
            PrintWriter printWriter = new PrintWriter( new BufferedWriter( new OutputStreamWriter( client.getOutputStream() ) ) ,true);
            while (!mIsServerDestroy){
                String msg = bufferedReader.readLine();
                if(msg==null){
                 break;
                }
                Log.i( "data", "responseClient: "+msg );
                SystemClock.sleep( 1000 );
                printWriter.println( mMessage[new Random(  ).nextInt( mMessage.length )] );
            }
            bufferedReader.close();
            printWriter.close();
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
