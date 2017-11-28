package mist.node;

import android.content.Context;

import org.bson.BSONException;
import org.bson.BsonDocument;
import org.bson.RawBsonDocument;

import java.util.ArrayList;
import java.util.List;

import addon.AddonException;
import addon.WishFile;

import mist.node.Endpoint.*;
import wish.Peer;


/**
 * Created by jan on 11/1/16.
 */

public class MistNode {
    /** startMistApp return value for success */
    private static final int MIST_NODE_SUCCESS = 0;
    /** startMistApp return error return if started multiple times */
    private static final int MIST_NODE_ERROR_MULTIPLE_TIMES = -1;
    /** startMistApp return error return for other errors */
    private static final int MIST_NODE_ERROR_UNSPECIFIED = -10;

    private static List<Error> nodeErrorHandleList = new ArrayList<>();

    static {
        System.loadLibrary("mist");
    }

    /* Private constructor must exist to enforce Singleton pattern */
    private MistNode() {}

    private static class MistNodeHolder {
        private static final MistNode INSTANCE = new MistNode();
    }

    public static MistNode getInstance() {
        return MistNodeHolder.INSTANCE;
    }

    public void startMistApp(Context context) {
        String appName = context.getPackageName();
        if (appName.length() > 32) {
            appName = appName.substring(0, 32);
        }
        int ret = startMistApp(appName, new WishFile(context));
        if (ret != MIST_NODE_SUCCESS) {
            if (ret == MIST_NODE_ERROR_MULTIPLE_TIMES) {
                throw new AddonException("MistNode cannot be started multiple times.");
            }
            else {
                throw new AddonException("Unspecified MistNode error.");
            }
        }
    }

    /**
        @return MIST_API_SUCCESS, for a successful start, or MIST_API_ERROR_MULTIPLE_TIMES for error
    */
    native int startMistApp(String appName, WishFile wishFile);
    native void stopMistApp();

    public native void addEndpoint(Endpoint ep);
    public native void removeEndpoint(Endpoint ep);

    public native void readResponse(String fullPath, int requestId, byte[] bson);
    public native void readError(String fullPath, int requestId, int code, String msg);

    public native void writeResponse(String fullPath, int requestId);
    public native void writeError(String fullPath, int requestId, int code, String msg);

    public native void invokeResponse(String fullPath, int requestId, byte[] bson);
    public native void invokeError(String fullPath, int requestId, int code, String msg);

    public native void changed(String fullPath);

    /**
     * Send a Mist request to remote peer.
     *
     * @param peer a BSON representation of the protocol peer
     * @param req a BSON representation of the RPC request, {op, args}
     * @return the RPC id; The invalid RPC id 0 is returned for any errors.
     */
    public native int request(byte[] peer, byte[] req, RequestCb cb); //will call mist_app_request

    public native void requestCancel(int id);


    /**
     * Read function called by JNI code. This function is called when handling control.read, or whenever an update of the Endpoint's value is
     * to be read, when satisfying a control.follow request perhaps after the Endpoint's value has been declared to have changed via mistNode.MistNode.changed().
     *
     * @param ep Endpoint object to be read
     * @param peerBson the peer in BSON format in case of control.read, or null if this is an internal read.
     * @param requestId the request id
     */
    protected void read(Endpoint ep, byte[] peerBson, final int requestId) {
        Peer peer = Peer.fromBson(peerBson);

        /* Check data type of endpoint */
        if (ep.getReadCb() instanceof ReadableInt) {
            ((ReadableInt) ep.getReadCb()).read(peer, new ReadableIntResponse(ep.getEpid(), requestId));
        } else if (ep.getReadCb() instanceof  ReadableBool) {
            ((ReadableBool) ep.getReadCb()).read(peer, new ReadableBoolResponse(ep.getEpid(), requestId));
        } else if (ep.getReadCb() instanceof  ReadableString) {
            ((ReadableString) ep.getReadCb()).read(peer, new ReadableStringResponse(ep.getEpid(), requestId));
        } else if (ep.getReadCb() instanceof ReadableFloat) {
            ((ReadableFloat) ep.getReadCb()).read(peer, new ReadableFloatResponse(ep.getEpid(), requestId));
        } else {
            if (ep.getReadCb() == null) { readError(ep.getEpid(), requestId, 346, "No callback function registered"); }
            else { readError(ep.getEpid(), requestId, 346, "Not supported callback function"); }
            return;
        }

    }

