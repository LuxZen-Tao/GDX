# LibGDX UI Guide for Unity Developers

## Introduction

Welcome! This guide is designed to help Unity developers understand how to create UI in libGDX and connect it to the simulation logic. The GDX project follows a clean architecture where:

- **`sim` module**: Pure Java simulation logic (the "truth source")
- **`core` module**: libGDX UI layer that displays simulation data
- **`SimBridge`**: The adapter that connects UI to simulation

## Table of Contents

1. [Architecture Overview](#architecture-overview)
2. [UI Framework Basics](#ui-framework-basics)
3. [Connecting UI to Simulation](#connecting-ui-to-simulation)
4. [Creating UI Elements](#creating-ui-elements)
5. [Layout and Positioning](#layout-and-positioning)
6. [Advanced Examples](#advanced-examples)
7. [Unity to libGDX Comparison](#unity-to-libgdx-comparison)

---

## Architecture Overview

### The Data Flow

```
GameState (sim) → SimBridge → PresentationSnapshot → UI Components
```

1. **GameState**: The source of truth containing all game data (cash, reputation, staff, etc.)
2. **Simulation**: Processes game logic and updates GameState
3. **SimBridge**: Thin adapter that exposes simulation operations to UI
4. **PresentationSnapshot**: Immutable data object containing UI-relevant information
5. **UI Screens**: libGDX screens that display data and handle user input

### Key Files

- `sim/src/main/java/GameState.java` - All game data lives here
- `core/src/main/java/com/javabar/gdx/SimBridge.java` - UI-to-simulation adapter
- `core/src/main/java/com/javabar/gdx/PresentationSnapshot.java` - UI data transfer object
- `core/src/main/java/com/javabar/gdx/GameScreen.java` - Main game UI screen

---

## UI Framework Basics

### Scene2D Overview

LibGDX uses **Scene2D** for UI, which is similar to Unity's UI system but with some differences:

| Unity Concept | libGDX Equivalent | Notes |
|---------------|-------------------|-------|
| Canvas | Stage | Container for all UI elements |
| RectTransform | Actor (with position/size) | Base class for all UI elements |
| Text | Label | Displays text |
| Button | TextButton | Clickable button with text |
| Panel | Table | Layout container |
| Event System | InputProcessor | Handles input events |
| Prefab/UI Element | Widget/Actor | Reusable UI components |

### Basic Setup

Every screen needs these core components:

```java
public class MyScreen extends ScreenAdapter {
    private final Stage stage;           // Like Unity's Canvas
    private final Skin skin;             // Contains styles and fonts
    
    public MyScreen() {
        // Create stage with viewport (handles screen resizing)
        stage = new Stage(new ScreenViewport());
        
        // Create or load UI skin (fonts, colors, styles)
        skin = UiSkinFactory.createBasicSkin();
        
        // Build UI here...
    }
    
    @Override
    public void show() {
        // Called when screen becomes active
        Gdx.input.setInputProcessor(stage);
    }
    
    @Override
    public void render(float delta) {
        // Clear screen
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        // Update and draw UI
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
```

---

## Connecting UI to Simulation

### Step 1: Extend PresentationSnapshot

First, add the data you need to display. Edit `PresentationSnapshot.java`:

```java
public final class PresentationSnapshot {
    // Existing fields...
    private final double money;
    private final double debt;
    
    // Add new fields for UI
    private final int staffCount;
    private final String pubName;
    
    public PresentationSnapshot(
        double money,
        double debt,
        // ... other params
        int staffCount,     // Add new parameters
        String pubName
    ) {
        this.money = money;
        this.debt = debt;
        this.staffCount = staffCount;
        this.pubName = pubName;
    }
    
    // Add getters
    public int staffCount() { return staffCount; }
    public String pubName() { return pubName; }
}
```

### Step 2: Update SimBridge.snapshot()

Modify `SimBridge.java` to pull the new data from GameState:

```java
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
        state.nightFights,
        state.staff.size(),           // NEW: Staff count
        state.pubName                  // NEW: Pub name
    );
}
```

### Step 3: Display in UI

Use the snapshot in your screen:

```java
public class GameScreen extends ScreenAdapter {
    private final SimBridge simBridge;
    private final Label staffLabel;
    
    public GameScreen(SimBridge simBridge) {
        this.simBridge = simBridge;
        
        // Create label
        staffLabel = new Label("", skin);
        
        // Add to stage
        stage.addActor(staffLabel);
        
        // Initial update
        refresh();
    }
    
    private void refresh() {
        PresentationSnapshot snapshot = simBridge.snapshot();
        
        // Update UI from snapshot
        staffLabel.setText("Staff: " + snapshot.staffCount());
    }
    
    @Override
    public void render(float delta) {
        // ... render code
        
        // Update periodically or after actions
        refresh();
    }
}
```

---

## Creating UI Elements

### Labels (Text Display)

```java
// Simple label
Label titleLabel = new Label("The Barva Pub", skin);
titleLabel.setFontScale(2.0f);  // Make text bigger

// Label with color
Label warningLabel = new Label("Low Cash!", skin);
Label.LabelStyle warningStyle = new Label.LabelStyle(skin.get(Label.LabelStyle.class));
warningStyle.fontColor = Color.RED;
warningLabel.setStyle(warningStyle);

// Updating label text
cashLabel.setText(String.format("Cash: £%.2f", snapshot.money()));
```

### Buttons

```java
TextButton saveButton = new TextButton("Save Game", skin);

// Add click listener
saveButton.addListener(new ClickListener() {
    @Override
    public void clicked(InputEvent event, float x, float y) {
        String result = simBridge.saveGame();
        System.out.println(result);
    }
});

// Shorter lambda version (recommended)
saveButton.addListener(e -> {
    if (!((TextButton)e.getListenerActor()).isPressed()) return false;
    simBridge.saveGame();
    return true;
});
```

### Text Input

```java
TextField nameInput = new TextField("", skin);
nameInput.setMessageText("Enter pub name...");  // Placeholder
nameInput.setMaxLength(30);

// Get text value
String pubName = nameInput.getText();
```

### Images

```java
// Load texture
Texture logoTexture = new Texture(Gdx.files.internal("logo.png"));
Image logo = new Image(logoTexture);

// Set size
logo.setSize(200, 100);
```

### Lists and Selection

```java
List<String> staffList = new List<>(skin);
String[] staffNames = {"John (FOH)", "Mary (BOH)", "Steve (Manager)"};
staffList.setItems(staffNames);

// Get selection
String selected = staffList.getSelected();

// Add selection listener
staffList.addListener(new ChangeListener() {
    @Override
    public void changed(ChangeEvent event, Actor actor) {
        System.out.println("Selected: " + staffList.getSelected());
    }
});
```

---

## Layout and Positioning

### Using Tables (Recommended)

Tables are like HTML tables - they arrange UI elements in rows and columns:

```java
Table root = new Table();
root.setFillParent(true);  // Fill entire screen
root.top().left();          // Align to top-left
root.pad(20);              // Padding around edges

// Add elements
root.add(titleLabel).colspan(2).row();  // Span 2 columns, then new row
root.add(cashLabel).width(200).row();   // Fixed width
root.add(debtLabel).expandX().row();    // Expand horizontally
root.add(saveButton).pad(10).row();     // Padding around button

stage.addActor(root);
```

### Table Layout Examples

#### Simple Vertical Stack
```java
Table vStack = new Table();
vStack.defaults().pad(5);  // Default padding for all cells

vStack.add(label1).row();
vStack.add(label2).row();
vStack.add(label3).row();
```

#### Horizontal Row
```java
Table hRow = new Table();
hRow.add(button1).padRight(10);
hRow.add(button2).padRight(10);
hRow.add(button3);
```

#### Grid Layout
```java
Table grid = new Table();
grid.defaults().pad(5).width(100);

// Row 1
grid.add(label1);
grid.add(label2);
grid.add(label3).row();

// Row 2
grid.add(button1);
grid.add(button2);
grid.add(button3).row();
```

#### Complex Layout
```java
Table layout = new Table();
layout.setFillParent(true);

// Header
Table header = new Table();
header.add(new Label("Game Stats", skin)).colspan(2).row();
layout.add(header).expandX().fillX().row();

// Stats section (2 columns)
layout.add(new Label("Cash:", skin)).right().padRight(10);
layout.add(cashValueLabel).left().row();

layout.add(new Label("Debt:", skin)).right().padRight(10);
layout.add(debtValueLabel).left().row();

// Button row at bottom
Table buttonRow = new Table();
buttonRow.add(saveButton).padRight(5);
buttonRow.add(loadButton).padRight(5);
buttonRow.add(quitButton);

layout.add(buttonRow).colspan(2).bottom().expandY().row();

stage.addActor(layout);
```

### Manual Positioning

For precise control (like Unity's anchors):

```java
// Position directly
label.setPosition(100, 200);  // X, Y from bottom-left

// Size
label.setSize(200, 50);

// Center on screen
label.setPosition(
    (Gdx.graphics.getWidth() - label.getWidth()) / 2,
    (Gdx.graphics.getHeight() - label.getHeight()) / 2
);
```

---

## Advanced Examples

### Example 1: Stats Panel with Live Updates

```java
public class StatsPanel {
    private final Table panel;
    private final Label cashLabel;
    private final Label debtLabel;
    private final Label repLabel;
    private final Label staffLabel;
    
    public StatsPanel(Skin skin) {
        panel = new Table();
        panel.setBackground(createPanelBackground());
        panel.pad(15);
        panel.defaults().pad(3).left();
        
        // Create labels
        cashLabel = new Label("", skin);
        debtLabel = new Label("", skin);
        repLabel = new Label("", skin);
        staffLabel = new Label("", skin);
        
        // Layout
        panel.add(new Label("=== STATS ===", skin)).center().colspan(2).row();
        panel.add(new Label("Cash:", skin)).right().padRight(10);
        panel.add(cashLabel).left().row();
        panel.add(new Label("Debt:", skin)).right().padRight(10);
        panel.add(debtLabel).left().row();
        panel.add(new Label("Reputation:", skin)).right().padRight(10);
        panel.add(repLabel).left().row();
        panel.add(new Label("Staff:", skin)).right().padRight(10);
        panel.add(staffLabel).left().row();
    }
    
    public void update(PresentationSnapshot snapshot) {
        cashLabel.setText(String.format("£%.2f", snapshot.money()));
        debtLabel.setText(String.format("£%.2f", snapshot.debt()));
        
        // Color-code reputation
        repLabel.setText(String.valueOf(snapshot.reputation()));
        if (snapshot.reputation() < 0) {
            repLabel.setColor(Color.RED);
        } else if (snapshot.reputation() > 50) {
            repLabel.setColor(Color.GREEN);
        } else {
            repLabel.setColor(Color.WHITE);
        }
        
        staffLabel.setText("5 / 8");  // Example: current/max
    }
    
    public Table getPanel() {
        return panel;
    }
    
    private Drawable createPanelBackground() {
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(0.2f, 0.2f, 0.3f, 0.9f);  // Semi-transparent
        pixmap.fill();
        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        return new TextureRegionDrawable(new TextureRegion(texture));
    }
}

// Usage in GameScreen:
statsPanel = new StatsPanel(skin);
root.add(statsPanel.getPanel()).top().left().pad(10);
```

### Example 2: Dialog Window

```java
public class ConfirmDialog extends Dialog {
    private Runnable onConfirm;
    
    public ConfirmDialog(String title, String message, Skin skin) {
        super(title, skin);
        
        text(message);
        button("Yes", true);   // Result value = true
        button("No", false);   // Result value = false
        
        // Center on screen
        setModal(true);  // Block interaction with other UI
    }
    
    public void setOnConfirm(Runnable callback) {
        this.onConfirm = callback;
    }
    
    @Override
    protected void result(Object object) {
        if (Boolean.TRUE.equals(object) && onConfirm != null) {
            onConfirm.run();
        }
    }
}

// Usage:
ConfirmDialog dialog = new ConfirmDialog(
    "Confirm Action",
    "Are you sure you want to close the pub?",
    skin
);
dialog.setOnConfirm(() -> {
    simBridge.closeService();
    refresh();
});
dialog.show(stage);
```

### Example 3: Staff List with Details

```java
public class StaffListPanel {
    private final Table panel;
    private final List<String> staffList;
    private final Label detailsLabel;
    
    public StaffListPanel(Skin skin, SimBridge bridge) {
        panel = new Table();
        panel.setBackground(createBackground());
        panel.pad(10);
        
        // Title
        panel.add(new Label("Staff", skin)).colspan(2).row();
        
        // Staff list
        staffList = new List<>(skin);
        ScrollPane scrollPane = new ScrollPane(staffList, skin);
        scrollPane.setScrollingDisabled(true, false);
        panel.add(scrollPane).width(200).height(150).pad(5);
        
        // Details panel
        detailsLabel = new Label("Select a staff member", skin);
        detailsLabel.setWrap(true);
        panel.add(detailsLabel).width(200).height(150).pad(5).top();
        
        // Selection listener
        staffList.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                updateDetails();
            }
        });
    }
    
    public void update(List<String> staffNames) {
        String[] array = staffNames.toArray(new String[0]);
        staffList.setItems(array);
    }
    
    private void updateDetails() {
        String selected = staffList.getSelected();
        if (selected != null) {
            // In real implementation, get details from simulation
            detailsLabel.setText(String.format(
                "Name: %s\nRole: Server\nMorale: 75%%\nWage: £50/week",
                selected
            ));
        }
    }
    
    public Table getPanel() {
        return panel;
    }
    
    private Drawable createBackground() {
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(0.15f, 0.16f, 0.22f, 1f);
        pixmap.fill();
        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        return new TextureRegionDrawable(new TextureRegion(texture));
    }
}
```

### Example 4: Action Menu with Simulation Integration

```java
public class ActionMenu {
    private final Table menu;
    private final SimBridge bridge;
    private final Runnable onUpdate;
    
    public ActionMenu(Skin skin, SimBridge bridge, Runnable onUpdate) {
        this.bridge = bridge;
        this.onUpdate = onUpdate;
        
        menu = new Table();
        menu.defaults().pad(5).width(150);
        
        // Service controls
        TextButton openBtn = new TextButton("Open Service", skin);
        TextButton closeBtn = new TextButton("Close Service", skin);
        TextButton advanceBtn = new TextButton("Next Round", skin);
        
        // Connect to simulation
        openBtn.addListener(e -> {
            if (!(e.getListenerActor() instanceof TextButton)) return false;
            if (!((TextButton)e.getListenerActor()).isPressed()) return false;
            bridge.openService();
            onUpdate.run();
            return true;
        });
        
        closeBtn.addListener(e -> {
            if (!(e.getListenerActor() instanceof TextButton)) return false;
            if (!((TextButton)e.getListenerActor()).isPressed()) return false;
            bridge.closeService();
            onUpdate.run();
            return true;
        });
        
        advanceBtn.addListener(e -> {
            if (!(e.getListenerActor() instanceof TextButton)) return false;
            if (!((TextButton)e.getListenerActor()).isPressed()) return false;
            bridge.advance();
            onUpdate.run();
            return true;
        });
        
        // Staff management
        TextButton hireBtn = new TextButton("Hire Staff", skin);
        hireBtn.addListener(e -> {
            if (!(e.getListenerActor() instanceof TextButton)) return false;
            if (!((TextButton)e.getListenerActor()).isPressed()) return false;
            showHireDialog();
            return true;
        });
        
        // Add to menu
        menu.add(new Label("Actions", skin)).row();
        menu.add(openBtn).row();
        menu.add(closeBtn).row();
        menu.add(advanceBtn).row();
        menu.add(hireBtn).row();
    }
    
    private void showHireDialog() {
        // Create and show hire staff dialog
        // This would interact with simulation through bridge
    }
    
    public Table getMenu() {
        return menu;
    }
}
```

---

## Unity to libGDX Comparison

### Common Patterns Translation

#### Unity: Setting Text
```csharp
textComponent.text = "Hello";
```

#### libGDX:
```java
label.setText("Hello");
```

---

#### Unity: Button Click
```csharp
button.onClick.AddListener(() => {
    Debug.Log("Clicked");
});
```

#### libGDX:
```java
button.addListener(e -> {
    if (!((TextButton)e.getListenerActor()).isPressed()) return false;
    System.out.println("Clicked");
    return true;
});
```

---

#### Unity: Anchoring to Top-Left
```csharp
rectTransform.anchorMin = new Vector2(0, 1);
rectTransform.anchorMax = new Vector2(0, 1);
rectTransform.pivot = new Vector2(0, 1);
```

#### libGDX:
```java
table.setFillParent(true);
table.top().left();
```

---

#### Unity: Vertical Layout Group
```csharp
// Add VerticalLayoutGroup component
VerticalLayoutGroup layout = gameObject.AddComponent<VerticalLayoutGroup>();
layout.spacing = 10;
```

#### libGDX:
```java
Table table = new Table();
table.defaults().pad(5);  // spacing
table.add(element1).row();
table.add(element2).row();
table.add(element3).row();
```

---

#### Unity: Getting Data from Singleton
```csharp
GameManager.Instance.GetPlayerMoney();
```

#### libGDX:
```java
PresentationSnapshot snapshot = simBridge.snapshot();
double money = snapshot.money();
```

---

## Best Practices

### 1. **Always Use Snapshots**
Never access `GameState` directly from UI code. Always go through `SimBridge.snapshot()`.

❌ **DON'T:**
```java
// Direct access to simulation state - BAD!
double cash = gameState.cash;
```

✅ **DO:**
```java
// Use snapshot - GOOD!
PresentationSnapshot snapshot = simBridge.snapshot();
double cash = snapshot.money();
```

### 2. **Refresh After Actions**
Always update the UI after performing simulation actions:

```java
button.addListener(e -> {
    if (!((TextButton)e.getListenerActor()).isPressed()) return false;
    simBridge.advance();  // Modify simulation
    refresh();             // Update UI
    return true;
});
```

### 3. **Use Tables for Layout**
Tables are more maintainable than manual positioning:

```java
// Responsive, maintainable
Table table = new Table();
table.setFillParent(true);
table.add(label).expandX().row();

// vs manual positioning (harder to maintain)
label.setPosition(100, 200);
```

### 4. **Dispose Resources**
Always dispose of resources in the `dispose()` method:

```java
@Override
public void dispose() {
    stage.dispose();
    skin.dispose();
    // Dispose textures, etc.
}
```

### 5. **Use Skins for Consistent Styling**
Create custom skins for different UI themes:

```java
// Define styles once
Skin skin = new Skin();
skin.add("title-font", bigFont);
skin.add("body-font", normalFont);

// Reuse everywhere
Label title = new Label("Title", skin, "title");
Label body = new Label("Body text", skin, "body");
```

---

## Quick Reference: Common Tasks

### Display Simulation Data
```java
PresentationSnapshot snapshot = simBridge.snapshot();
label.setText(String.format("Cash: £%.2f", snapshot.money()));
```

### Trigger Simulation Action
```java
button.addListener(e -> {
    if (!((TextButton)e.getListenerActor()).isPressed()) return false;
    simBridge.advance();
    refresh();
    return true;
});
```

### Create Dialog
```java
Dialog dialog = new Dialog("Title", skin);
dialog.text("Message");
dialog.button("OK", true);
dialog.show(stage);
```

### Create Scrollable List
```java
List<String> list = new List<>(skin);
list.setItems(new String[]{"Item 1", "Item 2", "Item 3"});
ScrollPane scroll = new ScrollPane(list, skin);
table.add(scroll).height(200);
```

### Add Background to Table
```java
Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
pixmap.setColor(0.2f, 0.2f, 0.3f, 1f);
pixmap.fill();
Texture texture = new Texture(pixmap);
pixmap.dispose();
table.setBackground(new TextureRegionDrawable(new TextureRegion(texture)));
```

---

## Next Steps

1. **Study the existing code**: Read `GameScreen.java` to see a working example
2. **Experiment**: Modify `GameScreen.java` to add new UI elements
3. **Expand PresentationSnapshot**: Add more data fields as needed
4. **Create new screens**: Implement staff management, inventory screens, etc.
5. **Read libGDX docs**: https://libgdx.com/wiki/graphics/2d/scene2d/scene2d

## Additional Resources

- libGDX Wiki: https://libgdx.com/wiki/
- Scene2D Documentation: https://libgdx.com/wiki/graphics/2d/scene2d/scene2d
- Scene2D UI Tutorial: https://libgdx.com/wiki/graphics/2d/scene2d/scene2d-ui
- API Javadocs: https://libgdx.badlogicgames.com/ci/nightlies/docs/api/

Good luck building your UI! 🎮
