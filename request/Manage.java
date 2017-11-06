package mistNode.request;

import wishApp.Peer;

/**
 * Created by jeppe on 11/30/16.
 */

public class Manage {

    public static void claim(Peer peer, ClaimCb callback) {
        ManageClaim.request(peer, callback);
    }

    public interface ClaimCb extends Callback {
        public void cb();
    }
}
