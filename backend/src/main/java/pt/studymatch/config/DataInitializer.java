package pt.studymatch.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pt.studymatch.service.DefaultAccountService;

@Component
public class DataInitializer implements CommandLineRunner {
    private final DefaultAccountService accounts;

    public DataInitializer(DefaultAccountService accounts) {
        this.accounts = accounts;
    }

    @Override
    @Transactional
    public void run(String... args) {
        // Prepara as três contas de acesso na primeira execução.
        accounts.createMissingAccounts();
    }
}
