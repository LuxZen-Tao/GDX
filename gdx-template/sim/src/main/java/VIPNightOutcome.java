package com.javabar.sim;

public record VIPNightOutcome(
        int unservedCount,
        int fightCount,
        int eventCount,
        int refundCount,
        double priceMultiplier,
        double foodQualitySignal
) {
}
