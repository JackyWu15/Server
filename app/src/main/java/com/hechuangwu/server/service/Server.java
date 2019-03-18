package com.hechuangwu.server.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.hechuangwu.server.aidl.Book;
import com.hechuangwu.server.aidl.IBookAidlInterface;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cwh on 2019/3/18.
 * 功能:
 */
public class Server extends Service {
    private List<Book> mBookList = new ArrayList<>(  );

    @Override
    public void onCreate() {
        super.onCreate();
        Book book1 = new Book(1,"埃斯库罗斯","1000.00$" );
        Book book2 = new Book(1,"索福克勒斯","2000.00$" );
        mBookList.add( book1 );
        mBookList.add( book2 );
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i( "data", "onStartCommand: >>>>>>>>>>>" );
        return super.onStartCommand( intent, flags, startId );
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.i( "data", "onBind: >>>>>>>>>>>>>>>>" );
        return mBinder;
    }

    IBookAidlInterface.Stub mBinder  = new IBookAidlInterface.Stub() {
        @Override
        public void addBook(Book book) throws RemoteException {
            mBookList.add( book );
        }

        @Override
        public List<Book> getBooKList() throws RemoteException {
            return mBookList;
        }
    };


}
