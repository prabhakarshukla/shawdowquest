# Implementation log

## November 13, 2026: continuation verification

Reviewed the requested core-foundation scope and found no unfinished implementation
tasks. Re-ran the existing Maven Wrapper with the portable JDK 21 and cached
dependencies using `-o -B -ntp -Dmaven.repo.local=.tools/repository clean compile test`.
Clean compilation passed and all 63 tests passed again: GameTest 14,
CharacterTest 29, PlayerTest 20, with zero failures, errors, or skips.
Only this log was updated during the continuation; no next-phase code was added.
JavaFX launch was not retried. The previously observed Windows Application
Control blocker remains unresolved, so GUI launch is still unverified.

## November 11, 2026: Phase 1 core game foundation

Completed the core-foundation scope explicitly requested for this task:
game states, directions, a session controller, shared character data and health,
player movement, and domain tests. This is a subset of the broader Phase 1 in
PROJECT_PLAN.md. Enemies, inventory, combat, levels, input, and rendering are
still deferred.

The project plan, architecture, existing JavaFX startup, screen classes,
GameConfig, and CSS were preserved. Existing constructors and methods were not
changed. No new domain API is wired into the title screen yet.

## File inventory

All paths below are relative to the repository root, currently `C:\java game`.

| Created file | Responsibility and important members |
| --- | --- |
| `src/main/java/com/shadowquest/core/GameState.java` | Enum containing MENU, PLAYING, PAUSED, GAME_OVER, and VICTORY; used by Game |
| `src/main/java/com/shadowquest/utils/Direction.java` | Shared UP, DOWN, LEFT, RIGHT enum; used by Player.move |
| `src/main/java/com/shadowquest/core/Game.java` | Owns private state and player references; starts sessions, pauses/resumes, and detects defeat on update |
| `src/main/java/com/shadowquest/player/Character.java` | Abstract base class with private name, health, maxHealth, attackPower, defense, x, and y; owns validation, health changes, and protected movement |
| `src/main/java/com/shadowquest/player/Player.java` | Extends Character; supplies initial stats and speed; translates Direction plus elapsed seconds into movement |
| `src/test/java/com/shadowquest/core/GameTest.java` | Tests session transitions, invalid commands/time, pause, defeat, restart, and independent sessions |
| `src/test/java/com/shadowquest/player/CharacterTest.java` | Tests shared constructor validation, health bounds, death, healing, and coordinate safety through a small concrete test subclass |
| `src/test/java/com/shadowquest/player/PlayerTest.java` | Tests player defaults, inherited validation, directions, time-based movement, invalid input, overflow, and death |
| `docs/IMPLEMENTATION_LOG.md` | This record of implementation, API contracts, decisions, results, and limitations |

| Modified file | Change |
| --- | --- |
| `pom.xml` | Added JUnit Jupiter 5.13.4 with test scope; existing Java 21, JavaFX 21, and Maven plugin settings retained |
| `README.md` | Updated current scope, tree, test instructions, and implementation-log link |
| `CONTRIBUTING.md` | Updated current scope and testing guidance; preserved the fork/branch/PR workflow |

No files were deleted. Existing `.gitkeep` markers remain. Maven regenerates
ignored `target/` output and caches dependencies under ignored `.tools/` during
local verification. These are not source changes.

## Current API contracts

These are the implemented Phase 1 subset and the position operations requested
for this task. The architecture's existing proposed signatures are retained
where implemented. No change to the architecture document was needed.

### Game

```java
public Game(); // Java supplies the default no-argument constructor.
public GameState getState();
public Player getPlayer();
public void startNewGame();
public void pause();
public void resume();
public void update(double deltaTime);
```

| Operation | Allowed starting state | Result |
| --- | --- | --- |
| Construct Game | No session | MENU, player is null |
| startNewGame | MENU or GAME_OVER | New Player named Arin, then PLAYING |
| pause | PLAYING | PAUSED, same player |
| resume | PAUSED | PLAYING, same player |
| update with a dead player | PLAYING | GAME_OVER |
| update with a living player | PLAYING | No state change |
| update | MENU, PAUSED, GAME_OVER, VICTORY | No domain update |

