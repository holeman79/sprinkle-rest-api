package com.kakaopay.sprinklerestapi.sprinkling.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kakaopay.sprinklerestapi.sprinkling.service.SprinklingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(SprinklingController.class)
@AutoConfigureRestDocs
public abstract class ApiDocumentationTest {
    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockBean
    protected SprinklingService sprinklingService;
}
