package pt.studymatch.dto;

/**
 * A null knowledge area removes the area of the course unit (e.g. Internship).
 */
public class KnowledgeAreaAssignmentDTO {

    private Long knowledgeAreaId;

    public KnowledgeAreaAssignmentDTO() {
    }

    public KnowledgeAreaAssignmentDTO(Long knowledgeAreaId) {
        this.knowledgeAreaId = knowledgeAreaId;
    }

    public Long getKnowledgeAreaId() {
        return knowledgeAreaId;
    }

    public void setKnowledgeAreaId(Long knowledgeAreaId) {
        this.knowledgeAreaId = knowledgeAreaId;
    }

}
