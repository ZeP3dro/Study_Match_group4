package pt.studymatch.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pt.studymatch.service.DefaultAccountService;
import pt.studymatch.domain.AccountSequence;
import pt.studymatch.repository.AccountSequenceRepository;

@Component
public class DataInitializer implements CommandLineRunner {
    private final DefaultAccountService accounts;
    private final AccountSequenceRepository sequences;

    public DataInitializer(DefaultAccountService accounts, AccountSequenceRepository sequences) {
        this.accounts = accounts;
        this.sequences = sequences;
    }

    @Override
    @Transactional
    public void run(String... args) {
        // Prepara as três contas de acesso na primeira execução.
        accounts.createMissingAccounts();
        if (!sequences.existsById(1L)) sequences.save(new AccountSequence());
    }
}
