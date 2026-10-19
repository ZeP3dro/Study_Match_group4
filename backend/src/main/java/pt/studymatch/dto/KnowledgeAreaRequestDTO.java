package pt.studymatch.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class KnowledgeAreaRequestDTO {

    @NotBlank(message = "O nome da área é obrigatório.")
    @Size(max = 100, message = "O nome pode ter no máximo 100 caracteres.")
    private String name;

    @Size(max = 500, message = "A descrição pode ter no máximo 500 caracteres.")
    private String description;

    public KnowledgeAreaRequestDTO() {
    }

    public KnowledgeAreaRequestDTO(String name, String description) {
        this.name = name;
        this.description = description;
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

}
