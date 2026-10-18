package pt.studymatch.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import pt.studymatch.enums.Role;

@Entity
@Table(name = "teachers")
public class Teacher extends UserAccount {

    @Column(nullable = false)
    private String department;

    protected Teacher() {
    }

    public Teacher(String number, String name, String encodedPassword, String department) {
        super(number, name, encodedPassword);
        changeDepartment(department);
    }

    public void changeDepartment(String newDepartment) {
        if (newDepartment == null || newDepartment.isBlank()) {
            throw new IllegalArgumentException("O departamento é obrigatório.");
        }
        this.department = newDepartment.strip();
    }

    @Override
    public Role getRole() {
        return Role.TEACHER;
    }

    public String getDepartment() {
        return department;
    }
}
