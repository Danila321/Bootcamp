package com.mygdx.game.utility;

import com.badlogic.gdx.utils.TimeUtils;
import com.mygdx.game.Managers.MemoryManager;

import java.util.ArrayList;

public class GameSession {
    public GameState state;

    int balance;

    long nextEnemySpawnTime;
    int countReleasedEnemies;
    boolean levelFlag = true;
    private int level;
    long startRestTime;

    public GameSession() {

    }

    public void startGame() {
        state = GameState.PLAYING;
        balance = 1000;
        countReleasedEnemies = 0;
        level = 0;
        startRestTime = 0;
    }

    public void pauseGame() {
        state = GameState.PAUSED;
    }

    public void resumeGame() {
        state = GameState.PLAYING;
    }

    public void endGame() {
        state = GameState.ENDED;
        ArrayList<Integer> recordsTable = MemoryManager.loadRecordsTable();
        if (recordsTable == null) {
            recordsTable = new ArrayList<>();
        }
        int foundIdx = 0;
        for (; foundIdx < recordsTable.size(); foundIdx++) {
            if (recordsTable.get(foundIdx) < getLevel()) break;
        }
        recordsTable.add(foundIdx, getLevel());
        MemoryManager.saveTableOfRecords(recordsTable);
    }

    public boolean shouldSpawnEnemy() {
        if (countReleasedEnemies < GameSettings.ENEMY_COUNT) {
            if (nextEnemySpawnTime <= TimeUtils.millis()) {
                nextEnemySpawnTime = TimeUtils.millis() + GameSettings.ENEMY_SPAWN_TIME;
                countReleasedEnemies++;
                return true;
            }
        } else {
            startRestTime = TimeUtils.millis();
        }
        return false;
    }

    public boolean isRest() {
        if (startRestTime + GameSettings.WAVE_REST_TIME > TimeUtils.millis()) {
            levelFlag = true;
            return true;
        } else {
            if (levelFlag) {
                countReleasedEnemies = 0;
                levelUp();
                levelFlag = false;
            }
            return false;
        }
    }

    public int getBalance(){
        return balance;
    }

    public void addBalance(int balance){
        this.balance += balance;
    }

    public void reduceBalance(int balance){
        this.balance -= balance;
    }

    public void levelUp() {
        level++;
        if (GameSettings.ENEMY_SPAWN_TIME > 600) {
            GameSettings.ENEMY_SPAWN_TIME -= 200;
        }
        GameSettings.ENEMY_SPEED += 0.3f;
        GameSettings.ENEMY_COUNT++;
    }

    public int getLevel() {
        return level;
    }
}