Start, pause, and resume calls outside their allowed states have no effect.
This preserves the proposed void signatures and treats inapplicable UI commands
as normal outcomes. For example, repeated Play cannot erase an active session.
There is no public setState method that could bypass these rules.

`update` always validates finite, non-negative elapsed seconds, even outside
PLAYING. Zero seconds is valid and can be used to check for defeat. It does not
run a timer, move the player, or advance levels. Damage does not directly notify
Game; defeat is detected on the next playing update. A paused session remains
paused until resume is called, then the next update detects any externally
changed health.

`getPlayer()` returns null before startNewGame. Afterwards it exposes the active
mutable Player through validated domain methods. Starting after defeat creates
a fresh instance; callers must obtain the current reference again after restart.

VICTORY is defined for compatibility with the agreed state vocabulary. There is
no victory trigger, arbitrary state setter, or return-to-menu operation in this
slice. Those require later progression and navigation work. The proposed
`getCurrentLevel()` is deferred because no Level implementation exists yet.

### Character

```java
protected Character(String name, double maxHealth, int attackPower, int defense);
public String getName();
public double getHealth();
public double getMaxHealth();
public int getAttackPower();
public int getDefense();
public double getX();
public double getY();
public boolean isAlive();
public void takeDamage(double amount);
public void heal(double amount);
protected final void moveBy(double deltaX, double deltaY);
```

- All fields are private. Name, maximum health, attack power, and defense are
  final, so these values cannot change after construction.
- The constructor rejects null/blank names, non-positive or non-finite maximum
  health, and negative attack or defense. It strips surrounding name whitespace,
  starts at full health, and starts at coordinates `(0, 0)`.
- `takeDamage` applies the given amount directly and clamps health at zero.
  Defense is stored but does not reduce damage yet. No attack system exists.
- `heal` limits restoration to the remaining health capacity before adding it.
  This prevents both exceeding maximum health and floating-point overflow.
- Damage and healing accept zero, reject negative/non-finite values, and do not
  mutate health on invalid input. `isAlive` means health is greater than zero.
- Healing from zero is permitted by this health primitive. It changes health
  only, and cannot change GAME_OVER back to PLAYING. Revive/item rules are not
  implemented.
- `moveBy` lets subclasses request displacement without exposing public setters.
  Both resulting coordinates must be finite before either field is assigned.
  It is final so subclasses cannot override this validation step.
- `Character` is abstract and cannot be instantiated directly. It currently
  contains implemented shared behavior, with no abstract methods. Java permits
  this. The proposed `attack` and per-character `update` methods are deferred
  rather than introducing empty implementations for excluded features.

### Player and Direction

```java
public Player(String name);
public double getSpeed();
public void move(Direction direction, double deltaTime);
```

Player inherits all public Character methods. Its initial tuning values are
maximum health 100, attack power 10, defense 5, speed 200 world units per second,
and position `(0, 0)`. These values are defined once in Player as named constants;
they are starter values, not a combat balance specification.

Movement uses `distance = speed * deltaTime`, with elapsed time in seconds:

| Direction | Position change |
| --- | --- |
| UP | Decrease Y |
| DOWN | Increase Y |
| LEFT | Decrease X |
| RIGHT | Increase X |

This follows the architecture's top-left origin convention. Signed finite
coordinates are permitted; level bounds and collision handling do not exist yet.
For example, moving RIGHT for 0.25 seconds at speed 200 changes X by 50.

Null direction, negative/non-finite time, multiplication overflow, and coordinate
overflow are rejected with IllegalArgumentException. Zero time produces no
displacement. Dead players do not move, but invalid arguments are still rejected.

Inventory, collection, item use, weapons, and combat methods remain deferred.
Player has no Game or JavaFX dependency. Future input/controller integration
must call move only while PLAYING; calling Player directly bypasses session
pause policy by design, not its input validation.

## Java concepts demonstrated

- Classes and objects: each Game and Player object holds its own state.
- Encapsulation: private fields are accessed through getters and validated
  operations rather than public mutable fields.
- Inheritance: Player extends Character and reuses its health and position logic.
- Abstraction: Character provides a shared model and cannot be constructed alone.
- Enums: GameState and Direction restrict values to named, valid alternatives.
- Constructors and super: Player delegates common initialization to Character.
- Static/final: class constants hold initial tuning values; final instance fields
  hold values that remain fixed after construction.
