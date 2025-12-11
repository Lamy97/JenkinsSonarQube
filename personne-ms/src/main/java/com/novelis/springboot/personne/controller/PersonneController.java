package com.novelis.springboot.personne.controller;

import com.novelis.springboot.personne.domain.Personne;
import com.novelis.springboot.personne.service.PersonneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/personne")
public class PersonneController {

    @Autowired
    private PersonneService personneService;

    @GetMapping("/all")
    public List<Personne> recupererList() {
        return personneService.recupererPersonnes();
    }

    @PostMapping(value = "/add", consumes = "application/json")
    public ResponseEntity<Boolean> add(@RequestBody Personne personne) {
        personneService.ajouterPersonne(personne);
        return new ResponseEntity<>(true, HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable("id") Integer id) throws Exception {
        personneService.deletePersonne(id);
        return new ResponseEntity<>(true, HttpStatus.OK);
    }
}
