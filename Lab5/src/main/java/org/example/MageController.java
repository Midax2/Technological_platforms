package org.example;

import java.util.Optional;

public class MageController {
    private MageRepository repository;
    public MageController(MageRepository repository) {
        this.repository = repository;
    }
    public String find(String name) {
        Optional<Mage> mageOptional = repository.find(name);
        if (mageOptional.isPresent()) {
            Mage mage = mageOptional.get();
            return "Name: " + mage.getName() + ", Level: " + mage.getLevel();
        } else {
            return "not found";
        }
    }

    public String delete(String name) {
        Optional<Mage> deletedMage = repository.delete(name);
        return deletedMage.isPresent() ? "done" : "not found";
    }

    public String save(String name, String level) {
        try {
            int mageLevel = Integer.parseInt(level);
            Mage mage = new Mage();
            mage.setName(name);
            mage.setLevel(mageLevel);
            repository.save(mage);
            return "done";
        } catch (NumberFormatException e) {
            return "bad request";
        }
    }
}
