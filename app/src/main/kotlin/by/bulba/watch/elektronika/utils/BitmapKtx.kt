package by.bulba.watch.elektronika.utils

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.graphics.RectF

private val paint = Paint().apply {
    this.isAntiAlias = true
    this.color = Color.RED
}
private val xfermodePaint = Paint().apply {
    this.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
}
private const val ROUNDED_ANGLES = 16f

internal fun Bitmap.makeCircle(): Bitmap {
    val output = Bitmap.createBitmap(this.width, this.height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(output)
    val rect = Rect(0, 0, this.width, this.height)
    canvas.drawCircle(this.width / 2f, this.height / 2f, this.width / 2f, paint)
    canvas.drawBitmap(this, rect, rect, xfermodePaint)
    return output
}

internal fun Bitmap.makeRoundedAngles(): Bitmap {
    val output = Bitmap.createBitmap(this.width, this.height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(output)
    val rect = Rect(0, 0, this.width, this.height)
    val rectF = RectF(0f, 0f, this.width.toFloat(), this.height.toFloat())
    canvas.drawRoundRect(rectF, ROUNDED_ANGLES, ROUNDED_ANGLES, paint)
    canvas.drawBitmap(this, rect, rect, xfermodePaint)
    return output
}


