package com.javabar.gdx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.javabar.sim.PresentationSnapshot;

public class GameScreen extends ScreenAdapter {
    private final JavaBarGdxGame game;
    private final Stage stage = new Stage(new ScreenViewport());
    private final Skin skin = UiSkinFactory.createBasicSkin();
    private final Label hud = new Label("", skin);
    private final Label saveMsg = new Label("", skin);
    private float saveMsgTtl;

    public GameScreen(JavaBarGdxGame game) {
        this.game = game;
        Table root = new Table();
        root.setFillParent(true);
        root.top().left().pad(12);
        root.defaults().left().pad(6);

        TextButton open = new TextButton("Open Service", skin);
        TextButton close = new TextButton("Close Service", skin);
        TextButton advance = new TextButton("Advance", skin);
        TextButton save = new TextButton("Save", skin);
        TextButton menu = new TextButton("Main Menu", skin);

        open.addListener(e -> { if (!open.isPressed()) return false; game.simBridge().openService(); game.audioSettings().onOpenService(); refresh(); return true; });
        close.addListener(e -> { if (!close.isPressed()) return false; game.simBridge().closeService(); game.audioSettings().onCloseService(); refresh(); return true; });
        advance.addListener(e -> { if (!advance.isPressed()) return false; game.simBridge().advance(); refresh(); return true; });
        save.addListener(e -> { if (!save.isPressed()) return false; saveMsg.setText(game.simBridge().saveGame()); saveMsgTtl = 2f; return true; });
        menu.addListener(e -> { if (!menu.isPressed()) return false; game.audioSettings().resetForMenu(); game.setScreen(new MainMenuScreen(game)); return true; });

        root.add(hud).row();
        Table buttons = new Table();
        buttons.defaults().pad(4);
        buttons.add(open);buttons.add(close);buttons.add(advance);buttons.add(save);buttons.add(menu);
        root.add(buttons).row();
        root.add(saveMsg).row();
        stage.addActor(root);
        refresh();
    }

    private void refresh() {
        PresentationSnapshot s = game.simBridge().snapshot();
        hud.setText(String.format("Cash £%.2f Debt £%.2f Rep %d Chaos %.1f W%d D%d R%d Service %s Traffic %d",
                s.money(), s.debt(), s.reputation(), s.chaos(), s.week(), s.day(), s.round(), s.serviceOpen() ? "OPEN" : "CLOSED", s.traffic()));
    }

    @Override public void show(){Gdx.input.setInputProcessor(stage);}    
    @Override public void render(float delta){
        Gdx.gl.glClearColor(0.07f,0.08f,0.1f,1); Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        if (saveMsgTtl > 0) { saveMsgTtl -= delta; if (saveMsgTtl <= 0) saveMsg.setText(""); }
        game.audioSettings().update(delta);
        stage.act(delta); stage.draw();
    }
    @Override public void resize(int w,int h){stage.getViewport().update(w,h,true);}    
    @Override public void dispose(){stage.dispose();skin.dispose();}
}
