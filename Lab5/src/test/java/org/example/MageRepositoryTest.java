package org.example;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
class MageRepositoryTest {
    @Test
    public void testFindNonExistingMage() {
        // Arrange
        MageRepository repository = new MageRepository();

        // Act
        Optional<Mage> result = repository.find("NonExistingMage");

        // Assert
        assertThat(result).isEmpty();
    }

    @Test
    public void testFindExistingMage() {
        // Arrange
        MageRepository repository = new MageRepository();
        Mage mage = new Mage();
        mage.setName("ExistingMage");
        mage.setLevel(1);
        repository.save(mage);

        // Act
        Optional<Mage> result = repository.find("ExistingMage");

        // Assert
        assertThat(result).isPresent().contains(mage);
    }

    @Test
    public void testDeleteNonExistingMage() {
        // Arrange
        MageRepository repository = new MageRepository();

        // Act
        Optional<Mage> result = repository.delete("NonExistingMage");

        // Assert
        assertThat(result).isEmpty();
    }

    @Test

    public void testDeleteExistingMage() {
        // Arrange
        MageRepository repository = new MageRepository();
        Mage mage = new Mage();
        mage.setName("ExistingMage");
        mage.setLevel(1);
        repository.save(mage);

        // Act
        Optional<Mage> result = repository.delete("ExistingMage");

        // Assert
        assertThat(result).isPresent().contains(mage);
    }

    @Test
    public void testSaveExistingMage() {
        // Arrange
        MageRepository repository = new MageRepository();
        Mage mage = new Mage();
        mage.setName("ExistingMage");
        mage.setLevel(1);
        repository.save(mage);

        // Act
        String result = repository.save(mage);

        // Assert
        assertThat(result).isEqualTo("bad request");
    }

    @Test
    public void testSaveNewMage() {
        // Arrange
        MageRepository repository = new MageRepository();
        Mage mage = new Mage();
        mage.setName("NewMage");
        mage.setLevel(1);

        // Act
        String result = repository.save(mage);

        // Assert
        assertThat(result).isEqualTo("done");
    }
}