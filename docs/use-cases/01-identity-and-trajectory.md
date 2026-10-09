# Use Cases – Student and Academic Identity, Academic Trajectory

Issue: #5. Actors and shared rules are described in [README.md](README.md).

## Student and Academic Identity

### UC01 – Sign in

**Objective:** Allow a registered user to access StudyMatch with the permissions of their role.

**Primary actor(s):** Student, Teacher, Academic Administrator

**Main success scenario:**
1. The user enters their institutional email and password.
2. The system validates the credentials.
3. The system identifies the user's role.
4. The system shows the home page for that role.

**Alternative / exceptional flows:**
- 2a. Invalid credentials: the system shows an error and the user stays on the sign-in page.
- 2b. The account is inactive: the system denies access and informs the user.

**Business rules:**
- Each user account has exactly one role.
- Only institutional email addresses are accepted.

**Domain concepts revealed:** UserAccount, Role

---

### UC02 – Register student

**Objective:** Create the academic identity of a student so that their information can be used by StudyMatch.

**Primary actor(s):** Academic Administrator

**Main success scenario:**
1. The administrator chooses to register a new student.
2. The administrator enters the student number, name and institutional email.
3. The administrator enters the year of first enrolment.
4. The system validates the data.
5. The system creates the student record and the associated user account.

**Alternative / exceptional flows:**
- 4a. The student number or email already exists: the system rejects the registration.
- 5a. The student is transferred from another institution: the administrator credits the equivalent course units (UC10).

**Business rules:**
- The student number is unique.
- A newly registered student starts with the status "Active".

**Domain concepts revealed:** Student, StudentNumber, UserAccount, StudentStatus

---

### UC03 – View academic identity

**Objective:** Allow a student to check the information StudyMatch holds about them.

**Primary actor(s):** Student

**Main success scenario:**
1. The student opens their identity page.
2. The system shows the student's name, number, email, curricular year and status.

**Alternative / exceptional flows:**
- 2a. Some information is incorrect: the student is informed that corrections must be requested from the Academic Administrator.

**Business rules:**
- A student can only see their own information.
- Identity data (number, status) cannot be changed by the student.

**Domain concepts revealed:** Student, CurricularYear, StudentStatus

---

### UC04 – Update student status

**Objective:** Keep the student's situation up to date so that only eligible students are considered for automatic group formation.

**Primary actor(s):** Academic Administrator

**Main success scenario:**
1. The administrator selects a student.
2. The administrator changes the status (e.g. Active, Suspended, Graduated, Dropped out).
3. The system validates the change and saves it.
4. The system records the date of the change.

**Alternative / exceptional flows:**
- 3a. The change is not allowed (e.g. from Graduated back to Active): the system rejects it.

**Business rules:**
- Only active students are eligible for group formation.
- Status changes are recorded with their date.

**Domain concepts revealed:** Student, StudentStatus, StatusChange

---

### UC05 – Register teacher

**Objective:** Create a teacher account so the teacher can record grades and define grouping contexts.

**Primary actor(s):** Academic Administrator

**Main success scenario:**
1. The administrator chooses to register a new teacher.
2. The administrator enters the teacher's name, institutional email and department.
3. The system validates the data.
4. The system creates the teacher record and the associated user account.

**Alternative / exceptional flows:**
- 3a. The email already exists: the system rejects the registration.

**Business rules:**
- The institutional email is unique.
- A teacher can only record grades for course units they are assigned to.

**Domain concepts revealed:** Teacher, UserAccount, Department

---

## Academic Trajectory

### UC06 – Manage course units

**Objective:** Define the course units of the Computer Engineering (Engenharia Informática) study plan.

**Primary actor(s):** Academic Administrator

**Main success scenario:**
1. The administrator chooses to create a course unit.
2. The administrator enters the name, code, ECTS, curricular year, semester and type (mandatory or optional).
3. The system validates the data.
4. The system adds the course unit to the study plan.

**Alternative / exceptional flows:**
- 1a. The administrator edits an existing course unit: the same validations apply.
- 3a. The course unit code already exists: the system rejects it.
- 3b. The curricular year is not between 1 and 3: the system rejects it.
- 3c. The new course unit would make the curricular year exceed 60 ECTS: the system rejects it.

**Business rules:**
- ECTS must be a positive value.
- The total ECTS of a curricular year cannot exceed 60.
- The semester is 1 or 2.
- Optional course units belong to an option group from which the student chooses one (e.g. 3rd year, 2nd semester options); each option group counts only once towards the 60 ECTS limit.

