# Definition of Done – Sprint 1

A task is only considered done when all the criteria that apply to it are met. The assignee checks them before moving the Issue to Done, and the reviewer confirms them during the review.

## All tasks
- [ ] The work is linked to a GitHub Issue with an assignee, labels and the Sprint 1 milestone.
- [ ] The work was done in its own branch, following the naming convention in [workflow.md](workflow.md).
- [ ] A Pull Request was opened with `Closes #<issue>` in the description.
- [ ] The Pull Request was reviewed and approved by another team member.
- [ ] All review comments were answered or resolved.
- [ ] The Pull Request was merged into `main` and the branch was deleted.
- [ ] The Issue is closed and its card is in the Done column.

## Documentation tasks
- [ ] The document is in the `docs/` folder and written in English.
- [ ] Markdown (tables, lists, Mermaid diagrams) renders correctly on GitHub.
- [ ] The document is consistent with the other documents (use cases, domain model, architecture).
- [ ] The README links to the document when relevant.

## Code tasks
- [ ] The code follows [coding-conventions.md](coding-conventions.md).
- [ ] The backend builds without errors (`./mvnw clean package`, or `.\mvnw.cmd clean package` on Windows).
- [ ] The frontend builds without errors (`npm run build`).
- [ ] The application starts and the changed feature works locally.
- [ ] Existing tests pass, and new business rules have at least one unit test.
- [ ] No unused code, commented-out code or debug prints.
- [ ] No passwords or secrets committed.
- [ ] The README setup instructions were updated if new steps are needed.

## Evolution
This Definition of Done applies to Sprint 1. It will be reviewed at the end of each sprint and is expected to become stricter (for example, test coverage targets or automated checks in CI).
