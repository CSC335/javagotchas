package com.jermowery.csc335.javagotchas.data;

import com.jermowery.csc335.javagotchas.proto.nano.DataProto.Data;
import com.google.common.io.ByteStreams;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by jeremy on 12/18/15.
 */
public class FromProtoDataProvider extends DataProvider {
    public FromProtoDataProvider(InputStream ios) {
        super(ios);
    }

    public Data getData() {
        try {
            return Data.parseFrom(ByteStreams.toByteArray(this.getStream()));
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return null;
        }
    }
}
