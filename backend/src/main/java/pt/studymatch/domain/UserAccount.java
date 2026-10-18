package pt.studymatch.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import java.util.Objects;
import java.util.regex.Pattern;
import pt.studymatch.enums.Role;

@Entity
@Table(name = "user_accounts")
// Alunos, docentes e administradores partilham os dados da conta.
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class UserAccount {

    public static final String EMAIL_DOMAIN = "studymatch.com";
    private static final Pattern NUMBER_FORMAT = Pattern.compile("[1-9][0-9]{0,7}");

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 8)
    private String number;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private boolean active;

    private int passwordVersion;

    protected UserAccount() {
    }

    protected UserAccount(String number, String name, String encodedPassword) {
        if (number == null || !NUMBER_FORMAT.matcher(number).matches()) {
            throw new IllegalArgumentException("O número tem de ser positivo e ter no máximo 8 dígitos.");
        }
        this.number = number;
        this.email = institutionalEmailFor(number);
        this.password = Objects.requireNonNull(encodedPassword, "Password is required");
        this.active = true;
        rename(name);
    }

    public static String institutionalEmailFor(String number) {
        return number + "@" + EMAIL_DOMAIN;
    }

    public abstract Role getRole();

    public void rename(String newName) {
        if (newName == null || newName.isBlank()) {
            throw new IllegalArgumentException("O nome é obrigatório.");
        }
        this.name = newName.strip();
    }

    public void changePassword(String encodedPassword) {
        this.password = Objects.requireNonNull(encodedPassword, "Password is required");
        // Invalida as sessões que ainda tenham a versão anterior.
        passwordVersion++;
    }

    public void deactivate() {
        this.active = false;
        // Invalida as sessões que ainda tenham a versão anterior.
        passwordVersion++;
    }

    public void activate() {
        this.active = true;
    }

    public int getPasswordVersion() {
        return passwordVersion;
    }

    public Long getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    @JsonIgnore
    public String getPassword() {
        return password;
    }

    public boolean isActive() {
        return active;
    }
}
