package com.javabar.gdx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.javabar.sim.PresentationSnapshot;

/**
 * Enhanced Game Screen Example
 * 
 * This screen demonstrates:
 * - Proper UI layout with Tables
 * - Connecting UI to simulation via SimBridge and PresentationSnapshot
 * - Creating panels with backgrounds
 * - Using dialogs
 * - Organizing UI into logical sections
 * 
 * This is a reference implementation showing best practices for Unity developers
 * learning libGDX.
 */
public class EnhancedGameScreen extends ScreenAdapter {
    private final JavaBarGdxGame game;
    private final Stage stage;
    private final Skin skin;
    
    // UI Components
    private final Label cashLabel;
    private final Label debtLabel;
    private final Label reputationLabel;
    private final Label chaosLabel;
    private final Label weekLabel;
    private final Label dayLabel;
    private final Label roundLabel;
    private final Label trafficLabel;
    private final Label serviceStatusLabel;
    private final Label saveMessageLabel;
    private float saveMessageTimer = 0;
    
    public EnhancedGameScreen(JavaBarGdxGame game) {
        this.game = game;
        this.stage = new Stage(new ScreenViewport());
        this.skin = UiSkinFactory.createBasicSkin();
        
        // Main layout container
        Table root = new Table();
        root.setFillParent(true);
        root.pad(10);
        
        // Create UI sections
        Table topBar = createTopBar();
        Table leftPanel = createStatsPanel();
        Table centerPanel = createCenterPanel();
        Table rightPanel = createActionsPanel();
        Table bottomBar = createBottomBar();
        
        // Layout structure
        root.add(topBar).expandX().fillX().colspan(3).row();
        root.add(leftPanel).width(250).expandY().fillY().top().left().pad(5);
        root.add(centerPanel).expand().fill().pad(5);
        root.add(rightPanel).width(200).expandY().fillY().top().right().pad(5);
        root.row();
        root.add(bottomBar).expandX().fillX().colspan(3).height(30);
        
        stage.addActor(root);
        
        // Initialize labels
        cashLabel = findActor("cashLabel");
        debtLabel = findActor("debtLabel");
        reputationLabel = findActor("reputationLabel");
        chaosLabel = findActor("chaosLabel");
        weekLabel = findActor("weekLabel");
        dayLabel = findActor("dayLabel");
        roundLabel = findActor("roundLabel");
        trafficLabel = findActor("trafficLabel");
        serviceStatusLabel = findActor("serviceStatusLabel");
        saveMessageLabel = findActor("saveMessageLabel");
        
        // Initial data refresh
        refresh();
    }
    
    /**
     * Top bar with game title and quick stats
     */
    private Table createTopBar() {
        Table bar = new Table();
        bar.setBackground(createBackground(0.15f, 0.16f, 0.22f, 1f));
        bar.pad(10);
        
        Label titleLabel = new Label("THE BARVA PUB - Management Sim", skin);
        titleLabel.setFontScale(1.5f);
        
        bar.add(titleLabel).expandX().center();
        
        return bar;
    }
    
    /**
     * Left panel showing detailed statistics from simulation
     */
    private Table createStatsPanel() {
        Table panel = new Table();
        panel.setBackground(createBackground(0.18f, 0.20f, 0.28f, 1f));
        panel.pad(15);
        panel.defaults().left().pad(3);
        
        // Section title
        Label titleLabel = new Label("=== STATISTICS ===", skin);
        titleLabel.setFontScale(1.2f);
        panel.add(titleLabel).center().colspan(2).padBottom(10).row();
        
        // Financial stats
        panel.add(new Label("Cash:", skin)).right().padRight(10);
        Label cashLbl = new Label("£0.00", skin);
        cashLbl.setName("cashLabel");
        panel.add(cashLbl).left().row();
        
        panel.add(new Label("Debt:", skin)).right().padRight(10);
        Label debtLbl = new Label("£0.00", skin);
        debtLbl.setName("debtLabel");
        panel.add(debtLbl).left().row();
        
        panel.add(new Label("Net Worth:", skin)).right().padRight(10);
        Label netWorthLbl = new Label("£0.00", skin);
        netWorthLbl.setName("netWorthLabel");
        panel.add(netWorthLbl).left().row();
        
        // Separator
        panel.add(new Label("", skin)).colspan(2).height(10).row();
        
        // Reputation & Chaos
        panel.add(new Label("Reputation:", skin)).right().padRight(10);
        Label repLbl = new Label("0", skin);
        repLbl.setName("reputationLabel");
        panel.add(repLbl).left().row();
        
        panel.add(new Label("Chaos:", skin)).right().padRight(10);
        Label chaosLbl = new Label("0.0", skin);
        chaosLbl.setName("chaosLabel");
        panel.add(chaosLbl).left().row();
        
        // Separator
        panel.add(new Label("", skin)).colspan(2).height(10).row();
        
        // Time stats
        panel.add(new Label("Week:", skin)).right().padRight(10);
        Label weekLbl = new Label("1", skin);
        weekLbl.setName("weekLabel");
        panel.add(weekLbl).left().row();
        
        panel.add(new Label("Day:", skin)).right().padRight(10);
        Label dayLbl = new Label("1", skin);
        dayLbl.setName("dayLabel");
        panel.add(dayLbl).left().row();
        
        panel.add(new Label("Round:", skin)).right().padRight(10);
        Label roundLbl = new Label("0", skin);
        roundLbl.setName("roundLabel");
        panel.add(roundLbl).left().row();
        
        // Separator
        panel.add(new Label("", skin)).colspan(2).height(10).row();
        
        // Service stats
        panel.add(new Label("Service:", skin)).right().padRight(10);
        Label serviceLbl = new Label("CLOSED", skin);
        serviceLbl.setName("serviceStatusLabel");
        panel.add(serviceLbl).left().row();
        
        panel.add(new Label("Traffic:", skin)).right().padRight(10);
        Label trafficLbl = new Label("0", skin);
        trafficLbl.setName("trafficLabel");
        panel.add(trafficLbl).left().row();
        
        return panel;
    }
    
