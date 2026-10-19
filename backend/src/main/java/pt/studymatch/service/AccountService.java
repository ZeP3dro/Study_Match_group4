package pt.studymatch.service;

import java.nio.charset.StandardCharsets;
import java.util.NoSuchElementException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pt.studymatch.domain.UserAccount;
import pt.studymatch.enums.Role;
import pt.studymatch.repository.UserAccountRepository;

/**
 * Changes to an existing account: the password (by its owner) and the active state
 * (by the academic administrator).
 */
@Service
@Transactional
public class AccountService {

    private static final int MIN_PASSWORD_LENGTH = 8;
    // BCrypt only uses the first 72 bytes of a password.
    private static final int MAX_PASSWORD_BYTES = 72;

    private final UserAccountRepository accounts;
    private final CurrentUserService currentUser;
    private final PasswordEncoder encoder;

    public AccountService(UserAccountRepository accounts, CurrentUserService currentUser, PasswordEncoder encoder) {
        this.accounts = accounts;
        this.currentUser = currentUser;
        this.encoder = encoder;
    }

    public void changePassword(String currentPassword, String newPassword, String confirmation) {
        UserAccount account = currentUser.currentAccount();
        if (currentPassword == null || !encoder.matches(currentPassword, account.getPassword())) {
            throw new IllegalArgumentException("A palavra-passe atual não está correta.");
        }
        if (newPassword == null || newPassword.length() < MIN_PASSWORD_LENGTH
                || newPassword.getBytes(StandardCharsets.UTF_8).length > MAX_PASSWORD_BYTES) {
            throw new IllegalArgumentException("A nova palavra-passe deve ter pelo menos 8 caracteres e no máximo 72 bytes.");
        }
        if (!newPassword.equals(confirmation)) {
            throw new IllegalArgumentException("A confirmação não coincide com a nova palavra-passe.");
        }
        if (encoder.matches(newPassword, account.getPassword())) {
            throw new IllegalArgumentException("Escolhe uma palavra-passe diferente da atual.");
        }
        account.changePassword(encoder.encode(newPassword));
    }

    /**
     * Blocks or unblocks the access of a student or teacher without deleting their history.
     */
    public UserAccount changeState(Long accountId, boolean active) {
        UserAccount account = accounts.findById(accountId)
                .orElseThrow(() -> new NoSuchElementException("Conta não encontrada."));
        if (account.getRole() == Role.ACADEMIC_ADMINISTRATOR) {
            throw new IllegalArgumentException("Esta operação destina-se apenas a contas de alunos e docentes.");
        }
        if (active) {
            account.activate();
        } else if (account.isActive()) {
            account.deactivate();
        }
        return account;
    }
}
