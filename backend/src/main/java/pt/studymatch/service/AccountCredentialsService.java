package pt.studymatch.service;

import java.security.SecureRandom;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pt.studymatch.domain.AccountSequence;
import pt.studymatch.repository.AccountSequenceRepository;
import pt.studymatch.repository.UserAccountRepository;

/**
 * Number and initial password of a new student or teacher account.
 */
@Service
public class AccountCredentialsService {

    private static final String PASSWORD_CHARACTERS = "abcdefghjkmnpqrstuvwxyzABCDEFGHJKMNPQRSTUVWXYZ23456789";
    private static final int PASSWORD_LENGTH = 16;

    private final AccountSequenceRepository sequences;
    private final UserAccountRepository accounts;
    private final SecureRandom random = new SecureRandom();

    public AccountCredentialsService(AccountSequenceRepository sequences, UserAccountRepository accounts) {
        this.sequences = sequences;
        this.accounts = accounts;
    }

    /**
     * Next free number of the shared counter (1, 2, 3…). The counter row stays locked until the
     * transaction ends, so two registrations at the same time never get the same number.
     */
    @Transactional
    public String nextNumber() {
        AccountSequence sequence = sequences.findCounterForUpdate().orElseThrow();
        String number = sequence.takeNextNumber();
        while (accounts.findByNumber(number).isPresent()) {
            number = sequence.takeNextNumber();
        }
        return number;
    }

    public String newInitialPassword() {
        StringBuilder password = new StringBuilder(PASSWORD_LENGTH);
        for (int i = 0; i < PASSWORD_LENGTH; i++) {
            password.append(PASSWORD_CHARACTERS.charAt(random.nextInt(PASSWORD_CHARACTERS.length())));
        }
        return password.toString();
    }
}
