package org.example;

import java.util.Optional;

public class MageController {
    private final MageRepository repository;

    public MageController(MageRepository repository) {
        this.repository = repository;
    }

    public String find(String name) {
        Optional<Mage> mageOptional = repository.find(name);
        if (mageOptional.isPresent()) {
            Mage mage = mageOptional.get();
            return "Name: " + mage.getName() + ", Level: " + mage.getLevel();
        } else {
            return "Not found";
        }
    }

    public String delete(String name) {
        Optional<Mage> deletedMage = repository.delete(name);
        return deletedMage.isPresent() ? "Done" : "Not found";
    }

    public String save(String name, String level) {
        try {
            int mageLevel = Integer.parseInt(level);
            Mage mage = new Mage(name, mageLevel);
            if (repository.find(name).isPresent()) {
                return "An object with name " + mage.getName() + " already exists.";
            }
            repository.save(mage);
            return "Done";
        } catch (NumberFormatException e) {
            return "Bad data";
        }
    }
}
