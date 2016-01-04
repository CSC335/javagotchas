package com.jermowery.csc335.javagotchas.player;

import com.google.common.io.ByteStreams;
import com.jermowery.csc335.javagotchas.proto.nano.PlayerProto.PlayerStats;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author jermowery@email.arizona.edu (Jeremy Mowery)
 *
 */
public class FromProtoPlayerDataProvider extends PlayerDataProvider {

    public FromProtoPlayerDataProvider(InputStream ios) {
        super(ios);
    }

    @Override
    public PlayerStats getPlayer() {
        try {
            return PlayerStats.parseFrom(ByteStreams.toByteArray(this.getStream()));
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return null;
        }
    }
}
