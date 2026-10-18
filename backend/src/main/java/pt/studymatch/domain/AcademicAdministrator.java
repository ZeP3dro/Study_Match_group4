package pt.studymatch.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import pt.studymatch.enums.Role;

@Entity
@Table(name = "academic_administrators")
public class AcademicAdministrator extends UserAccount {

    protected AcademicAdministrator() {
    }

    public AcademicAdministrator(String number, String name, String encodedPassword) {
        super(number, name, encodedPassword);
    }

    @Override
    public Role getRole() {
        return Role.ACADEMIC_ADMINISTRATOR;
    }
}
