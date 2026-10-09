# Use Cases – Cold Start and Grouping Context

Issue: #7. Actors and shared rules are described in [README.md](README.md).

## Cold-Start Strategy
When a group is formed for a course unit of area X, each student's score is calculated using only course units completed before the current semester, in this order:

1. **Area average:** ECTS-weighted average of the student's grades in area X.
2. **University average:** if there are no grades in area X, ECTS-weighted average of all course units with grades.
3. **Entry grade:** if there are no university grades, the higher-education entry grade converted to 0–20 (entry grade ÷ 10).

Examples:
- Algorithms and Programming (1st year, 1st semester): no student has university grades, so the **entry grade** is used.
- Databases (2nd year, 2nd semester): first course unit of Data and AI, so the **university average** is used.
- Web Technologies Lab (2nd year, 2nd semester): students have Programming grades, so the **area average** is used.

---

### UC16 – Register entry grade

**Objective:** Store the grade with which the student entered higher education, used when no university grades exist.

**Primary actor(s):** Academic Administrator

**Main success scenario:**
1. The administrator selects a student.
2. The administrator enters the entry grade (0–200).
3. The system validates the value.
4. The system saves the entry grade and updates the student profile (UC15).

**Alternative / exceptional flows:**
- 3a. The value is outside 0–200: the system rejects it.
- 1a. The entry grade is not available: the student is kept without entry grade and a warning is shown when groups are generated.

**Business rules:**
- The entry grade uses the 0–200 scale and is converted to 0–20 by dividing by 10.
- The entry grade is only used when the student has no university grades.

**Domain concepts revealed:** EntryGrade, Student, StudentProfile

---

### UC17 – Define grouping context

**Objective:** Describe the situation in which groups will be formed (course unit, activity and group size).

**Primary actor(s):** Teacher

**Main success scenario:**
1. The teacher selects a course unit they teach in the current academic year.
2. The teacher enters the activity name (e.g. "Practical Assignment 1").
3. The teacher defines the target group size.
4. The system validates the data.
5. The system saves the grouping context and shows the number of eligible students.

**Alternative / exceptional flows:**
- 1a. The course unit has no knowledge area: the system informs the teacher that groups cannot be formed.
- 4a. The group size is smaller than 2 or larger than the number of eligible students: the system rejects it.

**Business rules:**
- Only the teacher assigned to the course unit can define its grouping contexts.
- Eligible students are active students enrolled in the course unit in the current academic year.
- A course unit can have several grouping contexts (one per activity).

**Domain concepts revealed:** GroupingContext, Activity, GroupSize, CourseUnit, KnowledgeArea, AcademicYear

---

### UC18 – Generate groups automatically

**Objective:** Form balanced groups automatically for a grouping context.

**Primary actor(s):** Teacher

**Main success scenario:**
1. The teacher selects a grouping context and requests group generation.
2. The system obtains the eligible students.
3. For each student, the system calculates the score following the cold-start strategy (area average → university average → entry grade).
4. The system calculates the class average score.
5. The system distributes students into groups so that each group's average is as close as possible to the class average.
6. The system shows the proposed groups with each group's average.

**Alternative / exceptional flows:**
- 5a. The number of students is not a multiple of the group size: some groups have one more or one fewer member.
- 3a. A student has no university grades and no entry grade: the system excludes the student from the calculation and warns the teacher.
- 1a. Groups already exist for this context: the system asks for confirmation before replacing them.

**Business rules:**
- Only course units completed before the current semester are used in the score.
- Group sizes differ by at most one member.
- Groups are generated automatically; the teacher does not choose the members.

**Domain concepts revealed:** Group, GroupMembership, StudentScore, ScoreSource, ClassAverage, GroupingContext

---

### UC19 – Review and publish groups

**Objective:** Allow the teacher to check the generated groups and make them visible to students.

**Primary actor(s):** Teacher

**Main success scenario:**
1. The teacher opens the proposed groups of a grouping context.
2. The system shows each group, its members and its average, and the difference to the class average.
3. The teacher publishes the groups.
4. The system makes the groups visible to the students.

**Alternative / exceptional flows:**
- 3a. The teacher is not satisfied: the teacher can change the group size and generate the groups again (UC18).

**Business rules:**
- Students only see groups after they are published.
- Published groups cannot be regenerated without confirmation.

**Domain concepts revealed:** Group, GroupStatus (Proposed, Published)

---

### UC20 – View my group

**Objective:** Allow a student to see the group they were assigned to and the peer reviews they received in that group activity.

**Primary actor(s):** Student

**Main success scenario:**
1. The student opens the list of their course units.
2. The student selects a grouping context.
3. The system shows the student's group and the names of the other members.
4. After the review period closes, the system shows the average ratings per criterion and the anonymous comments the student received from the group (UC22).

**Alternative / exceptional flows:**
- 3a. The groups are not published yet: the system informs the student.
- 4a. The review period is still open: the system informs the student that reviews will be available when it closes.

**Business rules:**
- Students never see grades, averages or scores of other students, nor the group average; only teachers can see them.
- Peer reviews are shown anonymously.

**Domain concepts revealed:** Group, GroupMembership, GroupingContext, PeerReview
