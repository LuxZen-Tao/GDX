package com.javabar.gdx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class MainMenuScreen extends ScreenAdapter {
    private final JavaBarGdxGame game;
    private final Stage stage;
    private final Skin skin;
    private final Texture bg;

    public MainMenuScreen(JavaBarGdxGame game) {
        this.game = game;
        this.stage = new Stage(new ScreenViewport());
        this.skin = UiSkinFactory.createBasicSkin();
        this.bg = new Texture(Gdx.files.internal("libgdx.png"));

        game.audioSettings().resetForMenu();

        Table root = new Table();
        root.setFillParent(true);
        root.bottom().padBottom(96);

        TextButton newGame = new TextButton("New Game", skin);
        TextButton loadGame = new TextButton("Load Game", skin);
        loadGame.setDisabled(!game.simBridge().hasSave());

        newGame.addListener(e -> {
            if (!newGame.isPressed()) return false;
            if (game.simBridge().hasSave()) {
                Dialog d = new Dialog("Confirm", skin) {
                    @Override protected void result(Object object) {
                        if (Boolean.TRUE.equals(object)) {
                            game.simBridge().startNewGame();
                            game.setScreen(new LoadingScreen(game));
                        }
                    }
                };
                d.text("Overwrite existing save?");
                d.button("Yes", true);
                d.button("No", false);
                d.show(stage);
            } else {
                game.simBridge().startNewGame();
                game.setScreen(new LoadingScreen(game));
            }
            return true;
        });

        loadGame.addListener(e -> {
            if (!loadGame.isPressed() || loadGame.isDisabled()) return false;
            game.simBridge().loadGame();
            game.setScreen(new LoadingScreen(game));
            return true;
        });

        Table row = new Table();
        row.defaults().pad(10).width(240).height(55);
        row.add(newGame);
        row.add(loadGame);
        root.add(row);
        stage.addActor(root);
    }

    @Override public void show() { Gdx.input.setInputProcessor(stage); }
    @Override public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch().begin();
        game.batch().draw(bg,0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        game.batch().end();
        stage.act(delta);
        stage.draw();
    }
    @Override public void resize(int w,int h){stage.getViewport().update(w,h,true);}    
    @Override public void dispose(){stage.dispose();skin.dispose();bg.dispose();}
}
