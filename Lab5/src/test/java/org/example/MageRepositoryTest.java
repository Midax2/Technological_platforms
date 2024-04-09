package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class MageRepositoryTest {

    private MageRepository repository;

    @BeforeEach
    void setUp() {
        repository = new MageRepository();
    }

    @Test
    void testFindNonExistingMage() {
        Optional<Mage> result = repository.find("NonExistingMage");

        assertThat(result).isEmpty();
    }

    @Test
    void testFindExistingMage() {
        Mage mage = new Mage("ExistingMage", 1);
        repository.save(mage);

        Optional<Mage> result = repository.find("ExistingMage");

        assertThat(result).isPresent().contains(mage);
    }

    @Test
    void testDeleteNonExistingMage() {
        Optional<Mage> result = repository.delete("NonExistingMage");

        assertThat(result).isEmpty();
    }

    @Test
    void testDeleteExistingMage() {
        Mage mage = new Mage("ExistingMage", 1);
        repository.save(mage);

        Optional<Mage> result = repository.delete("ExistingMage");

        assertThat(result).isPresent().contains(mage);
    }

    @Test
    void testSaveExistingMage() {
        Mage mage = new Mage("ExistingMage", 1);
        repository.save(mage);

        String result = repository.save(mage);

        assertThat(result).isEqualTo("An object with name ExistingMage already exists.");
    }

    @Test
    void testSaveNewMage() {
        Mage mage = new Mage("NewMage", 1);

        String result = repository.save(mage);

        assertThat(result).isEqualTo("Done");
    }
}
