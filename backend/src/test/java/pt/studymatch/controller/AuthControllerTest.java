package pt.studymatch.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import pt.studymatch.repository.UserAccountRepository;
import pt.studymatch.service.StudentService;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Autowired
    private StudentService studentService;

    @Test
    void signInWithEmailReturnsTheRole() throws Exception {
        // Confirma que o login por email devolve o tipo de conta certo.
        mockMvc.perform(post("/auth/login").with(csrf()).contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"34567@studymatch.com\",\"password\":\"students\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.role").value("STUDENT"))
                .andExpect(jsonPath("$.name").value("Estudante"));
    }

    @Test
    void signInWithOnlyTheNumberAlsoWorks() throws Exception {
        mockMvc.perform(post("/auth/login").with(csrf()).contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"12345\",\"password\":\"admin\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.role").value("ACADEMIC_ADMINISTRATOR"));
    }

    @Test
    void wrongPasswordIsRejected() throws Exception {
        mockMvc.perform(post("/auth/login").with(csrf()).contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"34567@studymatch.com\",\"password\":\"wrong\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Email ou palavra-passe incorretos."));
    }

    @Test
    void emptyFieldsAreRejected() throws Exception {
        mockMvc.perform(post("/auth/login").with(csrf()).contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"\",\"password\":\"\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors.email").exists());
    }

    @Test
    void anonymousUserCannotSeeProfiles() throws Exception {
        mockMvc.perform(get("/students/1/profile"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void studentSeesOwnProfileButNotTheProfileOfOthers() throws Exception {
        MockHttpSession session = signIn("34567", "students");
        Long studentId = idOf("34567");
        Long otherStudentId = studentService.register("Outro estudante de teste", 2026, null).getStudent().getId();

        mockMvc.perform(get("/students/" + studentId + "/profile").session(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.areas[0].name").value("Programação"));
        mockMvc.perform(get("/students/" + otherStudentId + "/profile").session(session))
                .andExpect(status().isForbidden());
    }

    @Test
    void onlyAdministratorsCanListStudents() throws Exception {
        mockMvc.perform(get("/students").session(signIn("34567", "students")))
                .andExpect(status().isForbidden());
        mockMvc.perform(get("/students").session(signIn("12345", "admin")))
                .andExpect(status().isOk());
    }

    private MockHttpSession signIn(String number, String password) throws Exception {
        return (MockHttpSession) mockMvc.perform(post("/auth/login").with(csrf()).contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"" + number + "\",\"password\":\"" + password + "\"}"))
                .andExpect(status().isOk())
                .andReturn().getRequest().getSession();
    }

    private Long idOf(String number) {
        return userAccountRepository.findByEmailIgnoreCase(number + "@studymatch.com").orElseThrow().getId();
    }
}
