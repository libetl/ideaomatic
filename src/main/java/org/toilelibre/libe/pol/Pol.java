package org.toilelibre.libe.pol;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static java.util.Collections.unmodifiableCollection;

public class Pol {

    public static final Class<String> TEXTS = String.class;
    public static final Class<String> NAMES = String.class;
    public static final Class<Number> NUMBERS = Number.class;
    public static final boolean SOMETHING_TRUE = true;
    public static final boolean SOMETHING_FALSE = false;

    @FunctionalInterface
    public interface QuadriFunction<T, U, V, W, R> {
        R apply(T t, U u, V v, W w);

        default <R2> QuadriFunction<T, U, V, W, R2> andThen(Function<? super R, ? extends R2> after) {
            Objects.requireNonNull(after);
            return (T t, U u, V v, W w) -> after.apply(apply(t, u, v, w));
        }
    }

    @FunctionalInterface
    public interface QuadriConsumer<T, U, V, W> {

        void accept(T t, U u, V v, W w);

        default QuadriConsumer<T, U, V, W> andThen(QuadriConsumer<? super T, ? super U, ? super V, ? super W> after) {
            Objects.requireNonNull(after);

            return (l, r, v, w) -> {
                accept(l, r, v, w);
                after.accept(l, r, v, w);
            };
        }
    }

    @FunctionalInterface
    public interface TriFunction<T, U, V, R> {
        R apply(T t, U u, V v);

        default <R2> TriFunction<T, U, V, R2> andThen(Function<? super R, ? extends R2> after) {
            Objects.requireNonNull(after);
            return (T t, U u, V v) -> after.apply(apply(t, u, v));
        }
    }

    @FunctionalInterface
    public interface TriConsumer<T, U, V> {

        void accept(T t, U u, V v);

        default TriConsumer<T, U, V> andThen(TriConsumer<? super T, ? super U, ? super V> after) {
            Objects.requireNonNull(after);

            return (l, r, v) -> {
                accept(l, r, v);
                after.accept(l, r, v);
            };
        }
    }

    public static class DataHolder<T> implements SomeLanguageElements<DataHolder<T>>, ResultLanguageElements<T> {

        private final T result;

        public DataHolder(T result) {
            this.result = result;
        }

        public InvocationHelper<T> andUseTheResult() {
            return new InvocationHelper<T>(this.result);
        }

        public <R> DataHolder<R> become (Function<T, R> modifier) {
            return new DataHolder<>(modifier.apply(result));
        }

        public DataHolder<T> modify (Consumer<T> modifier) {
            modifier.accept(result);
            return this;
        }

        public <R> DataHolder<R> map (Function<T, R> modifier) {
            return new DataHolder<>(modifier.apply(result));
        }

        public <R> DataHolder<R> then (Function<T, R> modifier) {
            return map(modifier);
        }

        public T value() {
            return this.result;
        }
    }

    public static class QuadriInvocationHelper<T, U, V, W> implements SomeLanguageElements<QuadriInvocationHelper<T, U, V, W>> {
        private final T data;
        private final U secondData;
        private final V thirdData;
        private final W fourthData;

        public QuadriInvocationHelper(T data, U secondData, V thirdData, W fourthData) {
            this.data = data; this.secondData = secondData; this.thirdData = thirdData; this.fourthData = fourthData;
        }

        public <R> DataHolder<R> insideThe(QuadriFunction<T, U, V, W, R> call) {
            return to(call);
        }
        public <R> DataHolder<R> to(QuadriFunction<T, U, V, W, R> call) {
            return new DataHolder<>(call.apply(data, secondData, thirdData, fourthData));
        }

        public static <T, U, V, W, R> QuadriFunction<T, U, V, W, R> _do(QuadriConsumer<T, U, V, W> call) {
            return (data, secondData, thirdData, fourthData) -> {call.accept(data, secondData, thirdData, fourthData);return (R)Void.TYPE;};
        }
    }

    public static class TriInvocationHelper<T, U, V> implements SomeLanguageElements<TriInvocationHelper<T, U, V>> {
        private final T data;
        private final U secondData;
        private final V thirdData;

        public TriInvocationHelper(T data, U secondData, V thirdData) {
            this.data = data; this.secondData = secondData; this.thirdData = thirdData;
        }

        public <R> DataHolder<R> insideThe(TriFunction<T, U, V, R> call) {
            return to(call);
        }
        public <R> DataHolder<R> to(TriFunction<T, U, V, R> call) {
            return new DataHolder<>(call.apply(data, secondData, thirdData));
        }

        public static <T, U, V, R> TriFunction<T, U, V, R> _do(TriConsumer<T, U, V> call) {
            return (data, secondData, thirdData) -> {call.accept(data, secondData, thirdData);return (R)Void.TYPE;};
        }

        public <W> QuadriInvocationHelper<T, U, V, W> alongWith (W fourthData) {
            return and(fourthData);
        }
        public <W> QuadriInvocationHelper<T, U, V, W> and (W fourthData) {
            return new QuadriInvocationHelper<>(data, secondData, thirdData, fourthData);
        }
    }

    public static class BiInvocationHelper<T, U> implements SomeLanguageElements<BiInvocationHelper<T, U>> {
        private final T data;
        private final U secondData;

