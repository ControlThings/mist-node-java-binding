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
import org.bson.BsonBinaryWriter;
import org.bson.BsonDocument;
import org.bson.BsonDocumentReader;
import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.RawBsonDocument;
import org.bson.io.BasicOutputBuffer;

import wish.Peer;
import mist.node.MistNode;

/**
 * Created by jeppe on 25/07/16.
 */
class ControlModel {
    static int request(Peer peer, Control.ModelCb callback) {
        final String op = "control.model";

        BasicOutputBuffer buffer = new BasicOutputBuffer();
        BsonWriter writer = new BsonBinaryWriter(buffer);
        writer.writeStartDocument();

        writer.writeString("op", op);

        writer.writeStartArray("args");
        writer.writeEndArray();

        writer.writeInt32("id", 0);

        writer.writeEndDocument();
        writer.flush();

        return MistNode.getInstance().request(peer.toBson(), buffer.toByteArray(), new MistNode.RequestCb() {
            Control.ModelCb cb;

            @Override
            public void response(byte[] data) {
                BasicOutputBuffer outputBuffer;
                try {
                    BsonDocument bson = new RawBsonDocument(data);
                    BsonDocument bsonDocument = bson.get("data").asDocument();
                    BsonReader bsonReader = new BsonDocumentReader(bsonDocument);
                    outputBuffer = new BasicOutputBuffer();
                    BsonWriter bsonWriter = new BsonBinaryWriter(outputBuffer);
                    bsonWriter.pipe(bsonReader);
                } catch (BSONException e) {
                    cb.err(Callback.BSON_ERROR_CODE, Callback.BSON_ERROR_STRING);
                    return;
                }
                cb.cb(outputBuffer.toByteArray());
            }

            @Override
            public void end() {
                cb.end();
            }

            @Override
            public void err(int code, String msg) {
                cb.err(code, msg);
            }

            private MistNode.RequestCb init(Control.ModelCb callback) {
                this.cb = callback;
                return this;
            }

        }.init(callback));
    }
}
