package com.hechuangwu.server.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.os.SystemClock;
import android.util.Log;

import com.hechuangwu.server.aidl.Book;
import com.hechuangwu.server.aidl.IBookAidlInterface;
import com.hechuangwu.server.aidl.IBookListenerlInterface;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by cwh on 2019/3/18.
 * 功能:
 */
public class AIDLServer extends Service {
//    private List<Book> mBookList = new ArrayList<>(  );
    //处理线程同步问题
    private CopyOnWriteArrayList<Book> mBookList = new CopyOnWriteArrayList<>(  );
    //客户端传到服务端会生成新的接口对象，RemoteCallbackList可以通过Binder处理
    private RemoteCallbackList<IBookListenerlInterface> mBookListenerList = new RemoteCallbackList<>(  );
    private AtomicBoolean mAtomicBoolean = new AtomicBoolean(false);

    @Override
    public void onCreate() {
        super.onCreate();
        Book book1 = new Book(1,"埃斯库罗斯","1000.00$" );
        Book book2 = new Book(1,"索福克勒斯","2000.00$" );
        mBookList.add( book1 );
        mBookList.add( book2 );
        new Thread( new AddBookRunnable() ).start();
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

    private IBookAidlInterface.Stub mBinder  = new IBookAidlInterface.Stub() {
        @Override
        public void addBook(Book book) throws RemoteException {
            mBookList.add( book );
        }

        @Override
        public List<Book> getBookList() throws RemoteException {
            //模仿耗时
            SystemClock.sleep( 10000 );
            return mBookList;
        }

        @Override
        public void registerBookListener(IBookListenerlInterface iBookListenerlInterface) throws RemoteException {
                mBookListenerList.register( iBookListenerlInterface );
        }

        @Override
        public void unRegisterBookListener(IBookListenerlInterface iBookListenerlInterface) throws RemoteException {
                mBookListenerList.unregister( iBookListenerlInterface );
            Log.i( "data", "unRegisterBookListener: "+mBookListenerList.beginBroadcast() );
            mBookListenerList.finishBroadcast();
        }
    };

    private class AddBookRunnable implements Runnable{

        @Override
        public void run() {
            while (!mAtomicBoolean.get()){
                SystemClock.sleep( 5000 );
                Book book = new Book( 1, "托马斯莫尔", "10000$" );
                try {
                    addNewBook( book );
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private void addNewBook(Book book) throws RemoteException {
        int size = mBookListenerList.beginBroadcast();
        for (int i = 0; i < size; i++) {
            IBookListenerlInterface iBookListenerlInterface = mBookListenerList.getBroadcastItem( i );
            iBookListenerlInterface.addNewBookListener( book );
        }
        mBookListenerList.finishBroadcast();
    }

}
