package com.javabar.gdx;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class JavaBarGdxGame extends Game {
    private final AssetManager assets = new AssetManager();
    private final SimBridge simBridge = new SimBridge();
    private final AudioSettings audioSettings = new AudioSettings();
    private SpriteBatch batch;

    @Override
    public void create() {
        batch = new SpriteBatch();
        setScreen(new BootSplashScreen(this));
    }

    public AssetManager assets() { return assets; }
    public SpriteBatch batch() { return batch; }
    public SimBridge simBridge() { return simBridge; }
    public AudioSettings audioSettings() { return audioSettings; }

    @Override
    public void dispose() {
        super.dispose();
        batch.dispose();
        assets.dispose();
    }
}
