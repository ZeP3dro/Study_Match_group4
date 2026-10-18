package pt.studymatch.service;

import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pt.studymatch.domain.Teacher;
import pt.studymatch.repository.TeacherRepository;

@Service
@Transactional
public class TeacherService {

        public static class RegisteredTeacher {
        private final Teacher teacher;
        private final String initialPassword;

        RegisteredTeacher(Teacher teacher, String initialPassword) {
            this.teacher = teacher;
            this.initialPassword = initialPassword;
        }

        public Teacher getTeacher() {
            return teacher;
        }

        public String getInitialPassword() {
            return initialPassword;
        }
    }

    private final TeacherRepository teachers;
    private final AccountCredentialsService credentials;
    private final PasswordEncoder encoder;

    public TeacherService(TeacherRepository teachers, AccountCredentialsService credentials,
            PasswordEncoder encoder) {
        this.teachers = teachers;
        this.credentials = credentials;
        this.encoder = encoder;
    }

    // Sem número de pesquisa, devolve todos os docentes.
    @Transactional(readOnly = true)
    public List<Teacher> search(String number) {
        return teachers.findByNumberContainingOrderByNumberAsc(number == null ? "" : number.strip());
    }

    public RegisteredTeacher register(String name, String department) {
        String initialPassword = credentials.newInitialPassword();
        // Usa a sequência partilhada com os alunos e guarda a palavra-passe protegida.
        Teacher teacher = new Teacher(credentials.nextNumber(), name, encoder.encode(initialPassword), department);
        return new RegisteredTeacher(teachers.save(teacher), initialPassword);
    }

    public Teacher update(Long id, String name, String department) {
        // Procura o docente e atualiza o nome e o departamento.
        Teacher teacher = teachers.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Docente não encontrado."));
        teacher.rename(name);
        teacher.changeDepartment(department);
        return teacher;
    }
}
