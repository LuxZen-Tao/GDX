package com.javabar.gdx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.MathUtils;

public class LoadingScreen extends ScreenAdapter {
    private static final String[] LOADING = {"libgdx.png"};
    private static final String[] PHRASES = {
            "Wiping glasses, tuning the vibe.",
            "Cellar count in progress...",
            "Staff briefing: don't panic.",
            "Polishing taps for tonight's rush."
    };
    private final JavaBarGdxGame game;
    private BitmapFont font;
    private Texture current;
    private float elapsed;
    private int cycle;

    public LoadingScreen(JavaBarGdxGame game) { this.game = game; }

    @Override public void show() {
        font = new BitmapFont();
        font.getData().setScale(1.5f);
        swapImage();
    }

    private void swapImage() {
        if (current != null) current.dispose();
        current = new Texture(Gdx.files.internal(LOADING[MathUtils.random(LOADING.length - 1)]));
    }

    @Override public void render(float delta) {
        elapsed += delta;
        game.batch().begin();
        game.batch().draw(current,0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        String phrase = PHRASES[Math.min(cycle, PHRASES.length - 1)];
        font.draw(game.batch(), phrase, 90 + MathUtils.random(-20, 20), 120 + MathUtils.random(-14, 14));
        game.batch().end();
        if (elapsed >= 1.1f) {
            elapsed = 0;
            cycle++;
            if (cycle >= 2) {
                game.setScreen(new GameScreen(game));
            } else {
                swapImage();
            }
        }
    }

    @Override public void dispose() { current.dispose(); font.dispose(); }
}
