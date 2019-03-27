package com.hechuangwu.server.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.hechuangwu.server.aidl.BinderPool;

/**
 * Created by cwh on 2019/3/27.
 * 功能:
 */
public class BinderPoolServer extends Service {
    private Binder mBinder = new BinderPool.BinderPoolStub();

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
}
