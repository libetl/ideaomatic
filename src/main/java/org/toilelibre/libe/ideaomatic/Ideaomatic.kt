package org.toilelibre.libe.ideaomatic

import org.toilelibre.libe.ideaomatic.Ideaomatic._with
import java.time.*
import java.util.*
import java.util.Collections.unmodifiableCollection
import java.util.function.Predicate
import kotlin.reflect.KFunction
import kotlin.reflect.jvm.jvmErasure

object Ideaomatic {

    @JvmField
    val TEXTS: Class<String> = String::class.java
    @JvmField
    val NAMES: Class<String> = String::class.java
    @JvmField
    val NATURALS: Class<Int> = Int::class.java
    @JvmField
    val NUMBERS: Class<Number> = Number::class.java
    @JvmField
    val LIST: Class<List<*>> = List::class.java
    @JvmField
    val SOMETHING_TRUE = true
    @JvmField
    val SOMETHING_FALSE = false

    class Modifier<T>(private val toBeModified: T) {

        fun toDo(): T {
            return toBeModified
        }

        fun toDo(_that: (T) -> Any?): DataHolder<T> {
            _that.invoke(toBeModified)
            return DataHolder(toBeModified)
        }
    }

    class DataHolder<T>(private val result: T) : SomeLanguageElements<DataHolder<T>>, ResultLanguageElements<T> {

        fun useTheResult(): InvocationHelper<T> {
            return InvocationHelper(this.result)
        }

        fun <R> become(modifier: (T) -> R): DataHolder<R> {
            return DataHolder(modifier.invoke(result))
        }

        fun <R> _finally(modifier: (T) -> R): R {
            return modifier.invoke(result)
        }

        fun modify(modifier: (T) -> Unit): DataHolder<T> {
            modifier.invoke(result)
            return this
        }

        fun modifyIt(): Modifier<T> {
            return Modifier(result)
        }

        fun toDo(): T {
            return result
        }

        fun <R> map(modifier: (T) -> R): DataHolder<R> {
            return DataHolder(modifier.invoke(result))
        }

        fun <R> then(modifier: (T) -> R): DataHolder<R> {
            return map(modifier)
        }

        override fun value(): T {
            return this.result
        }
    }

    class QuadriInvocationHelper<T, U, V, W>(private val data: T, private val secondData: U, private val thirdData: V, private val fourthData: W) : SomeLanguageElements<QuadriInvocationHelper<T, U, V, W>> {

        fun <R> insideThe(call: (T, U, V, W) -> R): DataHolder<R> {
            return to(call)
        }

        fun <R> to(call: (T, U, V, W) -> R): DataHolder<R> {
            return DataHolder(call.invoke(data, secondData, thirdData, fourthData))
        }

        fun toDo() = arrayOf(data, secondData, thirdData, fourthData)

        companion object {

            fun <T, U, V, W, R> _do(call: (T, U, V, W) -> Unit): (T, U, V, W) -> R {
                return { data, secondData, thirdData, fourthData ->
                    call.invoke(data, secondData, thirdData, fourthData)
                    Void.TYPE as R
                }
            }
        }
    }

    class TriInvocationHelper<T, U, V>(private val data: T, private val secondData: U, private val thirdData: V) : SomeLanguageElements<TriInvocationHelper<T, U, V>> {

        fun <R> insideThe(call: (T, U, V) -> R): DataHolder<R> {
            return to(call)
        }

        fun <R> to(call: (T, U, V) -> R): DataHolder<R> {
            return DataHolder(call.invoke(data, secondData, thirdData))
        }

        fun <W> alongWith(aFourthData: W): QuadriInvocationHelper<T, U, V, W> {
            return and(aFourthData)
        }

        fun <W> and(aFourthData: W): QuadriInvocationHelper<T, U, V, W> {
            return QuadriInvocationHelper(data, secondData, thirdData, aFourthData)
        }

        fun toDo() = arrayOf(data, secondData, thirdData)

