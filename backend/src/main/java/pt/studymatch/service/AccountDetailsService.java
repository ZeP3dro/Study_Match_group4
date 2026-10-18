package pt.studymatch.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pt.studymatch.domain.UserAccount;
import pt.studymatch.repository.UserAccountRepository;

@Service
public class AccountDetailsService implements UserDetailsService {

    private final UserAccountRepository userAccountRepository;

    public AccountDetailsService(UserAccountRepository userAccountRepository) {
        this.userAccountRepository = userAccountRepository;
    }

    private static String toEmail(String emailOrNumber) {
        String value = emailOrNumber == null ? "" : emailOrNumber.strip().toLowerCase();
        // Se foi escrito só o número, completa o email institucional.
        return value.contains("@") ? value : UserAccount.institutionalEmailFor(value);
    }

    @Override
    public UserDetails loadUserByUsername(String emailOrNumber) {
        UserAccount account = userAccountRepository.findByEmailIgnoreCase(toEmail(emailOrNumber))
                .orElseThrow(() -> new UsernameNotFoundException("Account not found"));

        // Entrega ao Spring os dados necessários para validar o login.
        return User.withUsername(account.getEmail())
                .password(account.getPassword())
                .roles(account.getRole().name())
                .disabled(!account.isActive())
                .build();
    }
}
