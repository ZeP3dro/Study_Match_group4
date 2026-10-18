package pt.studymatch.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

// Dados do formulário; o nome e o departamento são obrigatórios.
public class TeacherRequestDTO {

    @NotBlank(message = "O nome é obrigatório.")
    @Size(max = 120, message = "O nome pode ter no máximo 120 caracteres.")
    private String name;

    @NotBlank(message = "O departamento é obrigatório.")
    @Size(max = 120, message = "O departamento pode ter no máximo 120 caracteres.")
    private String department;

    public TeacherRequestDTO() {
    }

    public TeacherRequestDTO(String name, String department) {
        this.name = name;
        this.department = department;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

}
