package mistNode.request;

import mistNode.Peer;

/**
 * Created by jeppe on 11/30/16.
 */

public class Manage {

    public static int claim(Peer peer, ClaimCb callback) {
       return ManageClaim.request(peer, callback);
    }

    public abstract static class ClaimCb extends Callback {
        public abstract void cb();
    }
}
