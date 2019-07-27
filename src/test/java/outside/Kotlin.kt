package outside

import org.junit.Test
import org.toilelibre.libe.ideaomatic.Ideaomatic.`tell me`
import org.toilelibre.libe.ideaomatic.Ideaomatic.so

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
}

