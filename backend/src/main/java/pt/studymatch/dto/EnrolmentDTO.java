package pt.studymatch.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class EnrolmentDTO {

    private Long id;

    private CourseUnitDTO courseUnit;

    private Long academicYearId;

    private String academicYear;

    private LocalDate enrolmentDate;

    private BigDecimal grade;

    private boolean passed;

    public EnrolmentDTO() {
    }

    public EnrolmentDTO(
            Long id,
            CourseUnitDTO courseUnit,
            Long academicYearId,
            String academicYear,
            LocalDate enrolmentDate,
            BigDecimal grade,
            boolean passed) {
        this.id = id;
        this.courseUnit = courseUnit;
        this.academicYearId = academicYearId;
        this.academicYear = academicYear;
        this.enrolmentDate = enrolmentDate;
        this.grade = grade;
        this.passed = passed;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CourseUnitDTO getCourseUnit() {
        return courseUnit;
    }

    public void setCourseUnit(CourseUnitDTO courseUnit) {
        this.courseUnit = courseUnit;
    }

    public Long getAcademicYearId() {
        return academicYearId;
    }

    public void setAcademicYearId(Long academicYearId) {
        this.academicYearId = academicYearId;
    }

    public String getAcademicYear() {
        return academicYear;
    }

    public void setAcademicYear(String academicYear) {
        this.academicYear = academicYear;
    }

    public LocalDate getEnrolmentDate() {
        return enrolmentDate;
    }

    public void setEnrolmentDate(LocalDate enrolmentDate) {
        this.enrolmentDate = enrolmentDate;
    }

    public BigDecimal getGrade() {
        return grade;
    }

    public void setGrade(BigDecimal grade) {
        this.grade = grade;
    }

    public boolean isPassed() {
        return passed;
    }

    public void setPassed(boolean passed) {
        this.passed = passed;
    }

}
