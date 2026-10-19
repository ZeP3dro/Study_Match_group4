package pt.studymatch.dto;

import java.math.BigDecimal;
import java.util.List;

public class ProfileDTO {

    private Long studentId;

    private BigDecimal universityAverage;

    private Integer entryGrade;

    private BigDecimal entryGradeOn20;

    private int courseUnitsCount;

    private int totalEcts;

    private boolean hasAcademicData;

    private List<AreaScoreDTO> areas;

    public ProfileDTO() {
    }

    public ProfileDTO(
            Long studentId,
            BigDecimal universityAverage,
            Integer entryGrade,
            BigDecimal entryGradeOn20,
            int courseUnitsCount,
            int totalEcts,
            boolean hasAcademicData,
            List<AreaScoreDTO> areas) {
        this.studentId = studentId;
        this.universityAverage = universityAverage;
        this.entryGrade = entryGrade;
        this.entryGradeOn20 = entryGradeOn20;
        this.courseUnitsCount = courseUnitsCount;
        this.totalEcts = totalEcts;
        this.hasAcademicData = hasAcademicData;
        this.areas = areas;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public BigDecimal getUniversityAverage() {
        return universityAverage;
    }

    public void setUniversityAverage(BigDecimal universityAverage) {
        this.universityAverage = universityAverage;
    }

    public Integer getEntryGrade() {
        return entryGrade;
    }

    public void setEntryGrade(Integer entryGrade) {
        this.entryGrade = entryGrade;
    }

    public BigDecimal getEntryGradeOn20() {
        return entryGradeOn20;
    }

    public void setEntryGradeOn20(BigDecimal entryGradeOn20) {
        this.entryGradeOn20 = entryGradeOn20;
    }

    public int getCourseUnitsCount() {
        return courseUnitsCount;
    }

    public void setCourseUnitsCount(int courseUnitsCount) {
        this.courseUnitsCount = courseUnitsCount;
    }

    public int getTotalEcts() {
        return totalEcts;
    }

    public void setTotalEcts(int totalEcts) {
        this.totalEcts = totalEcts;
    }

    public boolean getHasAcademicData() {
        return hasAcademicData;
    }

    public void setHasAcademicData(boolean hasAcademicData) {
        this.hasAcademicData = hasAcademicData;
    }

    public List<AreaScoreDTO> getAreas() {
        return areas;
    }

    public void setAreas(List<AreaScoreDTO> areas) {
        this.areas = areas;
    }

}
