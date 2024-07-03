package com.mygdx.game;

public class Money {
    private int count;

    public Money(int startCount) {
        this.count = startCount;
    }

    public void addBalance(int count) {
        this.count += count;
    }

    public void reduceBalance(int count) {
        this.count -= count;
    }

    public int getBalance() {
        return count;
    }
}