- Exceptions: invalid arguments produce IllegalArgumentException. Ordinary death
  and inapplicable commands do not throw. Tests use assertThrows to verify errors;
  no recoverable I/O or try/catch workflow is introduced in this phase.
- Packages separate session coordination, character rules, and shared directions.
- JUnit annotations identify normal and parameterized tests. Parameterized tests
  run the same check with several inputs. Test lambdas pass an operation to
  assertThrows without executing it beforehand.

Runtime polymorphism through overridden combat behavior, collections, interfaces,
file persistence, and event handling are not implemented in this phase. Tests use
Character references to a concrete test subclass, but do not claim this as an
implementation of the planned enemy polymorphism.

## Execution example

The existing application still follows Main -> ScreenManager -> MainMenuScreen.
No keyboard or screen action starts the new domain model yet. Tests or future
integration code can use the model as follows:

```java
Game game = new Game();                 // MENU, no player yet
game.startNewGame();                    // PLAYING, new Arin at (0, 0)
Player arin = game.getPlayer();
arin.move(Direction.RIGHT, 0.25);        // X = 50, Y = 0
arin.takeDamage(30);                    // Health = 70
arin.heal(10);                          // Health = 80
game.pause();                          // PAUSED
game.update(0.016);                     // No game rules run while paused
game.resume();                         // PLAYING
arin.takeDamage(1000);                  // Health = 0, isAlive() is false
game.update(0.016);                     // GAME_OVER
game.startNewGame();                    // New full-health Arin at (0, 0)
```

## Verification

Used the portable Microsoft OpenJDK 21.0.13.0.2+1 and Maven Wrapper 3.9.11 already
present in the workspace. The initial sandboxed build could not resolve Maven
Central DNS. Re-running with approved network access downloaded JUnit and passed.

```powershell
$env:JAVA_HOME = (Resolve-Path '.tools/jdk21/jdk-21.0.13.0.2+1').Path
$env:Path = "$env:JAVA_HOME\bin;$env:Path"
$env:MAVEN_USER_HOME = (Resolve-Path '.tools').Path + '\maven-user-home'
.\mvnw.cmd -B -ntp '-Dmaven.repo.local=.tools/repository' clean compile
.\mvnw.cmd -B -ntp '-Dmaven.repo.local=.tools/repository' test
```

| Check | Result |
| --- | --- |
| Clean compilation | BUILD SUCCESS; nine production source files compiled for Java 21 |
| GameTest | 14 passed |
| CharacterTest | 29 passed |
| PlayerTest | 20 passed |
| Total | 63 tests; 0 failures, 0 errors, 0 skipped |
| Domain dependency inspection | No JavaFX, Swing, or AWT imports in core, player, or utils |
| JavaFX launch | Not reattempted in this phase; prior Windows Application Control blocker remains unresolved |

The launch blocker recorded in INITIALIZATION.md prevents loading JavaFX's
`prism_d3d.dll` and `prism_sw.dll`. Successful compilation and domain tests do
not prove the GUI launched. No security settings were changed. Visual layout,
native-library loading, and UI integration remain unverified.

## Team integration and next step

Member 3 owns Character, Player, and their tests under the project plan.
Member 1 coordinates Game, GameState, and integration tests as the project lead;
the architecture leaves detailed core ownership for team agreement. Direction is
a shared contract to coordinate across Members 1, 3, and 5.

Member 2 will read positions and state for rendering. Member 4 can later use the
health methods for items after agreeing on item and healing rules. Member 5 will
use positions and session state for progression and persistence. None should
duplicate health fields, add JavaFX imports to domain code, or write arbitrary
state/position setters.

Coordinate changes to method signatures, nullability, units, direction signs,
state-transition rules, health behavior, and default stats with the affected
owners. Existing architecture methods for excluded modules remain proposals
for later work, not callable stubs.

Suggested next task, pending explicit approval: agree on the input/controller
boundary, then add keyboard movement and a placeholder Canvas player with pause
gating. Resolve the JavaFX launch environment first so that interaction can be
verified. No part of that next task was implemented here.
