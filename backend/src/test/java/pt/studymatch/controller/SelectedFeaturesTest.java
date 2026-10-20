package pt.studymatch.controller;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import javax.imageio.ImageIO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityManager;
import pt.studymatch.repository.UserAccountRepository;
import pt.studymatch.repository.UserPhotoRepository;
import pt.studymatch.service.StudentService;
import pt.studymatch.service.CourseUnitService;
import pt.studymatch.service.AcademicYearService;
import static org.assertj.core.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class SelectedFeaturesTest {
    @Autowired MockMvc mvc;
    @Autowired ObjectMapper json;
    @Autowired UserAccountRepository accounts;
    @Autowired UserPhotoRepository photos;
    @Autowired StudentService students;
    @Autowired CourseUnitService units;
    @Autowired AcademicYearService years;
    @Autowired EntityManager entityManager;

    private MockHttpSession login(String number, String password) throws Exception {
        return (MockHttpSession)mvc.perform(post("/auth/login").with(csrf()).contentType(MediaType.APPLICATION_JSON)
            .content("{\"email\":\""+number+"\",\"password\":\""+password+"\"}"))
            .andExpect(status().isOk()).andReturn().getRequest().getSession();
    }
    private long id(String number) { return accounts.findByNumber(number).orElseThrow().getId(); }
    private byte[] image(int size) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageIO.write(new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB), "png", out);
        return out.toByteArray();
    }
    private void upload(long accountId, MockHttpSession session, byte[] bytes) throws Exception {
        mvc.perform(multipart("/users/"+accountId+"/photo").file(new MockMultipartFile("photo","profile.png","image/png",bytes))
            .with(request -> { request.setMethod("PUT"); return request; }).session(session).with(csrf()))
            .andExpect(status().isNoContent());
    }
    @Test
    void allThreeRolesCanAddAndReplaceTheirOwnPhotoPersistently() throws Exception {
        String[][] credentials={{"12345","admin"},{"23456","teacher"},{"34567","students"}};
        for (String[] pair:credentials) {
            MockHttpSession session=login(pair[0],pair[1]);
            long id=id(pair[0]);
            upload(id,session,image(2));
            byte[] replacement=image(3);
            upload(id,session,replacement);
            entityManager.flush(); entityManager.clear();
            assertThat(photos.findById(id).orElseThrow().getContent()).isEqualTo(replacement);
            mvc.perform(get("/users/"+id+"/photo").session(session)).andExpect(status().isOk())
                .andExpect(content().bytes(replacement));
            mvc.perform(get("/auth/me").session(session)).andExpect(jsonPath("$.hasPhoto").value(true));
        }
        assertThat(photos.count()).isEqualTo(3);
    }
    @Test
    void studentAndTeacherCannotReadOrChangeAnotherUsersPhoto() throws Exception {
        for (MockHttpSession session:new MockHttpSession[]{login("34567","students"),login("23456","teacher")}) {
            mvc.perform(get("/users/"+id("12345")+"/photo").session(session)).andExpect(status().isForbidden());
            mvc.perform(multipart("/users/"+id("12345")+"/photo").file(new MockMultipartFile("photo","a.png","image/png",image(2)))
                .with(request->{request.setMethod("PUT");return request;}).session(session).with(csrf()))
                .andExpect(status().isForbidden());
        }
    }
    @Test
    void administratorCannotEditStudentOrTeacherPhotos() throws Exception {
        MockHttpSession session=login("12345","admin");
        for (String number : new String[]{"34567", "23456"}) {
            mvc.perform(multipart("/users/"+id(number)+"/photo")
                .file(new MockMultipartFile("photo","a.png","image/png",image(2)))
                .with(request->{request.setMethod("PUT");return request;}).session(session).with(csrf()))
                .andExpect(status().isForbidden());
        }
        assertThat(photos.count()).isZero();
    }
    @Test
    void searchesMatchOnlyNumbersAndEmptySearchListsExistingPeople() throws Exception {
        MockHttpSession session=login("12345","admin");
        mvc.perform(get("/students").param("number","345").session(session)).andExpect(jsonPath("$[0].number").value("34567"));
        mvc.perform(get("/students").param("number","Estudante").session(session)).andExpect(jsonPath("$.length()").value(0));
        mvc.perform(get("/teachers").param("number","234").session(session)).andExpect(jsonPath("$[0].number").value("23456"));
        mvc.perform(get("/teachers").param("number","Docente").session(session)).andExpect(jsonPath("$.length()").value(0));
        mvc.perform(get("/teachers").session(session)).andExpect(jsonPath("$.length()").value(1));
        mvc.perform(get("/students").session(session)).andExpect(jsonPath("$.length()").value(1));
    }
    @Test
    void studentInformationIsSavedAndInitialPasswordIsOnlyReturnedOnRegistration() throws Exception {
        MockHttpSession session=login("12345","admin");
        String response=mvc.perform(post("/students").session(session).with(csrf()).contentType(MediaType.APPLICATION_JSON)
            .content("{\"name\":\"Ana Teste\",\"firstEnrolmentYear\":2026,\"entryGrade\":150}"))
            .andExpect(status().isCreated()).andExpect(jsonPath("$.initialPassword").isString()).andReturn().getResponse().getContentAsString();
        long id=json.readTree(response).get("id").asLong();
        mvc.perform(put("/students/"+id).session(session).with(csrf()).contentType(MediaType.APPLICATION_JSON)
            .content("{\"name\":\"Ana Atualizada\",\"firstEnrolmentYear\":2025,\"entryGrade\":180}"))
            .andExpect(status().isOk()).andExpect(jsonPath("$.initialPassword").doesNotExist());
        entityManager.flush();entityManager.clear();
        mvc.perform(get("/students/"+id).session(session)).andExpect(jsonPath("$.name").value("Ana Atualizada"))
            .andExpect(jsonPath("$.entryGrade").value(180)).andExpect(jsonPath("$.password").doesNotExist());
        mvc.perform(get("/students/"+id+"/profile").session(session)).andExpect(jsonPath("$.areas[0].value").value(18));
    }
    @Test
    void duplicateCourseUnitAndInvalidGradeAreRejected() throws Exception {
        MockHttpSession session=login("12345","admin");
        long studentId=id("34567");
        String body="{\"courseUnitId\":"+units.findAll().getFirst().getId()+",\"academicYearId\":"+years.current().getId()+"}";
        String result=mvc.perform(post("/students/"+studentId+"/enrolments").session(session).with(csrf())
            .contentType(MediaType.APPLICATION_JSON).content(body)).andExpect(status().isCreated()).andReturn().getResponse().getContentAsString();
        mvc.perform(post("/students/"+studentId+"/enrolments").session(session).with(csrf())
            .contentType(MediaType.APPLICATION_JSON).content(body)).andExpect(status().isConflict());
        long enrolmentId=json.readTree(result).get("id").asLong();
        mvc.perform(put("/students/"+studentId+"/enrolments/"+enrolmentId+"/grade").session(session).with(csrf())
            .contentType(MediaType.APPLICATION_JSON).content("{\"grade\":21}"))
            .andExpect(status().isBadRequest());
    }
}
