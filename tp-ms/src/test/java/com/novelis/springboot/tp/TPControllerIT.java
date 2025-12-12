package com.novelis.springboot.tp;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(
        classes = TestApplication.class, // replace with your real application class if exists
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
class TPControllerIT {

    @LocalServerPort
    int port;

    @Autowired
    TestRestTemplate restTemplate;

    @Test
    void getTp_shouldReturnGreeting() {
        ResponseEntity<String> response =
                restTemplate.getForEntity("http://localhost:" + port + "/tp/", String.class);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isEqualTo("Greetings from TPController!");
    }
}