        companion object {

            fun <T, U, V, R> _do(call: (T, U, V) -> Unit): (T, U, V) -> R {
                return { data, secondData, thirdData ->
                    call.invoke(data, secondData, thirdData)
                    Void.TYPE as R
                }
            }
        }
    }

    class BiInvocationHelper<T, U>(private val data: T, private val secondData: U) : SomeLanguageElements<BiInvocationHelper<T, U>> {
        fun <R> insideThe(call: (T, U) -> R): DataHolder<R> {
            return to(call)
        }

        fun <R> to(call: (T, U) -> R): DataHolder<R> {
            return DataHolder(call.invoke(data, secondData))
        }

        fun <V> alongWith(aThirdData: V): TriInvocationHelper<T, U, V> {
            return and(aThirdData)
        }

        fun <V> and(aThirdData: V): TriInvocationHelper<T, U, V> {
            return TriInvocationHelper(data, secondData, aThirdData)
        }

        fun toDo() = arrayOf(data, secondData)

        companion object {

            fun <T, U, R> _do(call: (T, U) -> Unit): (T, U) -> R {
                return { data, secondData ->
                    call.invoke(data, secondData)
                    Void.TYPE as R
                }
            }
        }
    }

    class InvocationHelper<T>(private val data: T) : SomeLanguageElements<InvocationHelper<T>> {

        fun <R> insideThe(call: (T) -> R): DataHolder<R> {
            return to(call)
        }

        fun <R> to(call: (T) -> R): DataHolder<R> {
            return DataHolder(call.invoke(data))
        }

        fun <U> alongWith(aSecondData: U): BiInvocationHelper<T, U> {
            return and(aSecondData)
        }

        fun <U> and(aSecondData: U): BiInvocationHelper<T, U> {
            return BiInvocationHelper(data, aSecondData)
        }

        fun toDo(): T {
            return data
        }

        companion object {

            @JvmStatic
            fun <T> formA(call: (T) -> Any?): (T) -> T {
                return _do(call)
            }

            @JvmStatic
            fun <T> _do(call: (T) -> Any?): (T) -> T {
                return { data ->
                    call.invoke(data)
                    data
                }
            }

            @JvmStatic
            fun <T> silently(call: (T) -> Any?): (T) -> T {
                return _do(call)
            }

            @JvmStatic
            fun <T> _for(t: T): T {
                return t
            }

            @JvmStatic
            fun <T : (U) -> R, U, R> someFunctionFor(originTypeAs: Class<U>, andTargetTypeAs: Class<R>, withTheFollowingDefinition: T): T {
                return withTheFollowingDefinition
            }

            @JvmStatic
            fun <T> someFunctionFor(originTypeAs: Class<T>, withTheFollowingDefinition: (T) -> Any?): (T) -> Any? {
                return withTheFollowingDefinition
            }

            @JvmStatic
            fun <T : MutableList<U>, U> someListOf(originTypeAs: Class<U>): Class<T> {
                return MutableList::class.java as Class<T>
            }
        }

    }

    class ElementAdder<T>(private val element: T) : SomeLanguageElements<Any> {
        fun to(thatCollection: () -> MutableCollection<T>): CollectionHandler<MutableCollection<T>, T> {
            val collection = thatCollection.invoke()
            collection.add(element)
            return CollectionHandler(collection)
        }

        fun to(thatCollection: MutableCollection<T>): CollectionHandler<MutableCollection<T>, T> {
            thatCollection.add(element)
            return CollectionHandler(thatCollection)
        }

        fun value() = element
    }

    class CollectionHandler<T : MutableCollection<U>, U>(private val collection: T) : SomeLanguageElements<CollectionHandler<T, U>>, ResultLanguageElements<MutableCollection<U>> {

        val isEmpty: Boolean
            get() = this.collection.isEmpty()

        val isNotEmpty: Boolean
            get() = !this.collection.isEmpty()

