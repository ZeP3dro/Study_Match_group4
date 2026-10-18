package pt.studymatch.controller;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import pt.studymatch.domain.AcademicYear;
import pt.studymatch.dto.AcademicYearDTO;
import pt.studymatch.dto.CourseUnitDTO;
import pt.studymatch.dto.CourseUnitRequestDTO;
import pt.studymatch.dto.KnowledgeAreaAssignmentDTO;
import pt.studymatch.dto.StudyPlanDTO;
import pt.studymatch.mapper.CourseUnitMapper;
import pt.studymatch.service.AcademicYearService;
import pt.studymatch.service.CourseUnitService;

@RestController
public class CourseUnitController {
    private final CourseUnitService units;
    private final AcademicYearService years;
    private final CourseUnitMapper unitMapper;

    public CourseUnitController(CourseUnitService units, AcademicYearService years, CourseUnitMapper unitMapper) {
        this.units = units;
        this.years = years;
        this.unitMapper = unitMapper;
    }

    @GetMapping("/course-units")
    public List<CourseUnitDTO> units() {
        return units.findAll().stream().map(unitMapper::toDTO).toList();
    }

    @GetMapping("/academic-years")
    public List<AcademicYearDTO> years() {
        AcademicYear current = years.current();
        return years.findAll().stream().map(year -> new AcademicYearDTO(year.getId(), year.label(), year.getId().equals(current.getId()))).toList();
    }

    @GetMapping("/study-plan")
    public StudyPlanDTO studyPlan() {
        return unitMapper.toDTO(units.studyPlan());
    }

    @PostMapping("/course-units")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize(AccessRules.ADMINISTRATOR_ONLY)
    public CourseUnitDTO create(@Valid @RequestBody CourseUnitRequestDTO request) {
        return unitMapper.toDTO(units.create(request.getCode(), request.getName(), request.getEcts(),
                request.getCurricularYear(), request.getSemester(), request.getType(), request.getOptionGroup(),
                request.getKnowledgeAreaId()));
    }

    @PutMapping("/course-units/{id}")
    @PreAuthorize(AccessRules.ADMINISTRATOR_ONLY)
    public CourseUnitDTO update(@PathVariable Long id, @Valid @RequestBody CourseUnitRequestDTO request) {
        return unitMapper.toDTO(units.update(id, request.getName(), request.getEcts(), request.getCurricularYear(),
                request.getSemester(), request.getType(), request.getOptionGroup(), request.getKnowledgeAreaId()));
    }

    @PutMapping("/course-units/{id}/knowledge-area")
    @PreAuthorize(AccessRules.ADMINISTRATOR_ONLY)
    public CourseUnitDTO assign(@PathVariable Long id, @RequestBody KnowledgeAreaAssignmentDTO request) {
        return unitMapper.toDTO(units.assignKnowledgeArea(id, request.getKnowledgeAreaId()));
    }

    @DeleteMapping("/course-units/{id}")
    @PreAuthorize(AccessRules.ADMINISTRATOR_ONLY)
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        units.delete(id);
        return ResponseEntity.noContent().build();
    }
}
