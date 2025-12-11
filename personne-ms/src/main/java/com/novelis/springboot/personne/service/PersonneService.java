package com.novelis.springboot.personne.service;

import com.novelis.springboot.personne.domain.Personne;
import java.util.List;

public interface PersonneService {
    List<Personne> recupererPersonnes();
    void ajouterPersonne(Personne personne);
    void deletePersonne(Integer id) throws Exception;
}