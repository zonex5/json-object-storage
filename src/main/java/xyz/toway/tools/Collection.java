package xyz.toway.tools;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;

public class Collection implements IStorable {

    private final List<IStorable> elements;

    private transient Runnable commitFunction;

    private transient BiFunction<IStorable, Class<?>, IStorable> convertFunction;

    public Collection() {
        this.elements = new ArrayList<>();
    }

    private String uid;

    @Override
    public String getUid() {
        return uid;
    }

    @Override
    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setCommitFunction(Runnable commitFunction) {
        this.commitFunction = commitFunction;
    }

    public void setConvertFunction(BiFunction<IStorable, Class<?>, IStorable> convertFunction) {
        this.convertFunction = convertFunction;
    }

    public IStorable insert(IStorable object) {
        checkObjectParams(object);
        if (Objects.nonNull(object.getUid())) {
            throw new RuntimeException("The object has a non-null uid. Please use 'update' operation.");
        }
        object.setUid(UUID.randomUUID().toString());
        elements.add(object);
        return object;
    }

    public IStorable update(IStorable object) {
        checkObjectParams(object);
        if (Objects.isNull(object.getUid())) {
            throw new RuntimeException("The object has a null uid. Please use 'insert' operation.");
        }
        getByUid(object.getUid())
                .ifPresent(obj -> elements.set(elements.indexOf(obj), object));
        return object;
    }

    public Optional<IStorable> getByUid(String uid) {
        return elements.stream()
                .filter(e -> e.getUid().equals(uid))
                .findFirst();
    }

    public Optional<IStorable> getFirst() {
        return elements.stream().findFirst();
    }

    public <T> Optional<IStorable> getFirst(Class<T> clazz) {
        Objects.requireNonNull(convertFunction, "Convert function can't be null.");
        return elements.stream()
                .findFirst()
                .map(e -> convertFunction.apply(e, clazz));
    }

    public void commit() {
        if (Objects.nonNull(commitFunction)) {
            commitFunction.run();
        }
    }

    private void checkObjectParams(IStorable object) {
        if (Objects.isNull(object)) {
            throw new RuntimeException("The object must be non-null.");
        }
    }
}
