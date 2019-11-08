package outside

import org.junit.Assert.fail
import org.junit.Test
import org.toilelibre.libe.ideaomatic.Ideaomatic.Do
import org.toilelibre.libe.ideaomatic.Ideaomatic.Get
import org.toilelibre.libe.ideaomatic.Ideaomatic.`ok, now`
import org.toilelibre.libe.ideaomatic.Ideaomatic.`tell me`
import org.toilelibre.libe.ideaomatic.Ideaomatic.ok
import org.toilelibre.libe.ideaomatic.Ideaomatic.so
import org.toilelibre.libe.ideaomatic.Ideaomatic.well
import java.time.ZoneOffset

class Kotlin {

    @Test
    fun formatAText() {
        `tell me` {
            now use the text like letsSay
                    "My name is %s and I am %d years old" alongWith
                    { the text "Lily" } and
                    { a number like letMeRemember 3 } toDo formatting then
                    useTheResult _to displayInTheConsole
        }
    }

    @Test
    fun buildAList() {
        so {
            now add { a text like forExample "First Element" } _to
                    { a listOf TEXTS } and also add
                    { a value "Second Element" } andWithAll ofThem alongWith
                    { the text "Third Element" } _do concatenation then
                    useTheResult _to displayInTheConsole

        }
    }

    @Test
    fun buildLocalDateTime() {
        ok {
            please use a number like forExample 2018 __to Get::aYear and also with
                    { a number like letMeRemember 3 } __do Get::aYearMonth and also with
                    { a number like letsSay mmmh ohYes 3 } __do Get::aDate and also _with
                    { the number 14 and { the number 25 } } __do Get::aDateTime then
                    useTheResult alongWith { the value ZoneOffset.ofHours(2) } __to Get::aDateTimeWithTimezone then
                    useTheResult _to displayInTheConsole

        }
    }

    @Test
    fun joinText() {
        ok {
            now use the text "This text " alongWith { the text "will be displayed " } alongWith { the text "as one" } __to Do::join then
                    useTheResult _to displayInTheConsole

        }
    }

    fun method (arg0: String, arg1: Number, arg2: Boolean, arg3: Array<Byte>) =
        arg0 + arg1 + arg2 + arg3.toString()
    @Test
    fun fourArgsCall () {
        `ok, now` {
            please use the text "a" alongWith { the number 1 } and also with SOMETHING_TRUE and also _with { arrayOf<Byte>(0, 1, 0).injected } __to
                    ::method then useTheResult _to displayInTheConsole
        }
    }

    @Test
    fun reverseListConditionChecksForBetterReadability() {
        well {
            if (a listOf TEXTS isNot empty) {
                fail()
            }

            if (a listOf TEXTS __with { the text "An element" } `is` empty) {
                fail()
            }

            if (a listOf TEXTS __with { the text "An element" } doesNotContain "An element") {
                fail()
            }

            if (the text "foo" isNotIncludedIn { theList("foo", "bar") }) {
                fail()
            }
        }
    }
}



