package com.hechuangwu.server.aidl;

import android.os.IBinder;
import android.os.RemoteException;

/**
 * Created by cwh on 2019/3/27.
 * 功能:
 */
public class BinderPool {
    private static final int BINDER_ADD = 0;
    private static final int BINDER_SUB = 1;
    public static class BinderPoolStub extends IBinderPool.Stub {
        @Override
        public IBinder queryBinder(int type) throws RemoteException {
            IBinder iBinder = null;
            switch (type) {
                case BINDER_ADD:
                    iBinder = new Addition();
                    break;
                case BINDER_SUB:
                    iBinder = new Subtraction();
                    break;
            }
            return iBinder;
        }
    }
}
