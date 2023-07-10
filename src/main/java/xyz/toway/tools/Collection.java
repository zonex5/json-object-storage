package xyz.toway.tools;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static xyz.toway.tools.BaseStorageService.uuid;
import static xyz.toway.tools.Constants.ERROR_OBJECT_NULL;
import static xyz.toway.tools.Constants.ERROR_UID_NULL;

public class Collection<T extends IStorableObject> {
    private List<T> elements;

    private transient String name;

    private transient Consumer<Collection<T>> commitFunction;

    public Collection() {
        elements = new ArrayList<>();
    }

    protected List<T> getElements() {
        return elements;
    }

    protected void setElements(List<T> elements) {
        this.elements = elements;
    }

    protected String getName() {
        return name;
    }

    protected void setName(String name) {
        this.name = name;
    }

    protected void setCommitFunction(Consumer<Collection<T>> commitFunction) {
        this.commitFunction = commitFunction;
    }

    public Optional<T> getFirst() {
        return elements.stream().findFirst();
    }

    public Optional<T> getFirst(Predicate<T> p) {
        return elements.stream()
                .filter(p)
                .findFirst();
    }

    public List<T> getAll() {
        return elements;
    }

    public List<T> getAll(Predicate<T> p) {
        return elements.stream()
                .filter(p)
                .collect(Collectors.toList());
    }

    public Optional<T> getByUid(String uid) {
        Objects.requireNonNull(uid, ERROR_UID_NULL);
        return getFirst(obj -> obj.getUid().equals(uid));
    }

    public void commit() {
        if (Objects.nonNull(commitFunction)) {
            commitFunction.accept(this);
        }
    }

    public void insert(T object) {
        Objects.requireNonNull(object, ERROR_OBJECT_NULL);
        if (Objects.nonNull(object.getUid())) {
            throw new RuntimeException("The object has a non-null UID. Please use 'update' operation.");
        }
        object.setUid(uuid());
        elements.add(object);
    }

    public T update(T object) {  //check wo commit
        Objects.requireNonNull(object, ERROR_OBJECT_NULL);
        if (Objects.isNull(object.getUid())) {
            throw new RuntimeException("The object has a null UID. Please use 'insert' operation.");
        }
        getByUid(object.getUid())
                .ifPresent(obj -> elements.set(elements.indexOf(obj), object));
        return object;
    }

    public Optional<T> removeById(String uid) {
        Objects.requireNonNull(uid, ERROR_UID_NULL);
        var option = getFirst(obj -> obj.getUid().equals(uid));
        option.ifPresent(obj -> elements.remove(obj));
        return option;
    }

    public boolean remove(T object) {
        Objects.requireNonNull(object, ERROR_OBJECT_NULL);
        return elements.remove(object);
    }
}
