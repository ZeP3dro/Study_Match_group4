# Use Cases – Competencies (Knowledge Areas) and Student Profile

Issue: #6. Actors and shared rules are described in [README.md](README.md).

## Competency Model
StudyMatch represents competencies through **knowledge areas**. Each course unit belongs to **at most one** knowledge area (course units such as the Internship belong to none), and a student's grades in the course units of an area show their competency in that area.

Initial knowledge areas:
| Area | Example course units |
|---|---|
| Programming | Algorithms and Programming, Object-Oriented Programming, Web Technologies Lab |
| Mathematics and Physics | Linear Algebra, Mathematical Analysis, Physics, Applied Statistics |
| Information Systems and Software Engineering | Information Systems, Requirements Engineering, Software Quality, Project Management |
| Computer Systems and Networks | Computer Architecture, Computer Networks, Operating Systems, Security |
| Data and Artificial Intelligence | Databases, Artificial Intelligence, Data Analysis Lab |

## Profile Calculation Rules
These rules are referenced by the use cases below.

- **R1 – Best grade:** for each course unit, only the student's best grade is used (attempts and credited grades).
- **R2 – Failed course units:** the best grade is used even if it is below 10. A course unit the student never passed still counts with its best grade (e.g. 8 and then 9 → 9 is used).
- **R3 – Weighted averages:** area averages and the overall university average are ECTS-weighted.
- **R4 – Course units without area:** count for the overall university average but not for any area average.
- **R5 – Peer-review results:** averages per criterion are calculated when the profile is viewed, from the reviews submitted in UC22; they are not stored in the profile.

---

### UC12 – Manage knowledge areas

**Objective:** Create, edit and delete the knowledge areas used to group course units and represent student competencies.

**Primary actor(s):** Academic Administrator

**Main success scenario (create):**
1. The administrator chooses to create a knowledge area.
2. The administrator enters the name and a short description.
3. The system validates the data.
4. The system saves the knowledge area.

**Edit:**
1. The administrator selects an existing area and changes its name or description.
2. The system validates the data with the same rules as creation.
3. The system saves the changes.

**Delete:**
1. The administrator selects an existing area and chooses to delete it.
2. The system checks that no course unit belongs to the area.
3. The system asks for confirmation and deletes the area.

**Alternative / exceptional flows:**
- Create 3a / Edit 2a. An area with the same name already exists: the system rejects it.
- Delete 2a. The area has course units: the system rejects the deletion and lists those course units.

**Business rules:**
- Area names are unique; the comparison ignores upper/lower case and leading or trailing spaces.
- An area with course units cannot be deleted.

**Domain concepts revealed:** KnowledgeArea

---

### UC13 – Assign knowledge area to course unit

**Objective:** Associate each course unit with the knowledge area it develops, so its grades contribute to that area.

**Primary actor(s):** Academic Administrator

**Main success scenario:**
1. The administrator selects a course unit.
2. The administrator selects a knowledge area.
3. The system saves the association.
4. The system updates the profiles of students with grades in that course unit (UC15).

**Alternative / exceptional flows:**
- 2a. The course unit already has an area: the system asks for confirmation and replaces it.
  - 2a1. The administrator cancels: the current area is kept and nothing changes.
- 2b. The administrator removes the area (e.g. Internship): the course unit no longer contributes to any area average, and the system updates the profiles of students with grades in that course unit (UC15).

**Business rules:**
- Each course unit belongs to at most one knowledge area.
- Only course units with an area can be used in grouping contexts (defined in UC17).

**Domain concepts revealed:** CourseUnit, KnowledgeArea

---

### UC14 – View student profile

**Objective:** Show a student's profile, summarising their academic performance in each knowledge area and the peer reviews received in group activities.

**Primary actor(s):** Student, Teacher

**Main success scenario:**
1. The student opens their profile.
2. The system shows, for each knowledge area, the ECTS-weighted average and the number of course units and ECTS considered (R1–R4).
3. The system shows the overall university average and the entry grade.
4. The system calculates and shows the average peer-review rating for each review criterion (Commitment, Knowledge, Communication, Reliability) and the number of reviews received (R5).
5. The system shows the anonymous comments received from group members.

**Alternative / exceptional flows:**
- 1a. A teacher opens the profile of a student enrolled in one of their course units in the current academic year: the system shows the same information.
- 1b. A teacher tries to open the profile of any other student: the system denies access.
- 2a. The student has no grades in an area: the system shows "No data" for that area.
- 3a. The student has no grades at all: the system shows "No data" for the overall average and shows the entry grade, if available.
- 3b. The student has no grades and no entry grade: the system shows "No academic data available".
- 4a. The student has received fewer than 3 peer reviews: the system shows "Not enough reviews yet" and hides ratings and comments.

**Business rules:**
- A student can only see their own profile.
- A teacher can only see profiles of students enrolled in course units they teach in the current academic year; access ends when the teaching assignment ends.
- Peer reviews are always shown anonymously: the reviewer is never identified.
- Peer-review ratings and comments are only shown after the student has received at least 3 reviews, so that reviewers cannot be easily identified in small groups.

**Domain concepts revealed:** StudentProfile, AreaScore, KnowledgeArea, WeightedAverage, EntryGrade, PeerReview, ReviewCriterion

---

### UC15 – Update student profile

**Type:** Included use case (`<<include>>`). It is not started directly by an actor; it is part of UC06, UC09, UC10, UC13 and UC16.

**Objective:** Keep the academic part of the student profile consistent with the latest academic data.

**Included by:**
- UC09 Record grade – a grade is recorded or corrected;
- UC10 Credit equivalent course unit – a grade is credited;
- UC13 Assign knowledge area to course unit – an area is assigned, replaced or removed;
- UC06 Manage course units – the ECTS of a course unit are changed;
- UC16 Register entry grade – an entry grade is registered.

**Main success scenario:**
1. One of the including use cases changes academic data.
2. The system identifies the affected students.
3. For each student, the system recalculates the values affected by the change (R1–R4):
   - grades, areas or ECTS: area averages and overall university average;
   - entry grade: only the entry grade shown in the profile.
4. The system saves the updated profile.

**Alternative / exceptional flows:**
- 3a. A new attempt has a lower grade than a previous one: the profile keeps the best grade (R1).
- 3b. A grade is corrected to a lower value and it was the best grade: the system uses the next best attempt.

**Business rules:**
- Rules R1–R4 apply.
- Peer-review results are not maintained here; they are calculated when the profile is viewed (R5).

**Domain concepts revealed:** StudentProfile, AreaScore, Attempt, WeightedAverage, EntryGrade, ECTS
