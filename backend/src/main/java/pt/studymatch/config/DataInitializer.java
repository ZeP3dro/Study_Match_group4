package pt.studymatch.config;

import java.util.HashMap;
import java.util.Map;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pt.studymatch.domain.AcademicYear;
import pt.studymatch.domain.AccountSequence;
import pt.studymatch.domain.CourseUnit;
import pt.studymatch.domain.KnowledgeArea;
import pt.studymatch.domain.StudyPlan;
import pt.studymatch.enums.CourseUnitType;
import pt.studymatch.repository.AcademicYearRepository;
import pt.studymatch.repository.AccountSequenceRepository;
import pt.studymatch.repository.CourseUnitRepository;
import pt.studymatch.repository.KnowledgeAreaRepository;
import pt.studymatch.repository.StudyPlanRepository;
import pt.studymatch.service.AcademicYearService;
import pt.studymatch.service.DefaultAccountService;

@Component
public class DataInitializer implements CommandLineRunner {

    private final StudyPlanRepository studyPlanRepository;
    private final KnowledgeAreaRepository knowledgeAreaRepository;
    private final CourseUnitRepository courseUnitRepository;
    private final AcademicYearRepository academicYearRepository;
    private final AcademicYearService academicYearService;
    private final DefaultAccountService defaultAccountService;
    private final AccountSequenceRepository sequences;
    private final Map<String, KnowledgeArea> areas = new HashMap<>();

    public DataInitializer(StudyPlanRepository studyPlanRepository, KnowledgeAreaRepository knowledgeAreaRepository,
            CourseUnitRepository courseUnitRepository, AcademicYearRepository academicYearRepository,
            AcademicYearService academicYearService, DefaultAccountService defaultAccountService,
            AccountSequenceRepository sequences) {
        this.sequences = sequences;
        this.studyPlanRepository = studyPlanRepository;
        this.knowledgeAreaRepository = knowledgeAreaRepository;
        this.courseUnitRepository = courseUnitRepository;
        this.academicYearRepository = academicYearRepository;
        this.academicYearService = academicYearService;
        this.defaultAccountService = defaultAccountService;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (studyPlanRepository.count() == 0) {
            createKnowledgeAreas();
            createStudyPlan();
        }
        createAcademicYears();
        academicYearService.current();
        defaultAccountService.createMissingAccounts();
        if (!sequences.existsById(1L)) {
            sequences.save(new AccountSequence());
        }
    }

    private void createKnowledgeAreas() {
        area("PROG", "Programação", "Algoritmos, programação orientada a objetos e tecnologias web.");
        area("MAT", "Matemática e Física", "Análise, álgebra linear, física e estatística aplicada.");
        area("SI", "Sistemas de Informação e Engenharia de Software",
                "Engenharia de requisitos, qualidade de software e gestão de projetos.");
        area("SCR", "Sistemas de Computadores e Redes",
                "Arquitetura de computadores, sistemas operativos, redes e segurança.");
        area("DIA", "Dados e Inteligência Artificial", "Bases de dados, inteligência artificial e análise de dados.");
    }

    private void createStudyPlan() {
        StudyPlan plan = studyPlanRepository.save(new StudyPlan("Engenharia Informática", 3, 180));

        courseUnit(plan, "EI101", "Algoritmos e Programação", 6, 1, 1, "PROG");
        courseUnit(plan, "EI102", "Álgebra Linear", 6, 1, 1, "MAT");
        courseUnit(plan, "EI103", "Análise Matemática", 6, 1, 1, "MAT");
        courseUnit(plan, "EI104", "Arquitetura de Computadores", 6, 1, 1, "SCR");
        courseUnit(plan, "EI105", "Introdução à Engenharia Informática", 6, 1, 1, null);
        courseUnit(plan, "EI106", "Programação Orientada a Objetos", 6, 1, 2, "PROG");
        courseUnit(plan, "EI107", "Laboratório de Programação", 6, 1, 2, "PROG");
        courseUnit(plan, "EI108", "Física Aplicada", 6, 1, 2, "MAT");
        courseUnit(plan, "EI109", "Sistemas Operativos", 6, 1, 2, "SCR");
        courseUnit(plan, "EI110", "Análise de Sistemas", 6, 1, 2, "SI");

        courseUnit(plan, "EI201", "Tecnologias Web", 6, 2, 1, "PROG");
        courseUnit(plan, "EI202", "Engenharia de Software", 6, 2, 1, "SI");
        courseUnit(plan, "EI203", "Probabilidades e Estatística", 6, 2, 1, "MAT");
        courseUnit(plan, "EI204", "Bases de Dados", 6, 2, 1, "DIA");
        courseUnit(plan, "EI205", "Redes de Computadores", 6, 2, 1, "SCR");
        courseUnit(plan, "EI206", "Qualidade de Software", 6, 2, 2, "SI");
        courseUnit(plan, "EI207", "Engenharia de Requisitos", 6, 2, 2, "SI");
        courseUnit(plan, "EI208", "Gestão de Projetos", 6, 2, 2, "SI");
        courseUnit(plan, "EI209", "Inteligência Artificial", 6, 2, 2, "DIA");
        courseUnit(plan, "EI210", "Computação Distribuída", 6, 2, 2, "SCR");

        courseUnit(plan, "EI301", "Segurança Informática", 6, 3, 1, "SCR");
        courseUnit(plan, "EI302", "Análise de Dados", 6, 3, 1, "DIA");
        courseUnit(plan, "EI303", "Computação Gráfica", 6, 3, 1, "PROG");
        courseUnit(plan, "EI304", "Sistemas de Informação", 6, 3, 1, "SI");
        courseUnit(plan, "EI305", "Programação Móvel", 6, 3, 1, "PROG");
        courseUnit(plan, "EI306", "Aprendizagem Automática", 6, 3, 2, "DIA");
        courseUnit(plan, "EI307", "Projeto Final", 12, 3, 2, null);
        courseUnit(plan, "EI308", "Estágio", 12, 3, 2, null);
    }

    private void createAcademicYears() {
        for (int year = 2022; year <= 2026; year++) {
            AcademicYear candidate = AcademicYear.startingIn(year);
            if (academicYearRepository.findByStartDate(candidate.getStartDate()).isEmpty()) {
                academicYearRepository.save(candidate);
            }
        }
    }

    private void area(String key, String name, String description) {
        KnowledgeArea area = knowledgeAreaRepository.findAll().stream()
                .filter(existing -> existing.hasSameName(name))
                .findFirst()
                .orElseGet(() -> knowledgeAreaRepository.save(new KnowledgeArea(name, description)));
        areas.put(key, area);
    }

    private void courseUnit(StudyPlan plan, String code, String name, int ects, int curricularYear, int semester,
            String areaKey) {
        CourseUnit courseUnit = new CourseUnit(plan, code, name, ects, curricularYear, semester,
                CourseUnitType.MANDATORY);
        if (areaKey != null) {
            courseUnit.setKnowledgeArea(areas.get(areaKey));
        }
        courseUnitRepository.save(courseUnit);
    }

}
