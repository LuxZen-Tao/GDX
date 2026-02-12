package com.javabar.gdx;

public class LogoSplashScreen extends TimedImageScreen {
    public LogoSplashScreen(JavaBarGdxGame game) {
        super(game, "libgdx.png", 2.5f, () -> game.setScreen(new CoverScreen(game)));
    }
}
