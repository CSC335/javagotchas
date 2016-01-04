package com.jermowery.csc335.javagotchas.data;

import com.jermowery.csc335.javagotchas.proto.nano.DataProto.Data;

import java.io.InputStream;

/**
 * An abstract class for obtaining game data from an {@link java.io.InputStream InputStream}
 * @author jermowery@email.arizona.edu (Jeremy Mowery)
 */
public abstract class DataProvider {
    private InputStream ios;

    /**
     * Constructs this DataProvider using ios
     *
     * @param ios The {@link java.io.InputStream InputStream} to use.
     */
    public DataProvider(InputStream ios) {
        this.ios = ios;
    }

    /**
     * Get the {@link java.io.InputStream InputStream} used.
     *
     * @return the {@link java.io.InputStream InputStream} used.
     */
    public InputStream getStream() {
        return this.ios;
    }

    /**
     * Gets the {@link com.jermowery.csc335.javagotchas.proto.nano.DataProto.Data Data} object from the stream.
     * Note that is may be a blocking call.
     *
     * @return The {@link com.jermowery.csc335.javagotchas.proto.nano.DataProto.Data Data} object.
     */
    public abstract Data getData();
}
