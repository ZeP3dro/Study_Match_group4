package pt.studymatch.dto;

import java.math.BigDecimal;
import pt.studymatch.enums.StudentStatus;

public class StudentDTO extends AccountDTO {
    private int firstEnrolmentYear;
    private Integer entryGrade;
    private BigDecimal entryGradeOn20;
    private int curricularYear;
    private StudentStatus status;

    public StudentDTO() {
    }

    public int getFirstEnrolmentYear() {
        return firstEnrolmentYear;
    }

    public void setFirstEnrolmentYear(int firstEnrolmentYear) {
        this.firstEnrolmentYear = firstEnrolmentYear;
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

    public int getCurricularYear() {
        return curricularYear;
    }

    public void setCurricularYear(int curricularYear) {
        this.curricularYear = curricularYear;
    }

    public StudentStatus getStatus() {
        return status;
    }

    public void setStatus(StudentStatus status) {
        this.status = status;
    }
}
