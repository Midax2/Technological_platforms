package org.example;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class MageControllerTest {
    @Test
    public void testDeleteNonExistingMage() {
        MageRepository repositoryMock = Mockito.mock(MageRepository.class);

        MageController controller = new MageController(repositoryMock);
        String result = controller.delete("NonExistingMage");

        assertThat(result).isEqualTo("not found");
    }

    @Test

    public void testDeleteExistingMage() {
        MageRepository repositoryMock = Mockito.mock(MageRepository.class);
        Mage existingMage = new Mage();
        existingMage.setName("ExistingMage");
        existingMage.setLevel(1);
        Mockito.when(repositoryMock.delete("ExistingMage")).thenReturn(Optional.of(existingMage));

        MageController controller = new MageController(repositoryMock);
        String result = controller.delete("ExistingMage");

        assertThat(result).isEqualTo("done");
    }

    @Test
    public void testFindNonExistingMage() {
        MageRepository repositoryMock = Mockito.mock(MageRepository.class);
        Mockito.when(repositoryMock.find("NonExistingMage")).thenReturn(Optional.empty());

        MageController controller = new MageController(repositoryMock);
        String result = controller.find("NonExistingMage");

        assertThat(result).isEqualTo("not found");
    }

    @Test

    public void testSaveExistingMage() {
        MageRepository repositoryMock = Mockito.mock(MageRepository.class);
        Mage existingMage = new Mage();
        existingMage.setName("ExistingMage");
        existingMage.setLevel(1);
        Mockito.when(repositoryMock.find("ExistingMage")).thenReturn(Optional.of(existingMage));

        MageController controller = new MageController(repositoryMock);
        String result = controller.save("ExistingMage", "1");

        assertThat(result).isEqualTo("bad request");
    }

    @Test
    public void testSaveNewMage() {
        MageRepository repositoryMock = Mockito.mock(MageRepository.class);
        Mage newMage = new Mage();
        newMage.setName("NewMage");
        newMage.setLevel(1);
        Mockito.when(repositoryMock.find("NewMage")).thenReturn(Optional.empty());

        MageController controller = new MageController(repositoryMock);
        String result = controller.save("NewMage", "1");

        assertThat(result).isEqualTo("done");
    }

    @Test
    public void testFindExistingMage() {
        MageRepository repositoryMock = Mockito.mock(MageRepository.class);
        Mage existingMage = new Mage();
        existingMage.setName("ExistingMage");
        existingMage.setLevel(1);
        Mockito.when(repositoryMock.find("ExistingMage")).thenReturn(Optional.of(existingMage));

        MageController controller = new MageController(repositoryMock);
        String result = controller.find("ExistingMage");

        assertThat(result).isEqualTo("Name: ExistingMage, Level: 1");
    }
}