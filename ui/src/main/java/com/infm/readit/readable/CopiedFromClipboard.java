package com.infm.readit.readable;

import android.content.Context;
import android.preference.PreferenceManager;

import com.infm.readit.Constants;

/**
 * Created by infm on 6/12/14. Enjoy ;)
 */
public class CopiedFromClipboard extends Readable {

    public CopiedFromClipboard() {
        setTextType("text/plain");
    }

    @Override
    public String getLink() {
        return null;
    }

    @Override
    public void setLink(String link) {

    }

    @Override
    public ChunkData getChunkData() {
        return null;
    }

    @Override
    public void setChunkData(ChunkData data) {

    }
}
