package pt.studymatch.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.csrf.CsrfAuthenticationStrategy;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pt.studymatch.domain.UserAccount;
import pt.studymatch.dto.AccountDTO;
import pt.studymatch.dto.CsrfTokenDTO;
import pt.studymatch.dto.LoginDTO;
import pt.studymatch.dto.PasswordChangeDTO;
import pt.studymatch.mapper.AccountMapper;
import pt.studymatch.service.AccountService;
import pt.studymatch.service.CurrentUserService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final SecurityContextRepository securityContextRepository;
    private final CurrentUserService currentUserService;
    private final AccountService accountService;
    private final AccountMapper accountMapper;

    public AuthController(AuthenticationManager authenticationManager,
            SecurityContextRepository securityContextRepository, CurrentUserService currentUserService,
            AccountService accountService, AccountMapper accountMapper) {
        this.authenticationManager = authenticationManager;
        this.securityContextRepository = securityContextRepository;
        this.currentUserService = currentUserService;
        this.accountService = accountService;
        this.accountMapper = accountMapper;
    }

    @GetMapping("/csrf")
    public CsrfTokenDTO csrf(CsrfToken token) {
        // O frontend envia este token nos pedidos que alteram dados.
        return new CsrfTokenDTO(token.getHeaderName(), token.getToken());
    }

    @PostMapping("/login")
    public AccountDTO login(@Valid @RequestBody LoginDTO login, HttpServletRequest request,
            HttpServletResponse response) {
        // Confirma o número/email e a palavra-passe.
        Authentication authentication = authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken.unauthenticated(login.getEmail(), login.getPassword()));

        // Troca o identificador da sessão depois de entrar na conta.
        if (request.getSession(false) != null) {
            request.changeSessionId();
        }
        // Renova o token de segurança depois do login.
        new CsrfAuthenticationStrategy(new HttpSessionCsrfTokenRepository())
                .onAuthentication(authentication, request, response);
        // Associa a conta autenticada aos próximos pedidos desta sessão.
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
        securityContextRepository.saveContext(context, request, response);

        UserAccount account = currentUserService.currentAccount();
        // Permite rejeitar esta sessão se a palavra-passe mudar.
        request.getSession().setAttribute("passwordVersion", account.getPasswordVersion());
        return toDTO(account);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        // Fecha a sessão, se existir, sem criar uma sessão nova.
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        SecurityContextHolder.clearContext();
        return ResponseEntity.noContent().build();
    }

    // Devolve os dados da conta que tem sessão iniciada.
    @GetMapping("/me")
    public AccountDTO currentUser() {
        return toDTO(currentUserService.currentAccount());
    }

    @PutMapping("/password")
    public ResponseEntity<Void> changePassword(@RequestBody PasswordChangeDTO request,
            HttpServletRequest httpRequest) {
        accountService.changePassword(request.getCurrentPassword(), request.getNewPassword(),
                request.getConfirmation());
        logout(httpRequest);
        return ResponseEntity.noContent().build();
    }

    private AccountDTO toDTO(UserAccount account) {
        return accountMapper.toDTO(account, false);
    }
}
