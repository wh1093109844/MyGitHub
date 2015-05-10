/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: D:\\program\\Android\\AndroidProject\\MyGitHub\\MiuscPlayer\\src\\com\\example\\miuscplayer\\aidl\\OnProgressChanageListener.aidl
 */
package com.example.miuscplayer.aidl;
public interface OnProgressChanageListener extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.example.miuscplayer.aidl.OnProgressChanageListener
{
private static final java.lang.String DESCRIPTOR = "com.example.miuscplayer.aidl.OnProgressChanageListener";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.example.miuscplayer.aidl.OnProgressChanageListener interface,
 * generating a proxy if needed.
 */
public static com.example.miuscplayer.aidl.OnProgressChanageListener asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.example.miuscplayer.aidl.OnProgressChanageListener))) {
return ((com.example.miuscplayer.aidl.OnProgressChanageListener)iin);
}
return new com.example.miuscplayer.aidl.OnProgressChanageListener.Stub.Proxy(obj);
}
@Override public android.os.IBinder asBinder()
{
return this;
}
@Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
{
switch (code)
{
case INTERFACE_TRANSACTION:
{
reply.writeString(DESCRIPTOR);
return true;
}
case TRANSACTION_onProgressChange:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
int _arg1;
_arg1 = data.readInt();
this.onProgressChange(_arg0, _arg1);
reply.writeNoException();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.example.miuscplayer.aidl.OnProgressChanageListener
{
private android.os.IBinder mRemote;
Proxy(android.os.IBinder remote)
{
mRemote = remote;
}
@Override public android.os.IBinder asBinder()
{
return mRemote;
}
public java.lang.String getInterfaceDescriptor()
{
return DESCRIPTOR;
}
@Override public void onProgressChange(int progress, int max) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(progress);
_data.writeInt(max);
mRemote.transact(Stub.TRANSACTION_onProgressChange, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_onProgressChange = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
}
public void onProgressChange(int progress, int max) throws android.os.RemoteException;
}