        fun alsoAdd(thisOtherElement: U): CollectionHandler<T, U> {
            collection.add(thisOtherElement)
            return this
        }

        fun with(thisOtherElement: U): CollectionHandler<T, U> {
            return alsoAdd(thisOtherElement)
        }

        fun useThatAll(): InvocationHelper<T> {
            return InvocationHelper(collection)
        }

        override fun value(): MutableCollection<U> {
            return unmodifiableCollection(collection)
        }

        fun hasSize(ofExactly: Int): Boolean {
            return this.collection.size == ofExactly
        }

        operator fun contains(thisElement: U): Boolean {
            return this.collection.contains(thisElement)
        }

        fun doesNotContain(thisElement: U): Boolean {
            return !this.contains(thisElement)
        }

        fun loop(): Looper<T> {
            return Looper(DataHolder(collection))
        }

        fun <V> isIncludedIn(theCollection: List<V>): Boolean {
            return theCollection.contains(collection.first() as V)
        }

        fun <V> isNotIncludedIn(theCollection: List<V>): Boolean {
            return !isIncludedIn(theCollection)
        }
    }

    object Get {

        @JvmStatic
        fun aYear(value: Int): Year {
            return Year.of(value)
        }

        @JvmStatic
        fun aYearMonth(year: Year, value: Int): YearMonth {
            return year.atMonth(value)
        }

        @JvmStatic
        fun aDate(yearMonth: YearMonth, value: Int): LocalDate {
            return yearMonth.atDay(value)
        }

        @JvmStatic
        fun aDateTime(localDate: LocalDate, hours: Int?, minutes: Int?): LocalDateTime {
            return aDateTimeWithSeconds(localDate, hours!!, minutes!!, 0)
        }

        @JvmOverloads
        @JvmStatic
        fun aDateTimeWithSeconds(localDate: LocalDate, hours: Int, minutes: Int, seconds: Int, nanoseconds: Int = 0): LocalDateTime {
            return localDate.atTime(hours, minutes, seconds, nanoseconds)
        }

        @JvmStatic
        fun aDateTimeWithTimezone(localDateTime: LocalDateTime, zoneOffset: ZoneOffset): OffsetDateTime {
            return localDateTime.atOffset(zoneOffset)
        }
    }

    object Do {

        @JvmStatic
        fun <T> concatenation(list: MutableCollection<T>, element: T): Collection<T> {
            list.add(element)
            return list
        }

        @JvmStatic
        fun join(vararg s: String): String {
            return s.joinToString("")
        }

        @JvmStatic
        fun <T : MutableCollection<U>, U> merge(firstCollection: CollectionHandler<T, U>, secondCollection: CollectionHandler<T, U>): T {
            return (firstCollection.value() + secondCollection.value()).toMutableList() as T
        }

        @JvmStatic
        fun <T> println(t: T): Any? {
            System.out.println(t)
            return null
        }

        @JvmStatic
        fun <T> intersection(firstCollection: Collection<T>, secondCollection: Collection<T>): Collection<T> {
            return (firstCollection - secondCollection).toMutableList()
        }
    }

    class Looper<T> : SomeLanguageElements<Looper<T>> {

        private var useThis: DataHolder<T>? = null
        private var function: ((T) -> T)? = null
        private var predicate: ((Int) -> Boolean)? = null

        constructor(function: ((T) -> T)) {
            this.function = function
        }

        constructor(useThis: DataHolder<T>) {
            this.useThis = useThis
        }

        fun on(someDataCalled: T): Looper<T> {
            this.useThis = DataHolder(someDataCalled)
            return this
        }

        fun apply(function: ((T) -> T)): Looper<T> {
            this.function = function
            return this
        }

        fun _for(exactly: Int): Looper<T> {
            this.predicate = { i -> i > exactly }
            return this
        }

        fun times(): Looper<T> {
            var that = useThis!!.value()
            var i = 0
            while (!this.predicate!!.invoke(i)) {
                that = function!!.invoke(that)
                i++
            }
            this.useThis = DataHolder(that)
            return this
        }

