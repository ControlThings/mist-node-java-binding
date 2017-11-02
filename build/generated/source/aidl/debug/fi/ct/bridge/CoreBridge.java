/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: /media/jeppe/Samsung/Work/Android/Aar/mist-node-android/MistNodeApi/src/main/aidl/fi/ct/bridge/CoreBridge.aidl
 */
package fi.ct.bridge;
// Declare any non-default types here with import statements

public interface CoreBridge extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements fi.ct.bridge.CoreBridge
{
private static final java.lang.String DESCRIPTOR = "fi.ct.bridge.CoreBridge";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an fi.ct.bridge.CoreBridge interface,
 * generating a proxy if needed.
 */
public static fi.ct.bridge.CoreBridge asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof fi.ct.bridge.CoreBridge))) {
return ((fi.ct.bridge.CoreBridge)iin);
}
return new fi.ct.bridge.CoreBridge.Stub.Proxy(obj);
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
case TRANSACTION_sendAppToCore:
{
data.enforceInterface(DESCRIPTOR);
byte[] _arg0;
_arg0 = data.createByteArray();
byte[] _arg1;
_arg1 = data.createByteArray();
this.sendAppToCore(_arg0, _arg1);
reply.writeNoException();
return true;
}
case TRANSACTION_register:
{
data.enforceInterface(DESCRIPTOR);
android.os.IBinder _arg0;
_arg0 = data.readStrongBinder();
byte[] _arg1;
_arg1 = data.createByteArray();
fi.ct.bridge.AppBridge _arg2;
_arg2 = fi.ct.bridge.AppBridge.Stub.asInterface(data.readStrongBinder());
this.register(_arg0, _arg1, _arg2);
reply.writeNoException();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements fi.ct.bridge.CoreBridge
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
@Override public void sendAppToCore(byte[] wsid, byte[] data) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeByteArray(wsid);
_data.writeByteArray(data);
mRemote.transact(Stub.TRANSACTION_sendAppToCore, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void register(android.os.IBinder clientDeathListener, byte[] wsid, fi.ct.bridge.AppBridge service) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeStrongBinder(clientDeathListener);
_data.writeByteArray(wsid);
_data.writeStrongBinder((((service!=null))?(service.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_register, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_sendAppToCore = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_register = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
}
public void sendAppToCore(byte[] wsid, byte[] data) throws android.os.RemoteException;
public void register(android.os.IBinder clientDeathListener, byte[] wsid, fi.ct.bridge.AppBridge service) throws android.os.RemoteException;
}
