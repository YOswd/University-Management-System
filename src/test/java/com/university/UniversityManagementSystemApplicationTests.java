package com.university;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:university;DB_CLOSE_DELAY=-1",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "app.security.initial-admin.username=admin",
        "app.security.initial-admin.password=test-admin-password-123"
})

@AutoConfigureMockMvc
class UniversityManagementSystemApplicationTests {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void adminCanPerformCrudAcrossAllResources() throws Exception {
        var admin = user("admin").roles("ADMIN");
        MvcResult createdAdmin = mockMvc.perform(post("/api/admins")
                .with(admin)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"University Admin\"}"))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andReturn();
        String adminId = resourceId(createdAdmin);
        mockMvc.perform(get("/api/admins/{id}", adminId).with(admin))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("University Admin"));
        mockMvc.perform(put("/api/admins/{id}", adminId)
                .with(admin)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Updated Admin\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Admin"));
        mockMvc.perform(delete("/api/admins/{id}", adminId).with(admin).with(csrf()))
                .andExpect(status().isNoContent());

        MvcResult createdStudent = mockMvc.perform(post("/api/students")
                .with(admin)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Ada Student\",\"dept\":\"Science\"}"))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andReturn();
        String studentId = resourceId(createdStudent);
        mockMvc.perform(get("/api/students/{id}", studentId).with(admin))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Ada Student"));
        mockMvc.perform(put("/api/students/update/{id}", studentId)
                .with(admin)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Updated Student\",\"dept\":\"Mathematics\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Student"));
        mockMvc.perform(delete("/api/students/delete/{id}", studentId).with(admin).with(csrf()))
                .andExpect(status().isNoContent());

        MvcResult createdTeacher = mockMvc.perform(post("/api/teachers")
                .with(admin)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Ada Teacher\",\"pos\":\"Professor\",\"dept\":\"Science\"}"))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andReturn();
        String teacherId = resourceId(createdTeacher);
        mockMvc.perform(get("/api/teachers/{id}", teacherId).with(admin))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Ada Teacher"));
        mockMvc.perform(put("/api/teachers/update/{id}", teacherId)
                .with(admin)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Updated Teacher\",\"pos\":\"Lecturer\",\"dept\":\"Arts\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Teacher"));
        mockMvc.perform(delete("/api/teachers/delete/{id}", teacherId).with(admin).with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    void roleBasedAccessIsEnforced() throws Exception {
        mockMvc.perform(get("/api/admins"))
                .andExpect(status().isUnauthorized());
        mockMvc.perform(get("/api/admins").with(user("student").roles("STUDENT")))
                .andExpect(status().isForbidden());
        mockMvc.perform(get("/api/students").with(user("student").roles("STUDENT")))
                .andExpect(status().isForbidden());
        mockMvc.perform(get("/api/teachers").with(user("student").roles("STUDENT")))
                .andExpect(status().isOk());
        mockMvc.perform(get("/api/students").with(user("teacher").roles("TEACHER")))
                .andExpect(status().isOk());
        mockMvc.perform(delete("/api/students/delete/1")
                .with(user("teacher").roles("TEACHER"))
                .with(csrf()))
                .andExpect(status().isForbidden());
    }

    @Test
    void invalidRequestsAndMissingRecordsReturnExpectedErrors() throws Exception {
        var admin = user("admin").roles("ADMIN");
        mockMvc.perform(post("/api/admins")
                .with(admin)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"\"}"))
                .andExpect(status().isBadRequest());
        mockMvc.perform(post("/api/students")
                .with(admin)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"\",\"dept\":\"Science\"}"))
                .andExpect(status().isBadRequest());
        mockMvc.perform(post("/api/teachers")
                .with(admin)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Ada\",\"pos\":\"\",\"dept\":\"Science\"}"))
                .andExpect(status().isBadRequest());

        mockMvc.perform(get("/api/admins/99999").with(admin))
                .andExpect(status().isNotFound());
        mockMvc.perform(get("/api/students/99999").with(admin))
                .andExpect(status().isNotFound());
        mockMvc.perform(get("/api/teachers/99999").with(admin))
                .andExpect(status().isNotFound());
    }

    private static String resourceId(MvcResult result) {
        String location = result.getResponse().getHeader("Location");
        return location.substring(location.lastIndexOf('/') + 1);
    }
}
