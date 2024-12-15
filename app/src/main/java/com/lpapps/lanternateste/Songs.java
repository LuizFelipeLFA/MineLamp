package com.lpapps.lanternateste;

import android.content.Context;
import android.media.MediaPlayer;

public class Songs {

    boolean somClick = false;
    boolean somUnlick = false;

    public void Bclick() {
        somClick = true;
        if (!somUnlick) {
            MediaPlayer mediaPlayer = MediaPlayer.create(ctx, R.raw.bpress);
            mediaPlayer.start();
            somClick = false;
        }
    }

    public void Bunclick() {
        somUnlick = true;
        if (!somClick){
            MediaPlayer mediaPlayer = MediaPlayer.create(ctx, R.raw.bunpress);
            mediaPlayer.start();
            somUnlick = false;
        }
    }


    private Context ctx;
    public Songs(Context ctx){
        this.ctx = ctx;
    }

}
