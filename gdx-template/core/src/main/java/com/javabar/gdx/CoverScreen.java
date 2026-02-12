package com.javabar.gdx;

import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class CoverScreen extends TimedImageScreen {
    public CoverScreen(JavaBarGdxGame game) {
        super(game, "libgdx.png", 2.5f, () -> game.setScreen(new MainMenuScreen(game)));
        this.overlay = "A pub is never just a building; it's a story in taps and noise.";
    }
}
