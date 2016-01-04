package com.jermowery.csc335.javagotchas.player;

import java.io.InputStream;

/**
 * @author jermowery@email.arizona.edu (Jeremy Mowery)
 *
 */
public class PlayerDataProviderFactory {
    public static PlayerDataProvider getPlayerDataProvider(InputStream ios) {
        return new FromProtoPlayerDataProvider(ios);
    }
}
