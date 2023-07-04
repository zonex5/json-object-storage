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
        storageFile.write(gson.toJson(storage));
    }
}
