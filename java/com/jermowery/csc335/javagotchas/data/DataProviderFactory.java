package com.jermowery.csc335.javagotchas.data;

import java.io.InputStream;

/**
 * @author jermowery@email.arizona.edu (Jeremy Mowery)
 */
public class DataProviderFactory {

    public static DataProvider getDataProvider(InputStream ios) {
        return new FromProtoDataProvider(ios);
    }
}
