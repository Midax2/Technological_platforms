package org.example;

import org.mockito.Mockito;
import org.testng.annotations.Test;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class MageControllerTest {


        @Test
        public void testDeleteNonExistingMage() {
            MageRepository repositoryMock = Mockito.mock(MageRepository.class);

            MageController controller = new MageController(repositoryMock);
            String result = controller.delete("NonExistingMage");

            assertThat(result).isEqualTo("not found");
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
