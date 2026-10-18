package pt.studymatch.service;

import java.util.Optional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pt.studymatch.domain.AcademicAdministrator;
import pt.studymatch.domain.Student;
import pt.studymatch.domain.Teacher;
import pt.studymatch.domain.UserAccount;
import pt.studymatch.enums.Role;
import pt.studymatch.repository.UserAccountRepository;

@Service
public class DefaultAccountService {

    private final UserAccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final AcademicYearService academicYearService;

    public DefaultAccountService(UserAccountRepository accountRepository, PasswordEncoder passwordEncoder,
            AcademicYearService academicYearService) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
        this.academicYearService = academicYearService;
    }

    @Transactional
    public void createMissingAccounts() {
        // Cria apenas as contas iniciais que ainda não existem.
        createAdministrator();
        createTeacher();
        createStudent();
    }

    private void createAdministrator() {
        if (accountExists("12345", Role.ACADEMIC_ADMINISTRATOR)) {
            return;
        }
        String password = passwordEncoder.encode("admin");
        AcademicAdministrator administrator = new AcademicAdministrator("12345", "Administrador", password);
        accountRepository.save(administrator);
    }

    private void createTeacher() {
        if (accountExists("23456", Role.TEACHER)) {
            return;
        }
        String password = passwordEncoder.encode("teacher");
        Teacher teacher = new Teacher("23456", "Docente", password, "Departamento de Informática");
        accountRepository.save(teacher);
    }

    private void createStudent() {
        if (accountExists("34567", Role.STUDENT)) {
            return;
        }
        String password = passwordEncoder.encode("students");
        int year = academicYearService.current().getStartYear();
        Student student = new Student("34567", "Estudante", password, year, 152);
        accountRepository.save(student);
    }

    private boolean accountExists(String number, Role expectedRole) {
        // Preserva os dados e a palavra-passe das contas existentes.
        Optional<UserAccount> account = accountRepository.findByNumber(number);
        if (account.isEmpty()) {
            return false;
        }
        if (account.get().getRole() != expectedRole) {
            throw new IllegalStateException("O número " + number + " já pertence a outro perfil.");
        }
        return true;
    }
}
