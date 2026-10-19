package pt.studymatch.dto;

import jakarta.validation.constraints.NotNull;

public class EnrolmentRequestDTO {

    @NotNull(message = "Escolhe a unidade curricular.")
    private Long courseUnitId;

    @NotNull(message = "Escolhe o ano letivo.")
    private Long academicYearId;

    public EnrolmentRequestDTO() {
    }

    public EnrolmentRequestDTO(Long courseUnitId, Long academicYearId) {
        this.courseUnitId = courseUnitId;
        this.academicYearId = academicYearId;
    }

    public Long getCourseUnitId() {
        return courseUnitId;
    }

    public void setCourseUnitId(Long courseUnitId) {
        this.courseUnitId = courseUnitId;
    }

    public Long getAcademicYearId() {
        return academicYearId;
    }

    public void setAcademicYearId(Long academicYearId) {
        this.academicYearId = academicYearId;
    }

}
