package com.novelis.springboot.personne.service;

import com.novelis.springboot.personne.domain.Personne;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class PersonneServiceImpTest {

    private PersonneServiceImp service;

    @BeforeEach
    void setUp() throws Exception {
        service = new PersonneServiceImp();
        resetStaticState();
    }

    @Test
    void recupererPersonnes_shouldReturnEmptyListInitially() {
        List<Personne> personnes = service.recupererPersonnes();
        assertThat(personnes).isEmpty();
    }

    @Test
    void ajouterPersonne_shouldAssignIncrementedIdAndStorePerson() {
        Personne p = new Personne();
        p.setNom("Mohammed"); // adapt fields you have

        service.ajouterPersonne(p);

        assertThat(service.recupererPersonnes()).hasSize(1);
        assertThat(service.recupererPersonnes().get(0).getId()).isEqualTo(1);
        assertThat(service.recupererPersonnes().get(0).getNom()).isEqualTo("Mohammed");
    }

    @Test
    void ajouterPersonne_shouldIncrementIds() {
        Personne p1 = new Personne();
        p1.setNom("A");

        Personne p2 = new Personne();
        p2.setNom("B");

        service.ajouterPersonne(p1);
        service.ajouterPersonne(p2);

        assertThat(service.recupererPersonnes()).hasSize(2);
        assertThat(service.recupererPersonnes().get(0).getId()).isEqualTo(1);
        assertThat(service.recupererPersonnes().get(1).getId()).isEqualTo(2);
    }

    @Test
    void deletePersonne_shouldRemoveExistingPerson() throws Exception {
        Personne p = new Personne();
        p.setNom("ToDelete");
        service.ajouterPersonne(p);

        service.deletePersonne(1);

        assertThat(service.recupererPersonnes()).isEmpty();
    }

    @Test
    void deletePersonne_shouldThrowWhenIdNotFound() {
        assertThatThrownBy(() -> service.deletePersonne(999))
                .isInstanceOf(Exception.class)
                .hasMessageContaining("Personne non trouvée avec id: 999");
    }

    /**
     * Because the service uses static fields, we MUST reset them for test isolation.
     */
    private void resetStaticState() throws Exception {
        Field idField = PersonneServiceImp.class.getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(null, 0);

        Field personnesField = PersonneServiceImp.class.getDeclaredField("personnes");
        personnesField.setAccessible(true);
        ((List<?>) personnesField.get(null)).clear();
    }
}
