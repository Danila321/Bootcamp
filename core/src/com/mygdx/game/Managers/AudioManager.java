package com.mygdx.game.Managers;

import static com.mygdx.game.screens.SettingsScreen.getMusic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.mygdx.game.utility.GameResources;

public class AudioManager {
    public static Music backgroundMusic;
    public Sound shootSound,towerCreateSound;
    public Sound explosionSound;
    public Sound deploySound;
    public static int sound;
    public boolean isMusicOn;

    public static void updateSoundFlag(int count) {
        MemoryManager.saveSoundSettings(count);
    }
    public static void updateMusicFlag(int count) {
        MemoryManager.saveMusicSettings(count);
    }


    public AudioManager() {

        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal(GameResources.BACKGROUND_MUSIC_PATH));
        shootSound = Gdx.audio.newSound(Gdx.files.internal(GameResources.SHOOT_SOUND_PATH));
        towerCreateSound = Gdx.audio.newSound(Gdx.files.internal(GameResources.TOWER_CREATE));

        AudioManager.backgroundMusic.setVolume(0f);
        backgroundMusic.setLooping(true);
        backgroundMusic.play();

    }
}