    /**
     * Center panel - main game view/content area
     */
    private Table createCenterPanel() {
        Table panel = new Table();
        panel.setBackground(createBackground(0.07f, 0.08f, 0.1f, 1f));
        panel.pad(20);
        
        Label placeholder = new Label(
            "=== MAIN GAME VIEW ===\n\n" +
            "This area would show:\n" +
            "- Pub interior visualization\n" +
            "- Customer sprites\n" +
            "- Staff positions\n" +
            "- Current events\n\n" +
            "For now, use the Action panel\n" +
            "to interact with the simulation.",
            skin
        );
        placeholder.setWrap(true);
        placeholder.setAlignment(1); // Center
        
        panel.add(placeholder).expand().center();
        
        return panel;
    }
    
    /**
     * Right panel with action buttons that interact with simulation
     */
    private Table createActionsPanel() {
        Table panel = new Table();
        panel.setBackground(createBackground(0.18f, 0.20f, 0.28f, 1f));
        panel.pad(15);
        panel.defaults().width(180).pad(5);
        
        // Section title
        Label titleLabel = new Label("=== ACTIONS ===", skin);
        titleLabel.setFontScale(1.2f);
        panel.add(titleLabel).center().padBottom(10).row();
        
        // Service controls
        TextButton openButton = new TextButton("Open Service", skin);
        openButton.addListener(e -> {
            if (!(e.getListenerActor() instanceof TextButton)) return false;
            if (!((TextButton)e.getListenerActor()).isPressed()) return false;
            game.simBridge().openService();
            game.audioSettings().onOpenService();
            refresh();
            return true;
        });
        
        TextButton closeButton = new TextButton("Close Service", skin);
        closeButton.addListener(e -> {
            if (!(e.getListenerActor() instanceof TextButton)) return false;
            if (!((TextButton)e.getListenerActor()).isPressed()) return false;
            game.simBridge().closeService();
            game.audioSettings().onCloseService();
            refresh();
            return true;
        });
        
        TextButton advanceButton = new TextButton("Next Round", skin);
        advanceButton.addListener(e -> {
            if (!(e.getListenerActor() instanceof TextButton)) return false;
            if (!((TextButton)e.getListenerActor()).isPressed()) return false;
            game.simBridge().advance();
            refresh();
            return true;
        });
        
        panel.add(openButton).row();
        panel.add(closeButton).row();
        panel.add(advanceButton).row();
        
        // Separator
        panel.add(new Label("", skin)).height(15).row();
        
        // Game management
        TextButton saveButton = new TextButton("Save Game", skin);
        saveButton.addListener(e -> {
            if (!(e.getListenerActor() instanceof TextButton)) return false;
            if (!((TextButton)e.getListenerActor()).isPressed()) return false;
            String result = game.simBridge().saveGame();
            showSaveMessage(result);
            return true;
        });
        
        TextButton infoButton = new TextButton("Show Info", skin);
        infoButton.addListener(e -> {
            if (!(e.getListenerActor() instanceof TextButton)) return false;
            if (!((TextButton)e.getListenerActor()).isPressed()) return false;
            showInfoDialog();
            return true;
        });
        
        TextButton menuButton = new TextButton("Main Menu", skin);
        menuButton.addListener(e -> {
            if (!(e.getListenerActor() instanceof TextButton)) return false;
            if (!((TextButton)e.getListenerActor()).isPressed()) return false;
            game.audioSettings().resetForMenu();
            game.setScreen(new MainMenuScreen(game));
            return true;
        });
        
        panel.add(saveButton).row();
        panel.add(infoButton).row();
        panel.add(menuButton).row();
        
        return panel;
    }
    
