package xyz.toway.tools;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;


/**
 * Object Storage based on gson
 */
public class GObjectStorage extends BaseStorage {

    private final Gson gson;

    private final HashMap<String, IStorable> storage;

    private final HashMap<String, Collection> collections;

    private static final TypeToken<HashMap<String, IStorable>> storageTypeToken = new TypeToken<>() {
    };

    public GObjectStorage(String fileName) throws Exception {
        super(fileName);
        gson = new Gson();
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

    public void commit(HashMap<String, IStorable> storage) {
        writeToFile(gson.toJson(storage));
    }

    public boolean contains(String name) {
        return storage.containsKey(name);
    }

    public IStorable set(String key, IStorable object) {
        checkKeyParams(key);
        if (Objects.isNull(object)) {
            throw new RuntimeException("The object must be non-null.");
        }
        if (Objects.isNull(object.getUid())) {
            object.setUid(UUID.randomUUID().toString());
        }
        storage.put(key, object);
        return object;
    }

    public Optional<IStorable> get(String key) {
        checkKeyParams(key);
        return !storage.containsKey(key) ? Optional.empty() : Optional.of(storage.get(key));
    }

    private void checkKeyParams(String key) {
        if (Objects.isNull(key)) {
            throw new RuntimeException("The key must be non-null.");
        }
    }


    /*public <T> T getObject(String name, Class<T> clazz) {
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
    }*/


}



/*  for collection!
     public IStorable insert(String key, IStorable object) {
        checkKeyObjectParams(key, object);
        if (Objects.nonNull(object.getUid())) {
            throw new RuntimeException("The object has a non-null uid. Please use 'update' operation.");
        }
        object.setUid(UUID.randomUUID().toString());
        storage.put(key, object);
        return object;
    }

    public IStorable update(String key, IStorable object) {
        checkKeyObjectParams(key, object);
        if (Objects.isNull(object.getUid())) {
            throw new RuntimeException("The object has a null uid. Please use 'insert' operation.");
        }
        if (!storage.containsKey(key)) {
            throw new RuntimeException("The storage doesn't contains key '" + key + "'.");
        }
        IStorable storedObject = storage.get(key);
        return object;
    }
 */