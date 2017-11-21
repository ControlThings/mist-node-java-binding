package mist.node.request;

import org.bson.BsonBinary;
import org.bson.BsonBinaryWriter;
import org.bson.BsonWriter;
import org.bson.io.BasicOutputBuffer;

import mist.node.MistNode;
import wish.Peer;


/**
 * Created by jeppe on 16/12/2016.
 */

public class ControlRequestMapping {

    static int request(Peer dstPeer, Peer srcPeer, String srcEndpoint, String dstEndpoint, Control.RequestMappingCB callback) {
        final String op = "control.requestMapping";

        BasicOutputBuffer buffer = new BasicOutputBuffer();
        BsonWriter writer = new BsonBinaryWriter(buffer);
        writer.writeStartDocument();

        writer.writeString("op", op);

        writer.writeStartArray("args");

        // which peer the signal will come from
        writer.writeStartDocument();
        writer.writeBinaryData("luid", new BsonBinary(dstPeer.getRuid()));
        writer.writeBinaryData("ruid", new BsonBinary(srcPeer.getRuid()));
        writer.writeBinaryData("rhid", new BsonBinary(srcPeer.getRhid()));
        writer.writeBinaryData("rsid", new BsonBinary(srcPeer.getRsid()));
        writer.writeString("protocol", srcPeer.getProtocol());
        writer.writeEndDocument();

        writer.writeString(srcEndpoint);

        writer.writeStartDocument();
        writer.writeString("type", "direct");
        writer.writeString("interval", "change");
        writer.writeEndDocument();

        writer.writeString(dstEndpoint);

        writer.writeStartDocument();
        writer.writeString("type", "write");
        writer.writeEndDocument();

        writer.writeEndArray();

        writer.writeInt32("id", 0);

        writer.writeEndDocument();
        writer.flush();

        return MistNode.getInstance().request(dstPeer.toBson(), buffer.toByteArray(), new MistNode.RequestCb() {
            Control.RequestMappingCB cb;

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

            private MistNode.RequestCb init(Control.RequestMappingCB callback) {
                this.cb = callback;
                return this;
            }

        }.init(callback));
    }
}