        public BiInvocationHelper(T data, U secondData) {
            this.data = data; this.secondData = secondData;
        }
        public <R> DataHolder<R> insideThe(BiFunction<T, U, R> call) {
            return to(call);
        }
        public <R> DataHolder<R> to(BiFunction<T, U, R> call) {
            return new DataHolder<>(call.apply(data, secondData));
        }

        public static <T, U, R> BiFunction<T, U, R> _do(BiConsumer<T, U> call) {
            return (data, secondData) -> {call.accept(data, secondData);return (R)Void.TYPE;};
        }

        public <V> TriInvocationHelper<T, U, V> alongWith (V thirdData) {
            return and(thirdData);
        }
        public <V> TriInvocationHelper<T, U, V> and (V thirdData) {
            return new TriInvocationHelper<>(data, secondData, thirdData);
        }
    }

    public static class InvocationHelper<T> implements SomeLanguageElements<InvocationHelper<T>> {
        private final T data;

        public InvocationHelper(T data) {
            this.data = data;
        }

        public <R> DataHolder<R> insideThe(Function<T, R> call) {
            return to(call);
        }
        public <R> DataHolder<R> to(Function<T, R> call) {
            return new DataHolder<>(call.apply(data));
        }

        public static <T, R> Function<T, R> _do(Consumer<T> call) {
            return (data) -> {call.accept(data);return (R)Void.TYPE;};
        }

        public <U> BiInvocationHelper<T, U> alongWith (U secondData) {
            return and(secondData);
        }
        public <U> BiInvocationHelper<T, U> and (U secondData) {
            return new BiInvocationHelper<>(data, secondData);
        }
    }

    public static class ElementAdder<T> implements SomeLanguageElements {
        private final T element;

        public ElementAdder(T element) {
            this.element = element;
        }
        public CollectionHandler<Collection<T>, T> to(Supplier<Collection<T>> collectionBuilder) {
            Collection<T> collection = collectionBuilder.get();
            collection.add(element);
            return new CollectionHandler<>(collection);
        }

        public CollectionHandler<Collection<T>, T> to(Collection<T> collection) {
            collection.add(element);
            return new CollectionHandler<>(collection);
        }
    }

    public static class CollectionHandler<T extends Collection<U>, U> implements SomeLanguageElements<CollectionHandler<T, U>>, ResultLanguageElements<Collection<U>> {
        private final T collection;

        public CollectionHandler(T collection) {
            this.collection = collection;
        }

        public CollectionHandler<T, U> alsoAdd(U element) {
            collection.add(element);
            return this;
        }

        public CollectionHandler<T, U> with(U element) {
            return alsoAdd(element);
        }

        public InvocationHelper<T> useThatAll () {
            return new InvocationHelper<>(collection);
        }

        public Stream<U> thenDo() {
            return collection.stream();
        }

        @Override
        public Collection<U> value() {
            return unmodifiableCollection(collection);
        }

        public boolean isEmpty() {
            return this.collection.isEmpty();
        }

        public boolean isNotEmpty() {
            return !this.collection.isEmpty();
        }

        public boolean hasSize(int number) {
            return this.collection.size() == number;
        }

        public boolean contains(U element) {
            return this.collection.contains(element);
        }

        public boolean doesNotContain(U element) {
            return !this.contains(element);
        }
    }

    public static class Do {

        public static <T> Collection<T> concatenation(Collection<T> list, T element) {
            list.add(element);
            return list;
        }

        public static String join(String... s) {
            return String.join("", s);
        }
    }

    public interface SomeLanguageElements<T extends SomeLanguageElements> {

        default T and() {
            return (T) this;
        }

        default T then () {
            return (T) this;
        }
    }

    public interface ResultLanguageElements<T> {
        T value();

        default T ok () {
            return value();
        }
        default T stop () {
            return value();
        }
        default T thatIsAll () {
            return value();
        }
    }

    public static <T> T a (T t) {
        return t;
    }

    public static <T> T the (T t) {
        return t;
    }

    public static <T> T justCreated (T t) {
        return t;
    }

    public static <T> T _this (T t) {
        return t;
    }

    public static <T> T that (T t) {
        return t;
    }

    public static <T> T one (T t) {
        return t;
    }

    public static <T> T some (T t) {
        return t;
    }

    public static <T> T value (T t) {
        return t;
    }

    public static <T> String like (T t) {
        return (String)t;
    }
    public static <T> String text (T t) {
        return (String)t;
    }

    public static <T> Number number (T t) {
        return (Number) t;
    }

    public static <T> InvocationHelper<T> use (T data) {
        return new InvocationHelper<T>(data);
    }

    public static <T, U extends Collection<T>> CollectionHandler<U, T> _a (U theCollection) {
        return new CollectionHandler<>(theCollection);
    }
    public static <T, U extends Collection<T>> CollectionHandler<U, T> newList () {
        return new CollectionHandler<>((U)new ArrayList<T>());
    }
    public static <T, U extends Collection<T>> CollectionHandler<U, T> newListOf (Class<T> clazz) {
        return newList();
    }

    public static <T> ElementAdder<T> add (T element) {
        return new ElementAdder<>(element);
    }

}
