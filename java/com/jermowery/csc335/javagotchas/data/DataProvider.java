package com.jermowery.csc335.javagotchas.data;

import com.jermowery.csc335.javagotchas.proto.nano.DataProto.Data;

import java.io.InputStream;

/**
 * Created by jeremy on 12/18/15.
 */
public abstract class DataProvider {
    private InputStream ios;

    public DataProvider(InputStream ios) {
        this.ios = ios;
    }

    public InputStream getStream() {
        return this.ios;
    }

    public abstract Data getData();
}
