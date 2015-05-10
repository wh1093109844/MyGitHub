/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: D:\\program\\Android\\AndroidProject\\MyGitHub\\MiuscPlayer\\src\\com\\example\\miuscplayer\\aidl\\IPlayerService.aidl
 */
package com.example.miuscplayer.aidl;
public interface IPlayerService extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.example.miuscplayer.aidl.IPlayerService
{
private static final java.lang.String DESCRIPTOR = "com.example.miuscplayer.aidl.IPlayerService";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.example.miuscplayer.aidl.IPlayerService interface,
 * generating a proxy if needed.
 */
public static com.example.miuscplayer.aidl.IPlayerService asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.example.miuscplayer.aidl.IPlayerService))) {
return ((com.example.miuscplayer.aidl.IPlayerService)iin);
}
return new com.example.miuscplayer.aidl.IPlayerService.Stub.Proxy(obj);
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
case TRANSACTION_stop:
{
data.enforceInterface(DESCRIPTOR);
this.stop();
reply.writeNoException();
return true;
}
case TRANSACTION_start:
{
data.enforceInterface(DESCRIPTOR);
this.start();
reply.writeNoException();
return true;
}
case TRANSACTION_seekTo:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
this.seekTo(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_getPosition:
{
data.enforceInterface(DESCRIPTOR);
int _result = this.getPosition();
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_getDuration:
{
data.enforceInterface(DESCRIPTOR);
int _result = this.getDuration();
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_setOnProgressChanagerListener:
{
data.enforceInterface(DESCRIPTOR);
com.example.miuscplayer.aidl.OnProgressChanageListener _arg0;
_arg0 = com.example.miuscplayer.aidl.OnProgressChanageListener.Stub.asInterface(data.readStrongBinder());
this.setOnProgressChanagerListener(_arg0);
reply.writeNoException();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.example.miuscplayer.aidl.IPlayerService
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
@Override public void stop() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_stop, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void start() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_start, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void seekTo(int progress) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(progress);
mRemote.transact(Stub.TRANSACTION_seekTo, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public int getPosition() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getPosition, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public int getDuration() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getDuration, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public void setOnProgressChanagerListener(com.example.miuscplayer.aidl.OnProgressChanageListener onProgressChanagerListener) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeStrongBinder((((onProgressChanagerListener!=null))?(onProgressChanagerListener.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_setOnProgressChanagerListener, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_stop = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_start = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_seekTo = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
static final int TRANSACTION_getPosition = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
static final int TRANSACTION_getDuration = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
static final int TRANSACTION_setOnProgressChanagerListener = (android.os.IBinder.FIRST_CALL_TRANSACTION + 5);
}
public void stop() throws android.os.RemoteException;
public void start() throws android.os.RemoteException;
public void seekTo(int progress) throws android.os.RemoteException;
public int getPosition() throws android.os.RemoteException;
public int getDuration() throws android.os.RemoteException;
public void setOnProgressChanagerListener(com.example.miuscplayer.aidl.OnProgressChanageListener onProgressChanagerListener) throws android.os.RemoteException;
}
