package pt.studymatch.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pt.studymatch.domain.UserAccount;
import pt.studymatch.enums.Role;
import pt.studymatch.repository.UserAccountRepository;

@Service
public class CurrentUserService {

    private final UserAccountRepository userAccountRepository;

    public CurrentUserService(UserAccountRepository userAccountRepository) {
        this.userAccountRepository = userAccountRepository;
    }

    public UserAccount currentAccount() {
        // Obtém a autenticação associada ao pedido atual.
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new AccessDeniedException("Not signed in");
        }
        return userAccountRepository.findByEmailIgnoreCase(authentication.getName())
                .orElseThrow(() -> new AccessDeniedException("Account not found"));
    }

        // O aluno só consulta os seus dados; a administração pode consultar todos.
    public void checkCanViewStudent(Long studentId) {
        UserAccount account = currentAccount();
        boolean isAdministrator = account.getRole() == Role.ACADEMIC_ADMINISTRATOR;
        boolean isTheStudent = account.getRole() == Role.STUDENT && account.getId().equals(studentId);
        if (!isAdministrator && !isTheStudent) {
            throw new AccessDeniedException("Not allowed to see this student");
        }
    }

    // Só o dono da conta pode alterar a fotografia.
    public void checkCanEditPhoto(Long accountId) {
        UserAccount account = currentAccount();
        if (!account.getId().equals(accountId)) {
            throw new AccessDeniedException("Não podes aceder à fotografia deste utilizador.");
        }
    }

    // A fotografia pode ser consultada pelo dono ou pela administração.
    public void checkCanViewPhoto(Long accountId) {
        UserAccount account = currentAccount();
        if (account.getRole() != Role.ACADEMIC_ADMINISTRATOR && !account.getId().equals(accountId)) {
            throw new AccessDeniedException("Não podes consultar esta fotografia.");
        }
    }

    public boolean isSessionValid(Authentication authentication,
            HttpServletRequest request) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }
        // Volta a consultar a conta para detetar alterações ao acesso.
        UserAccount account = userAccountRepository.findByEmailIgnoreCase(authentication.getName()).orElse(null);
        HttpSession session = request.getSession(false);
        if (account == null || !account.isActive() || session == null) {
            return false;
        }
        // Uma versão diferente significa que a palavra-passe já mudou.
        return Integer.valueOf(account.getPasswordVersion()).equals(session.getAttribute("passwordVersion"));
    }
}
