/**
 * Copyright (C) 2020, ControlThings Oy Ab
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * @license Apache-2.0
 */
package mist.node.request;

import org.bson.BSONException;
import org.bson.BsonArray;
import org.bson.BsonBinary;
import org.bson.BsonDocument;
import org.bson.BsonDocumentReader;
import org.bson.BsonReader;
import org.bson.BsonValue;
import org.bson.RawBsonDocument;
import org.bson.io.BasicOutputBuffer;

import utils.bson.BsonExtendedBinaryWriter;
import utils.bson.BsonExtendedWriter;
import wish.Peer;
import mist.node.MistNode;

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
                BsonValue bsonValue;
                try {
                    BsonDocument bson = new RawBsonDocument(data);
                    if (bson.containsKey("data")) {
                        bsonValue = bson.get("data");
                    } else {
                        return;
                    }
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
                if (bsonValue.isBinary()) {
                    cb.cbByte(bsonValue.asBinary().getData());
                }
                if (bsonValue.isDocument()) {
                    cb.cbDocument(bsonValue.asDocument());
                }
                if (bsonValue.isArray()) {
                    cb.cbArray(bsonValue.asArray());
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
