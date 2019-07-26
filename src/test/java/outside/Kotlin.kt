package outside

import org.junit.Test
import org.toilelibre.libe.ideaomatic.Ideaomatic.`tell me`

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
}

