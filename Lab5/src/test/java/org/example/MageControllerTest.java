package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class MageControllerTest {

    @Mock
    private MageRepository repositoryMock;

    private MageController controller;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        controller = new MageController(repositoryMock);
    }

    @Test
    public void testDeleteNonExistingMage() {
        when(repositoryMock.delete("NonExistingMage")).thenReturn(Optional.empty());

        String result = controller.delete("NonExistingMage");

        assertThat(result).isEqualTo("Not found");
    }

    @Test
    public void testDeleteExistingMage() {
        Mage existingMage = new Mage("ExistingMage", 1);
        when(repositoryMock.delete("ExistingMage")).thenReturn(Optional.of(existingMage));

        String result = controller.delete("ExistingMage");

        assertThat(result).isEqualTo("Done");
    }

    @Test
    public void testFindNonExistingMage() {
        when(repositoryMock.find("NonExistingMage")).thenReturn(Optional.empty());

        String result = controller.find("NonExistingMage");

        assertThat(result).isEqualTo("Not found");
    }

    @Test
    public void testSaveExistingMage() {
        Mage existingMage = new Mage("ExistingMage", 1);
        when(repositoryMock.find("ExistingMage")).thenReturn(Optional.of(existingMage));

        String result = controller.save("ExistingMage", "1");

        assertThat(result).isEqualTo("An object with name ExistingMage already exists.");
    }

    @Test
    public void testSaveNewMage() {
        when(repositoryMock.find("NewMage")).thenReturn(Optional.empty());

        String result = controller.save("NewMage", "1");

        assertThat(result).isEqualTo("Done");
    }

    @Test
    public void testFindExistingMage() {
        Mage existingMage = new Mage("ExistingMage", 1);
        when(repositoryMock.find("ExistingMage")).thenReturn(Optional.of(existingMage));

        String result = controller.find("ExistingMage");

        assertThat(result).isEqualTo("Name: ExistingMage, Level: 1");
    }
}
