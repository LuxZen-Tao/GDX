package com.javabar.gdx;

import com.badlogic.gdx.ScreenAdapter;

public class BootSplashScreen extends TimedImageScreen {
    public BootSplashScreen(JavaBarGdxGame game) {
        super(game, "libgdx.png", 3f, () -> game.setScreen(new LogoSplashScreen(game)));
    }
}
