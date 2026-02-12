package com.javabar.gdx.swingcompat;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.javabar.gdx.JavaBarGdxGame;

public class Lwjgl3Launcher {
    public static void main(String[] args) {
        new Lwjgl3Application(new JavaBarGdxGame(), config());
    }

    private static Lwjgl3ApplicationConfiguration config() {
        Lwjgl3ApplicationConfiguration cfg = new Lwjgl3ApplicationConfiguration();
        cfg.setTitle("Barva");
        cfg.setWindowedMode(1280, 720);
        cfg.useVsync(true);
        return cfg;
    }
}
