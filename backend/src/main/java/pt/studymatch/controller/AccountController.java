package pt.studymatch.controller;

import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pt.studymatch.domain.UserAccount;
import pt.studymatch.dto.AccountDTO;
import pt.studymatch.dto.AccountStateDTO;
import pt.studymatch.mapper.AccountMapper;
import pt.studymatch.service.AccountService;

@RestController
public class AccountController {

    private final AccountService accounts;
    private final AccountMapper mapper;

    public AccountController(AccountService accounts, AccountMapper mapper) {
        this.accounts = accounts;
        this.mapper = mapper;
    }

    @PutMapping("/users/{id}/active")
    @PreAuthorize(AccessRules.ADMINISTRATOR_ONLY)
    public AccountDTO changeState(@PathVariable Long id, @Valid @RequestBody AccountStateDTO request) {
        UserAccount account = accounts.changeState(id, request.getActive());
        return mapper.toDTO(account, false);
    }
}
