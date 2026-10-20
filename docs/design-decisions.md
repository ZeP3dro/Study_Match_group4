# Design Decisions — Sprint 2

Main design decisions taken during Sprint 2. The [domain model](domain-model.md) and the [architecture](architecture.md) from Sprint 1 still apply; this document explains how the part implemented in Sprint 2 was designed and why.

Implemented in Sprint 2: sign-in and access by role, students, teachers, the study plan and knowledge areas, enrolments and grades, the student profile, the cold-start strategy and the contribution of each course unit. Group formation, peer reviews, teaching assignments and credited course units (UC07, UC10, UC17–UC22) remain future work.

## 1. Student information

| Attribute | Why it was chosen |
|---|---|
| Number (generated) | Identifies the student and is used to sign in. It comes from a counter shared with teachers (1, 2, 3…), so the administrator never has to pick a free number and two accounts can never get the same one. |
| Email = `number@studymatch.com` | Derived from the number, so it is always institutional and unique. Users can sign in with the number or the email. |
| Name | Shown in every list and profile. |
| First enrolment year | The first academic year in which the student can be enrolled. |
| Entry grade (0–200, optional) | The higher-education entry grade. It is the evidence used by the cold-start strategy when the student has no grades yet. |
| Academic status | Active, Suspended, Graduated or Dropped out. Only active students can enrol. A graduated student cannot go back to Active. |
| Account active / inactive | Whether the account can sign in. It is separate from the academic status, so a graduate keeps access to their history without being able to enrol. People are deactivated, never deleted. |
| Curricular year (derived) | Not stored: calculated from the passed ECTS (60 ECTS per year, up to the 3rd year), so it never gets out of date. |

The administrator creates, views, edits and searches students (by number). The data is stored in MySQL.

## 2. Academic history

- The history is a list of **enrolments**. Each enrolment links a student, a course unit and an academic year, and has at most one grade (0–20, one decimal place; 10 or more is a pass).
- The administrator records the grade and can correct it. The screen asks for confirmation when the new grade is lower.
- A student cannot enrol twice in the same course unit in the same academic year. A failed course unit can be repeated in a later year; a passed one cannot.
- Limits from the use cases: at most 42 ECTS per semester and 75 per academic year for a student, and 60 ECTS per curricular year in the study plan.
- Optional course units of the same **option group** are alternatives: a student enrols in only one of them, and the group counts once in the 60 ECTS limit.

**Why:** the profile only needs the best grade of each course unit, but keeping one enrolment per academic year shows the student's real path (including repetitions) and lets the administrator correct mistakes.

## 3. Student profile

| Question from the brief | Decision |
|---|---|
| Which characteristics appear | One value per **knowledge area** (Programming; Mathematics and Physics; Information Systems and Software Engineering; Computer Systems and Networks; Data and Artificial Intelligence), plus the university average and the passed ECTS. |
| How academic information contributes | Each course unit belongs to at most one area. The value of an area is the ECTS-weighted average of the best grade of each of its course units. Failed grades count. Course units without an area (e.g. Internship) count only for the university average. |
| How the profile is updated | The profile is not stored: it is calculated from the enrolments every time it is opened, so a new or corrected grade appears immediately. |
| How missing information is handled | An area without grades uses the cold-start strategy below. When there is nothing to use, the area shows "No data". A grade of 0 is a real value, not missing data. |

Students only see their own profile; the administrator can see any profile.

## 4. Cold-start strategy

For each area, the value comes from the first source available:

1. **Area average** — the student has grades in that area.
2. **University average** — no grades in the area, but grades in other course units.
3. **Entry grade ÷ 10** — no grades at all (e.g. 152 / 200 → 15.2).
4. **No data** — no grades and no entry grade.

The profile shows which source was used, and values from steps 2 and 3 are labelled as estimates.

**When academic evidence appears:** as soon as the first grade of an area is recorded, that area switches to its own average; areas still without grades use the university average instead of the entry grade. Nothing has to be recalculated or saved, because the profile is always built from the current grades.

**Why:** every student has an entry grade, so a new student gets a useful profile from day one, and real results replace it automatically.

Note on UC14: "No data" is only shown when there are no grades and no entry grade. The rule "only course units completed before the current semester" belongs to the future group-formation score, not to the profile.

## 5. Team feature and extras

- **Contribution of each course unit (team feature).** The detail page of an area lists the course units used, with academic year, ECTS, best grade and weight in the area (course unit ECTS ÷ total ECTS of the area), and shows the formula. The student can see exactly where each value comes from.
- **Search.** Students and teachers are searched by number.
- **Profile photo.** Every user can add or change their own photo (JPEG or PNG, up to 2 MB). The administrator can see it but not change it. Photos are stored in their own table, so listing students does not load images.
- **Roles and views.** Separate areas for students, the administrator and teachers; each user only sees the menu and data of their role.
- **My account.** Every user can change their password; the administrator can deactivate and reactivate student and teacher accounts.

## 6. Access and validation

- Sign-in uses a server session (cookie) and BCrypt-hashed passwords. Requests that change data carry a CSRF token.
- Each endpoint declares which roles can use it, and the backend checks it whatever the frontend shows.
- Changing the password or deactivating an account ends the sessions that were open.
- Request format (required fields, value ranges) is checked in the DTOs; business rules are checked in the domain and services. Errors are returned with a clear message shown on the screen.
- Three default accounts (administrator, teacher and student) are created on the first start, so the application can be tried right away.
