package pt.studymatch.mapper;

import org.springframework.stereotype.Component;
import pt.studymatch.domain.Teacher;
import pt.studymatch.domain.UserAccount;
import pt.studymatch.dto.AccountDTO;

// Prepara os dados da conta sem devolver a palavra-passe.
@Component
public class AccountMapper {

    public AccountDTO toDTO(UserAccount account, boolean hasPhoto) {
        AccountDTO dto = new AccountDTO(account.getId(), account.getNumber(), account.getName(), account.getEmail(),
                account.getRole(), hasPhoto);
        dto.setActive(account.isActive());
        if (account instanceof Teacher teacher) {
            dto.setDepartment(teacher.getDepartment());
        }
        return dto;
    }

}
