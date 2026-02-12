# Barva libGDX shell

This project runs the Barva game shell with libGDX UI (`core` + `lwjgl3`) and pure Java simulation logic in `sim`.

## Modules
- `sim`: logic-only simulation (no Swing, no libGDX)
- `core`: libGDX screens, UI flow, simulation bridge
- `lwjgl3`: desktop launcher

## Canonical commands
- `./gradlew lwjgl3:run`
- `./gradlew build`
- `./gradlew lwjgl3:dist` (optional)
