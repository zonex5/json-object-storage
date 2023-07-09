package xyz.toway.tools;

import java.util.*;
import java.util.function.Consumer;

import static xyz.toway.tools.Constants.ERROR_OBJECT_NULL;

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

    public void insert(T object) {
        Objects.requireNonNull(object, ERROR_OBJECT_NULL);
        if (Objects.nonNull(object.getUid())) {
            throw new RuntimeException("The object has a non-null uid. Please use 'update' operation.");
        }
        object.setUid(UUID.randomUUID().toString());
        elements.add(object);
    }

    public Optional<T> getFirst() {
        return elements.stream().findFirst();
    }

    public Optional<T> getByUid(String uid) {
        return elements.stream()
                .filter(e -> e.getUid().equals(uid))
                .findFirst();
    }

    public void commit() {
        if (Objects.nonNull(commitFunction)) {
            commitFunction.accept(this);
        }
    }

    public T update(T object) {
        Objects.requireNonNull(object, ERROR_OBJECT_NULL);
        if (Objects.isNull(object.getUid())) {
            throw new RuntimeException("The object has a null uid. Please use 'insert' operation.");
        }
        getByUid(object.getUid())
                .ifPresent(obj -> elements.set(elements.indexOf(obj), object));
        return object;
    }


}
