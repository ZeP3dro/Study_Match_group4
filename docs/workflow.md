Team Workflow

Development Flow
All work follows: Issue → Branch → Implementation → Pull Request → Review → Merge.
Direct commits to main are not allowed (branch protection is enabled).

Issues
Every relevant task must have a GitHub Issue before work starts.
Each Issue has: an assignee, at least one label, and the current sprint milestone.
The assignee is responsible for moving the Issue across the project board.

Project Board
| Column | Meaning |
|---|---|
| Backlog | Identified, not yet planned |
| Todo | Planned for the sprint, not started |
| In Progress | Someone is working on it |
| In Review | Pull Request open, waiting for review |
| Done | Reviewed and merged |

Labels
Type: feature, docs, bug, setup
Area: frontend, backend, database, modelling

Branch Naming
Format: type/issue-number-short-description

Examples:
feature/13-health-endpoint
docs/1-team-workflow
fix/20-login-error
setup/11-database-config


Commits

Short message in English, imperative form (e.g. "Add health endpoint").
Reference the Issue when relevant (e.g. "Add health endpoint (#13)").


Pull Requests

One Pull Request per Issue.
The description must include Closes #<issue-number>.
The author requests at least one reviewer from the team.
The author cannot approve their own Pull Request.
At least 1 approval is required before merging.


Reviews

Reviewers check correctness, clarity and consistency with the conventions.
Feedback is given through comments; the author answers or updates the code.
Reviews should be done within 24 hours whenever possible.


Merging and Branches After Merge

After approval, the author merges the Pull Request.
Merged branches are not deleted. They are kept in the repository as a
record of the work done in each Issue, so the development history can be
reviewed during the Sprint Review.
Merged branches must not receive new commits. Any further change requires
a new Issue and a new branch.
