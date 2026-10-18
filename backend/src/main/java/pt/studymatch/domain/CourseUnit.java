package pt.studymatch.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import pt.studymatch.enums.CourseUnitType;

/**
 * A course unit of the study plan. It belongs to at most one knowledge area (UC06, UC13).
 */
@Entity
@Table(name = "course_units")
public class CourseUnit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private StudyPlan studyPlan;

    @Column(nullable = false, unique = true, length = 20)
    private String code;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int ects;

    @Column(nullable = false)
    private int curricularYear;

    @Column(nullable = false)
    private int semester;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private CourseUnitType type;

    @Column(length = 50)
    private String optionGroup;

    @ManyToOne
    private KnowledgeArea knowledgeArea;

    protected CourseUnit() {
    }

    public CourseUnit(StudyPlan plan, String code, String name, int ects, int year, int semester,
            CourseUnitType type) {
        this(plan, code, name, ects, year, semester, type, null);
    }

    public CourseUnit(StudyPlan plan, String code, String name, int ects, int year, int semester,
            CourseUnitType type, String optionGroup) {
        if (code == null || code.isBlank()) {
            throw new IllegalArgumentException("O código da unidade curricular é obrigatório.");
        }
        studyPlan = plan;
        this.code = code.strip().toUpperCase();
        update(name, ects, year, semester, type, optionGroup);
        plan.addCourseUnit(this);
    }

    public void update(String name, int ects, int year, int semester, CourseUnitType type, String group) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("O nome da unidade curricular é obrigatório.");
        }
        if (ects <= 0) {
            throw new IllegalArgumentException("Os ECTS têm de ser maiores que 0.");
        }
        if (semester != 1 && semester != 2) {
            throw new IllegalArgumentException("O semestre tem de ser 1 ou 2.");
        }
        CourseUnitType validType = type == null ? CourseUnitType.MANDATORY : type;
        String validGroup = null;
        if (validType == CourseUnitType.OPTIONAL) {
            if (group == null || group.isBlank()) {
                throw new IllegalArgumentException("Indica o grupo de opção da UC opcional.");
            }
            validGroup = group.strip().toUpperCase();
        }
        studyPlan.checkCanAccommodate(this, year, semester, ects, validType, validGroup);
        this.name = name.strip();
        this.ects = ects;
        curricularYear = year;
        this.semester = semester;
        this.type = validType;
        optionGroup = validGroup;
    }

    public String getOptionGroup() {
        return optionGroup;
    }

    public boolean isOptional() {
        return type == CourseUnitType.OPTIONAL;
    }

    public boolean hasSameOptionGroup(CourseUnit other) {
        return isOptional() && other.isOptional() && curricularYear == other.curricularYear
                && optionGroup.equals(other.optionGroup);
    }

    public void setKnowledgeArea(KnowledgeArea newKnowledgeArea) {
        this.knowledgeArea = newKnowledgeArea;
    }

    public boolean hasKnowledgeArea() {
        return knowledgeArea != null;
    }

    public boolean belongsTo(KnowledgeArea area) {
        return knowledgeArea != null && area != null && knowledgeArea.getId().equals(area.getId());
    }

    public Long getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public int getEcts() {
        return ects;
    }

    public int getCurricularYear() {
        return curricularYear;
    }

    public int getSemester() {
        return semester;
    }

    public CourseUnitType getType() {
        return type;
    }

    public KnowledgeArea getKnowledgeArea() {
        return knowledgeArea;
    }
}
