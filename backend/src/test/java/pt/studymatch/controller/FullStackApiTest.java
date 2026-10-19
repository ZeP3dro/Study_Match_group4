package pt.studymatch.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import pt.studymatch.domain.UserAccount;
import pt.studymatch.repository.UserAccountRepository;
import pt.studymatch.service.AcademicYearService;
import pt.studymatch.service.CourseUnitService;
import pt.studymatch.service.StudentService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/** Exercises HTTP, authorization, real services, profile rules and persisted data together. */
@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:defaultaccounts;MODE=MySQL;DATABASE_TO_LOWER=TRUE;DB_CLOSE_DELAY=-1"})
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class FullStackApiTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper json;
    @Autowired
    private UserAccountRepository accounts;
    @Autowired
    private CourseUnitService courseUnits;
    @Autowired
    private AcademicYearService academicYears;
    @Autowired
    private StudentService students;

    @Test
    void allThreeDefaultAccountsShouldSignInWithTheirOwnPassword() throws Exception {
        signIn("12345", "admin");
        signIn("23456", "teacher");
        MockHttpSession studentSession = signIn("34567", "students");
        mvc.perform(get("/auth/me").session(studentSession))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.role").value("STUDENT"))
                .andExpect(jsonPath("$.password").doesNotExist());
    }

    @Test
    void missingOrWrongCsrfTokenShouldRejectLogin() throws Exception {
        String credentials = json.writeValueAsString(Map.of("email", "12345", "password", "admin"));
        mvc.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON).content(credentials))
                .andExpect(status().isForbidden());
        mvc.perform(post("/auth/login").with(csrf().useInvalidToken())
                        .contentType(MediaType.APPLICATION_JSON).content(credentials))
                .andExpect(status().isForbidden());
    }

    @Test
    void browserCsrfFlowShouldRotateSessionAndAllowLogout() throws Exception {
        MvcResult tokenResponse = mvc.perform(get("/auth/csrf"))
                .andExpect(status().isOk()).andReturn();
        JsonNode token = json.readTree(tokenResponse.getResponse().getContentAsString());
        MockHttpSession session = (MockHttpSession) tokenResponse.getRequest().getSession();
        String previousId = session.getId();
        mvc.perform(post("/auth/login").session(session)
                        .header(token.get("headerName").asText(), token.get("token").asText())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"12345\",\"password\":\"admin\"}"))
                .andExpect(status().isOk());
        assertThat(session.getId()).isNotEqualTo(previousId);
        mvc.perform(post("/auth/logout").session(session)
                        .header(token.get("headerName").asText(), token.get("token").asText()))
                .andExpect(status().isForbidden());
        JsonNode renewed = json.readTree(mvc.perform(get("/auth/csrf").session(session))
                .andReturn().getResponse().getContentAsString());
        mvc.perform(post("/auth/logout").session(session)
                        .header(renewed.get("headerName").asText(), renewed.get("token").asText()))
                .andExpect(status().isNoContent());
        assertThat(session.isInvalid()).isTrue();
        mvc.perform(get("/auth/me")).andExpect(status().isUnauthorized());
    }

    @Test
    void disabledAccountAndExternalEmailShouldBeRejected() throws Exception {
        UserAccount teacher = accounts.findByNumber("23456").orElseThrow();
        teacher.deactivate();
        mvc.perform(post("/auth/login").with(csrf()).contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"23456\",\"password\":\"teacher\"}"))
                .andExpect(status().isForbidden());
        mvc.perform(post("/auth/login").with(csrf()).contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"34567@example.com\",\"password\":\"students\"}"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void studentAndTeacherShouldNotChangeAcademicData() throws Exception {
        Long studentId = accounts.findByNumber("34567").orElseThrow().getId();
        for (MockHttpSession session : new MockHttpSession[] {
                signIn("34567", "students"), signIn("23456", "teacher")}) {
            mvc.perform(get("/students").session(session)).andExpect(status().isForbidden());
            mvc.perform(post("/students/" + studentId + "/enrolments").session(session).with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"courseUnitId\":1,\"academicYearId\":1}"))
                    .andExpect(status().isForbidden());
        }
    }

    @Test
    void shouldPersistStudentAndGradeAndImmediatelyReplaceColdStartWithAcademicEvidence() throws Exception {
        MockHttpSession admin = signIn("12345", "admin");
        MvcResult created = mvc.perform(post("/students").session(admin).with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Cenário Completo\",\"firstEnrolmentYear\":2026,\"entryGrade\":150}"))
                .andExpect(status().isCreated()).andReturn();
        long studentId = json.readTree(created.getResponse().getContentAsString()).get("id").asLong();
        mvc.perform(get("/students/" + studentId + "/profile").session(admin))
                .andExpect(jsonPath("$.areas[0].source").value("ENTRY_GRADE"))
                .andExpect(jsonPath("$.areas[0].value").value(15));

        long courseUnitId = courseUnits.findAll().getFirst().getId();
        long yearId = academicYears.current().getId();
        String enrolment = json.writeValueAsString(Map.of("courseUnitId", courseUnitId, "academicYearId", yearId));
        MvcResult enrolled = mvc.perform(post("/students/" + studentId + "/enrolments")
                        .session(admin).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(enrolment))
                .andExpect(status().isCreated()).andReturn();
        long enrolmentId = json.readTree(enrolled.getResponse().getContentAsString()).get("id").asLong();
        mvc.perform(put("/students/" + studentId + "/enrolments/" + enrolmentId + "/grade")
                        .session(admin).with(csrf()).contentType(MediaType.APPLICATION_JSON).content("{\"grade\":18}"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.grade").value(18));
        mvc.perform(get("/students/" + studentId + "/profile").session(admin))
                .andExpect(jsonPath("$.universityAverage").value(18))
                .andExpect(jsonPath("$.areas[0].source").value("AREA_AVERAGE"))
                .andExpect(jsonPath("$.areas[1].source").value("UNIVERSITY_AVERAGE"));
        mvc.perform(put("/students/" + studentId + "/enrolments/" + enrolmentId + "/grade")
                        .session(admin).with(csrf()).contentType(MediaType.APPLICATION_JSON).content("{\"grade\":10}"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.grade").value(10));

        mvc.perform(get("/students/" + studentId + "/profile").session(admin))
                .andExpect(jsonPath("$.universityAverage").value(10));
        long otherStudentId = students.register("Outra Pessoa", 2026, null).getStudent().getId();
        mvc.perform(put("/students/" + otherStudentId + "/enrolments/" + enrolmentId + "/grade")
                        .session(admin).with(csrf()).contentType(MediaType.APPLICATION_JSON).content("{\"grade\":20}"))
                .andExpect(status().isNotFound());
    }

    private MockHttpSession signIn(String number, String password) throws Exception {
        return (MockHttpSession) mvc.perform(post("/auth/login").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json.writeValueAsString(Map.of("email", number, "password", password))))
                .andExpect(status().isOk()).andReturn().getRequest().getSession();
    }
}
