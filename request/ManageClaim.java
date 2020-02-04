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

import org.bson.BsonBinaryWriter;
import org.bson.BsonWriter;
import org.bson.io.BasicOutputBuffer;

import mist.node.MistNode;
import wish.Peer;

/**
 * Created by jeppe on 10/4/16.
 */
class ManageClaim {

    static int request(Peer peer, Manage.ClaimCb callback) {
        final String op = "manage.claim";

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
            private Manage.ClaimCb cb;

            @Override
            public void response(byte[] data) {
                cb.cb();
            }

            @Override
            public void end() {
                cb.end();
            }

            @Override
            public void err(int code, String msg) {
                cb.err(code, msg);
            }

            private MistNode.RequestCb init(Manage.ClaimCb callback) {
                this.cb = callback;
                return this;
            }

        }.init(callback));

    }
}
