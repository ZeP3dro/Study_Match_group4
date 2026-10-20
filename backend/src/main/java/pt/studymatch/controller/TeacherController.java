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
import pt.studymatch.domain.Teacher;
import pt.studymatch.dto.AccountDTO;
import pt.studymatch.dto.TeacherRequestDTO;
import pt.studymatch.mapper.AccountMapper;
import pt.studymatch.service.PhotoService;
import pt.studymatch.service.TeacherService;
import pt.studymatch.service.TeacherService.RegisteredTeacher;

@RestController
@RequestMapping("/teachers")
// Só a administração pode gerir docentes.
@PreAuthorize(AccessRules.ADMINISTRATOR_ONLY)
public class TeacherController {

    private final TeacherService teachers;
    private final PhotoService photos;
    private final AccountMapper mapper;

    public TeacherController(TeacherService teachers, PhotoService photos, AccountMapper mapper) {
        this.teachers = teachers;
        this.photos = photos;
        this.mapper = mapper;
    }

    @GetMapping
    public List<AccountDTO> search(@RequestParam(defaultValue = "") String number) {
        return teachers.search(number).stream().map(this::toDTO).toList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AccountDTO register(@Valid @RequestBody TeacherRequestDTO request) {
        RegisteredTeacher registered = teachers.register(request.getName(), request.getDepartment());
        AccountDTO dto = toDTO(registered.getTeacher());
        // Devolve a palavra-passe inicial apenas no registo.
        dto.setInitialPassword(registered.getInitialPassword());
        return dto;
    }

    @PutMapping("/{id}")
    public AccountDTO update(@PathVariable Long id, @Valid @RequestBody TeacherRequestDTO request) {
        return toDTO(teachers.update(id, request.getName(), request.getDepartment()));
    }

    private AccountDTO toDTO(Teacher teacher) {
        return mapper.toDTO(teacher, photos.hasPhoto(teacher.getId()));
    }
}
