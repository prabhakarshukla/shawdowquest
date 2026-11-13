# Shadow Quest: The Lost Realm — Project Plan

> **Project type:** Group Java class project  
> **Team size:** 5 members  
> **Language:** Java  
> **GUI:** JavaFX  
> **Build tool:** Maven  
> **Rendering approach:** JavaFX Canvas  
> **Repository:** `shadow-quest-lost-realm`

---

## 1. Project Overview

**Shadow Quest: The Lost Realm** is a 2D adventure game developed in Java. The player controls Arin, a young warrior trapped in the ancient realm of Eldoria after it has been corrupted by the Shadow Lord.

The player must explore four areas, defeat enemies, collect useful items, unlock routes, and ultimately defeat the Shadow Lord to escape the realm.

The project must be both:

1. A playable game with a coherent beginning, progression, and ending.
2. A practical demonstration of Java concepts taught in the course.

The team should prioritize a stable, playable core game over ambitious optional features.

## 2. Story and Level Sequence

1. **Mystic Forest** — Introduces movement and basic combat. Enemies include goblins and wolves. The player finds an ancient key.
2. **Ancient Ruins** — Introduces stronger enemies, including skeletons and dark archers. The player obtains a more powerful weapon.
3. **Shadow Dungeon** — Contains stronger enemies, obstacles, and a mini-boss.
4. **Shadow Castle** — The final area, where Arin faces the Shadow Lord.

The story, protagonist, four locations, and final objective must remain consistent throughout development.

## 3. Core Gameplay

### Must-have gameplay

- Main menu with Play, Help, and Exit.
- Player movement using WASD and arrow keys.
- Basic player animation or sprite-state changes.
- Player health and damage.
- Basic attack using Space.
- At least three enemy types with different behaviour or stats.
- Collision detection between the player, enemies, walls, and objects.
- Collectible items and coins.
- A basic inventory.
- Keys and locked progression between levels.
- Four sequential levels.
- A final boss encounter.
- Pause, game-over, and victory screens.
- Clear feedback for damage, collection, level completion, and defeat.

### Should-have gameplay

- Multiple weapon types.
- Health potions and usable inventory items.
- Enemy movement or simple detection/follow behaviour.
- Save and load progress.
- Basic sound effects.
- Simple puzzles or interactable obstacles.

### Optional features

- XP and player-level progression.
- Advanced enemy AI.
- Achievements.
- Additional weapons and enemy types.
- More elaborate animations and visual effects.

Optional features must not delay the completion of the must-have scope.

## 4. Required Java Concepts

The following concepts must be visible in the actual source code and, where useful, demonstrated during the project presentation.

| Java concept | Planned implementation |
|---|---|
| Classes and objects | Player, Enemy, Weapon, Item, Inventory, Level, Game |
| Encapsulation | Private fields and validated methods for health, coins, inventory, and game state |
| Inheritance | Goblin, Orc, DarkArcher, ShadowLord extend Enemy |
| Abstraction | Abstract Character class for shared character properties and behaviour |
| Polymorphism | Enemy references invoke the correct subclass behaviour |
| Method overriding | Enemy subclasses override attack/update or other suitable behaviour |
| Method overloading | Meaningful overloads such as attack(target) and attack(target, weapon); avoid artificial overloads |
| Interfaces | Attackable and Collectable where the contract is genuinely shared |
| Constructors | Initialize game entities with valid starting state |
| Collections | ArrayList for enemies and inventory; Set for unique keys where appropriate |
| Exception handling | Handle file I/O, malformed save data, and missing essential resources |
| File handling | Save/load player progress if time permits; otherwise demonstrate a smaller reliable persistence feature |
| Enum | GameState and other finite state categories |
| Static/final | Shared immutable constants and configuration |
| Packages | Separate app, core, player, enemy, items, levels, ui, and utils |
| Event handling | JavaFX keyboard and button event handlers |
| Lambda expressions | Concise JavaFX event handlers where appropriate |
| GUI and animation | JavaFX Scene, Canvas, and AnimationTimer |

Do not use exceptions for normal game outcomes such as an enemy being defeated or the player losing health.

## 5. Five-Member Work Distribution

Each member owns a module and its tests/documentation. Shared contracts must be agreed before parallel implementation.

### Member 1 — Project Lead, UI and Integration (You)

**Owns:**
- Application entry point and JavaFX screen flow.
- Main menu, help screen, pause screen, HUD, game-over screen, victory screen.
- Keyboard and UI event wiring.
- Canvas rendering integration.
- GitHub repository maintenance, integration, and release checks.

**Does not own:** All gameplay logic. Gameplay modules remain with their respective owners.

### Member 2 — Visuals and Rendering Support

**Owns:**
- Sprite and background asset selection/creation.
- Tile/map visual assets and animation frames.
- Rendering helpers or renderer implementation in coordination with Member 1.
- Asset attribution and resource-folder organization.
- Visual consistency and asset-loading tests.

**Boundary:** Must not independently change core game rules or player/enemy APIs. Coordinate any renderer-facing data requirements with the project lead.

### Member 3 — Player and Combat

**Owns:**
- Abstract Character and Player.
- Enemy base class and enemy subclasses.
- Attack/damage/defence/health behaviour.
- Combat rules and enemy behaviour.
- Unit tests for combat and character state.

