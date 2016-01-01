package com.jermowery.csc335.javagotchas.player;

import com.jermowery.csc335.javagotchas.proto.nano.PlayerProto.PlayerStats;

import java.io.InputStream;

/**
 * Created by Jeremy on 12/31/2015.
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
