package by.bulba.watch.elektronika.complication

internal sealed class Complication {
    abstract val id: Identifier

    object Left : Complication() {
        override val id: Identifier = Identifier(100)
    }

    object Right : Complication() {
        override val id: Identifier = Identifier(101)
    }

    @JvmInline
    value class Identifier(val value: Int)
}