**Domain concepts revealed:** CourseUnit, StudyPlan, CurricularYear, Semester, CourseUnitType, OptionGroup, ECTS

---

### UC07 – Assign teacher to course unit

**Objective:** Define which teacher is responsible for a course unit in an academic year, so they can record grades and define grouping contexts.

**Primary actor(s):** Academic Administrator

**Main success scenario:**
1. The administrator selects a course unit and an academic year.
2. The administrator selects one or more teachers.
3. The system saves the assignment.

**Alternative / exceptional flows:**
- 2a. The teacher is already assigned to that course unit in that academic year: the system informs the administrator.

**Business rules:**
- Assignments are valid for one academic year.
- A course unit may have more than one teacher.

**Domain concepts revealed:** TeachingAssignment, Teacher, CourseUnit, AcademicYear

---

### UC08 – Enrol student in course unit

**Objective:** Register that a student is attending a course unit in a given academic year.

**Primary actor(s):** Academic Administrator

**Main success scenario:**
1. The administrator selects a student and an academic year.
2. The administrator selects the course units to enrol the student in.
3. The system validates the enrolment.
4. The system saves the course unit enrolments.

**Alternative / exceptional flows:**
- 3a. The student has already passed or been credited the course unit: the system rejects it.
- 3b. The student is not active: the system rejects the enrolment.
- 3c. The enrolment would exceed 75 ECTS in the academic year or 42 ECTS in a semester: the system rejects it and shows the current total.

**Business rules:**
- A student cannot be enrolled twice in the same course unit in the same academic year.
- A student may enrol again in a course unit they have failed.
- In one academic year, a student can be enrolled in course units totalling at most 75 ECTS.
- In one semester, a student can be enrolled in course units totalling at most 42 ECTS.

**Domain concepts revealed:** CourseUnitEnrolment, Student, CourseUnit, AcademicYear, Semester, ECTS

---

### UC09 – Record grade

**Objective:** Register the result of a student's attempt at a course unit, so it becomes part of their academic history.

**Primary actor(s):** Teacher

**Main success scenario:**
1. The teacher selects a course unit they are assigned to.
2. The system shows the enrolled students.
3. The teacher selects the evaluation period (e.g. Regular, Resit, Special).
4. The teacher enters the grade of one or more students.
5. The system validates the grades and records the attempts.
6. The system updates the profiles of the affected students (UC15).

**Alternative / exceptional flows:**
- 1a. The teacher is not assigned to the course unit: the system denies access.
- 5a. A grade is outside the 0–20 scale: the system rejects that grade.
- 5b. The student already has a grade in the same evaluation period: the system asks the teacher to confirm the correction.

**Business rules:**
- Grades range from 0 to 20; a course unit is passed with 10 or more.
- A student may have several attempts; only the best grade counts for the profile.
- Only the teacher assigned to the course unit can record its grades.

**Domain concepts revealed:** Attempt, Grade, EvaluationPeriod, CourseUnitEnrolment, Teacher

---

### UC10 – Credit equivalent course unit

**Objective:** Record the grades of a transferred student in the course units the university considers equivalent.

**Primary actor(s):** Academic Administrator

**Main success scenario:**
1. The administrator selects a transferred student.
2. The administrator selects a course unit of the study plan.
3. The administrator enters the credited grade.
4. The system validates the data.
5. The system records the credited course unit in the student's academic history.
6. The system updates the student's profile (UC15).

**Alternative / exceptional flows:**
- 4a. The grade is outside the 0–20 scale: the system rejects it.
- 4b. The student already has a grade in that course unit: the system asks the administrator to confirm.

**Business rules:**
- Credited grades are recorded on the 0–20 scale, as defined by the university.
- A credited course unit counts as passed and is treated like any other grade in averages and group formation.
- A student cannot enrol in a course unit that has been credited.

**Domain concepts revealed:** CreditedCourseUnit, Grade, Student, CourseUnit

---

### UC11 – View academic record

**Objective:** Allow a student to consult their academic history.

**Primary actor(s):** Student

**Main success scenario:**
1. The student opens their academic record.
2. The system shows the course units completed, organised by year and semester, with the best grade and ECTS of each.
3. The system shows the total ECTS completed and the overall ECTS-weighted average.

**Alternative / exceptional flows:**
- 2a. The student has no grades yet: the system shows an empty record and the entry grade, if available.
- 2b. The student selects a course unit: the system shows all attempts at that course unit.

**Business rules:**
- A student can only see their own academic record.
- The overall average is ECTS-weighted and uses the best grade of each course unit.

**Domain concepts revealed:** AcademicRecord, Attempt, Grade, ECTS, WeightedAverage
