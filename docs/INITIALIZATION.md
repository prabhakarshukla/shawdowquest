# Initialization report

Verified on November 11, 2026, on Windows 11 x64. Work is limited to Phase 0.
The existing workspace was empty and had no Git repository or project files.
No GitHub repository, commit, or remote has been created.

## Environment and verification

The installed Oracle JDK is Java 25. Its executables work when called directly,
but the Oracle `javapath` shortcuts on `PATH` failed with exit code -1073740791.
`JAVA_HOME` was unset, and `mvn` was not available on `PATH`.

For verification, a portable Microsoft OpenJDK 21.0.13.0.2+1 was downloaded
from Microsoft's official distribution and checked against its SHA-256 checksum.
It is under `.tools/jdk21/jdk-21.0.13.0.2+1`. The official Maven Wrapper 3.3.4
was checked against Maven Central's published checksum and copied into the
project. The wrapper downloaded Maven 3.9.11.

All verification-specific JDK, Maven distribution, dependency cache, and
download files are under the ignored `.tools/` directory. System Java settings
were not changed. The JavaFX launch also extracted its normal native-library
cache under `%USERPROFILE%\.openjfx\cache\21.0.13+2\amd64`.

| Check | Result |
| --- | --- |
| `java -version` with the portable JDK | OpenJDK 21.0.13.0.2 |
| `javac -version` with the portable JDK | javac 21.0.13.0.2 |
| `mvnw.cmd -version` | Maven 3.9.11 using the portable JDK 21 |
| Maven `clean compile test` | BUILD SUCCESS; four Java source files compiled with release 21 |
| Available tests | No automated test classes exist in this initialization; zero tests executed |
| Maven `javafx:run` | Attempted; blocked by Windows Application Control before a window opened |
| Supplied planning documents | SHA-256 hashes match the copies in `docs/` byte for byte |

The launch failed while loading both `prism_d3d.dll` and `prism_sw.dll`, with
`java.lang.UnsatisfiedLinkError` and the message:

```text
An Application Control policy has blocked this file
```

JavaFX then reported `Error initializing QuantumRenderer: no suitable pipeline
found`. This is a host policy blocker. Launch, visual layout, resizing, and
normal window-close behavior remain unverified. Ask the device administrator
to review the libraries or rerun on a permitted development machine. No
security policy was changed or bypassed.

Commands used from the repository root in PowerShell:

```powershell
$env:JAVA_HOME = (Resolve-Path '.tools/jdk21/jdk-21.0.13.0.2+1').Path
$env:Path = "$env:JAVA_HOME\bin;$env:Path"
$env:MAVEN_USER_HOME = (Resolve-Path '.tools').Path + '\maven-user-home'
java -version
javac -version
.\mvnw.cmd -version
.\mvnw.cmd -B -ntp '-Dmaven.repo.local=.tools/repository' clean compile test
.\mvnw.cmd -B -ntp '-Dmaven.repo.local=.tools/repository' javafx:run
```

These paths are specific to this verification workspace. Team members should
follow the portable setup instructions in the root README with their own JDK 21.
The Phase 0 requirement that all five members can run the application still
needs confirmation on their machines.

## Created project files

| File | Purpose |
| --- | --- |
| `pom.xml` | Java release 21, UTF-8, JavaFX 21.0.13 controls dependency, compiler, test runner, and JavaFX Maven run configuration |
| `mvnw` | Official Maven Wrapper launcher for macOS/Linux |
| `mvnw.cmd` | Official Maven Wrapper launcher for Windows |
| `.mvn/wrapper/maven-wrapper.properties` | Pins Wrapper 3.3.4 and Maven 3.9.11 |
| `.gitignore` | Excludes generated Java/Maven output, local tools, VS Code/IDE state, OS files, and local secrets |
| `README.md` | Requirements, setup, build/run commands, structure, and troubleshooting |
| `CONTRIBUTING.md` | Five-member ownership and fork, feature branch, review, and pull request workflow |
| `.github/PULL_REQUEST_TEMPLATE.md` | Prompts for change description, verification, and integration impact |
| `docs/PROJECT_PLAN.md` | Unmodified copy of the supplied project plan |
| `docs/ARCHITECTURE.md` | Unmodified copy of the supplied architecture |
| `docs/INITIALIZATION.md` | This verification report and complete project-file inventory |
| `src/main/java/com/shadowquest/app/Main.java` | JavaFX application entry point; delegates screen presentation |
| `src/main/java/com/shadowquest/core/GameConfig.java` | Shared title and initial 960 x 600 viewport settings |
| `src/main/java/com/shadowquest/ui/ScreenManager.java` | Creates the initial scene and displays it on the primary stage |
| `src/main/java/com/shadowquest/ui/MainMenuScreen.java` | Minimal title-screen layout, with future menu actions deferred |
| `src/main/resources/styles/launch.css` | Dark background and readable centered title styling |

Each file below is an empty `.gitkeep` that lets Git preserve an agreed future
package, resource, or test directory. None defines a class or method.

```text
src/main/java/com/shadowquest/input/.gitkeep
src/main/java/com/shadowquest/player/.gitkeep
src/main/java/com/shadowquest/enemy/.gitkeep
src/main/java/com/shadowquest/items/.gitkeep
src/main/java/com/shadowquest/levels/.gitkeep
src/main/java/com/shadowquest/render/.gitkeep
src/main/java/com/shadowquest/persistence/.gitkeep
src/main/java/com/shadowquest/utils/.gitkeep
src/main/resources/images/.gitkeep
src/main/resources/maps/.gitkeep
src/main/resources/audio/.gitkeep
src/test/java/com/shadowquest/player/.gitkeep
src/test/java/com/shadowquest/enemy/.gitkeep
src/test/java/com/shadowquest/items/.gitkeep
src/test/java/com/shadowquest/levels/.gitkeep
src/test/java/com/shadowquest/persistence/.gitkeep
```

Generated `target/` files and downloaded `.tools/` contents are local artifacts,
not project source files. The source tree follows the architecture's package
names. UI layout is in `ui`, startup is in `app`, and shared configuration is
in `core`. Future domain, input, rendering, and persistence packages remain
reserved without introducing gameplay or changing the proposed contracts.
