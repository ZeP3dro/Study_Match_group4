package pt.studymatch.dto;

import jakarta.validation.constraints.NotBlank;

// Dados recebidos do formulário; os campos vazios são rejeitados.
public class LoginDTO {

    @NotBlank(message = "Indica o número de identificação.")
    private String email;

    @NotBlank(message = "Indica a palavra-passe.")
    private String password;

    public LoginDTO() {
    }

    public LoginDTO(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
