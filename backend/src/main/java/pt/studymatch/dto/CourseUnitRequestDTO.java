package pt.studymatch.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import pt.studymatch.enums.CourseUnitType;

public class CourseUnitRequestDTO {

    @NotBlank(message = "O código é obrigatório.")
    @Size(max = 20, message = "O código pode ter no máximo 20 caracteres.")
    private String code;

    @NotBlank(message = "O nome é obrigatório.")
    @Size(max = 120, message = "O nome pode ter no máximo 120 caracteres.")
    private String name;

    @NotNull(message = "Indica os ECTS.")
    @Min(value = 1, message = "Os ECTS têm de ser maiores que 0.")
    private Integer ects;

    @NotNull(message = "Indica o ano curricular.")
    @Min(value = 1, message = "O ano curricular tem de estar entre 1 e 3.")
    @Max(value = 3, message = "O ano curricular tem de estar entre 1 e 3.")
    private Integer curricularYear;

    @NotNull(message = "Indica o semestre.")
    @Min(value = 1, message = "O semestre tem de ser 1 ou 2.")
    @Max(value = 2, message = "O semestre tem de ser 1 ou 2.")
    private Integer semester;

    private CourseUnitType type;

    private Long knowledgeAreaId;

    @Size(max = 50)
    private String optionGroup;

    public CourseUnitRequestDTO() {
    }

    public CourseUnitRequestDTO(
            String code,
            String name,
            Integer ects,
            Integer curricularYear,
            Integer semester,
            CourseUnitType type,
            Long knowledgeAreaId) {
        this.code = code;
        this.name = name;
        this.ects = ects;
        this.curricularYear = curricularYear;
        this.semester = semester;
        this.type = type;
        this.knowledgeAreaId = knowledgeAreaId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getEcts() {
        return ects;
    }

    public void setEcts(Integer ects) {
        this.ects = ects;
    }

    public Integer getCurricularYear() {
        return curricularYear;
    }

    public void setCurricularYear(Integer curricularYear) {
        this.curricularYear = curricularYear;
    }

    public Integer getSemester() {
        return semester;
    }

    public void setSemester(Integer semester) {
        this.semester = semester;
    }

    public CourseUnitType getType() {
        return type;
    }

    public void setType(CourseUnitType type) {
        this.type = type;
    }

    public Long getKnowledgeAreaId() {
        return knowledgeAreaId;
    }

    public void setKnowledgeAreaId(Long knowledgeAreaId) {
        this.knowledgeAreaId = knowledgeAreaId;
    }

    public String getOptionGroup() {
        return optionGroup;
    }

    public void setOptionGroup(String optionGroup) {
        this.optionGroup = optionGroup;
    }
}
