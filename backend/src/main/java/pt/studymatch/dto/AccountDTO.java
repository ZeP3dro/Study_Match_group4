package pt.studymatch.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import pt.studymatch.enums.Role;

public class AccountDTO {
    private Long id;
    private String number;
    private String name;
    private String email;
    private Role role;
    private boolean hasPhoto;
    private boolean active;
    private String department;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String initialPassword;

    public AccountDTO() {
    }

    public AccountDTO(Long id, String number, String name, String email, Role role, boolean hasPhoto) {
        this.id = id;
        this.number = number;
        this.name = name;
        this.email = email;
        this.role = role;
        this.hasPhoto = hasPhoto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public boolean getHasPhoto() {
        return hasPhoto;
    }

    public void setHasPhoto(boolean hasPhoto) {
        this.hasPhoto = hasPhoto;
    }

    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getInitialPassword() {
        return initialPassword;
    }

    public void setInitialPassword(String initialPassword) {
        this.initialPassword = initialPassword;
    }
}
