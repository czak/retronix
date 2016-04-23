package pl.czak.retronix.android;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import pl.czak.retronix.engine.Sound;

/**
 * Created by czak on 23/04/16.
 */
public class SoundBank {
    private AssetManager assets;
    private SoundPool soundPool;

    private Map<Sound, Integer> soundIds = new HashMap<>();

    public SoundBank(Context context) {
        assets = context.getAssets();
        soundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 0);

        // Load the sound files
        for (Sound sound : Sound.values()) {
            try {
                AssetFileDescriptor afd = assets.openFd(sound.getFilename());
                soundIds.put(sound, soundPool.load(afd, 1));
            } catch (IOException e) {
                System.out.println("Unable to load sound: " + sound);
            }

        }
    }

    public void play(Sound sound) {
        soundPool.play(soundIds.get(sound), 1, 1, 0, 0, 1);
    }


    public void release() {
        soundPool.release();
    }
}
