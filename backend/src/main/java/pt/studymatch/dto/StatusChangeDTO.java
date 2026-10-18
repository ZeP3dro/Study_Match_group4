package pt.studymatch.dto;

import jakarta.validation.constraints.NotNull;
import pt.studymatch.enums.StudentStatus;

public class StatusChangeDTO {

    @NotNull(message = "Escolhe o novo estado.")
    private StudentStatus status;

    public StatusChangeDTO() {
    }

    public StatusChangeDTO(StudentStatus status) {
        this.status = status;
    }

    public StudentStatus getStatus() {
        return status;
    }

    public void setStatus(StudentStatus status) {
        this.status = status;
    }

}
