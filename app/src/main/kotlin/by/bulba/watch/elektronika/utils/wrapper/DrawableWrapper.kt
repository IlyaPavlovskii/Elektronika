package by.bulba.watch.elektronika.utils.wrapper

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat

sealed class DrawableWrapper {
    abstract fun get(context: Context): Drawable

    data class Id(@DrawableRes val id: Int) : DrawableWrapper() {
        override fun get(context: Context): Drawable {
            val drawable = if (id != 0) ContextCompat.getDrawable(context, id)
            else error("Drawable id mustn't be null")
            return requireNotNull(drawable)
        }
    }

    data class Raw(val drawable: Drawable) : DrawableWrapper() {
        override fun get(context: Context): Drawable {
            return drawable
        }
    }
}
