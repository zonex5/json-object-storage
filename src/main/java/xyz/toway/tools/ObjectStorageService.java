package xyz.toway.tools;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import xyz.toway.tools.storage.ObjectStorage;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;
import java.util.Properties;

import static xyz.toway.tools.Constants.ERROR_KEY_NULL;
import static xyz.toway.tools.Constants.ERROR_OBJECT_NULL;

public class ObjectStorageService extends BaseStorageService {

    private final Gson gson, convertGson;

    private final ObjectStorage storage;

    private final boolean binary;

    public ObjectStorageService(String fileName) throws Exception {
        super(fileName);
        gson = new GsonBuilder().setPrettyPrinting().create();
        convertGson = new GsonBuilder().create();
        binary = Boolean.parseBoolean(getProperties().getProperty("binary", "false"));
        storage = new ObjectStorage();
        initObjectStorage();
    }

    private void initObjectStorage() {
        try {
            var str = gson.fromJson(readFromFile(binary), ObjectStorage.class);
            if (Objects.isNull(str)) {
                storage.setObjects(new HashMap<>());
                storage.setCollections(new HashMap<>());
                commit();
            } else {
                storage.setObjects(str.getObjects());
                storage.setCollections(str.getCollections());
            }
        } catch (Exception e) {
            throw new RuntimeException("The storage file is corrupted or has incompatible format.");
        }
    }

    private <T extends IStorableObject> T convert(String value, Class<T> clazz) {
        Objects.requireNonNull(clazz, "Class<T> can't be null.");
        return (Objects.isNull(value) || value.isBlank()) ? null : convertGson.fromJson(value, clazz);
    }

    public <T extends IStorableObject> T addObject(String key, T object) {
        Objects.requireNonNull(key, ERROR_KEY_NULL);
        Objects.requireNonNull(object, ERROR_OBJECT_NULL);

        if (Objects.isNull(object.getUid())) {
            object.setUid(uuid());
        }
        storage.getObjects().put(key, gson.toJson(object));
        return object;
    }

    public <T extends IStorableObject> Optional<T> getObject(String key, Class<T> clazz) {
        Objects.requireNonNull(key, ERROR_KEY_NULL);

        return Optional.ofNullable(storage.getObjects().get(key))
                .map(json -> convert(json, clazz));
    }

    public void removeObject(String key) {
        storage.getObjects().remove(key);
    }

    public boolean contains(String key) {
        return storage.getObjects().containsKey(key);
    }

    public void commit() {
        writeToFile(gson.toJson(storage), binary);
    }

    public <T extends IStorableObject> Collection<T> getCollection(String name, Class<T> clazz) {
        Objects.requireNonNull(name, ERROR_KEY_NULL);

        Type type = TypeToken.getParameterized(Collection.class, clazz).getType();

        Collection<T> collection;
        var json = storage.getCollections().get(name);
        if (json != null) {
            collection = gson.fromJson(json, type);
        } else {
            collection = new Collection<>();
            storage.getCollections().put(name, convertGson.toJson(collection));
            //commit();
        }
        collection.setName(name);
        collection.setCommitFunction(this::collectionCommit);
        return collection;
    }

    private <T extends IStorableObject> void collectionCommit(Collection<T> collection) {
        storage.getCollections().put(collection.getName(), convertGson.toJson(collection));
        commit();
    }
}
