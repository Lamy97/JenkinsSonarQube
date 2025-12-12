package com.novelis.springboot.tp;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = TPController.class)
@ContextConfiguration(classes = TestApplication.class) // remove if you already have a real @SpringBootApplication
class TPControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void index_shouldReturnGreeting() throws Exception {
        mockMvc.perform(get("/tp/"))
                .andExpect(status().isOk())
                .andExpect(content().string("Greetings from TPController!"));
    }
}
