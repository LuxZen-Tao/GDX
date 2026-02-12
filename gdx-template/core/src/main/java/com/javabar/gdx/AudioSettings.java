package com.javabar.gdx;

import com.badlogic.gdx.math.MathUtils;

public class AudioSettings {
    private static final String[] MENU_PROFILES = {"Jazz_Lounge", "Indie_Alt", "Pop_Party"};

    private float musicVolume = 0.7f;
    private float chatterVolume = 0f;
    private float chatterTarget = 0f;
    private String profile = MENU_PROFILES[0];

    public void resetForMenu() {
        profile = MENU_PROFILES[0];
        musicVolume = 0.7f;
        chatterVolume = 0f;
        chatterTarget = 0f;
    }

    public void onOpenService() {
        profile = MENU_PROFILES[MathUtils.random(MENU_PROFILES.length - 1)];
        chatterTarget = 0.6f;
    }

    public void onCloseService() {
        chatterTarget = 0f;
    }

    public void update(float delta) {
        float speed = 0.6f * delta;
        chatterVolume += Math.signum(chatterTarget - chatterVolume) * speed;
        if (Math.abs(chatterTarget - chatterVolume) < speed) chatterVolume = chatterTarget;
    }

    public float musicVolume() { return musicVolume; }
    public float chatterVolume() { return chatterVolume; }
    public String profile() { return profile; }
}
