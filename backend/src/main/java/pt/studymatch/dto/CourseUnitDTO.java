package pt.studymatch.dto;

import jakarta.validation.constraints.Size;
import pt.studymatch.enums.CourseUnitType;

public class CourseUnitDTO {

    private Long id;

    private String code;

    private String name;

    private int ects;

    private int curricularYear;

    private int semester;

    private CourseUnitType type;

    private Long knowledgeAreaId;

    @Size(max = 50)
    private String optionGroup;

    private String knowledgeAreaName;

    public CourseUnitDTO() {
    }

    public CourseUnitDTO(
            Long id,
            String code,
            String name,
            int ects,
            int curricularYear,
            int semester,
            CourseUnitType type,
            Long knowledgeAreaId,
            String knowledgeAreaName) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.ects = ects;
        this.curricularYear = curricularYear;
        this.semester = semester;
        this.type = type;
        this.knowledgeAreaId = knowledgeAreaId;
        this.knowledgeAreaName = knowledgeAreaName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public int getEcts() {
        return ects;
    }

    public void setEcts(int ects) {
        this.ects = ects;
    }

    public int getCurricularYear() {
        return curricularYear;
    }

    public void setCurricularYear(int curricularYear) {
        this.curricularYear = curricularYear;
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
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

    public String getKnowledgeAreaName() {
        return knowledgeAreaName;
    }

    public void setKnowledgeAreaName(String knowledgeAreaName) {
        this.knowledgeAreaName = knowledgeAreaName;
    }

    public String getOptionGroup() {
        return optionGroup;
    }

    public void setOptionGroup(String optionGroup) {
        this.optionGroup = optionGroup;
    }
}
