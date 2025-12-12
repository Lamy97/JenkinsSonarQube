package com.novelis.springboot.personne.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.novelis.springboot.personne.domain.Personne;
import com.novelis.springboot.personne.service.PersonneService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = PersonneController.class)
@org.springframework.test.context.ContextConfiguration(classes = com.novelis.springboot.personne.TestApplication.class)
class PersonneControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;

    @MockBean PersonneService personneService;

    @Test
    void recupererList_shouldReturn200AndJsonArray() throws Exception {
        Personne p1 = new Personne();
        p1.setId(1);
        p1.setNom("Mohammed");

        given(personneService.recupererPersonnes()).willReturn(List.of(p1));

        mockMvc.perform(get("/personne/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nom").value("Mohammed"));
    }

    @Test
    void add_shouldReturn201AndTrue() throws Exception {
        Personne p = new Personne();
        p.setNom("New");

        mockMvc.perform(post("/personne/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(p)))
                .andExpect(status().isCreated())
                .andExpect(content().string("true"));

        then(personneService).should(times(1)).ajouterPersonne(any(Personne.class));
    }

    @Test
    void delete_shouldReturn200AndTrue() throws Exception {
        willDoNothing().given(personneService).deletePersonne(1);

        mockMvc.perform(delete("/personne/delete/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        then(personneService).should(times(1)).deletePersonne(1);
    }

    @Test
    void delete_shouldReturn500_whenServiceThrows() throws Exception {
        willThrow(new Exception("Personne non trouvée avec id: 1"))
                .given(personneService).deletePersonne(1);

        mockMvc.perform(delete("/personne/delete/1"))
                .andExpect(status().isInternalServerError());
    }
}
