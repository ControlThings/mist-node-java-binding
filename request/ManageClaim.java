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
