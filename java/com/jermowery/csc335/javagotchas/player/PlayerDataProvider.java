package com.jermowery.csc335.javagotchas.player;

import com.jermowery.csc335.javagotchas.proto.nano.PlayerProto.PlayerStats;

import java.io.InputStream;

/**
 * @author jermowery@email.arizona.edu (Jeremy Mowery)
 *
 */
public abstract class PlayerDataProvider {
    private InputStream ios;

    public PlayerDataProvider(InputStream ios) {
        this.ios = ios;
    }

    public InputStream getStream() {
        return this.ios;
    }

    public abstract PlayerStats getPlayer();

}
