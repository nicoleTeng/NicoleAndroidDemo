/*
 * This file is IBookManager.java file's coyp, just manual format for reader.
 * Do not use this file.
 */
package com.example.aidl;

public interface IBookManagerFormat extends android.os.IInterface {
    
    public java.util.List<com.example.aidl.Book> getBookList() throws android.os.RemoteException;
    public void addBook(com.example.aidl.Book book) throws android.os.RemoteException;
    public void registerListener(com.example.aidl.IOnNewBookArrivedListener listener) throws android.os.RemoteException;
    public void unregisterListener(com.example.aidl.IOnNewBookArrivedListener listener) throws android.os.RemoteException;

    /** Local-side IPC implementation stub class. */
    public static abstract class Stub extends android.os.Binder implements com.example.aidl.IBookManagerFormat {

        static final int TRANSACTION_getBookList = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
        static final int TRANSACTION_addBook = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
        static final int TRANSACTION_registerListener = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
        static final int TRANSACTION_unregisterListener = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);

        private static final java.lang.String DESCRIPTOR = "com.example.aidl.IBookManagerFormat";
        
        /** Construct the stub at attach it to the interface. */
        public Stub() {
            this.attachInterface(this, DESCRIPTOR);
        }
        
        /**
         * Cast an IBinder object into an com.example.aidl.IBookManager interface,
         * generating a proxy if needed.
         * 
         * 用于将服务端的Binder对象转换成客户端所需的AIDL接口类型的对象
         */
        public static com.example.aidl.IBookManagerFormat asInterface(android.os.IBinder obj) {
            if (obj == null) {
                return null;
            }
            android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if ( iin != null && iin instanceof com.example.aidl.IBookManagerFormat ) {
                // 客户端和服务端位于同一进程，返回服务端的Stub对象本身
                return (com.example.aidl.IBookManagerFormat) iin;
            }
            // 客户端和服务端位于不同进程，返回系统封装后的Stub.proxy对象
            return new com.example.aidl.IBookManagerFormat.Stub.Proxy(obj);
        }
        
        // 返回当前Binder对象
        @Override 
        public android.os.IBinder asBinder() {
            return this;
        }
        
        /* 运行在服务端中的Binder线程池
         * 当客户端发起跨进程请求时，远程请求会通过系统底层封装后交由此方法来处理
         * 服务端通过code可以确定客户端请求的目标方法
         * 接着从data中获取目标方法所需的参数
         * 然后执行目标方法
         * 当执行完毕，向reply中写入返回值
         */
        @Override 
        public boolean onTransact(int code, android.os.Parcel data,
                android.os.Parcel reply, int flags) throws android.os.RemoteException {
            switch (code)  {
                case INTERFACE_TRANSACTION:  {
                    reply.writeString(DESCRIPTOR);
                    return true;
                }
                case TRANSACTION_getBookList: {
                    data.enforceInterface(DESCRIPTOR);
                    java.util.List<com.example.aidl.Book> _result = this.getBookList();
                    reply.writeNoException();
                    reply.writeTypedList(_result);
                    return true;
                }
                case TRANSACTION_addBook: {
                    data.enforceInterface(DESCRIPTOR);
                    com.example.aidl.Book _arg0;
                    if ((0!=data.readInt())) {
                        _arg0 = com.example.aidl.Book.CREATOR.createFromParcel(data);
                    } else {
                        _arg0 = null;
                    }
                    this.addBook(_arg0);
                    reply.writeNoException();
                    return true;
                }
                case TRANSACTION_registerListener: {
                    data.enforceInterface(DESCRIPTOR);
                    com.example.aidl.IOnNewBookArrivedListener _arg0;
                    _arg0 = com.example.aidl.IOnNewBookArrivedListener.Stub.asInterface(data.readStrongBinder());
                    this.registerListener(_arg0);
                    reply.writeNoException();
                    return true;
                }
                case TRANSACTION_unregisterListener: {
                    data.enforceInterface(DESCRIPTOR);
                    com.example.aidl.IOnNewBookArrivedListener _arg0;
                    _arg0 = com.example.aidl.IOnNewBookArrivedListener.Stub.asInterface(data.readStrongBinder());
                    this.unregisterListener(_arg0);
                    reply.writeNoException();
                    return true;
                }
            }
            return super.onTransact(code, data, reply, flags);
        }
        
        private static class Proxy implements com.example.aidl.IBookManagerFormat {
            private android.os.IBinder mRemote;

            Proxy(android.os.IBinder remote) {
                mRemote = remote;
            }
            
            @Override 
            public android.os.IBinder asBinder() {
                return mRemote;
            }
            
            public java.lang.String getInterfaceDescriptor() {
                return DESCRIPTOR;
            }
            
            /*
             * 运行在客户端，当客户端远程调用此方法时，它的内部实现是这样的：
             * 1. 初始化方法所需要的输入型Parcel对象_data、输出型Parcel对象_reply和返回值对象List
             * 2. 接着调用transact方法来发起PRC(远程过程调用)请求，同时当前线程挂起
             * 3. 然后服务端的onTransact方法会被调用，直到PRC过程返回后，当前线程继续执行，并从_reply中取出PRC过程的返回结果
             * 4. 最后返回_reply中的数据
             */
            @Override 
            public java.util.List<com.example.aidl.Book> getBookList()
                    throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                java.util.List<com.example.aidl.Book> _result;
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    mRemote.transact(Stub.TRANSACTION_getBookList, _data, _reply, 0);
                    _reply.readException();
                    _result = _reply.createTypedArrayList(com.example.aidl.Book.CREATOR);
                }
                finally {
                    _reply.recycle();
                    _data.recycle();
                }
                return _result;
            }
            
            @Override 
            public void addBook(com.example.aidl.Book book) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    if ((book!=null)) {
                        _data.writeInt(1);
                        book.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    mRemote.transact(Stub.TRANSACTION_addBook, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
    
            @Override 
            public void registerListener(com.example.aidl.IOnNewBookArrivedListener listener)
                    throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeStrongBinder((((listener!=null))?(listener.asBinder()):(null)));
                    mRemote.transact(Stub.TRANSACTION_registerListener, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
            
            @Override 
            public void unregisterListener(com.example.aidl.IOnNewBookArrivedListener listener)
                    throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeStrongBinder((((listener!=null))?(listener.asBinder()):(null)));
                    mRemote.transact(Stub.TRANSACTION_unregisterListener, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }           
        } // Proxy end
    } // Stub end
}

