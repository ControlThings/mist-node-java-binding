/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: /media/jeppe/Samsung/Work/Android/Aar/mist-node-android/MistNodeApi/src/main/aidl/fi/ct/bridge/AppBridge.aidl
 */
package fi.ct.bridge;
// Declare any non-default types here with import statements

public interface AppBridge extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements fi.ct.bridge.AppBridge
{
private static final java.lang.String DESCRIPTOR = "fi.ct.bridge.AppBridge";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an fi.ct.bridge.AppBridge interface,
 * generating a proxy if needed.
 */
public static fi.ct.bridge.AppBridge asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof fi.ct.bridge.AppBridge))) {
return ((fi.ct.bridge.AppBridge)iin);
}
return new fi.ct.bridge.AppBridge.Stub.Proxy(obj);
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
case TRANSACTION_sendCoreToApp:
{
data.enforceInterface(DESCRIPTOR);
byte[] _arg0;
_arg0 = data.createByteArray();
this.sendCoreToApp(_arg0);
reply.writeNoException();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements fi.ct.bridge.AppBridge
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
@Override public void sendCoreToApp(byte[] data) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeByteArray(data);
mRemote.transact(Stub.TRANSACTION_sendCoreToApp, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_sendCoreToApp = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
}
public void sendCoreToApp(byte[] data) throws android.os.RemoteException;
}
