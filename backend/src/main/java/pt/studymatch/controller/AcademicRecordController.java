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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import pt.studymatch.dto.EnrolmentDTO;
import pt.studymatch.dto.EnrolmentRequestDTO;
import pt.studymatch.dto.GradeRequestDTO;
import pt.studymatch.mapper.EnrolmentMapper;
import pt.studymatch.service.CurrentUserService;
import pt.studymatch.service.EnrolmentService;

@RestController
@RequestMapping("/students/{studentId}/enrolments")
public class AcademicRecordController {
    private final EnrolmentService enrolments;
    private final CurrentUserService currentUser;
    private final EnrolmentMapper mapper;

    public AcademicRecordController(EnrolmentService enrolments, CurrentUserService currentUser, EnrolmentMapper mapper) {
        this.enrolments = enrolments;
        this.currentUser = currentUser;
        this.mapper = mapper;
    }

    @GetMapping
    public List<EnrolmentDTO> list(@PathVariable Long studentId) {
        currentUser.checkCanViewStudent(studentId);
        return enrolments.enrolmentsOf(studentId).stream().map(mapper::toDTO).toList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize(AccessRules.ADMINISTRATOR_ONLY)
    public EnrolmentDTO enrol(@PathVariable Long studentId, @Valid @RequestBody EnrolmentRequestDTO request) {
        return mapper.toDTO(enrolments.enrol(studentId, request.getCourseUnitId(), request.getAcademicYearId()));
    }

    @PutMapping("/{enrolmentId}/grade")
    @PreAuthorize(AccessRules.ADMINISTRATOR_ONLY)
    public EnrolmentDTO grade(@PathVariable Long studentId, @PathVariable Long enrolmentId,
            @Valid @RequestBody GradeRequestDTO request) {
        return mapper.toDTO(enrolments.recordGrade(studentId, enrolmentId, request.getGrade()));
    }
}
