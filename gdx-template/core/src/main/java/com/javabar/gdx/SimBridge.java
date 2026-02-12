package com.javabar.gdx;

public class SimBridge {
    private double money = 2500;
    private double debt = 0;
    private int reputation = 50;
    private double chaos = 5;
    private boolean serviceOpen;
    private int week = 1;
    private int day = 1;
    private int round;
    private int traffic = 10;

    private int unservedLastTick;
    private int refundsLastTick;
    private int fightsLastTick;

    public SimBridge() {
        newGame();
    }

    public void newGame() {
        money = 2500;
        debt = 0;
        reputation = 50;
        chaos = 5;
        serviceOpen = false;
        week = 1;
        day = 1;
        round = 0;
        traffic = 10;
        unservedLastTick = 0;
        refundsLastTick = 0;
        fightsLastTick = 0;
    }

    public void openService() {
        serviceOpen = true;
    }

    public void closeService() {
        serviceOpen = false;
    }

    public void advanceTick() {
        if (!serviceOpen) {
            openService();
        }
        round++;

        unservedLastTick = round % 3;
        refundsLastTick = round % 2;
        fightsLastTick = round % 4 == 0 ? 1 : 0;

        traffic = Math.max(0, 8 + reputation / 10 - (int) chaos + (round % 5));
        money += traffic * 12 - refundsLastTick * 8 - fightsLastTick * 15;
        chaos = Math.max(0, chaos + (fightsLastTick > 0 ? 0.8 : -0.2));
        reputation = Math.max(0, Math.min(100, reputation + 1 - fightsLastTick));

        if (round % 7 == 0) {
            day++;
            if (day > 7) {
                day = 1;
                week++;
            }
        }
    }

    public PresentationSnapshot snapshot() {
        return new PresentationSnapshot(
                money,
                debt,
                reputation,
                chaos,
                serviceOpen,
                week,
                day,
                round,
                traffic,
                unservedLastTick,
                refundsLastTick,
                fightsLastTick
        );
    }
}
