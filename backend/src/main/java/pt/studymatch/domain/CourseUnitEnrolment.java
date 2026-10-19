package pt.studymatch.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

/**
 * Enrolment of a student in a course unit in one academic year (UC08), with a grade
 * that can be recorded or corrected (UC09).
 */
@Entity
@Table(name = "course_unit_enrolments",
        uniqueConstraints = @UniqueConstraint(columnNames = {"student_id", "course_unit_id", "academic_year_id"}))
public class CourseUnitEnrolment {

    public static final int MAX_ECTS_PER_ACADEMIC_YEAR = 75;
    public static final int MAX_ECTS_PER_SEMESTER = 42;
    private static final BigDecimal MAX_GRADE = BigDecimal.valueOf(20);
    private static final BigDecimal PASS_MARK = BigDecimal.TEN;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Student student;

    @ManyToOne(optional = false)
    private CourseUnit courseUnit;

    @ManyToOne(optional = false)
    private AcademicYear academicYear;

    @Column(nullable = false)
    private LocalDate enrolmentDate;

    @Column(precision = 3, scale = 1)
    private BigDecimal grade;

    protected CourseUnitEnrolment() {
    }

    public CourseUnitEnrolment(Student student, CourseUnit courseUnit, AcademicYear academicYear,
            LocalDate enrolmentDate) {
        this.student = student;
        this.courseUnit = courseUnit;
        this.academicYear = academicYear;
        this.enrolmentDate = enrolmentDate;
    }

    /**
     * UC08: the enrolments of one academic year (plus an optional new course unit) cannot exceed
     * 42 ECTS per semester and 75 ECTS per year.
     */
    public static void checkEctsLimits(List<CourseUnitEnrolment> enrolmentsOfTheYear, CourseUnit newCourseUnit) {
        int firstSemester = 0;
        int secondSemester = 0;
        for (CourseUnitEnrolment enrolment : enrolmentsOfTheYear) {
            CourseUnit unit = enrolment.getCourseUnit();
            if (unit.getSemester() == 1) {
                firstSemester += unit.getEcts();
            } else {
                secondSemester += unit.getEcts();
            }
        }
        if (newCourseUnit != null && newCourseUnit.getSemester() == 1) {
            firstSemester += newCourseUnit.getEcts();
        } else if (newCourseUnit != null) {
            secondSemester += newCourseUnit.getEcts();
        }

        int yearTotal = firstSemester + secondSemester;
        if (firstSemester > MAX_ECTS_PER_SEMESTER || secondSemester > MAX_ECTS_PER_SEMESTER
                || yearTotal > MAX_ECTS_PER_ACADEMIC_YEAR) {
            throw new IllegalArgumentException("Inscrição inválida: " + firstSemester + " ECTS no 1.º semestre, "
                    + secondSemester + " no 2.º, " + yearTotal + " no ano. Limites: 42 por semestre e 75 por ano.");
        }
    }

    /**
     * Grades go from 0 to 20 with at most one decimal place.
     */
    public static BigDecimal validateGrade(BigDecimal value) {
        if (value == null) {
            throw new IllegalArgumentException("A nota é obrigatória.");
        }
        if (value.compareTo(BigDecimal.ZERO) < 0 || value.compareTo(MAX_GRADE) > 0) {
            throw new IllegalArgumentException("A nota tem de estar entre 0 e 20.");
        }
        try {
            return value.setScale(1, RoundingMode.UNNECESSARY);
        } catch (ArithmeticException exception) {
            throw new IllegalArgumentException("A nota só pode ter uma casa decimal.");
        }
    }

    /**
     * Records or corrects the grade of this enrolment.
     */
    public void recordGrade(BigDecimal newGrade) {
        grade = validateGrade(newGrade);
    }

    public boolean hasGrade() {
        return grade != null;
    }

    public boolean isPassed() {
        return grade != null && grade.compareTo(PASS_MARK) >= 0;
    }

    public Long getId() {
        return id;
    }

    public Student getStudent() {
        return student;
    }

    public CourseUnit getCourseUnit() {
        return courseUnit;
    }

    public AcademicYear getAcademicYear() {
        return academicYear;
    }

    public LocalDate getEnrolmentDate() {
        return enrolmentDate;
    }

    public BigDecimal getGrade() {
        return grade;
    }
}
