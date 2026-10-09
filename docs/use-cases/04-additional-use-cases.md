# Additional Value-Adding Use Cases

Issue: #8. Actors and shared rules are described in [README.md](README.md).

These use cases go beyond the capability areas required in the Sprint 1 brief. Each one includes a justification of why it adds value to StudyMatch.

---

### UC21 – Explain group formation

**Justification (transparency):** Automatically formed groups can feel arbitrary. Showing how the groups were formed increases trust in the system and helps the teacher detect problems (e.g. many students relying on the entry grade).

**Objective:** Show how the groups of a grouping context were formed.

**Primary actor(s):** Teacher, Student

**Main success scenario:**
1. The teacher opens the explanation of a grouping context.
2. The system shows the knowledge area used, the class average and each group's average.
3. The system shows, for each student, which source was used for the score (area average, university average or entry grade).

**Alternative / exceptional flows:**
- 1a. A student opens the explanation: the system only shows the knowledge area used and how the groups are balanced, without any averages or grades.

**Business rules:**
- Only teachers can see averages and scores (class, group or individual).
- Students never see grades or averages of other students, even within the same group.

**Domain concepts revealed:** GroupingExplanation, ScoreSource, ClassAverage

---

### UC22 – Review group members

**Justification (collaboration):** Grades show academic performance but not how well students work in a team. Anonymous peer reviews after each group activity give students feedback on their teamwork and give teachers additional evidence about each student, enriching the student profile.

**Objective:** Allow each group member to anonymously rate the other members and the group after a group activity.

**Primary actor(s):** Student

**Main success scenario:**
1. The student opens a published group whose activity has ended.
2. The system shows the other group members.
3. For each member, the student gives a rating from 1 to 5 in each review criterion and, optionally, a comment.
4. The student gives an overall rating from 1 to 5 to the group.
5. The system validates and saves the reviews.
6. The ratings and comments become part of each reviewed student's profile (UC14).

**Alternative / exceptional flows:**
- 3a. The student does not rate all criteria for a member: the system asks the student to complete them.
- 5a. The student already submitted reviews for this group: the system allows editing until the review period closes.
- 1a. The review period has closed: the system informs the student that reviews are no longer accepted.

**Business rules:**
- Review criteria: Commitment, Knowledge, Communication and Reliability (meeting deadlines).
- Ratings range from 1 to 5.
- A student cannot review themselves.
- Each student reviews each member only once per group (editable while the period is open).
- Reviews are anonymous: the reviewed student and the teacher never see who gave each rating or comment.

**Domain concepts revealed:** PeerReview, ReviewCriterion, Rating, Comment, GroupRating, ReviewPeriod
