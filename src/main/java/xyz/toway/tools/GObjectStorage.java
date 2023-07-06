package xyz.toway.tools;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;


/**
 * Object Storage based on gson
 */
public class GObjectStorage extends BaseStorage {

    private static final String ERROR_KEY = "The key must be non-null.";

    private final Gson gson;

    private final HashMap<String, IStorable> storage;

    private final HashMap<String, Collection> collections;

    private static final TypeToken<HashMap<String, Object>> storageTypeToken = new TypeToken<>() {
    };

    public GObjectStorage(String fileName) throws Exception {
        super(fileName);
        //gson = new Gson();
        gson = new GsonBuilder().setPrettyPrinting().create();
        collections = new HashMap<>();
        storage = getMainStorage();
    }

    /**
     * Get a main storage (or create, if it doesn't exist)
     */
    private HashMap<String, IStorable> getMainStorage() {
        HashMap<String, IStorable> storage;
        try {
            storage = gson.fromJson(readFromFile(), storageTypeToken.getType());
            if (Objects.isNull(storage)) {
                storage = new HashMap<>();
                commit(storage);
            }
        } catch (Exception e) {
            throw new RuntimeException("The storage file is corrupted or has incompatible format.");
        }
        return storage;
    }

    public void commit() {
        commit(storage);
    }

    private void commit(HashMap<String, IStorable> storage) {
        writeToFile(gson.toJson(storage));
    }

    public boolean contains(String name) {
        return storage.containsKey(name);
    }

    public <T extends IStorable> T setObject(String key, T object) {
        Objects.requireNonNull(key, ERROR_KEY);
        Objects.requireNonNull(object, "The object must be non-null.");

        if (Objects.isNull(object.getUid())) {
            object.setUid(uuid());
        }
        storage.put(getObjectName(key), object);
        return object;
    }

    public <T extends IStorable> Optional<T> getObject(String key, Class<T> clazz) {
        Objects.requireNonNull(key, ERROR_KEY);
        return Optional.ofNullable((Object) storage.get(getObjectName(key)))
                .map(obj -> gson.fromJson(gson.toJson(obj), clazz));
    }

    /**
     * Removes the mapping for the specified key from this map if present.
     */
    public void removeObject(String name) {
        storage.remove(getObjectName(name));
    }

    public Collection getCollection(String name) {
        Objects.requireNonNull(name, "The collection name must be non-null.");
        Collection collection = Optional.ofNullable(collections.get(name))
                .map(obj -> gson.fromJson(gson.toJson(obj), Collection.class))
                .orElse(new Collection());
        if (Objects.isNull(collection.getUid())) {
            collection.setUid(uuid());
            collection.setCommitFunction(this::commit);
            collection.setConvertFunction((obj, clazz) -> (IStorable) gson.fromJson(gson.toJson(obj), clazz));
            collections.put(getCollectionName(name), collection);
            storage.put(getCollectionName(name), collection);
        }
        return collection;
    }

    private <T> T convert(Object object, Class<T> clazz) {
        return gson.fromJson(gson.toJson(object), clazz);
    }

    private String getObjectName(String key) {
        return "object$$" + key;
    }

    private String getCollectionName(String key) {
        return "collection$$" + key;
    }
}