**Boundary:** Must not directly create JavaFX screens or manipulate the JavaFX Stage.

### Member 4 — Items and Inventory

**Owns:**
- Item hierarchy, Weapon, HealthPotion, Key.
- Inventory and collection behaviour.
- Coin/reward handling.
- Collectable and other relevant interfaces.
- Unit tests for inventory and item behaviour.

**Boundary:** Inventory and item logic must remain independent of JavaFX UI classes.

### Member 5 — Levels, Progression and Persistence

**Owns:**
- Level model and the four level definitions.
- Level transitions, doors, keys, obstacles, and puzzle rules.
- Save/load service and save-data validation.
- Custom save exception if genuinely useful.
- Tests for progression and persistence.

**Boundary:** Must use agreed player/inventory interfaces and must not duplicate their logic.

### Shared responsibilities

- Every member reviews and tests their own changes.
- Every member writes a short README or usage note for their module.
- Every member contributes commits and pull requests.
- All members help test the integrated game.
- Project lead coordinates integration; this does not make the lead the owner of every module.

## 6. Development Phases

### Phase 0 — Repository and architecture baseline

- Confirm Java/JDK and Maven versions.
- Add Maven project configuration and JavaFX dependency.
- Add package structure and application entry point.
- Add README, `.gitignore`, and contribution instructions.
- Confirm the project launches on all team members' machines.

**Exit condition:** Every member can clone the repository and run the starter application.

### Phase 1 — Contracts and independent modules

- Agree on class responsibilities and method signatures.
- Implement Player/Enemy/Combat, Items/Inventory, and Level/Progression models.
- Create small unit tests for each module.
- Keep the JavaFX UI as a simple placeholder during this phase.

**Exit condition:** Modules compile and can be tested without launching the complete game.

### Phase 2 — First playable prototype

- Render a basic map and player.
- Implement movement and collision.
- Add one enemy and working combat.
- Add one collectible and inventory interaction.
- Add health display and pause/resume.

**Exit condition:** A player can move, fight, take damage, collect an item, and restart.

### Phase 3 — Complete game progression

- Build all four levels.
- Add keys, doors, level transitions, and the mini-boss.
- Add the Shadow Lord final encounter.
- Add game-over and victory flows.

**Exit condition:** The game can be completed from the first level to the final victory screen.

### Phase 4 — Persistence and polish

- Add save/load if stable and feasible.
- Validate malformed or missing save files.
- Improve sprites, animations, feedback, and sound.
- Fix bugs and test edge cases.

**Exit condition:** No known critical issue blocks the normal game flow.

### Phase 5 — Submission readiness

- Test on a clean clone.
- Verify all required Java concepts in source code.
- Update README, controls, setup steps, screenshots, and team contributions.
- Prepare a short demonstration script and project explanation.

**Exit condition:** Another student can clone, run, play, and understand the project using the README.

## 7. GitHub Collaboration Rules

Use a fork-and-pull-request workflow.

1. The project lead owns the main repository.
2. Each teammate forks the repository.
3. Each teammate creates a feature branch in their fork.
4. Work is committed in small, meaningful commits.
5. The teammate opens a pull request against the main repository's `main` branch.
6. The project lead or another assigned reviewer checks the code and runs the relevant tests.
7. Merge only when the change builds and does not break the agreed contracts.

Suggested branch names:

- `feature/javafx-ui`
- `feature/rendering-assets`
- `feature/player-combat`
- `feature/inventory-items`
- `feature/levels-save-system`

Rules:

- Never force-push to the shared `main` branch.
- Do not commit IDE-specific files, `target/`, or generated output.
- Do not commit private credentials or personal data.
- Do not add unlicensed or unattributed game assets.
- Keep pull requests focused; avoid changing unrelated modules.
- Pull/rebase the latest upstream `main` before starting or updating a feature branch.
- Resolve conflicts with the owners of the affected modules.

## 8. Definition of Done

A feature is done only when:

- It compiles with the project's configured Java version.
- It follows the agreed package and API boundaries.
- It has been tested, manually or with automated tests as appropriate.
- It handles invalid input and edge cases where relevant.
- It does not break the existing game.
- Its public classes and important methods are documented.
- Its pull request explains what changed and how it was tested.

## 9. Scope and Risk Control

- Build one complete playable level before expanding to four.
- Use placeholder shapes or freely licensed assets until gameplay works.
- Keep game rules independent of JavaFX rendering.
- Do not introduce a database, networking, or a game engine unless the team explicitly approves a real need.
- Keep save/load simple; JSON or a clearly documented text format is sufficient.
- Avoid adding optional features until the end-to-end game is playable.
- If the semester deadline becomes tight, reduce visual complexity before removing core game progression.

## 10. Final Deliverable

A runnable JavaFX 2D adventure game with:

- A main menu and playable game screen.
- Player movement, collisions, health, and combat.
- Multiple enemy types and collectible items.
- Inventory and level progression.
- Four connected levels and a final boss.
- Pause, game-over, and victory screens.
- Demonstrable Java OOP, collections, event handling, exception handling, GUI, and file handling where implemented.
- A clear README, setup instructions, and team contribution history.
