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

import android.util.Log;

/**
 * Created by jeppe on 3/29/17.
 */

abstract class Callback {

    public static final int BSON_ERROR_CODE = 836;
    public static final String BSON_ERROR_STRING = "Bad BSON structure";

    public void err(int code, String msg) {
        Log.d("Error", msg + " code: " + code);
    };
    public void end(){};
}
