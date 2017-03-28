package org.toilelibre.libe.pol;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Collections.unmodifiableCollection;

public class Pol {

    public static final Class<String> TEXTS = String.class;
    public static final Class<String> NAMES = String.class;
    public static final Class<Integer> NATURALS = Integer.class;
    public static final Class<Number> NUMBERS = Number.class;
    public static final Class<List> LIST = List.class;
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

        public InvocationHelper<T> useTheResult() {
            return new InvocationHelper<>(this.result);
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

        public <W> QuadriInvocationHelper<T, U, V, W> alongWith (W aFourthData) {
            return and(aFourthData);
        }
        public <W> QuadriInvocationHelper<T, U, V, W> and (W aFourthData) {
            return new QuadriInvocationHelper<>(data, secondData, thirdData, aFourthData);
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

        public <V> TriInvocationHelper<T, U, V> alongWith (V aThirdData) {
            return and(aThirdData);
        }
        public <V> TriInvocationHelper<T, U, V> and (V aThirdData) {
            return new TriInvocationHelper<>(data, secondData, aThirdData);
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
            return (data) -> {call.accept(data);return (R)data;};
        }

        public static <T, R> Function<T, R> silently(Consumer<T> call) {
            return _do(call);
        }

        public static <T> T _for(T t) {
            return t;
        }

        public static <T extends Function<U, R>, U, R> T someFunctionFor(Class<U> originTypeAs, Class<R> andTargetTypeAs, T withTheFollowingDefinition) {
            return withTheFollowingDefinition;
        }

        public static <T extends Consumer<U>, U> T someFunctionFor(Class<U> originTypeAs, T withTheFollowingDefinition) {
            return withTheFollowingDefinition;
        }

        public static <T extends List<U>, U> Class<T> someListOf(Class<U> originTypeAs) {
            return (Class) List.class;
        }

        public <U> BiInvocationHelper<T, U> alongWith (U aSecondData) {
            return and(aSecondData);
        }
        public <U> BiInvocationHelper<T, U> and (U aSecondData) {
            return new BiInvocationHelper<>(data, aSecondData);
        }
    }

    public static class ElementAdder<T> implements SomeLanguageElements {
        private final T element;

        public ElementAdder(T element) {
            this.element = element;
        }
        public CollectionHandler<Collection<T>, T> to(Supplier<Collection<T>> thatCollection) {
            Collection<T> collection = thatCollection.get();
            collection.add(element);
            return new CollectionHandler<>(collection);
        }

        public CollectionHandler<Collection<T>, T> to(Collection<T> thatCollection) {
            thatCollection.add(element);
            return new CollectionHandler<>(thatCollection);
        }
    }

    public static class CollectionHandler<T extends Collection<U>, U> implements SomeLanguageElements<CollectionHandler<T, U>>, ResultLanguageElements<Collection<U>> {
        private final T collection;

        public CollectionHandler(T collection) {
            this.collection = collection;
        }

        public CollectionHandler<T, U> alsoAdd(U thisOtherElement) {
            collection.add(thisOtherElement);
            return this;
        }

        public CollectionHandler<T, U> with(U thisOtherElement) {
            return alsoAdd(thisOtherElement);
        }

        public InvocationHelper<T> useThatAll () {
            return new InvocationHelper<>(collection);
        }

        public Stream<U> _do() {
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

        public boolean hasSize(int ofExactly) {
            return this.collection.size() == ofExactly;
        }

        public boolean contains(U thisElement) {
            return this.collection.contains(thisElement);
        }

        public boolean doesNotContain(U thisElement) {
            return !this.contains(thisElement);
        }

        public Looper<T> loop() {
            return new Looper<>(new DataHolder<>(collection));
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

        public static <T extends Collection<U>, U> T merge(CollectionHandler<T, U> firstCollection, CollectionHandler<T, U> secondCollection) {
            return (T)Stream.concat(firstCollection._do(), secondCollection._do()).collect(Collectors.toList());
        }
    }

    public static class Looper<T> implements SomeLanguageElements<Looper<T>> {

        private DataHolder<T> useThis;
        private Function<T, T> function;
        private Predicate<Integer> predicate;

        public Looper(Function<T, T> function) {
            this.function = function;
        }

        public Looper(DataHolder<T> useThis) {
            this.useThis = useThis;
        }

        public Looper<T> on(T someDataCalled) {
            this.useThis = new DataHolder<T>(someDataCalled);
            return this;
        }

        public Looper<T> apply (Function<T, T> function) {
            this.function = function;
            return this;
        }

        public Looper<T> _for (final int exactly) {
            this.predicate = i -> i > exactly;
            return this;
        }

        public Looper<T> times() {
            T that = useThis.value();
            int i = 0;
            while (!this.predicate.test(i)) {
                that = function.apply(that);
                i++;
            }
            this.useThis = new DataHolder<>(that);
            return this;
        }

        public Looper<T> forAllElements() {
            this.useThis = new DataHolder<>(function.apply(useThis.value()));
            return this;
        }

        public Looper<T> until(Predicate<T> weHave) {
            T that = useThis.value();
            while (!weHave.test(that)) {
                that = function.apply(that);
            }
            this.useThis = new DataHolder<>(that);
            return this;
        }

        public DataHolder<T> afterThat () {
            return this.useThis;
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

    public static <T> T a (T value) {
        return value;
    }

    public static <T> T the (T value) {
        return value;
    }

    public static <T> T justCreated (T thingCalled) {
        return thingCalled;
    }

    public static <T> T _this (T value) {
        return value;
    }

    public static <T> T that (T value) {
        return value;
    }

    public static <T> T one (T value) {
        return value;
    }

    public static <T> T some (T value) {
        return value;
    }

    public static <T> T value (T whichIs) {
        return whichIs;
    }

    public static <T> T like (T letsSay) {
        return letsSay;
    }

    public static <T> String text (T saying) {
        return (String)saying;
    }

    public static <T> Number number (T ofValue) {
        return (Number) ofValue;
    }

    public static <T> Integer natural (T number) {
        return (Integer) number;
    }

    public static <T> InvocationHelper<T> use (T that) {
        return new InvocationHelper<>(that);
    }

    public static <T> T weHave (T theResult) {
        return theResult;
    }

    public static <U extends Collection<T>, V extends Collection<R>, T, R> Function<U, V> forEach (Function<T, R> elementInTheCollection) {
        return collection -> (V)collection.stream().map(t -> elementInTheCollection.apply(t)).collect(Collectors.toList());
    }

    public static <T> List<T> theList (T... containingExactly) {
        return Arrays.asList(containingExactly);
    }

    public static <T, U extends Collection<T>> CollectionHandler<U, T> withTheList (T... containingExactly) {
        return new CollectionHandler(Arrays.asList(containingExactly));
    }

    public static <T> DataHolder<T> with (T data) {
        return new DataHolder<>(data);
    }


    public static <T, U extends Collection<T>> CollectionHandler<U, T> _a (U collection) {
        return new CollectionHandler<>(collection);
    }
    public static <T, U extends Collection<T>> CollectionHandler<U, T> newList () {
        return new CollectionHandler<>((U)new ArrayList<T>());
    }
    public static <T, U extends Collection<T>> CollectionHandler<U, T> newListOf (Class<T> thisClass) {
        return newList();
    }

    public static <T> ElementAdder<T> add (T element) {
        return new ElementAdder<>(element);
    }

    public static <T> Looper<T> andNow (DataHolder<T> useThis) {
        return new Looper<>(useThis);
    }

    public static <T> Looper<T> applySilently (Consumer<T> theFunction) {
        return new Looper<>(InvocationHelper._do(theFunction));
    }

    public static <T> Looper<T> apply (Function<T, T> theFunction) {
        return new Looper<>(theFunction);
    }

}
