package pt.studymatch.dto;

import java.util.List;

public class KnowledgeAreaDTO {

    private Long id;

    private String name;

    private String description;

    private List<CourseUnitDTO> courseUnits;

    public KnowledgeAreaDTO() {
    }

    public KnowledgeAreaDTO(Long id, String name, String description, List<CourseUnitDTO> courseUnits) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.courseUnits = courseUnits;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<CourseUnitDTO> getCourseUnits() {
        return courseUnits;
    }

    public void setCourseUnits(List<CourseUnitDTO> courseUnits) {
        this.courseUnits = courseUnits;
    }

}