    /**
     * Bottom bar with messages and notifications
     */
    private Table createBottomBar() {
        Table bar = new Table();
        bar.setBackground(createBackground(0.15f, 0.16f, 0.22f, 1f));
        bar.pad(5);
        
        Label msgLabel = new Label("", skin);
        msgLabel.setName("saveMessageLabel");
        bar.add(msgLabel).center().expandX();
        
        return bar;
    }
    
    /**
     * Refresh all UI elements with current simulation data
     * This is the key method that connects UI to simulation!
     */
    private void refresh() {
        // Get current state from simulation via SimBridge
        PresentationSnapshot snapshot = game.simBridge().snapshot();
        
        // Update financial labels
        cashLabel.setText(String.format("£%.2f", snapshot.money()));
        debtLabel.setText(String.format("£%.2f", snapshot.debt()));
        
        // Calculate and display net worth
        double netWorth = snapshot.money() - snapshot.debt();
        Label netWorthLabel = findActor("netWorthLabel");
        if (netWorthLabel != null) {
            netWorthLabel.setText(String.format("£%.2f", netWorth));
            // Color code: green if positive, red if negative
            netWorthLabel.setColor(netWorth >= 0 ? Color.GREEN : Color.RED);
        }
        
        // Update reputation with color coding
        reputationLabel.setText(String.valueOf(snapshot.reputation()));
        if (snapshot.reputation() < 0) {
            reputationLabel.setColor(Color.RED);
        } else if (snapshot.reputation() > 50) {
            reputationLabel.setColor(Color.GREEN);
        } else {
            reputationLabel.setColor(Color.YELLOW);
        }
        
        // Update chaos with color coding
        chaosLabel.setText(String.format("%.1f", snapshot.chaos()));
        if (snapshot.chaos() > 50) {
            chaosLabel.setColor(Color.RED);
        } else if (snapshot.chaos() > 20) {
            chaosLabel.setColor(Color.YELLOW);
        } else {
            chaosLabel.setColor(Color.GREEN);
        }
        
        // Update time information
        weekLabel.setText(String.valueOf(snapshot.week()));
        dayLabel.setText(String.valueOf(snapshot.day()));
        roundLabel.setText(String.valueOf(snapshot.round()));
        
        // Update service status
        if (snapshot.serviceOpen()) {
            serviceStatusLabel.setText("OPEN");
            serviceStatusLabel.setColor(Color.GREEN);
        } else {
            serviceStatusLabel.setText("CLOSED");
            serviceStatusLabel.setColor(Color.GRAY);
        }
        
        // Update traffic
        trafficLabel.setText(String.valueOf(snapshot.traffic()));
    }
    
    /**
     * Show a temporary save message
     */
    private void showSaveMessage(String message) {
        saveMessageLabel.setText(message);
        saveMessageLabel.setColor(Color.CYAN);
        saveMessageTimer = 3.0f; // Show for 3 seconds
    }
    
    /**
     * Show an information dialog (example of dialog usage)
     */
    private void showInfoDialog() {
        PresentationSnapshot snapshot = game.simBridge().snapshot();
        
        Dialog dialog = new Dialog("Game Information", skin);
        dialog.pad(20);
        
        String info = String.format(
            "Current Game Status\n\n" +
            "Week %d, Day %d\n" +
            "Cash: £%.2f\n" +
            "Debt: £%.2f\n" +
            "Reputation: %d\n" +
            "Service: %s\n\n" +
            "This dialog demonstrates how to\n" +
            "show information from the simulation.",
            snapshot.week(),
            snapshot.day(),
            snapshot.money(),
            snapshot.debt(),
            snapshot.reputation(),
            snapshot.serviceOpen() ? "Open" : "Closed"
        );
        
        dialog.text(info);
        dialog.button("OK", true);
        dialog.show(stage);
    }
    
    /**
     * Helper to find actor by name in the stage
     */
    @SuppressWarnings("unchecked")
    private <T extends Actor> T findActor(String name) {
        return (T) stage.getRoot().findActor(name);
    }
    
    /**
     * Helper to create colored backgrounds for panels
     */
    private Drawable createBackground(float r, float g, float b, float a) {
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(r, g, b, a);
        pixmap.fill();
        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        return new TextureRegionDrawable(new TextureRegion(texture));
    }
    
    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }
    
    @Override
    public void render(float delta) {
        // Clear screen
        Gdx.gl.glClearColor(0.05f, 0.05f, 0.08f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        // Update save message timer
        if (saveMessageTimer > 0) {
            saveMessageTimer -= delta;
            if (saveMessageTimer <= 0) {
                saveMessageLabel.setText("");
            }
        }
        
        // Update audio
        game.audioSettings().update(delta);
        
        // Update and render UI
        stage.act(delta);
        stage.draw();
    }
    
    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }
    
    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }
}
