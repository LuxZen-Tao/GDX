package com.javabar.sim;

public record MusicProfile(
        double trafficMultiplier,
        double spendMultiplier,
        double lingerMultiplier,
        double chaosDelta,
        double reputationDriftDelta,
        double staffMoraleDelta
) {}
