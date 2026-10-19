package pt.studymatch.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public class GradeRequestDTO {

    @NotNull(message = "Indica a nota.")
    @DecimalMin(value = "0", message = "A nota tem de estar entre 0 e 20.")
    @DecimalMax(value = "20", message = "A nota tem de estar entre 0 e 20.")
    @Digits(integer = 2, fraction = 1, message = "A nota só pode ter uma casa decimal.")
    private BigDecimal grade;

    public GradeRequestDTO() {
    }

    public GradeRequestDTO(BigDecimal grade) {
        this.grade = grade;
    }

    public BigDecimal getGrade() {
        return grade;
    }

    public void setGrade(BigDecimal grade) {
        this.grade = grade;
    }

}