        fun onEveryElement(): Looper<T> {
            this.useThis = DataHolder(function!!.invoke(useThis!!.value()))
            return this
        }

        fun until(weHave: Predicate<T>): Looper<T> {
            return _until { weHave.test(it) }
        }

        fun _until(weHave: (T) -> Boolean): Looper<T> {
            var that = useThis!!.value()
            while (!weHave.invoke(that)) {
                that = function!!.invoke(that)
            }
            this.useThis = DataHolder(that)
            return this
        }

        fun afterThat(): DataHolder<T> {
            return this.useThis!!
        }

    }

    interface SomeLanguageElements<T : Any> {

        fun and(): T {
            return this as T
        }

        fun then(): T {
            return this as T
        }
    }

    interface ResultLanguageElements<T> {
        fun value(): T

        fun ok(): T {
            return value()
        }

        fun stop(): T {
            return value()
        }

        fun thatIsAll(): T {
            return value()
        }
    }

    @JvmStatic
    fun <T> call(theGeneratorCalled: () -> T): T {
        return theGeneratorCalled.invoke()
    }

    @JvmStatic
    fun <T> a(value: T): T {
        return value
    }

    @JvmStatic
    fun <T> to(value: T): T {
        return value
    }

    @JvmStatic
    fun <T> the(value: T): T {
        return value
    }

    @JvmStatic
    fun <T> justCreated(thingCalled: T): T {
        return thingCalled
    }

    @JvmStatic
    fun <T> _this(value: T): T {
        return value
    }

    @JvmStatic
    fun <T> that(value: T): T {
        return value
    }

    @JvmStatic
    fun <T> one(value: T): T {
        return value
    }

    @JvmStatic
    fun <T> some(value: T): T {
        return value
    }

    @JvmStatic
    fun <T> value(whichIs: T): T {
        return whichIs
    }

    @JvmStatic
    fun <T> like(letsSay: T): T {
        return letsSay
    }

    @JvmStatic
    fun <T> letsSay(mmmh: T): T {
        return mmmh
    }

    @JvmStatic
    fun <T> text(saying: T): String {
        return saying as String
    }

    @JvmStatic
    fun <T> number(ofValue: T): Number {
        return ofValue as Number
    }

    @JvmStatic
    fun <T> natural(number: T): Int? {
        return number as Int
    }

    infix fun <T, U> InvocationHelper<T>.alongWith(somethingElse: U) = this.and(somethingElse)
    infix fun <T, U> InvocationHelper<T>.alongWith(somethingElse: () -> InvocationHelper<U>) = this.and(somethingElse.invoke().toDo())
    infix fun <T, U> InvocationHelper<T>.and(somethingElse: U) = this.and(somethingElse)
    infix fun <T, U> InvocationHelper<T>.and(somethingElse: () -> InvocationHelper<U>) = this.and(somethingElse.invoke().toDo())
    infix fun <T, U, V> BiInvocationHelper<T, U>.alongWith(somethingElse: V) = this.and(somethingElse)
    infix fun <T, U, V> BiInvocationHelper<T, U>.alongWith(somethingElse: () -> InvocationHelper<V>) = this.and(somethingElse.invoke().toDo())
    infix fun <T, U, V> BiInvocationHelper<T, U>.and(somethingElse: V) = this.and(somethingElse)
    infix fun <T, U, V> BiInvocationHelper<T, U>.and(somethingElse: () -> InvocationHelper<V>) = this.and(somethingElse.invoke().toDo())
    infix fun Ideaomatic.use(something: String) = InvocationHelper(something)
    infix fun Ideaomatic.use(something: Int) = InvocationHelper(something)
    infix fun Ideaomatic.use(something: Boolean) = InvocationHelper(something)
    infix fun <T> Ideaomatic.use(something: T) = something
    infix fun <T> Ideaomatic.add(something: () -> InvocationHelper<T>) = ElementAdder(something.invoke().toDo())
    infix fun <T> CollectionHandler<MutableList<T>, T>.add(something: () -> InvocationHelper<T>) =
            this.alsoAdd(something.invoke().toDo())

