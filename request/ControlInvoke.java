package mistNode.request;

import org.bson.BSONException;
import org.bson.BsonArray;
import org.bson.BsonBinary;
import org.bson.BsonDocument;
import org.bson.BsonDocumentReader;
import org.bson.BsonReader;
import org.bson.RawBsonDocument;
import org.bson.io.BasicOutputBuffer;

import utils.bson.BsonExtendedBinaryWriter;
import utils.bson.BsonExtendedWriter;
import wishApp.Peer;
import mistNode.MistNode;

/**
 * Created by jeppe on 26/07/16.
 */
class ControlInvoke {
    static int request(Peer peer, String epid, Boolean booleanVal, Integer intVal, Float floatVal, String stringVal, byte[] byteVal, BsonDocument documentVal, BsonArray arrayVal, final Control.InvokeCb callback) {
        final String op = "control.invoke";

        BasicOutputBuffer buffer = new BasicOutputBuffer();
        BsonExtendedWriter writer = new BsonExtendedBinaryWriter(buffer);
        writer.writeStartDocument();

        writer.writeString("op", op);

        writer.writeStartArray("args");

        writer.writeString(epid);

        if (stringVal != null) {
            writer.writeString(stringVal);
        }else if (booleanVal != null) {
            writer.writeBoolean(booleanVal);
        } else if (intVal != null) {
            writer.writeInt32(intVal);
        } else if (floatVal != null) {
            writer.writeDouble(floatVal);
        } else if (byteVal != null) {
            writer.writeBinaryData(new BsonBinary(byteVal));
        } else if (documentVal != null) {
            BsonReader bsonReader = new BsonDocumentReader(documentVal);
            writer.pipe(bsonReader);
        } else if (arrayVal != null) {
            writer.writeStartArray();
            writer.pipeArray(arrayVal);
            writer.writeEndArray();
        }

        writer.writeEndArray();

        writer.writeInt32("id", 0);

        writer.writeEndDocument();
        writer.flush();

        return MistNode.getInstance().request(peer.toBson(), buffer.toByteArray(), new MistNode.RequestCb() {
            private Control.InvokeCb cb;

            @Override
            public void response(byte[] data) {

                try {
                    BsonDocument bson = new RawBsonDocument(data);
                    if (bson.get("data").isBoolean()) {
                        cb.cbBool(bson.get("data").asBoolean().getValue());
                    } else if (bson.get("data").isInt32()) {
                        cb.cbInt(bson.get("data").asInt32().getValue());
                    } else if (bson.get("data").isDouble()) {
                        cb.cbFloat((float) bson.get("data").asDouble().getValue());
                    } else if (bson.get("data").isString()) {
                        cb.cbString(bson.get("data").asString().getValue());
                    } else if (bson.get("data").isBinary()) {
                        cb.cbByte(bson.get("data").asBinary().getData());
                    } else if (bson.get("data").isDocument()) {
                        cb.cbDocument(bson.get("data").asDocument());
                    } else if (bson.get("data").isArray()) {
                        cb.cbArray(bson.get("data").asArray());
                    } else {
                        return;
                    }
                } catch (BSONException e) {
                    cb.err(Callback.BSON_ERROR_CODE, Callback.BSON_ERROR_STRING);
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

            private MistNode.RequestCb init(Control.InvokeCb callback) {
                this.cb = callback;
                return this;
            }

        }.init(callback));
    }
}
