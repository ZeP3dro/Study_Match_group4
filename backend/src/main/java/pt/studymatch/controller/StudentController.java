package pt.studymatch.controller;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import pt.studymatch.domain.Student;
import pt.studymatch.dto.StatusChangeDTO;
import pt.studymatch.dto.StudentDTO;
import pt.studymatch.dto.StudentRequestDTO;
import pt.studymatch.mapper.AccountMapper;
import pt.studymatch.service.CurrentUserService;
import pt.studymatch.service.ProfileService;
import pt.studymatch.service.StudentService;
import pt.studymatch.service.StudentService.RegisteredStudent;

@RestController
@RequestMapping("/students")
public class StudentController {

    private final StudentService students;
    private final ProfileService profiles;
    private final CurrentUserService currentUser;
    private final AccountMapper mapper;

    public StudentController(StudentService students, ProfileService profiles, CurrentUserService currentUser, AccountMapper mapper) {
        this.students = students;
        this.profiles = profiles;
        this.currentUser = currentUser;
        this.mapper = mapper;
    }

    @GetMapping
    @PreAuthorize(AccessRules.ADMINISTRATOR_ONLY)
    public List<StudentDTO> search(@RequestParam(defaultValue = "") String number) {
        return students.search(number).stream().map(this::toDTO).toList();
    }

    @GetMapping("/{id}")
    public StudentDTO find(@PathVariable Long id) {
        currentUser.checkCanViewStudent(id);
        return toDTO(students.findById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize(AccessRules.ADMINISTRATOR_ONLY)
    public StudentDTO create(@Valid @RequestBody StudentRequestDTO request) {
        RegisteredStudent registered = students.register(request.getName(), request.getFirstEnrolmentYear(),
                request.getEntryGrade());
        StudentDTO dto = toDTO(registered.getStudent());
        dto.setInitialPassword(registered.getInitialPassword());
        return dto;
    }

    @PutMapping("/{id}")
    @PreAuthorize(AccessRules.ADMINISTRATOR_ONLY)
    public StudentDTO update(@PathVariable Long id, @Valid @RequestBody StudentRequestDTO request) {
        return toDTO(students.update(id, request.getName(), request.getFirstEnrolmentYear(), request.getEntryGrade()));
    }

    @PutMapping("/{id}/status")
    @PreAuthorize(AccessRules.ADMINISTRATOR_ONLY)
    public StudentDTO changeStatus(@PathVariable Long id, @Valid @RequestBody StatusChangeDTO request) {
        return toDTO(students.changeStatus(id, request.getStatus()));
    }

    private StudentDTO toDTO(Student student) {
        return mapper.toDTO(student, profiles.curricularYearOf(student), false);
    }
}
