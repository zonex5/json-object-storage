package xyz.toway.tools;

import java.util.Map;

class ObjectStorageUnit {
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
