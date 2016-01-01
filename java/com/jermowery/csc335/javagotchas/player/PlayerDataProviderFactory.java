package com.jermowery.csc335.javagotchas.player;

import java.io.InputStream;

/**
 * Created by Jeremy on 12/31/2015.
 */
public class PlayerDataProviderFactory {
    public static PlayerDataProvider getPlayerDataProvider(InputStream ios) {
        return new FromProtoPlayerDataProvider(ios);
    }
}