    protected void write(Endpoint ep, byte[] peerBson, int requestId, byte[] args) {
        Peer peer = Peer.fromBson(peerBson);

        if (ep.getWriteCb() instanceof WritableBool) {

            boolean value;
            try {
                BsonDocument bson = new RawBsonDocument(args);
                value = bson.get("args").asBoolean().getValue();
            } catch (BSONException e) {
                writeError(ep.getEpid(), requestId, 452, "Bad BSON structure");
                return;
            }

            ((WritableBool) ep.getWriteCb()).write(value, peer, new WriteResponse(ep.getEpid(), requestId));

        } else if (ep.getWriteCb() instanceof WritableInt) {

            int value;
            try {
                BsonDocument bson = new RawBsonDocument(args);
                value = bson.get("args").asInt32().getValue();
            } catch (BSONException e) {
                writeError(ep.getEpid(), requestId, 452, "Bad BSON structure");
                return;
            }

            ((WritableInt) ep.getWriteCb()).write(value, peer, new WriteResponse(ep.getEpid(), requestId));

        } else if (ep.getWriteCb() instanceof WritableFloat) {

            float value;
            try {
                BsonDocument bson = new RawBsonDocument(args);
                value = (float) bson.get("args").asDouble().getValue();
            } catch (BSONException e) {
                writeError(ep.getEpid(), requestId, 452, "Bad BSON structure");
                return;
            }

            ((WritableFloat) ep.getWriteCb()).write(value, peer, new WriteResponse(ep.getEpid(), requestId));

        } else if (ep.getWriteCb() instanceof WritableString) {

            String value;
            try {
                BsonDocument bson = new RawBsonDocument(args);
                value = bson.get("args").asString().getValue();
            } catch (BSONException e) {
                writeError(ep.getEpid(), requestId, 452, "Bad BSON structure");
                return;
            }

            ((WritableString) ep.getWriteCb()).write(value, peer, new WriteResponse(ep.getEpid(), requestId));

        } else {
            if (ep.getWriteCb() == null) { writeError(ep.getEpid(), requestId, 346, "No callback function registered"); }
            else { writeError(ep.getEpid(), requestId, 346, "Not supported callback function"); }
            return;
        }
    }

    protected void invoke(Endpoint ep, byte[] peerBson, int requestId, byte[] args) {

        Peer peer = Peer.fromBson(peerBson);

        if (peer == null) {
            writeError(ep.getEpid(), requestId, 347, "Invalid peer");
            return;
        }

        if (ep.getInvokeCb() == null) {
            writeError(ep.getEpid(), requestId, 346, "No callback function registered");
            return;
        }

        ep.getInvokeCb().invoke(args, peer, new InvokeResponse(ep.getEpid(), requestId));
    }

    void online(byte[] peerBson) {

    }

    void offline(byte[] peerBson) {

    }
    
    static void registerNodeRpcErrorHandler(Error error) {
        synchronized (nodeErrorHandleList) {
            nodeErrorHandleList.add(error);
        }
    }

    interface Error {
        public void cb(int code, String msg);
    }

    public abstract static class RequestCb {

        /**
         * The callback invoked when "ack" is received for a RPC request
         *
         * @param data a document containing RPC return value as 'data' element
         */
        public void ack(byte[] data) {
            response(data);
            end();
        };

        /**
         * The callback invoked when "sig" is received for a RPC request
         *
         * @param data the contents of 'data' element of the RPC reply
         */
        public void sig(byte[] data) {
            response(data);
        };

        public abstract void response(byte[] data);
        public abstract void end();

        /**
         * The callback invoked when "err" is received for a failed RPC request
         *
         * @param code the error code
         * @param msg  a free-text error message
         */
        public void err(int code, String msg) {
            synchronized (nodeErrorHandleList) {
                for (Error error : nodeErrorHandleList) {
                    error.cb(code, msg);
                }
            }
        };
    }
}
