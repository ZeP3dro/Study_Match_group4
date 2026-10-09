# StudyMatch – Use-Case Model (Sprint 1)

This folder contains the initial use-case model of StudyMatch.

**Scope:** StudyMatch supports a single degree programme, Computer Engineering (Engenharia Informática), with a 3-year, 180 ECTS study plan. Supporting other programmes may be considered in later sprints.

 Use cases are grouped by topic, following the capability areas defined in the Sprint 1 brief, plus additional use cases proposed by the team.

## Actors
| Actor | Description |
|---|---|
| Student | Uses StudyMatch to consult their academic information, profile and groups. |
| Academic Administrator | Manages academic services: course units, knowledge areas, students and teachers. |
| Teacher | Records grades in the course units they teach and defines the context in which groups are automatically formed. |

## Files
| File | Topic | Use cases | Issue |
|---|---|---|---|
| [01-identity-and-trajectory.md](01-identity-and-trajectory.md) | Student and academic identity, academic trajectory | UC01–UC11 | #5 |
| [02-areas-and-profile.md](02-areas-and-profile.md) | Competencies (knowledge areas) and student profile | UC12–UC15 | #6 |
| [03-cold-start-and-grouping.md](03-cold-start-and-grouping.md) | Cold start and grouping context | UC16–UC20 | #7 |
| [04-additional-use-cases.md](04-additional-use-cases.md) | Additional value-adding use cases | UC21–UC22 | #8 |

## Use-Case Index
| ID | Name | Primary actor |
|---|---|---|
| UC01 | Sign in | All users |
| UC02 | Register student | Academic Administrator |
| UC03 | View academic identity | Student |
| UC04 | Update student status | Academic Administrator |
| UC05 | Register teacher | Academic Administrator |
| UC06 | Manage course units | Academic Administrator |
| UC07 | Assign teacher to course unit | Academic Administrator |
| UC08 | Enrol student in course unit | Academic Administrator |
| UC09 | Record grade | Teacher |
| UC10 | Credit equivalent course unit | Academic Administrator |
| UC11 | View academic record | Student |
| UC12 | Manage knowledge areas | Academic Administrator |
| UC13 | Assign knowledge area to course unit | Academic Administrator |
| UC14 | View student profile | Student, Teacher |
| UC15 | Update student profile | System (triggered by UC09) |
| UC16 | Register entry grade | Academic Administrator |
| UC17 | Define grouping context | Teacher |
| UC18 | Generate groups automatically | Teacher |
| UC19 | Review and publish groups | Teacher |
| UC20 | View my group | Student |
| UC21 | Explain group formation | Teacher, Student |
| UC22 | Review group members | Student |

## Shared Business Rules
These rules apply across several use cases and were agreed by the team.

**Grades**
- Grades range from 0 to 20; a course unit is passed with 10 or more.
- A student may have several attempts at the same course unit; only the **best grade** counts.
- Failed course units also count: if a student never passed a course unit, its best grade (even below 10) is used in the averages (e.g. 8 and then 9 → 9 is used).
- Only the teacher assigned to a course unit can record its grades.
- Transferred students keep the grades of the course units the university considers equivalent (credited course units), which count like any other grade.

**Privacy**
- Students can only see their own grades and averages.
- Students never see grades or averages of other students, even within the same group; only teachers can.

**Knowledge areas (competency model)**
- Each course unit belongs to **exactly one** knowledge area, or to none (e.g. Internship).
- Initial areas: Programming; Mathematics and Physics; Information Systems and Software Engineering; Computer Systems and Networks; Data and Artificial Intelligence.

**Student score used for group formation**
For a grouping context in a course unit of area X, each student's score is calculated using only course units completed **before** the current semester, following this order:
1. **Area average:** ECTS-weighted average of the student's grades in area X.
2. **University average:** if there are no grades in area X, ECTS-weighted average of all course units with grades.
3. **Entry grade:** if there are no university grades, the higher-education entry grade, converted from 0–200 to 0–20 (divided by 10).

**Enrolment limits**
- The total ECTS of a curricular year in the study plan cannot exceed 60.
- A student can be enrolled in at most 75 ECTS per academic year and 42 ECTS per semester.

**Peer reviews**
- After a group activity, each member rates the other members from 1 to 5 in Commitment, Knowledge, Communication and Reliability, with an optional comment, and gives an overall rating to the group.
- Reviews are anonymous and become part of the reviewed student's profile.

**Group formation**
- Groups are formed automatically by the system.
- Groups are balanced: each group's average score should be as close as possible to the class average score.
- Only active students enrolled in the course unit in the current academic year are included.
