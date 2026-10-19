package pt.studymatch.dto;

import jakarta.validation.constraints.NotNull;

public class AccountStateDTO {
    @NotNull(message = "Indica se a conta está ativa.")
    private Boolean active;

    public AccountStateDTO() {
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
