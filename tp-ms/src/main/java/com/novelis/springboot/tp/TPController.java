package com.novelis.springboot.tp;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tp")
public class TPController {

    @GetMapping("/")
    public String index() {
        return "Greetings from TPController!";
    }
}
