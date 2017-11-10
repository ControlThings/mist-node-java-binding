package mistNode.request;

import org.bson.BsonArray;
import org.bson.BsonDocument;

import wishApp.Peer;

/**
 * Created by jeppe on 11/30/16.
 */

public class Control {

    public static int model(Peer peer, ModelCb callback){
        return ControlModel.request(peer, callback);
    };

    public static int read(Peer peer, String epid, Control.ReadCb callback) {
       return ControlRead.request(peer, epid, callback);
    }

    public static int write(Peer peer, String epid, Boolean state, Control.WriteCb callback) {
        return ControlWrite.request(peer, epid, state, null, null, null, callback);
    }
    public static int write(Peer peer, String epid, int state, Control.WriteCb callback) {
        return ControlWrite.request(peer, epid, null,  state, null, null, callback);
    }
    public static int write(Peer peer, String epid, float state, Control.WriteCb callback) {
        return ControlWrite.request(peer, epid, null, null, state, null, callback);
    }
    public static int write(Peer peer, String epid, String state, Control.WriteCb callback) {
        return ControlWrite.request(peer, epid, null, null, null, state, callback);
    }

    public static int invoke(Peer peer, String epid, InvokeCb callback) {
        return ControlInvoke.request(peer, epid, null, null, null, null, null, null, null, callback);
    }
    public static int invoke(Peer peer, String epid, String value, InvokeCb callback) {
        return ControlInvoke.request(peer, epid,  null, null, null, value, null, null, null,callback);
    }
    public static int invoke(Peer peer, String epid, Boolean value, InvokeCb callback) {
        return ControlInvoke.request(peer, epid, value, null, null, null, null, null, null, callback);
    }
    public static int invoke(Peer peer, String epid, int value, InvokeCb callback) {
        return ControlInvoke.request(peer, epid,null, value, null, null, null, null, null,callback);
    }
    public static int invoke(Peer peer, String epid, float value, InvokeCb callback) {
        return ControlInvoke.request(peer, epid,null, null, value, null, null, null, null,callback);
    }
    public static int invoke(Peer peer, String epid, byte[] value, InvokeCb callback) {
        return ControlInvoke.request(peer, epid, null, null, null, null, value, null, null,callback);
    }
    public static int invoke(Peer peer, String epid, BsonDocument value, InvokeCb callback) {
        return ControlInvoke.request(peer, epid,  null, null, null, null, null, value, null,callback);
    }
    public static int invoke(Peer peer, String epid, BsonArray value, InvokeCb callback) {
        return ControlInvoke.request(peer, epid,null, null, null, null, null, null, value, callback);
    }

    public static int follow(Peer peer, FollowCb callback) {
        return ControlFollow.request(peer, callback);
    }

    public static int requestMapping(Peer dst, Peer src, String srcEndpoint, String dstEndpoint, RequestMappingCB callback) {
        return ControlRequestMapping.request(dst, src, srcEndpoint, dstEndpoint, callback);
    }

    public abstract static class ModelCb extends Callback {
        public abstract void cb(byte[] BsonData);
    }

    public abstract static class ReadCb extends Callback {
        public void cbBool(Boolean data) {};
        public void cbInt(int data) {};
        public void cbFloat(double data) {};
        public void cbString(String data) {};
    }

    public abstract static class WriteCb extends Callback {
        public abstract void cb();
    }

    public abstract static class InvokeCb extends Callback {
        public void cbBool(boolean value) {};
        public void cbInt(int value) {};
        public void cbFloat(float value) {};
        public void cbString(String value) {};
        public void cbByte(byte[] value) {};
        public void cbDocument(BsonDocument value) {};
        public void cbArray(BsonArray value) {};
    }

    public abstract static class FollowCb extends Callback {
        public void cbBool(String epid, boolean value) {};
        public void cbInt(String epid, int value) {};
        public void cbFloat(String epid, double value) {};
        public void cbString(String epid, String value) {};
    }

    public abstract static class RequestMappingCB extends Callback {
        public void cb() {};
    }
}
