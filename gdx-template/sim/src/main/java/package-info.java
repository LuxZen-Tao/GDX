/**
 * Pure simulation engine with zero UI dependencies.
 * 
 * <p>This package contains all game logic, economic systems, and simulation state.
 * It has NO dependencies on any UI framework (Swing, libGDX, etc.).</p>
 * 
 * <h2>UI Integration Pattern</h2>
 * <p>The simulation achieves UI independence through:</p>
 * <ul>
 *   <li><b>Data Transfer Objects</b>: {@link PresentationSnapshot} provides read-only game state</li>
 *   <li><b>Callback Interfaces</b>: {@link UILogger} publishes events via {@code Consumer<>} callbacks</li>
 *   <li><b>Bridge Classes</b>: {@link SimBridge} provides basic simulation control</li>
 * </ul>
 * 
 * <h2>Design Philosophy</h2>
 * <p>This module can work with ANY UI framework - LibGDX, Swing, JavaFX, Android, web, etc.
 * The UI layer depends on sim, but sim never depends on UI.</p>
 */
package com.javabar.sim;
