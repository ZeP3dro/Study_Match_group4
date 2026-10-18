package pt.studymatch.mapper;

import org.springframework.stereotype.Component;
import pt.studymatch.domain.Student;
import pt.studymatch.domain.Teacher;
import pt.studymatch.domain.UserAccount;
import pt.studymatch.dto.AccountDTO;
import pt.studymatch.dto.StudentDTO;

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

    public StudentDTO toDTO(Student student, int curricularYear, boolean hasPhoto) {
        StudentDTO dto = new StudentDTO();
        dto.setId(student.getId());
        dto.setNumber(student.getNumber());
        dto.setName(student.getName());
        dto.setEmail(student.getEmail());
        dto.setRole(student.getRole());
        dto.setActive(student.isActive());
        dto.setStatus(student.getStatus());
        dto.setHasPhoto(hasPhoto);
        dto.setFirstEnrolmentYear(student.getFirstEnrolmentYear());
        dto.setEntryGrade(student.getEntryGrade());
        dto.setEntryGradeOn20(student.entryGradeOn20().orElse(null));
        dto.setCurricularYear(curricularYear);
        return dto;
    }
}
