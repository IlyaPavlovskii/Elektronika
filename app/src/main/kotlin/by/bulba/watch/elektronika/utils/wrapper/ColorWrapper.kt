package by.bulba.watch.elektronika.utils.wrapper

import android.content.Context
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat

sealed class ColorWrapper {

    @ColorInt
    abstract fun get(context: Context): Int

    data class Raw(@ColorInt val color: Int) : ColorWrapper() {
        override fun get(context: Context): Int {
            return color
        }
    }

    data class Id(@ColorRes val id: Int) : ColorWrapper() {
        override fun get(context: Context): Int {
            return ContextCompat.getColor(context, id)
        }
    }

    companion object {
        val TRANSPARENT = Raw(0)
    }
}
