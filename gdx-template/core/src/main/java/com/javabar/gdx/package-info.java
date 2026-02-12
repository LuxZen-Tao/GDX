/**
 * LibGDX UI layer providing rendering and user interaction.
 * 
 * <p>This package contains libGDX-specific UI code and adapters that extend
 * the pure simulation layer with UI-specific functionality.</p>
 * 
 * <h2>UI Adapters</h2>
 * <ul>
 *   <li><b>{@link SimBridge}</b>: Extends {@link com.javabar.sim.SimBridge} with save/load persistence
 *       using libGDX's file I/O and JSON serialization</li>
 *   <li><b>{@link AudioSettings}</b>: Manages audio lifecycle, crossfading, and profile switching
 *       (extends {@link com.javabar.sim.AudioSettings} with game loop integration)</li>
 * </ul>
 * 
 * <h2>Screen Classes</h2>
 * <ul>
 *   <li>{@link JavaBarGdxGame}: Main game class and screen coordinator</li>
 *   <li>{@link GameScreen}: Simple game UI</li>
 *   <li>{@link EnhancedGameScreen}: Advanced game UI with detailed panels</li>
 *   <li>{@link MainMenuScreen}: Menu and game management</li>
 * </ul>
 * 
 * <h2>Data Flow</h2>
 * <p>UI screens call {@code game.simBridge().snapshot()} to get {@link com.javabar.sim.PresentationSnapshot}
 * with current game state, then render it using libGDX Scene2D UI components.</p>
 */
package com.javabar.gdx;
