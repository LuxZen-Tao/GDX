# GDX

A [libGDX](https://libgdx.com/) project generated with [gdx-liftoff](https://github.com/libgdx/gdx-liftoff).

This project was generated with a template including simple application launchers and an `ApplicationAdapter` extension that draws libGDX logo.

## Platforms

- `core`: Main module with the application logic shared by all platforms.
- `lwjgl3`: Primary desktop platform using LWJGL3; was called 'desktop' in older docs.

## Gradle

This project uses [Gradle](https://gradle.org/) to manage dependencies.
The Gradle wrapper was included, so you can run Gradle tasks using `gradlew.bat` or `./gradlew` commands.
Useful Gradle tasks and flags:

- `--continue`: when using this flag, errors will not stop the tasks from running.
- `--daemon`: thanks to this flag, Gradle daemon will be used to run chosen tasks.
- `--offline`: when using this flag, cached dependency archives will be used.
- `--refresh-dependencies`: this flag forces validation of all dependencies. Useful for snapshot versions.
- `build`: builds sources and archives of every project.
- `cleanEclipse`: removes Eclipse project data.
- `cleanIdea`: removes IntelliJ project data.
- `clean`: removes `build` folders, which store compiled classes and built archives.
- `eclipse`: generates Eclipse project data.
- `idea`: generates IntelliJ project data.
- `lwjgl3:jar`: builds application's runnable jar, which can be found at `lwjgl3/build/libs`.
- `lwjgl3:run`: starts the application.
- `test`: runs unit tests (if any).

Note that most tasks that are not specific to a single project can be run with `name:` prefix, where the `name` should be replaced with the ID of a specific project.
For example, `core:clean` removes `build` folder only from the `core` project.


## Quick local run sequence (desktop)

If you copied this project and just want to run it locally, use this exact order:

1. **Use a supported JDK** (Java 17+ recommended):
   - macOS/Linux: `java -version`
   - Windows (PowerShell): `java -version`
2. **Go to project folder**:
   - macOS/Linux: `cd gdx-template`
   - Windows (PowerShell): `cd gdx-template`
3. **Make wrapper executable (macOS/Linux only)**:
   - `chmod +x ./gradlew`
4. **List projects/tasks to verify Gradle works**:
   - macOS/Linux: `./gradlew projects`
   - Windows: `./gradlew.bat projects`
5. **Run desktop app**:
   - macOS/Linux: `./gradlew lwjgl3:run`
   - Windows: `./gradlew.bat lwjgl3:run`

### If you get dependency/proxy errors

- Try clearing forced proxy vars in your shell for this session:
  - macOS/Linux: `unset http_proxy https_proxy HTTP_PROXY HTTPS_PROXY`
- Then refresh dependencies and run again:
  - macOS/Linux: `./gradlew --refresh-dependencies lwjgl3:run`
  - Windows: `./gradlew.bat --refresh-dependencies lwjgl3:run`

### Build a runnable desktop jar

- macOS/Linux: `./gradlew lwjgl3:jar`
- Windows: `./gradlew.bat lwjgl3:jar`
- Output: `lwjgl3/build/libs/`
