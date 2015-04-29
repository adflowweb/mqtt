/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: /Users/nadir93/git/mqtt/PMCClient/app/src/main/aidl/kr/co/adflow/push/IPushService.aidl
 */
package kr.co.adflow.push;
// Declare any non-default types here with import statements

public interface IPushService extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements kr.co.adflow.push.IPushService
{
private static final java.lang.String DESCRIPTOR = "kr.co.adflow.push.IPushService";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an kr.co.adflow.push.IPushService interface,
 * generating a proxy if needed.
 */
public static kr.co.adflow.push.IPushService asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof kr.co.adflow.push.IPushService))) {
return ((kr.co.adflow.push.IPushService)iin);
}
return new kr.co.adflow.push.IPushService.Stub.Proxy(obj);
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
case TRANSACTION_preCheck:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
java.lang.String _arg1;
_arg1 = data.readString();
java.lang.String _result = this.preCheck(_arg0, _arg1);
reply.writeNoException();
reply.writeString(_result);
return true;
}
case TRANSACTION_getSubscriptions:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _result = this.getSubscriptions();
reply.writeNoException();
reply.writeString(_result);
return true;
}
case TRANSACTION_isConnected:
{
data.enforceInterface(DESCRIPTOR);
boolean _result = this.isConnected();
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_unsubscribe:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
java.lang.String _result = this.unsubscribe(_arg0);
reply.writeNoException();
reply.writeString(_result);
return true;
}
case TRANSACTION_subscribe:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
int _arg1;
_arg1 = data.readInt();
java.lang.String _result = this.subscribe(_arg0, _arg1);
reply.writeNoException();
reply.writeString(_result);
return true;
}
case TRANSACTION_ack:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
java.lang.String _arg1;
_arg1 = data.readString();
java.lang.String _result = this.ack(_arg0, _arg1);
reply.writeNoException();
reply.writeString(_result);
return true;
}
case TRANSACTION_existPMAByUFMI:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
java.lang.String _result = this.existPMAByUFMI(_arg0);
reply.writeNoException();
reply.writeString(_result);
return true;
}
case TRANSACTION_existPMAByUserID:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
java.lang.String _result = this.existPMAByUserID(_arg0);
reply.writeNoException();
reply.writeString(_result);
return true;
}
case TRANSACTION_sendMsg:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
java.lang.String _arg1;
_arg1 = data.readString();
java.lang.String _arg2;
_arg2 = data.readString();
java.lang.String _arg3;
_arg3 = data.readString();
java.lang.String _result = this.sendMsg(_arg0, _arg1, _arg2, _arg3);
reply.writeNoException();
reply.writeString(_result);
return true;
}
case TRANSACTION_sendMsgWithOpts:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
java.lang.String _arg1;
_arg1 = data.readString();
int _arg2;
_arg2 = data.readInt();
java.lang.String _arg3;
_arg3 = data.readString();
java.lang.String _arg4;
_arg4 = data.readString();
int _arg5;
_arg5 = data.readInt();
java.lang.String _result = this.sendMsgWithOpts(_arg0, _arg1, _arg2, _arg3, _arg4, _arg5);
reply.writeNoException();
reply.writeString(_result);
return true;
}
case TRANSACTION_updateUFMI:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
java.lang.String _result = this.updateUFMI(_arg0);
reply.writeNoException();
reply.writeString(_result);
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements kr.co.adflow.push.IPushService
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
@Override public java.lang.String preCheck(java.lang.String sender, java.lang.String topic) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(sender);
_data.writeString(topic);
mRemote.transact(Stub.TRANSACTION_preCheck, _data, _reply, 0);
_reply.readException();
_result = _reply.readString();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public java.lang.String getSubscriptions() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getSubscriptions, _data, _reply, 0);
_reply.readException();
_result = _reply.readString();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public boolean isConnected() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_isConnected, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public java.lang.String unsubscribe(java.lang.String topic) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(topic);
mRemote.transact(Stub.TRANSACTION_unsubscribe, _data, _reply, 0);
_reply.readException();
_result = _reply.readString();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public java.lang.String subscribe(java.lang.String topic, int qos) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(topic);
_data.writeInt(qos);
mRemote.transact(Stub.TRANSACTION_subscribe, _data, _reply, 0);
_reply.readException();
_result = _reply.readString();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public java.lang.String ack(java.lang.String msgID, java.lang.String tokenID) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(msgID);
_data.writeString(tokenID);
mRemote.transact(Stub.TRANSACTION_ack, _data, _reply, 0);
_reply.readException();
_result = _reply.readString();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public java.lang.String existPMAByUFMI(java.lang.String ufmi) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(ufmi);
mRemote.transact(Stub.TRANSACTION_existPMAByUFMI, _data, _reply, 0);
_reply.readException();
_result = _reply.readString();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public java.lang.String existPMAByUserID(java.lang.String userID) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(userID);
mRemote.transact(Stub.TRANSACTION_existPMAByUserID, _data, _reply, 0);
_reply.readException();
_result = _reply.readString();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public java.lang.String sendMsg(java.lang.String sender, java.lang.String receiver, java.lang.String contentType, java.lang.String content) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(sender);
_data.writeString(receiver);
_data.writeString(contentType);
_data.writeString(content);
mRemote.transact(Stub.TRANSACTION_sendMsg, _data, _reply, 0);
_reply.readException();
_result = _reply.readString();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public java.lang.String sendMsgWithOpts(java.lang.String sender, java.lang.String receiver, int qos, java.lang.String contentType, java.lang.String content, int expiry) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(sender);
_data.writeString(receiver);
_data.writeInt(qos);
_data.writeString(contentType);
_data.writeString(content);
_data.writeInt(expiry);
mRemote.transact(Stub.TRANSACTION_sendMsgWithOpts, _data, _reply, 0);
_reply.readException();
_result = _reply.readString();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public java.lang.String updateUFMI(java.lang.String ufmi) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(ufmi);
mRemote.transact(Stub.TRANSACTION_updateUFMI, _data, _reply, 0);
_reply.readException();
_result = _reply.readString();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
}
static final int TRANSACTION_preCheck = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_getSubscriptions = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_isConnected = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
static final int TRANSACTION_unsubscribe = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
static final int TRANSACTION_subscribe = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
static final int TRANSACTION_ack = (android.os.IBinder.FIRST_CALL_TRANSACTION + 5);
static final int TRANSACTION_existPMAByUFMI = (android.os.IBinder.FIRST_CALL_TRANSACTION + 6);
static final int TRANSACTION_existPMAByUserID = (android.os.IBinder.FIRST_CALL_TRANSACTION + 7);
static final int TRANSACTION_sendMsg = (android.os.IBinder.FIRST_CALL_TRANSACTION + 8);
static final int TRANSACTION_sendMsgWithOpts = (android.os.IBinder.FIRST_CALL_TRANSACTION + 9);
static final int TRANSACTION_updateUFMI = (android.os.IBinder.FIRST_CALL_TRANSACTION + 10);
}
public java.lang.String preCheck(java.lang.String sender, java.lang.String topic) throws android.os.RemoteException;
public java.lang.String getSubscriptions() throws android.os.RemoteException;
public boolean isConnected() throws android.os.RemoteException;
public java.lang.String unsubscribe(java.lang.String topic) throws android.os.RemoteException;
public java.lang.String subscribe(java.lang.String topic, int qos) throws android.os.RemoteException;
public java.lang.String ack(java.lang.String msgID, java.lang.String tokenID) throws android.os.RemoteException;
public java.lang.String existPMAByUFMI(java.lang.String ufmi) throws android.os.RemoteException;
public java.lang.String existPMAByUserID(java.lang.String userID) throws android.os.RemoteException;
public java.lang.String sendMsg(java.lang.String sender, java.lang.String receiver, java.lang.String contentType, java.lang.String content) throws android.os.RemoteException;
public java.lang.String sendMsgWithOpts(java.lang.String sender, java.lang.String receiver, int qos, java.lang.String contentType, java.lang.String content, int expiry) throws android.os.RemoteException;
public java.lang.String updateUFMI(java.lang.String ufmi) throws android.os.RemoteException;
}