    val the get() = Ideaomatic
    val a get() = Ideaomatic
    infix fun <T> Ideaomatic.listOf(type: Class<T>) = InvocationHelper(mutableListOf<T>())
    infix fun Ideaomatic.text(like: String) = InvocationHelper(like)
    infix fun Ideaomatic.text(like: Ideaomatic) = like
    infix fun Ideaomatic.number(like: Ideaomatic) = like
    infix fun Ideaomatic.number(like: Int) = InvocationHelper(like)
    infix fun Ideaomatic.value(like: Ideaomatic) = like
    infix fun Ideaomatic.letsSay(mmmh: Ideaomatic) = mmmh
    infix fun <T> Ideaomatic.value(like: T) = InvocationHelper(like)
    val like get() = Ideaomatic
    val mmmh get() = Ideaomatic
    infix fun <T> Ideaomatic.forExample(that: T) = InvocationHelper(that)
    infix fun <T> Ideaomatic.letsSay(mmmh: T) = InvocationHelper(mmmh)
    infix fun <T> Ideaomatic.letMeRemember(ohYes: T) = InvocationHelper(ohYes)
    infix fun <T> Ideaomatic.ohYes(ohYes: T) = InvocationHelper(ohYes)

    infix fun <T> T.and(anything: Ideaomatic) = this
    infix fun <T> CollectionHandler<MutableList<T>, T>.andWithAll(anything: Ideaomatic) = InvocationHelper(this.value())

    infix fun <T, R> InvocationHelper<T>.__to(operation: KFunction<R>) = DataHolder(operation.call(toDo()))
    infix fun <T, R> InvocationHelper<T>._to(operation: (Array<*>) -> R) = DataHolder(operation(arrayOf(toDo() as Any)))
    infix fun <T, R> InvocationHelper<T>._toDo(operation: KFunction<R>) = DataHolder(operation.call(toDo()))
    infix fun <T, R> InvocationHelper<T>.toDo(operation: (Array<*>) -> R) = DataHolder(operation(arrayOf(toDo() as Any)))
    infix fun <T, R> InvocationHelper<T>.__do(operation: KFunction<R>) = DataHolder(operation.call(toDo()))
    infix fun <T, R> InvocationHelper<T>._do(operation: (Array<*>) -> R) = DataHolder(operation(arrayOf(toDo() as Any)))
    infix fun <T, U, R> BiInvocationHelper<T, U>.__to(operation: KFunction<R>) = callAndTweakVarargs(toDo(), operation)
    infix fun <T, U, R> BiInvocationHelper<T, U>._to(operation: (Array<*>) -> R) = DataHolder(operation(toDo()))
    infix fun <T, U, R> BiInvocationHelper<T, U>._toDo(operation: KFunction<R>) = callAndTweakVarargs(toDo(), operation)
    infix fun <T, U, R> BiInvocationHelper<T, U>.toDo(operation: (Array<*>) -> R) = DataHolder(operation(toDo()))
    infix fun <T, U, R> BiInvocationHelper<T, U>.__do(operation: KFunction<R>) = callAndTweakVarargs(toDo(), operation)
    infix fun <T, U, R> BiInvocationHelper<T, U>._do(operation: (Array<*>) -> R) = DataHolder(operation(toDo()))
    infix fun <T, U, V, R> TriInvocationHelper<T, U, V>.__to(operation: KFunction<R>) = callAndTweakVarargs(toDo(), operation)
    infix fun <T, U, V, R> TriInvocationHelper<T, U, V>._to(operation: (Array<*>) -> R) = DataHolder(operation(toDo()))
    infix fun <T, U, V, R> TriInvocationHelper<T, U, V>.toDo(operation: (Array<*>) -> R) = DataHolder(operation(toDo()))
    infix fun <T, U, V, R> TriInvocationHelper<T, U, V>._toDo(operation: KFunction<R>) = callAndTweakVarargs(toDo(), operation)
    infix fun <T, U, V, R> TriInvocationHelper<T, U, V>._do(operation: (Array<*>) -> R) = DataHolder(operation(toDo()))
    infix fun <T, U, V, R> TriInvocationHelper<T, U, V>.__do(operation: KFunction<R>) = callAndTweakVarargs(toDo(), operation)
    infix fun <T, U, V, W, R> QuadriInvocationHelper<T, U, V, W>.__to(operation: KFunction<R>) = callAndTweakVarargs(toDo(), operation)
    infix fun <T, U, V, W, R> QuadriInvocationHelper<T, U, V, W>._to(operation: (Array<*>) -> R) = DataHolder(operation(toDo()))
    infix fun <T, U, V, W, R> QuadriInvocationHelper<T, U, V, W>._toDo(operation: KFunction<R>) = callAndTweakVarargs(toDo(), operation)
    infix fun <T, U, V, W, R> QuadriInvocationHelper<T, U, V, W>.toDo(operation: (Array<*>) -> R) = DataHolder(operation(toDo()))
    infix fun <T, U, V, W, R> QuadriInvocationHelper<T, U, V, W>.__do(operation: KFunction<R>) = callAndTweakVarargs(toDo(), operation)
    infix fun <T, U, V, W, R> QuadriInvocationHelper<T, U, V, W>._do(operation: (Array<*>) -> R) = DataHolder(operation(toDo()))

