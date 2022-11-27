package by.bulba.watch.elektronika.data

/**
 * Relative factor to flexible positioning drawer elements on the screen
 * */
@JvmInline
value class RelativeFactor(val factor: Float)

inline val Number.rf: RelativeFactor
    get() = RelativeFactor(this.toFloat())