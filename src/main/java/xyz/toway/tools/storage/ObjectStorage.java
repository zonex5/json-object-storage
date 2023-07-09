package xyz.toway.tools.storage;

import java.util.Map;

public class ObjectStorage {
    private Map<String, String> objects;
    private Map<String, String> collections;

    public Map<String, String> getObjects() {
        return objects;
    }

    public void setObjects(Map<String, String> objects) {
        this.objects = objects;
    }

    public Map<String, String> getCollections() {
        return collections;
    }

    public void setCollections(Map<String, String> collections) {
        this.collections = collections;
    }
}
