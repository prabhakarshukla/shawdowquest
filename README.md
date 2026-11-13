# Shadow Quest: The Lost Realm

A five-member college project built with Java 21, JavaFX 21, and Maven.
Phase 2.4 connects keyboard input, the game loop, and player movement to a
temporary Canvas gameplay screen. The user currently handles coding,
architecture, and integration. Combat and world rendering remain future work.

The source of truth is [PROJECT_PLAN.md](docs/PROJECT_PLAN.md) and
[ARCHITECTURE.md](docs/ARCHITECTURE.md). Dated revisions preserve the original
plans while recording the current roadmap and implemented contracts.

## Requirements

- A JDK 21 installation. Set `JAVA_HOME` to its directory and put its `bin`
  directory first on `PATH`.
- A graphical desktop session to run JavaFX.
- Internet access on the first build to download Maven and dependencies.
- Git for the team's GitHub workflow.

The committed Maven Wrapper downloads Maven 3.9.11. A separate Maven install or
JavaFX SDK download is unnecessary. The only application dependency is
`javafx-controls`, which brings in JavaFX graphics, including Canvas, and base.
FXML, Swing, and third-party game libraries are not used.

## Setup and run

Main repository: [prabhakarshukla/shawdowquest](https://github.com/prabhakarshukla/shawdowquest).
The GitHub repository name differs from the Maven artifact name
`shadow-quest-lost-realm`; this does not affect building or running the project.

Clone your fork and open the repository root, the directory containing `pom.xml`.

In Windows PowerShell, point this terminal at your JDK 21 installation:

```powershell
$env:JAVA_HOME = 'C:\path\to\jdk-21'
$env:Path = "$env:JAVA_HOME\bin;$env:Path"
java -version
javac -version
.\mvnw.cmd -version
.\mvnw.cmd clean compile
.\mvnw.cmd test
.\mvnw.cmd javafx:run
```

On macOS or Linux, set `JAVA_HOME` to your JDK 21 installation, then run:

```sh
export PATH="$JAVA_HOME/bin:$PATH"
java -version
javac -version
sh ./mvnw -version
sh ./mvnw clean compile
sh ./mvnw test
sh ./mvnw javafx:run
```

If Maven 3.9.11 is installed, the equivalent commands are `mvn clean compile`,
`mvn test`, and `mvn javafx:run`.

Expected result: a dark window with "Shadow Quest: The Lost Realm" centered in
light text. Press **Enter** to start the movement demo. Hold **WASD or arrow keys**
to move the circle, including diagonally; release to stop. Click **Back to title**
to leave. Re-entering starts a fresh player. Space, E, and Esc are reserved inputs
with no action yet. There is no camera or collision boundary, so the circle can
leave the viewport; move back or restart from the title. Close
the window using its title-bar close button; Maven should report `BUILD SUCCESS`.

`test` runs JUnit 5 tests for game transitions, player initialization, movement,
health, and invalid inputs without starting JavaFX. JUnit is a test-only
dependency. Reports are written to `target/surefire-reports/`.

## Project structure

```text
.
|-- pom.xml                         Java 21 compilation and JavaFX run support
|-- mvnw / mvnw.cmd                 Maven Wrapper launchers
|-- .mvn/wrapper/                   Wrapper version and Maven download settings
|-- .github/PULL_REQUEST_TEMPLATE.md
|-- docs/                          Plans, current roadmap, architecture, learning report
|-- src/main/java/com/shadowquest/
|   |-- app/Main.java               JavaFX entry point
|   |-- core/GameConfig.java        Title and initial viewport dimensions
|   |-- core/GameState.java         Session state enum
|   |-- core/Game.java              Session transitions and death detection
|   |-- core/FrameClock.java        Frame timestamps to elapsed seconds
|   |-- core/GameLoop.java          JavaFX update/render scheduling
|   |-- ui/ScreenManager.java       Stage and screen presentation
|   |-- ui/MainMenuScreen.java      Minimal launch-screen layout
|   |-- ui/GameScreen.java          Temporary Canvas player and return button
|   |-- input/InputHandler.java     Held movement/action keys
|   |-- player/Character.java      Shared stats, health, and position
|   |-- player/Player.java         Player initialization and movement
|   |-- enemy/                     Reserved for enemies and combat
|   |-- items/                     Reserved for items and inventory
|   |-- levels/                    Reserved for levels and progression
|   |-- render/                    Reserved for Canvas rendering and assets
|   |-- persistence/               Reserved for save/load
|   `-- utils/Direction.java       Four world directions
|-- src/main/resources/
|   |-- styles/launch.css          Launch-screen styling
|   |-- images/                    Reserved image assets
|   |-- maps/                      Reserved map resources
|   `-- audio/                     Reserved audio assets
`-- src/test/java/com/shadowquest/
    |-- core/GameTest.java
    |-- player/CharacterTest.java
    |-- player/PlayerTest.java
    |-- enemy/
    |-- items/
    |-- levels/
    `-- persistence/
```

Reserved directories contain `.gitkeep` files so Git preserves the agreed
structure. Existing markers may remain as packages receive implementations.
Maven writes generated output under `target/`, which is ignored by Git.

`Main` starts JavaFX and delegates to `ScreenManager`. The screen manager
creates the scene and displays `MainMenuScreen`. The screen reads shared
window settings from `GameConfig` and loads CSS from the classpath.
Future domain packages must remain independent of JavaFX, and the renderer
must only display domain state.

## Troubleshooting

- If Java fails to start or Maven reports the wrong Java version, correct
  `JAVA_HOME` and `PATH` in the same terminal, then check `mvnw -version` again.
- If compilation reports an unsupported release, use JDK 21.
- If dependency downloads fail, check network/proxy access to Maven Central.
- If a window cannot open, run from a graphical desktop rather than a headless
  session.
- If JavaFX reports "An Application Control policy has blocked this file",
  ask the device administrator to review the JavaFX native libraries or verify
  the application on a permitted development machine.
- In VS Code, open the repository folder and select JDK 21 for this project.
  Keep personal editor configuration out of commits.

See [CONTRIBUTING.md](CONTRIBUTING.md) for the fork and pull request workflow.
No external assets are included in this baseline.

See [the initialization report](docs/INITIALIZATION.md) for the file inventory,
build results, and the environment issue that blocked local launch verification.
See [the implementation log](docs/IMPLEMENTATION_LOG.md) for the current Phase 1
APIs, transition rules, test results, and integration limitations.
See [the project development report](docs/PROJECT_DEVELOPMENT_REPORT.md) for the
permanent development record, class explanations, and pre-push audit.

Build references: [OpenJFX Maven setup](https://openjfx.io/openjfx-docs/),
[JavaFX 21 release notes](https://gluonhq.com/products/javafx/openjfx-21-release-notes/),
and [Apache Maven Wrapper](https://maven.apache.org/tools/wrapper/).
