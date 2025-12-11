package com.novelis.springboot.personne.service;

import com.novelis.springboot.personne.domain.Personne;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PersonneServiceImp implements PersonneService {
    private static Integer id = 0;
    private static List<Personne> personnes = new ArrayList<>();

    @Override
    public List<Personne> recupererPersonnes() {
        return personnes;
    }

    @Override
    public void ajouterPersonne(Personne personne) {
        id += 1;
        personne.setId(id);
        personnes.add(personne);
    }

    @Override
    public void deletePersonne(Integer id) throws Exception {
        Optional<Personne> personneToDelete = personnes.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst();
        if (personneToDelete.isPresent()) {
            personnes.remove(personneToDelete.get());
        } else {
            throw new Exception("Personne non trouvée avec id: " + id);
        }
    }
}
