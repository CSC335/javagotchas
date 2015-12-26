package com.jermowery.csc335.javagotchas.data;

import java.io.InputStream;

/**
 * Created by jeremy on 12/18/15.
 */
public class DataProviderFactory {

    public static DataProvider getDefaultDataProvider(InputStream ios) {
        return new FromProtoDataProvider(ios);
    }
}
