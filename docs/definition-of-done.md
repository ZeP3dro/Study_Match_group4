# Definition of Done

A task is only considered done when all the criteria that apply to it are met. The assignee checks them before moving the Issue to Done, and the reviewer confirms them during the review.

The Sprint 1 criteria still apply. Sprint 2 adds the criteria at the end, which follow the Sprint 2 brief (working full-stack features, automated tests and a reproducible build).

## All tasks
- [ ] The work is linked to a GitHub Issue with an assignee, labels and the milestone of the current sprint.
- [ ] The work was done in its own branch, following the naming convention in [workflow.md](workflow.md).
- [ ] A Pull Request was opened with `Closes #<issue>` in the description, or `Refs #<issue>` when some acceptance criteria are still open.
- [ ] The Pull Request was reviewed and approved by another team member.
- [ ] All review comments were answered or resolved.
- [ ] The Pull Request was merged into `main`. The branch is kept, as described in [workflow.md](workflow.md), and receives no new commits.
- [ ] The Issue is closed and its card is in the Done column.

## Documentation tasks
- [ ] The document is in the `docs/` folder and written in English.
- [ ] Markdown (tables, lists, Mermaid diagrams) renders correctly on GitHub.
- [ ] The document is consistent with the other documents (use cases, domain model, architecture).
- [ ] The README links to the document when relevant.

## Code tasks
- [ ] The code follows [coding-conventions.md](coding-conventions.md).
- [ ] The backend builds and the tests pass (`./mvnw clean test`, or `.\mvnw.cmd clean test` on Windows).
- [ ] The frontend builds without errors (`npm run build`).
- [ ] The application starts and the changed feature works locally.
- [ ] No unused code, commented-out code or debug prints.
- [ ] No passwords or secrets committed.
- [ ] The README setup instructions were updated if new steps are needed.

## Added in Sprint 2
- [ ] Each new business rule has at least one JUnit 5 test of its behaviour, not of getters and setters.
- [ ] The tests cover a normal case, an invalid case and the boundary values of the rule (e.g. grade 0, 9.9, 10 and 20).
- [ ] Profile and cold-start rules are covered by tests, including the change from cold-start values to academic evidence.
- [ ] The feature works end to end: frontend → API → business logic → database.
- [ ] Every new endpoint is limited to the roles that need it, and this was checked with each role concerned.
- [ ] Invalid input shows a clear message on the screen.
- [ ] The Pull Request description says what was tested and how, and what was not checked.

## Evolution
This Definition of Done was defined in Sprint 1 and extended in Sprint 2. It will be reviewed at the end of each sprint and is expected to become stricter (for example, test coverage targets or automated checks in CI).
