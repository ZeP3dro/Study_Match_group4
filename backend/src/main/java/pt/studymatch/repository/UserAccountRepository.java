package pt.studymatch.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import pt.studymatch.domain.UserAccount;

// Procura contas na base de dados pelo número ou pelo email.
public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {
    Optional<UserAccount> findByNumber(String number);
    Optional<UserAccount> findByEmailIgnoreCase(String email);
}
