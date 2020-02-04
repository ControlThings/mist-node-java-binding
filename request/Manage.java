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

import wish.Peer;

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
