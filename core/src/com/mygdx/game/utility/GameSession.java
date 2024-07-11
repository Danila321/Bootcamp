package com.mygdx.game.utility;

import com.badlogic.gdx.utils.TimeUtils;
import com.mygdx.game.Managers.MemoryManager;

import java.util.ArrayList;

import java.sql.Time;

public class GameSession {
    public GameState state;

    int balance;

    long nextEnemySpawnTime;
    long nextEnemySpawnTime2 = TimeUtils.millis() + GameSettings.ENEMY_SPAWN_TIME2;
    long nextEnemySpawnTime3 = TimeUtils.millis() + GameSettings.ENEMY_SPAWN_TIME3;
    int countReleasedEnemies;
    int countReleasedEnemies2;
    int countReleasedEnemies3;

    boolean levelFlag = true;
    private int level;
    long startRestTime;

    public GameSession() {

    }

    public void startGame() {
        state = GameState.PLAYING;
        balance = 1000;
        countReleasedEnemies = 0;
        countReleasedEnemies2 = 0;
        countReleasedEnemies3 = 0;
        GameSettings.ENEMY_COUNT = 2;
        GameSettings.ENEMY2_COUNT = 2;
        GameSettings.ENEMY3_COUNT = 1;
        GameSettings.ENEMY_SPEED = 1.0f;
        GameSettings.ENEMY2_SPEED = 1.0f;
        GameSettings.ENEMY3_SPEED = 1.0f;
        GameSettings.ENEMY1_HEALTH = 5;
        GameSettings.ENEMY2_HEALTH = 10;
        GameSettings.ENEMY3_HEALTH = 20;
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

    public boolean shouldSpawnEnemy2() {
        if (countReleasedEnemies2 < GameSettings.ENEMY2_COUNT) {
            if (nextEnemySpawnTime2 <= TimeUtils.millis()) {
                nextEnemySpawnTime2 = TimeUtils.millis() + GameSettings.ENEMY_SPAWN_TIME2;
                countReleasedEnemies2++;
                return true;
            }
        } else {
            startRestTime = TimeUtils.millis();
        }
        return false;
    }

    public boolean shouldSpawnEnemy3() {
        if (countReleasedEnemies3 < GameSettings.ENEMY3_COUNT) {
            if (nextEnemySpawnTime3 <= TimeUtils.millis()) {
                nextEnemySpawnTime3 = TimeUtils.millis() + GameSettings.ENEMY_SPAWN_TIME3;
                countReleasedEnemies3++;
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
                countReleasedEnemies2 = 0;
                countReleasedEnemies3 = 0;
                levelUp();
                levelFlag = false;
            }
            return false;
        }
    }

    public int getBalance() {
        return balance;
    }

    public void addBalance(int balance) {
        this.balance += balance;
    }

    public void reduceBalance(int balance) {
        this.balance -= balance;
    }

    public void levelUp() {
        if (level % 10 == 0) {
            GameSettings.ENEMY3_SPEED += 0.2f;
            GameSettings.ENEMY3_HEALTH += 50;
        } else if (level % 2 == 0) {
            if (GameSettings.ENEMY_SPAWN_TIME2 > 600) {
                GameSettings.ENEMY_SPAWN_TIME2 -= 200;
            }
            GameSettings.ENEMY2_HEALTH += 2;
            if (GameSettings.ENEMY2_SPEED < 5f) {
                GameSettings.ENEMY2_SPEED += 0.2f;
            }
            GameSettings.ENEMY2_COUNT++;
        } else {
            if (GameSettings.ENEMY_SPAWN_TIME > 500) {
                GameSettings.ENEMY_SPAWN_TIME -= 250;
            }
            GameSettings.ENEMY1_HEALTH++;
            if (GameSettings.ENEMY_SPEED < 5f) {
                GameSettings.ENEMY_SPEED += 0.3f;
            }
            GameSettings.ENEMY_COUNT++;
        }
        level++;
    }

    public int getLevel() {
        return level;
    }
}
