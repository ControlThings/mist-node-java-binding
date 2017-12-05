package mist.node.request;

import org.bson.BSONException;
import org.bson.BsonBinaryWriter;
import org.bson.BsonDocument;
import org.bson.BsonValue;
import org.bson.BsonWriter;
import org.bson.RawBsonDocument;
import org.bson.io.BasicOutputBuffer;

import mist.node.MistNode;
import wish.Peer;

/**
 * Created by jeppe on 26/07/16.
 */
class ControlRead {
    static int request(Peer peer, String epid, final Control.ReadCb callback) {
        final String op = "control.read";

        BasicOutputBuffer buffer = new BasicOutputBuffer();
        BsonWriter writer = new BsonBinaryWriter(buffer);
        writer.writeStartDocument();

        writer.writeString("op", op);

        writer.writeStartArray("args");
        writer.writeString(epid);
        writer.writeEndArray();

        writer.writeInt32("id", 0);

        writer.writeEndDocument();
        writer.flush();

        return MistNode.getInstance().request(peer.toBson(), buffer.toByteArray(), new MistNode.RequestCb() {
            private Control.ReadCb cb;

            @Override
            public void response(byte[] data) {
                BsonValue bsonValue;
                try {
                    BsonDocument bson = new RawBsonDocument(data);
                    bsonValue = bson.get("data");
                } catch (BSONException e) {
                    cb.err(Callback.BSON_ERROR_CODE, Callback.BSON_ERROR_STRING);
                    return;
                }
                if (bsonValue.isBoolean()) {
                    cb.cbBool(bsonValue.asBoolean().getValue());
                }
                if (bsonValue.isInt32()) {
                    cb.cbInt(bsonValue.asInt32().getValue());
                }
                if (bsonValue.isDouble()) {
                    cb.cbFloat((float) bsonValue.asDouble().getValue());
                }
                if (bsonValue.isString()) {
                    cb.cbString(bsonValue.asString().getValue());
                }
            }

            @Override
            public void end() {
                cb.end();
            }

            @Override
            public void err(int code, String msg) {
                cb.err(code, msg);
            }

            private MistNode.RequestCb init(Control.ReadCb callback) {
                this.cb = callback;
                return this;
            }

        }.init(callback));
    }
}
