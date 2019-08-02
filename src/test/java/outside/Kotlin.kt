package outside

import org.junit.Test
import org.toilelibre.libe.ideaomatic.Ideaomatic
import org.toilelibre.libe.ideaomatic.Ideaomatic.Do
import org.toilelibre.libe.ideaomatic.Ideaomatic.Get
import org.toilelibre.libe.ideaomatic.Ideaomatic.`tell me`
import org.toilelibre.libe.ideaomatic.Ideaomatic.ok
import org.toilelibre.libe.ideaomatic.Ideaomatic.so
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
}



