package pt.studymatch.service;

import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pt.studymatch.domain.Student;
import pt.studymatch.enums.StudentStatus;
import pt.studymatch.repository.StudentRepository;

/**
 * UC02 register student, UC03 view and UC04 update status.
 */
@Service
@Transactional
public class StudentService {

    /** The initial password is only known right after the registration. */
    public static class RegisteredStudent {
        private final Student student;
        private final String initialPassword;

        RegisteredStudent(Student student, String initialPassword) {
            this.student = student;
            this.initialPassword = initialPassword;
        }

        public Student getStudent() {
            return student;
        }

        public String getInitialPassword() {
            return initialPassword;
        }
    }

    private final StudentRepository students;
    private final AccountCredentialsService credentials;
    private final PasswordEncoder encoder;

    public StudentService(StudentRepository students, AccountCredentialsService credentials,
            PasswordEncoder encoder) {
        this.students = students;
        this.credentials = credentials;
        this.encoder = encoder;
    }

    @Transactional(readOnly = true)
    public List<Student> search(String number) {
        return students.findByNumberContainingOrderByNumberAsc(number == null ? "" : number.strip());
    }

    @Transactional(readOnly = true)
    public Student findById(Long id) {
        return students.findById(id).orElseThrow(() -> new NoSuchElementException("Estudante não encontrado."));
    }

    /**
     * Loads the student and locks the row, so two enrolments at the same time cannot both pass the ECTS limits.
     */
    public Student findForEnrolment(Long id) {
        return students.findForEnrolment(id)
                .orElseThrow(() -> new NoSuchElementException("Estudante não encontrado."));
    }

    public RegisteredStudent register(String name, int firstEnrolmentYear, Integer entryGrade) {
        String initialPassword = credentials.newInitialPassword();
        Student student = new Student(credentials.nextNumber(), name, encoder.encode(initialPassword),
                firstEnrolmentYear, entryGrade);
        return new RegisteredStudent(students.save(student), initialPassword);
    }

    public Student update(Long id, String name, int firstEnrolmentYear, Integer entryGrade) {
        Student student = findById(id);
        student.rename(name);
        student.changeFirstEnrolmentYear(firstEnrolmentYear);
        student.changeEntryGrade(entryGrade);
        return student;
    }

    public Student changeStatus(Long id, StudentStatus status) {
        Student student = findById(id);
        student.changeStatus(status);
        return student;
    }
}
