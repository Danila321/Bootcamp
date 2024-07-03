package com.mygdx.game;

public class Money {
    private int count;

    public Money(int startCount) {
        this.count = startCount;
    }

    void addBalance(int count) {
        this.count += count;
    }

    void reduceBalance(int count) {
        this.count -= count;
    }

    int getBalance() {
        return count;
    }
}