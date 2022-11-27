package by.bulba.watch.elektronika.utils.wrapper

import android.content.Context
import androidx.annotation.DimenRes
import androidx.annotation.Px

sealed class DimensWrapper {
    abstract fun getFloat(context: Context): Float

    abstract fun getInt(context: Context): Int

    data class Raw(@Px val pixels: Int) : DimensWrapper() {

        override fun getInt(context: Context): Int {
            return pixels
        }

        override fun getFloat(context: Context): Float {
            return getInt(context).toFloat()
        }
    }

    data class Id(@DimenRes val id: Int) : DimensWrapper() {

        override fun getInt(context: Context): Int {
            return context.resources.getDimensionPixelSize(id)
        }

        override fun getFloat(context: Context): Float {
            return context.resources.getDimension(id)
        }
    }
}