    private fun <R> callAndTweakVarargs(value: Array<*>, operation: KFunction<R>) =
            if(operation.parameters.size == 1 && operation.parameters[0].isVararg)
                DataHolder(operation.call((java.lang.reflect.Array.newInstance(operation.parameters[0].type.jvmErasure.java.componentType,
                                                                          value.size) as Array<Any?>).apply {
                    System.arraycopy(value, 0, this, 0, value.size)
                }))
            else DataHolder(operation.call(*value))

    infix fun <R> DataHolder<R>.then(afterThat: Ideaomatic) = this
    val useTheResult get() = Ideaomatic
    val also get() = Ideaomatic
    val ofThem get() = Ideaomatic

    infix fun <R> DataHolder<R>._to(_do: (R) -> Any?) = _do(value())

    infix fun <T, U> DataHolder<T>.with(anotherThing: () -> InvocationHelper<U>) = BiInvocationHelper(this.value(), anotherThing.invoke().toDo())
    infix fun <T, U> DataHolder<T>.alongWith(anotherThing: () -> InvocationHelper<U>) = BiInvocationHelper(this.value(), anotherThing.invoke().toDo())
    infix fun <T, U, V> DataHolder<T>._with(anotherThing: () -> BiInvocationHelper<U, V>) = anotherThing.invoke().let { TriInvocationHelper(this.value(), it.toDo()[0] as U, it.toDo()[1] as V) }
    infix fun <T, U, V> DataHolder<T>._alongWith(anotherThing: () -> BiInvocationHelper<U, V>) = anotherThing.invoke().let { TriInvocationHelper(this.value(), it.toDo()[0] as U, it.toDo()[1] as V) }
    infix fun <T, U, V, W> DataHolder<T>.__with(anotherThing: () -> TriInvocationHelper<U, V, W>) = anotherThing.invoke().let {
        QuadriInvocationHelper(this.value(), it.toDo()[0] as U, it.toDo()[1] as V, it.toDo()[2] as W) }
    infix fun <T, U, V, W> DataHolder<T>.__alongWith(anotherThing: () -> TriInvocationHelper<U, V, W>) = anotherThing.invoke().let {
        QuadriInvocationHelper(this.value(), it.toDo()[0] as U, it.toDo()[1] as V, it.toDo()[2] as W) }

