package by.bulba.watch.elektronika.editor.face

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import by.bulba.watch.elektronika.data.watchface.PaletteStyle
import by.bulba.watch.elektronika.databinding.PaletteConfigItemBinding

internal class PaletteConfigAdapter(
    private val leftComplicationClickListener: OnComplicationClickListener? = null,
    private val rightComplicationClickListener: OnComplicationClickListener? = null,
) : RecyclerView.Adapter<PaletteConfigAdapter.Holder>() {

    private val items: MutableList<PaletteItem> = mutableListOf()

    fun interface OnComplicationClickListener {
        fun onComplicationClick()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder = Holder(
        PaletteConfigItemBinding.inflate(LayoutInflater.from(parent.context))
    ).apply {
        this.binding.leftComplication.setOnClickListener {
            leftComplicationClickListener?.onComplicationClick()
        }
        this.binding.rightComplication.setOnClickListener {
            rightComplicationClickListener?.onComplicationClick()
        }
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = items.size

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(items: List<PaletteItem>) {
        this.items.clear()
        this.items.addAll(items)
        this.notifyDataSetChanged()
    }

    fun getItem(index: Int): PaletteItem = items[index]

    class Holder(
        internal val binding: PaletteConfigItemBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: PaletteItem) {
            binding.watchFace.setImageBitmap(item.image)
        }
    }
}

internal data class PaletteItem(
    val image: Bitmap,
    val domainMeta: PaletteStyle.Identifier,
)