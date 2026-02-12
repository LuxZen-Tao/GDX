package com.javabar.gdx;

public final class PresentationSnapshot {
    private final double money;
    private final double debt;
    private final int reputation;
    private final double chaos;
    private final boolean serviceOpen;
    private final int week;
    private final int day;
    private final int round;
    private final int traffic;
    private final int unservedLastTick;
    private final int refundsLastTick;
    private final int fightsLastTick;

    public PresentationSnapshot(
            double money,
            double debt,
            int reputation,
            double chaos,
            boolean serviceOpen,
            int week,
            int day,
            int round,
            int traffic,
            int unservedLastTick,
            int refundsLastTick,
            int fightsLastTick
    ) {
        this.money = money;
        this.debt = debt;
        this.reputation = reputation;
        this.chaos = chaos;
        this.serviceOpen = serviceOpen;
        this.week = week;
        this.day = day;
        this.round = round;
        this.traffic = traffic;
        this.unservedLastTick = unservedLastTick;
        this.refundsLastTick = refundsLastTick;
        this.fightsLastTick = fightsLastTick;
    }

    public double money() { return money; }

    public double debt() { return debt; }

    public int reputation() { return reputation; }

    public double chaos() { return chaos; }

    public boolean serviceOpen() { return serviceOpen; }

    public int week() { return week; }

    public int day() { return day; }

    public int round() { return round; }

    public int traffic() { return traffic; }

    public int unservedLastTick() { return unservedLastTick; }

    public int refundsLastTick() { return refundsLastTick; }

    public int fightsLastTick() { return fightsLastTick; }
}
