package com.parachute.booking.admin;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.time.Instant;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
class AdminServiceCreateTestIntegrated {

    @Autowired
    AdminRepository adminRepository;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @Test
    void createAdmin_andReturnStatusCode200() throws Exception {
        //given
        adminRepository.deleteAll();
        AdminDto adminDto = new AdminDto.AdminDtoBuilder()
                .login("Admin1")
                .password("Admin pass")
                .email("admin@gmail.com")
                .build();

        String requestBody = objectMapper.writeValueAsString(adminDto);
        MockHttpServletRequestBuilder post = post("/admin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody);

        //when
        MvcResult result = mockMvc.perform(post).andReturn();

        //then
        MockHttpServletResponse response = result.getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
        List<Admin> admins = adminRepository.findAll();
        assertThat(admins.size()).isEqualTo(1);
        assertThat(admins.get(0)).satisfies(admin -> {
            assertThat(admin.getLogin()).isEqualTo("Admin1");
            assertThat(admin.getPassword()).isEqualTo("Admin pass");
            assertThat(admin.getEmail()).isEqualTo("admin@gmail.com");
        });
    }

    @Test
    void createAdmin_andReturnStatusCode400() throws Exception {
        //given
        adminRepository.deleteAll();
        AdminDto adminDto = new AdminDto.AdminDtoBuilder()
                .login("")
                .password("Admin pass")
                .email("admin@gmail.com")
                .build();

        String requestBody = objectMapper.writeValueAsString(adminDto);
        MockHttpServletRequestBuilder post = post("/admin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody);

        //when
        MvcResult result = mockMvc.perform(post).andReturn();

        //then
        MockHttpServletResponse response = result.getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        List<Admin> admins = adminRepository.findAll();
        assertThat(admins).isEmpty();
    }


}
