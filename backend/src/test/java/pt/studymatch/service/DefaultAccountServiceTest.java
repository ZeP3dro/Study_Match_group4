package pt.studymatch.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import pt.studymatch.domain.UserAccount;
import pt.studymatch.enums.Role;
import pt.studymatch.repository.UserAccountRepository;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:defaultaccounts;MODE=MySQL;DATABASE_TO_LOWER=TRUE;DB_CLOSE_DELAY=-1"})
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class DefaultAccountServiceTest {

    @Autowired
    private UserAccountRepository accounts;
    @Autowired
    private DefaultAccountService accountService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void shouldCreateExactlyThreeAccountsWithExpectedRolesAndHashedPasswords() {
        assertThat(accounts.count()).isEqualTo(3);
        assertAccount("12345", "admin", Role.ACADEMIC_ADMINISTRATOR);
        assertAccount("23456", "teacher", Role.TEACHER);
        assertAccount("34567", "students", Role.STUDENT);
    }

    @Test
    void repeatedInitializationShouldPreserveAnExistingPassword() {
        UserAccount admin = accounts.findByNumber("12345").orElseThrow();
        String changedHash = passwordEncoder.encode("a-new-test-password");
        admin.changePassword(changedHash);

        accountService.createMissingAccounts();
        accountService.createMissingAccounts();

        assertThat(accounts.count()).isEqualTo(3);
        assertThat(accounts.findByNumber("12345").orElseThrow().getPassword()).isEqualTo(changedHash);
    }

    @Test
    void missingAccountShouldBeCreatedEvenWhenOtherAccountsAndStudyPlanAlreadyExist() {
        accounts.delete(accounts.findByNumber("34567").orElseThrow());
        accounts.flush();

        accountService.createMissingAccounts();

        assertThat(accounts.count()).isEqualTo(3);
        assertAccount("34567", "students", Role.STUDENT);
    }

    @Test
    void samePasswordShouldProduceDifferentHashesThatBothVerify() {
        String first = passwordEncoder.encode("same-test-password");
        String second = passwordEncoder.encode("same-test-password");

        assertThat(first).isNotEqualTo(second);
        assertThat(passwordEncoder.matches("same-test-password", first)).isTrue();
        assertThat(passwordEncoder.matches("same-test-password", second)).isTrue();
        assertThat(passwordEncoder.matches("wrong", first)).isFalse();
    }

    private void assertAccount(String number, String password, Role role) {
        UserAccount account = accounts.findByNumber(number).orElseThrow();
        assertThat(account.getRole()).isEqualTo(role);
        assertThat(account.getEmail()).isEqualTo(number + "@studymatch.com");
        assertThat(account.getPassword()).isNotEqualTo(password).startsWith("$2a$12$");
        assertThat(passwordEncoder.matches(password, account.getPassword())).isTrue();
    }
}
