package pt.studymatch.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pt.studymatch.domain.Student;
import pt.studymatch.dto.ProfileDTO;
import pt.studymatch.mapper.ProfileMapper;
import pt.studymatch.service.CurrentUserService;
import pt.studymatch.service.ProfileService;
import pt.studymatch.service.StudentService;

@RestController
@RequestMapping("/students/{studentId}/profile")
public class ProfileController {
    private final CurrentUserService currentUser;
    private final StudentService students;
    private final ProfileService profiles;
    private final ProfileMapper mapper;

    public ProfileController(CurrentUserService currentUser, StudentService students, ProfileService profiles, ProfileMapper mapper) {
        this.currentUser = currentUser;
        this.students = students;
        this.profiles = profiles;
        this.mapper = mapper;
    }

    @GetMapping
    public ProfileDTO profile(@PathVariable Long studentId) {
        currentUser.checkCanViewStudent(studentId);
        Student student = students.findById(studentId);
        return mapper.toDTO(student, profiles.profileOf(student));
    }
}