    infix fun <T> ElementAdder<T>._to(theCollection: () -> InvocationHelper<MutableList<T>>) =
            CollectionHandler(theCollection.invoke().toDo().apply { add(value()) })

    val displayInTheConsole = { T: Any? -> System.out.println(T) }

    val formatting = { parameters: Array<*> ->
        String.format(parameters[0].toString(), *parameters.drop(1).toTypedArray())
    }

    val concatenation = { parameters: Array<*> ->
        parameters.flatMap {
            when (it) {
                is Collection<*> -> it.toList()
                else             -> listOf(it)
            }
        }
    }


    @JvmStatic
    fun <T> use(that: T): InvocationHelper<T> {
        return InvocationHelper(that)
    }

    @JvmStatic
    fun <T> weHave(theResult: Predicate<T>): Predicate<T> {
        return theResult
    }

    @JvmStatic
    fun <T> _weHave(theResult: (T) -> Boolean): (T) -> Boolean {
        return theResult
    }

    @JvmStatic
    fun <T, R> withThe(data: (T) -> R): (T) -> R {
        return data
    }

    @JvmStatic
    fun <T, R> _apply(data: (T) -> R): (T) -> R {
        return data
    }

    @JvmStatic
    fun <U : Collection<T>, V : Collection<R>, T, R> forEach(elementInTheCollection: (T) -> R): (U) -> V {
        return { collection -> collection.map { t -> elementInTheCollection.invoke(t) } as V }
    }

    @JvmStatic
    fun <T> theList(vararg containingExactly: T): List<T> {
        return Arrays.asList<T>(*containingExactly)
    }

    @JvmStatic
    fun <T> theModifiableList(vararg containingExactly: T): List<T> {
        return ArrayList(theList(*containingExactly))
    }

    @JvmStatic
    fun <T> _theList(vararg containingExactly: T): CollectionHandler<MutableList<T>, T> {
        return CollectionHandler(mutableListOf(*containingExactly))
    }

    @JvmStatic
    fun <T, U : MutableCollection<T>> withTheList(vararg containingExactly: T): CollectionHandler<U, T> {
        return CollectionHandler(mutableListOf(*containingExactly) as U)
    }

    @JvmStatic
    fun <T> with(data: T): DataHolder<T> {
        return DataHolder(data)
    }


    @JvmStatic
    fun <T, U : MutableCollection<T>> _a(collection: U): CollectionHandler<U, T> {
        return CollectionHandler(collection)
    }

    @JvmStatic
    fun <T, U : MutableCollection<T>> _a(data: T): CollectionHandler<U, T> {
        return CollectionHandler(mutableListOf<T>(data) as U)
    }

    @JvmStatic
    fun <T, U : MutableCollection<T>> newList(): CollectionHandler<U, T> {
        return CollectionHandler(ArrayList<T>() as U)
    }

    @JvmStatic
    fun <T, U : MutableCollection<T>> newListOf(thisClass: Class<T>): CollectionHandler<U, T> {
        return newList()
    }

    @JvmStatic
    fun <T> add(element: T): ElementAdder<T> {
        return ElementAdder(element)
    }

    @JvmStatic
    fun <T> andNow(useThis: DataHolder<T>): Looper<T> {
        return Looper(useThis)
    }

    @JvmStatic
    fun <T> applySilently(theFunction: (T) -> Any?): Looper<T> {
        return Looper(InvocationHelper._do(theFunction))
    }

    @JvmStatic
    fun <T> apply(theFunction: (T) -> T): Looper<T> {
        return Looper(theFunction)
    }

    val <T> T.`ok, now` get() = this

    val <T> T.now get() = this

    val <T> T.please get() = this

    fun `tell me`(now: Ideaomatic.() -> Unit) = now(Ideaomatic)

    fun so(now: Ideaomatic.() -> Unit) = now(Ideaomatic)

    fun ok(now: Ideaomatic.() -> Unit) = now(Ideaomatic)

}
