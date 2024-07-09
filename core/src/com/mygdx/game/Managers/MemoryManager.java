package com.mygdx.game.Managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.Json;

import java.util.ArrayList;

public class MemoryManager {
    private static final Preferences preferences = Gdx.app.getPreferences("User saves");

    public static void saveSoundSettings(int voice) {
        preferences.putInteger("Sound", voice);
        preferences.flush();
    }

    public static int SoundValue() {
        return preferences.getInteger("Sound");
    }

    public static void  saveMusicSettings(int music) {
        preferences.putInteger("Music", music);
        preferences.flush();
    }

    public static int MusicValue() {
        return preferences.getInteger("Music");
    }

    public static void saveTableOfRecords(ArrayList<Integer> table) {
        Json json = new Json();
        String tableInString = json.toJson(table);
        preferences.putString("recordTable", tableInString);
        preferences.flush();
    }

    public static ArrayList<Integer> loadRecordsTable() {
        if (!preferences.contains("recordTable"))
            return null;

        String scores = preferences.getString("recordTable");
        Json json = new Json();
        ArrayList<Integer> table = json.fromJson(ArrayList.class, scores);
        return table;
    }
}