package com.javabar.gdx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Base64Coder;
import com.badlogic.gdx.utils.Json;
import com.javabar.sim.GameFactory;
import com.javabar.sim.GameState;
import com.javabar.sim.PresentationSnapshot;
import com.javabar.sim.Simulation;
import com.javabar.sim.UILogger;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/** Thin UI-to-simulation adapter for libGDX screens. */
public class SimBridge {
    private static final int SAVE_VERSION = 1;
    private static final String SAVE_FILE = "savegame.json";

    private GameState state;
    private Simulation simulation;
    private UILogger logger;

    public void startNewGame() {
        startNewGame(System.currentTimeMillis());
    }

    public void startNewGame(long seed) {
        state = GameFactory.newGame(seed);
        logger = new UILogger();
        simulation = new Simulation(state, logger);
    }

    public boolean hasSave() {
        return Gdx.files.local(SAVE_FILE).exists();
    }

    public String saveGame() {
        ensureLive();
        SaveEnvelope envelope = new SaveEnvelope();
        envelope.saveVersion = SAVE_VERSION;
        envelope.seed = state.random.nextLong();
        envelope.payload = serializeState(state);
        Json json = new Json();
        Gdx.files.local(SAVE_FILE).writeString(json.toJson(envelope), false, "UTF-8");
        return "Saved!";
    }

    public String loadGame() {
        FileHandle handle = Gdx.files.local(SAVE_FILE);
        if (!handle.exists()) {
            return "No save found.";
        }
        try {
            Json json = new Json();
            SaveEnvelope envelope = json.fromJson(SaveEnvelope.class, handle);
            if (envelope == null || envelope.saveVersion != SAVE_VERSION || envelope.payload == null) {
                return "Save file is incompatible.";
            }
            state = deserializeState(envelope.payload);
            logger = new UILogger();
            simulation = new Simulation(state, logger);
            return "Loaded.";
        } catch (Exception ex) {
            return "Save file is corrupt.";
        }
    }

    public void openService() { ensureLive(); simulation.openNight(); }
    public void closeService() { ensureLive(); simulation.closeNight("Manual close"); }

    public void advance() {
        ensureLive();
        if (!state.nightOpen) simulation.openNight();
        simulation.playRound();
    }

    public PresentationSnapshot snapshot() {
        ensureLive();
        return new PresentationSnapshot(
                state.cash,
                state.totalCreditBalance(),
                state.reputation,
                state.chaos,
                state.nightOpen,
                state.weekCount,
                state.dayIndex + 1,
                state.roundInNight,
                state.nightPunters.size(),
                state.nightUnserved,
                state.nightRefunds,
                state.nightFights
        );
    }

    private void ensureLive() {
        if (simulation == null) startNewGame();
    }

    private static String serializeState(GameState gameState) {
        try (ByteArrayOutputStream bytes = new ByteArrayOutputStream();
             ObjectOutputStream out = new ObjectOutputStream(bytes)) {
            out.writeObject(gameState);
            out.flush();
            return String.valueOf(Base64Coder.encode(bytes.toByteArray()));
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private static GameState deserializeState(String payload) {
        byte[] bytes = Base64Coder.decode(payload);
        try (ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(bytes))) {
            return (GameState) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new IllegalStateException(e);
        }
    }

    public static class SaveEnvelope {
        public int saveVersion;
        public long seed;
        public String payload;
    }
}
