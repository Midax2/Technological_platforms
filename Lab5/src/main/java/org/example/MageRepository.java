package org.example;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class MageRepository {
    private final Map<String, Mage> collection = new HashMap<>();

    public Optional<Mage> find(String name) {
        return Optional.ofNullable(collection.get(name));
    }

    public Optional<Mage> delete(String name) {
        if (collection.containsKey(name)) {
            return Optional.of(collection.remove(name));
        } else {
            return Optional.empty();
        }
    }

    public String save(Mage mage) {
        if (collection.containsKey(mage.getName())) {
            return "An object with name " + mage.getName() + " already exists.";
        } else {
            collection.put(mage.getName(), mage);
            return "Done";
        }
    }
}
