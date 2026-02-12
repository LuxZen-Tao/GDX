package com.javabar.sim;

public record PendingUpgradeInstall(PubUpgrade upgrade, int nightsRemaining, int totalNights) implements java.io.Serializable {}
