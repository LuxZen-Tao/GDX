# Step-by-Step Tutorial: Your First libGDX UI Screen

## Tutorial Goal
By the end of this tutorial, you'll create a simple screen that displays your pub's name and cash, with a button to add money.

## Prerequisites
- Basic Java knowledge
- The GDX project running on your machine

## Step 1: Create Your Screen Class

Create a new file: `gdx-template/core/src/main/java/com/javabar/gdx/MyFirstScreen.java`

```java
package com.javabar.gdx;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class MyFirstScreen extends ScreenAdapter {
    private final JavaBarGdxGame game;
    private final Stage stage;
    private final Skin skin;
    
    public MyFirstScreen(JavaBarGdxGame game) {
        this.game = game;
        this.stage = new Stage(new ScreenViewport());
        this.skin = UiSkinFactory.createBasicSkin();
        
        // We'll add UI here in the next steps
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

**What's happening here?**
- `ScreenAdapter` - Base class for screens in libGDX (like Unity's Scene)
- `Stage` - Container for all UI elements (like Unity's Canvas)
- `Skin` - Contains fonts and styles for UI elements
- `show()` - Called when screen becomes active
- `render(delta)` - Called every frame (like Unity's Update)
- `dispose()` - Clean up resources when done

## Step 2: Add Import Statements

Add these imports at the top of your file:

```java
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.javabar.sim.PresentationSnapshot;
```

## Step 3: Add UI Labels

Add these fields at the top of your class (after the `skin` field):

```java
private final Label titleLabel;
private final Label cashLabel;
```

Then in the constructor, after `this.skin = ...`, add:

```java
// Create labels
titleLabel = new Label("My Pub Manager", skin);
titleLabel.setFontScale(2.0f);  // Make it bigger

cashLabel = new Label("Cash: £0.00", skin);

// Create layout
Table root = new Table();
root.setFillParent(true);
root.top().pad(20);

// Add labels to layout
root.add(titleLabel).row();
root.add(cashLabel).padTop(20).row();

// Add layout to stage
stage.addActor(root);
```

**What's happening?**
- `Label` - Displays text (like Unity's Text component)
- `Table` - Layout container (like Unity's Vertical Layout Group)
- `setFillParent(true)` - Make table fill the screen
- `top().pad(20)` - Align to top with padding
- `row()` - Start a new row (like adding a new element below)

## Step 4: Connect to Simulation Data

Add a method to update labels from simulation:

```java
private void refresh() {
    // Get current game state from simulation
    PresentationSnapshot snapshot = game.simBridge().snapshot();
    
    // Update cash label
    cashLabel.setText(String.format("Cash: £%.2f", snapshot.money()));
}
```

Call `refresh()` at the end of your constructor (after `stage.addActor(root)`):

```java
refresh();  // Initial update
```

**What's happening?**
- `game.simBridge().snapshot()` - Gets current game state
- `snapshot.money()` - Gets cash amount from simulation
- `setText()` - Updates the label text

## Step 5: Add an Interactive Button

Add this field at the top of your class:

```java
private final TextButton addMoneyButton;
```

In the constructor, after creating `cashLabel`, add:

```java
// Create button
addMoneyButton = new TextButton("Add £100", skin);

// Add click listener
addMoneyButton.addListener(e -> {
    if (!(e.getListenerActor() instanceof TextButton)) return false;
    if (!((TextButton)e.getListenerActor()).isPressed()) return false;
    
    // Modify simulation by advancing (which earns money)
    game.simBridge().advance();
    
    // Update UI to show new state
    refresh();
    
    return true;
});
```

Add the button to your layout (before `stage.addActor(root)`):

```java
root.add(titleLabel).row();
root.add(cashLabel).padTop(20).row();
root.add(addMoneyButton).padTop(10).row();  // NEW LINE
```

**What's happening?**
- `TextButton` - Clickable button (like Unity's Button)
- `addListener()` - Handles button clicks
- `game.simBridge().advance()` - Tells simulation to progress
- `refresh()` - Updates UI to show new cash amount

## Step 6: Test Your Screen

To access your new screen, add a button in `MainMenuScreen.java`.

After line 33, add:

```java
TextButton myFirstScreen = new TextButton("My First Screen", skin);

