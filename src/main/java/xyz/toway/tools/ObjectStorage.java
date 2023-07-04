package xyz.toway.tools;

import java.util.HashMap;
import java.util.Map;

public class ObjectStorage {
    private final Map<String, Object> objects = new HashMap<>();

    public Map<String, Object> getObjects() {
        return objects;
    }

    public boolean contains(String name) {
        return objects.containsKey(name);
    }

    public Object get(String object) {
        return objects.get(object);
    }

    public <T> void put(String name, T object) {
        objects.put(name, object);
    }
}
