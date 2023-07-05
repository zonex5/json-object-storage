package xyz.toway.tools;

import com.google.gson.Gson;

import java.util.Objects;

public class JsonObjectStorage {

    private final Gson gson;

    private final StorageFile storageFile;

    private final ObjectStorage storage;

    public JsonObjectStorage(String storageFile) throws Exception {
        this.storageFile = new StorageFile(storageFile);
        gson = new Gson();
        storage = createObjectStorage();
    }

    private ObjectStorage createObjectStorage() {
        ObjectStorage str;
        try {
            str = gson.fromJson(storageFile.read(), ObjectStorage.class);
        } catch (Exception e) {
            throw new RuntimeException("The storage file is corrupted.");
        }
        if (Objects.isNull(str)) {
            str = new ObjectStorage();
            storageFile.write(gson.toJson(str));
        }
        return str;
    }

    public boolean containsObject(String name) {
        return storage.contains(name);
    }

    /**
     * Check if object is instance of class.
     *
     * @param name  name of stored object
     * @param clazz checked class
     * @return true - if object is instance of clazz, false - otherwise or object not fount in the storage
     */
    public <T> boolean isObject(String name, Class<T> clazz) {
        if (!containsObject(name))
            return false;
        else {
            try {
                return gson.fromJson(gson.toJson(storage.get(name)), clazz) != null;  //todo serialize 'LinkedHashMap to object'
            } catch (Exception e) {
                return false;
            }
        }
    }

    public <T> T getObject(String name, Class<T> clazz) {
        if (!storage.contains(name)) {
            throw new RuntimeException("Can't find storage '" + name + "'");
        }
        try {
            return gson.fromJson(gson.toJson(storage.get(name)), clazz);  //todo serialize 'LinkedHashMap to object'
        } catch (Exception e) {
            throw new RuntimeException("The storage '" + name + "' can't be casted to " + clazz.getName());
        }
    }

    public <T> void setObject(String name, T object) {
        storage.put(name, object);
    }

    public void commit() {
        storageFile.write(gson.toJson(storage));
    }
}
