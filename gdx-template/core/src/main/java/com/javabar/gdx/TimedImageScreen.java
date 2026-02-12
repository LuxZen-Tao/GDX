package com.javabar.gdx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class TimedImageScreen extends ScreenAdapter {
    protected final JavaBarGdxGame game;
    private final String texturePath;
    private final float duration;
    private final Runnable next;
    protected String overlay;
    private float elapsed;
    private Texture texture;
    private BitmapFont font;

    public TimedImageScreen(JavaBarGdxGame game, String texturePath, float duration, Runnable next) {
        this.game = game;
        this.texturePath = texturePath;
        this.duration = duration;
        this.next = next;
    }

    @Override
    public void show() {
        texture = new Texture(Gdx.files.internal(texturePath));
        font = new BitmapFont();
        font.getData().setScale(1.4f);
        font.setColor(Color.WHITE);
    }

    @Override
    public void render(float delta) {
        elapsed += delta;
        game.batch().begin();
        game.batch().draw(texture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        if (overlay != null) {
            font.draw(game.batch(), overlay, 48, 70);
        }
        game.batch().end();
        if (elapsed >= duration) next.run();
    }

    @Override
    public void dispose() {
        texture.dispose();
        font.dispose();
    }
}
