# Contributing to Shadow Quest

Read [PROJECT_PLAN.md](docs/PROJECT_PLAN.md) and
[ARCHITECTURE.md](docs/ARCHITECTURE.md) before changing code. The current scope is
the Phase 1 core foundation. Read [IMPLEMENTATION_LOG.md](docs/IMPLEMENTATION_LOG.md)
for implemented APIs and deferred work. Agree on further scope and contract
changes before extending the game.

Update [PROJECT_DEVELOPMENT_REPORT.md](docs/PROJECT_DEVELOPMENT_REPORT.md) in every
future phase with the purpose, exact changed files, method explanations, Java
concepts, test results, problems and solutions, and integration notes.

## Ownership

| Member | Responsibility |
| --- | --- |
| 1 | Application startup, UI, screen flow, integration, and repository maintenance |
| 2 | Visual assets, attribution, and rendering support with Member 1 |
| 3 | Character, player, enemies, and combat |
| 4 | Items, inventory, collection, and rewards |
| 5 | Levels, progression, and persistence |

Each member owns relevant tests and module usage notes. Coordinate changes
to shared code with affected owners. Domain classes must not depend on JavaFX.
The lead integrates modules rather than implementing everyone else's work.

## Fork and feature branch workflow

1. Fork [prabhakarshukla/shawdowquest](https://github.com/prabhakarshukla/shawdowquest)
   on GitHub.
2. Clone your fork and add the lead's repository as `upstream`. Replace the
   example YOUR_USERNAME below with your GitHub account.

   ```sh
   git clone https://github.com/YOUR_USERNAME/shawdowquest.git
   cd shawdowquest
   git remote add upstream https://github.com/prabhakarshukla/shawdowquest.git
   git remote -v
   ```

3. Start from the latest upstream `main` with a clean working tree.

   ```sh
   git fetch upstream
   git switch main
   git merge --ff-only upstream/main
   git push origin main
   git switch -c feature/javafx-ui
   ```

   Other suggested names are `feature/rendering-assets`,
   `feature/player-combat`, `feature/inventory-items`, and
   `feature/levels-save-system`.

4. Make a focused change in your module. Add tests where appropriate and
   document any public API or asset source/license. Review `git diff`, stage
   specific files, and make small, meaningful commits.

   ```sh
   git status
   git diff
   git add path/to/changed-file
   git commit -m "Describe the completed change"
   ```

5. Update your branch before submitting or updating a pull request.

   ```sh
   git fetch upstream
   git rebase upstream/main
   ```

   Resolve conflicts with affected module owners. Use `git rebase --continue`
   after staging resolved files, or `git rebase --abort` to return to the
   pre-rebase state. Never discard someone else's work to resolve a conflict.

6. Build and run the relevant checks using JDK 21.

   ```powershell
   .\mvnw.cmd clean compile
   .\mvnw.cmd test
   .\mvnw.cmd javafx:run
   ```

   On macOS/Linux, use `sh ./mvnw` instead of `.\mvnw.cmd`. JUnit 5 domain tests
   run without JavaFX. For UI changes, verify the title screen opens, resizes,
   and closes without errors. Report a host-policy launch blocker separately
   from compilation and unit-test results.

7. Push your feature branch to your fork.

   ```sh
   git push -u origin feature/javafx-ui
   ```

   If a rebase rewrites an already published branch that only you use, update
   that feature branch with `git push --force-with-lease origin feature/javafx-ui`.
   Never force-push shared `main` or rewrite a teammate's branch.

8. Open a pull request from your fork's feature branch to the lead repository's
   `main`. Explain the behavior change, verification, and any affected contracts.
   Request review from the lead or assigned reviewer. Address feedback and
   rerun relevant checks before merging.

## Review and merge rules

- Merge only changes that build with Java 21 and preserve the agreed contracts.
- Discuss API changes with affected owners before implementation. Update the
  architecture document only for an explicitly agreed change, together with
  callers and tests where practical.
- Keep commits free of `target/`, local tools, IDE settings, credentials, and
  personal data.
- Include source, author, and license for assets before committing them.
- Keep gameplay rules separate from UI and drawing code.
- Avoid unnecessary dependencies and unrelated cleanup.
- Each teammate should verify the starter on their own machine before the
  team considers the Phase 0 exit condition complete.
