package pt.studymatch.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class StudentRequestDTO {

    @NotBlank(message = "O nome é obrigatório.")
    @Size(max = 120, message = "O nome pode ter no máximo 120 caracteres.")
    private String name;

    @NotNull(message = "Indica o ano da primeira inscrição.")
    @Min(value = 2000, message = "O ano da primeira inscrição não é válido.")
    @Max(value = 2100, message = "O ano da primeira inscrição não é válido.")
    private Integer firstEnrolmentYear;

    @Min(value = 0, message = "A nota de ingresso tem de estar entre 0 e 200.")
    @Max(value = 200, message = "A nota de ingresso tem de estar entre 0 e 200.")
    private Integer entryGrade;

    public StudentRequestDTO() {
    }

    public StudentRequestDTO(String name, Integer firstEnrolmentYear, Integer entryGrade) {
        this.name = name;
        this.firstEnrolmentYear = firstEnrolmentYear;
        this.entryGrade = entryGrade;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getFirstEnrolmentYear() {
        return firstEnrolmentYear;
    }

    public void setFirstEnrolmentYear(Integer firstEnrolmentYear) {
        this.firstEnrolmentYear = firstEnrolmentYear;
    }

    public Integer getEntryGrade() {
        return entryGrade;
    }

    public void setEntryGrade(Integer entryGrade) {
        this.entryGrade = entryGrade;
    }

}
