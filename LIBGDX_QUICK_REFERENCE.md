# LibGDX UI Quick Reference Card

## Getting Simulation Data

```java
// ALWAYS use SimBridge to get data
PresentationSnapshot snapshot = simBridge.snapshot();

// Access data through snapshot
double cash = snapshot.money();
double debt = snapshot.debt();
int reputation = snapshot.reputation();
boolean isOpen = snapshot.serviceOpen();
```

## Modifying Simulation State

```java
// Open service
simBridge.openService();

// Close service  
simBridge.closeService();

// Advance to next round
simBridge.advance();

// Save game
String result = simBridge.saveGame();

// Load game
String result = simBridge.loadGame();

// ALWAYS refresh UI after simulation changes!
refresh();
```

## Creating UI Elements

```java
// Label (text display)
Label label = new Label("Hello World", skin);
label.setText("New text");
label.setFontScale(2.0f);
label.setColor(Color.RED);

// Button
TextButton button = new TextButton("Click Me", skin);
button.addListener(e -> {
    if (!(e.getListenerActor() instanceof TextButton)) return false;
    if (!((TextButton)e.getListenerActor()).isPressed()) return false;
    // Do something
    return true;
});

// Text Input
TextField input = new TextField("", skin);
input.setMessageText("Placeholder text");
String text = input.getText();

// List
List<String> list = new List<>(skin);
list.setItems(new String[]{"Item 1", "Item 2"});
String selected = list.getSelected();

// Scrollable list
ScrollPane scroll = new ScrollPane(list, skin);
```

## Layout with Tables

```java
// Create table
Table table = new Table();
table.setFillParent(true);  // Fill screen
table.pad(10);              // Outer padding

// Set default properties for all cells
table.defaults().pad(5).width(200);

// Add elements
table.add(label1).row();                    // Add and new row
table.add(label2).colspan(2).row();        // Span 2 columns
table.add(button).expandX().fillX().row(); // Expand horizontally

// Alignment
table.top().left();    // Align to top-left
table.center();        // Center content
table.bottom().right(); // Bottom-right

// Add to stage
stage.addActor(table);
```

## Common Layout Patterns

```java
// Horizontal row of buttons
Table row = new Table();
row.add(button1).padRight(5);
row.add(button2).padRight(5);
row.add(button3);

// Vertical stack
Table stack = new Table();
stack.defaults().pad(5);
stack.add(label1).row();
stack.add(label2).row();
stack.add(label3).row();

// Two-column form
Table form = new Table();
form.add(new Label("Name:", skin)).right().padRight(10);
form.add(nameInput).left().row();
form.add(new Label("Age:", skin)).right().padRight(10);
form.add(ageInput).left().row();
```

## Dialogs

```java
// Simple dialog
Dialog dialog = new Dialog("Title", skin);
dialog.text("Message text here");
dialog.button("OK", true);
dialog.button("Cancel", false);
dialog.show(stage);

// Dialog with callback
dialog.setObject(myData);
// Override result() to handle button clicks
```

## Creating Backgrounds

```java
// Solid color background
Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
pixmap.setColor(0.2f, 0.2f, 0.3f, 1f);  // RGBA
pixmap.fill();
Texture texture = new Texture(pixmap);
pixmap.dispose();

Drawable bg = new TextureRegionDrawable(new TextureRegion(texture));
table.setBackground(bg);
```

## Screen Template

```java
public class MyScreen extends ScreenAdapter {
    private final Stage stage;
    private final Skin skin;
    private final SimBridge bridge;
    
    public MyScreen(SimBridge bridge) {
        this.bridge = bridge;
        this.stage = new Stage(new ScreenViewport());
        this.skin = UiSkinFactory.createBasicSkin();
        
        // Build UI
        Table root = new Table();
        root.setFillParent(true);
        // ... add components
        stage.addActor(root);
    }
    
    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }
    
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }
    
    @Override
    public void resize(int w, int h) {
        stage.getViewport().update(w, h, true);
    }
    
    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }
}
```

## Updating UI from Simulation

```java
private void refresh() {
    // Get current state
    PresentationSnapshot s = bridge.snapshot();
    
    // Update labels
    cashLabel.setText(String.format("£%.2f", s.money()));
    debtLabel.setText(String.format("£%.2f", s.debt()));
    
    // Color coding
    if (s.reputation() < 0) {
        repLabel.setColor(Color.RED);
    } else {
        repLabel.setColor(Color.GREEN);
    }
    
    // Conditional display
    if (s.serviceOpen()) {
        statusLabel.setText("OPEN");
    } else {
        statusLabel.setText("CLOSED");
    }
}
```

## Common Mistakes to Avoid

```java
// ❌ DON'T access GameState directly
double cash = gameState.cash;  // BAD!

// ✅ DO use SimBridge and snapshot
PresentationSnapshot s = bridge.snapshot();
double cash = s.money();  // GOOD!

// ❌ DON'T forget to refresh UI
button.addListener(e -> {
    bridge.advance();  // Changed state
    // Missing refresh()!
    return true;
});

// ✅ DO refresh after changes
button.addListener(e -> {
    if (!((TextButton)e.getListenerActor()).isPressed()) return false;
    bridge.advance();
    refresh();  // Update UI!
    return true;
});

// ❌ DON'T forget to dispose
// Memory leak!

// ✅ DO dispose resources
@Override
public void dispose() {
    stage.dispose();
    skin.dispose();
}
```

## Finding Actors by Name

```java
// Set name
label.setName("myLabel");

// Find later
Label found = stage.getRoot().findActor("myLabel");
```

## Color Constants

```java
Color.WHITE
Color.BLACK
Color.RED
Color.GREEN
Color.BLUE
Color.YELLOW
Color.CYAN
Color.MAGENTA
Color.GRAY

// Custom color
new Color(0.5f, 0.5f, 0.5f, 1.0f)  // RGBA (0-1)
```

## Formatting Text

```java
// Money
String.format("£%.2f", amount)  // £123.45

// Percentage
String.format("%.1f%%", percent)  // 75.5%

// Integer with padding
String.format("%03d", number)  // 007

// Multi-line
String text = "Line 1\nLine 2\nLine 3";
label.setText(text);
label.setWrap(true);  // Enable wrapping
```

## Useful Properties

```java
// Table cell properties
.width(200)           // Fixed width
.height(100)          // Fixed height
.pad(10)              // All sides padding
.padTop(5)            // Top padding only
.expandX()            // Expand horizontally
.expandY()            // Expand vertically
.fillX()              // Fill horizontally
.fillY()              // Fill vertically
.colspan(2)           // Span columns
.row()                // Start new row

// Table alignment
.top()
.bottom()
.left()
.right()
.center()

// Actor properties
.setPosition(x, y)
.setSize(width, height)
.setColor(color)
.setVisible(boolean)
.setScale(scaleX, scaleY)
```

## Key Differences from Unity

| Unity | libGDX |
|-------|--------|
| `text.text = "Hi"` | `label.setText("Hi")` |
| `gameObject.SetActive(false)` | `actor.setVisible(false)` |
| `transform.position` | `actor.setPosition(x, y)` |
| `RectTransform.sizeDelta` | `actor.setSize(w, h)` |
| `onClick.AddListener()` | `addListener()` |
| Vertical Layout Group | `Table` with `.row()` |
| Horizontal Layout Group | `Table` without `.row()` |
| Canvas | `Stage` |
| UI Element | `Actor` |