myFirstScreen.addListener(e -> {
    if (!myFirstScreen.isPressed()) return false;
    game.simBridge().startNewGame();
    game.setScreen(new MyFirstScreen(game));
    return true;
});
```

And update the button row to include your new button:

```java
Table row = new Table();
row.defaults().pad(10).width(240).height(55);
row.add(newGame);
row.add(loadGame);
row.add(myFirstScreen);  // ADD THIS
row.add(enhancedDemo);
root.add(row);
```

## Step 7: Run and Test

```bash
cd gdx-template
./gradlew lwjgl3:run
```

Click "My First Screen" and you should see:
- A title at the top
- Your current cash amount
- A button that, when clicked, advances the game and updates the cash

## Complete Code

Here's the full `MyFirstScreen.java`:

```java
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

public class MyFirstScreen extends ScreenAdapter {
    private final JavaBarGdxGame game;
    private final Stage stage;
    private final Skin skin;
    
    private final Label titleLabel;
    private final Label cashLabel;
    private final TextButton addMoneyButton;
    
    public MyFirstScreen(JavaBarGdxGame game) {
        this.game = game;
        this.stage = new Stage(new ScreenViewport());
        this.skin = UiSkinFactory.createBasicSkin();
        
        // Create labels
        titleLabel = new Label("My Pub Manager", skin);
        titleLabel.setFontScale(2.0f);
        
        cashLabel = new Label("Cash: £0.00", skin);
        
        // Create button
        addMoneyButton = new TextButton("Advance Game", skin);
        addMoneyButton.addListener(e -> {
            if (!(e.getListenerActor() instanceof TextButton)) return false;
            if (!((TextButton)e.getListenerActor()).isPressed()) return false;
            game.simBridge().advance();
            refresh();
            return true;
        });
        
        // Create layout
        Table root = new Table();
        root.setFillParent(true);
        root.top().pad(20);
        
        // Add elements to layout
        root.add(titleLabel).row();
        root.add(cashLabel).padTop(20).row();
        root.add(addMoneyButton).padTop(10).row();
        
        // Add layout to stage
        stage.addActor(root);
        
        // Initial data refresh
        refresh();
    }
    
    private void refresh() {
        PresentationSnapshot snapshot = game.simBridge().snapshot();
        cashLabel.setText(String.format("Cash: £%.2f", snapshot.money()));
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

## What You Learned

✅ How to create a libGDX screen
✅ How to use Stage and Skin
✅ How to create Labels and Buttons
✅ How to use Tables for layout
✅ How to get data from simulation (SimBridge → PresentationSnapshot)
✅ How to modify simulation state (SimBridge methods)
✅ How to refresh UI after changes

## Next Steps

1. **Add more data**: Show reputation, debt, week number
   - Get them from `snapshot.reputation()`, `snapshot.debt()`, etc.

2. **Add more buttons**: 
   - Open/Close service
   - Save/Load game
   - Different simulation actions

3. **Add colors**: Color-code labels based on values
   ```java
   if (snapshot.money() < 0) {
       cashLabel.setColor(Color.RED);
   } else {
       cashLabel.setColor(Color.GREEN);
   }
   ```

4. **Create panels**: Group related elements in separate Tables
   - Stats panel
   - Actions panel
   - Info panel

5. **Add dialogs**: Show popup windows
   ```java
   Dialog dialog = new Dialog("Title", skin);
   dialog.text("Message");
   dialog.button("OK");
   dialog.show(stage);
   ```

## Remember

- **Always use SimBridge** to interact with simulation
- **Always call refresh()** after modifying simulation
- **Never access GameState directly** from UI code
- **Use PresentationSnapshot** to read simulation data
- **Tables are your friend** for layout

## Questions?

Check out:
- `LIBGDX_UI_GUIDE.md` - Detailed guide with more examples
- `LIBGDX_QUICK_REFERENCE.md` - Quick syntax reference
- `EnhancedGameScreen.java` - Advanced example
- libGDX docs: https://libgdx.com/wiki/

Happy coding! 🎮
