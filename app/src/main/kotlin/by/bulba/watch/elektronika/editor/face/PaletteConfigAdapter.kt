package by.bulba.watch.elektronika.editor.face

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import by.bulba.watch.elektronika.data.watchface.PaletteStyle
import by.bulba.watch.elektronika.databinding.PaletteConfigItemBinding

internal class PaletteConfigAdapter : RecyclerView.Adapter<PaletteConfigAdapter.Holder>() {

    private val items: MutableList<PaletteItem> = mutableListOf()

    fun interface OnPaletteItemClickListener {
        fun onItemClicked(menuItem: PaletteItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder = Holder(
        PaletteConfigItemBinding.inflate(LayoutInflater.from(parent.context))
    ).apply {
//        this.binding.root.setOnClickListener {
//            this.getTag()?.also { item -> callback?.onItemClicked(item) }
//        }
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

    class Holder(
        internal val binding: PaletteConfigItemBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: PaletteItem) {
            //binding.watchFace.setImageBitmap(item.image)
            binding.leftComplication.text = item.domainMeta.value
        }

        fun getTag(): PaletteItem? = binding.root.tag as? PaletteItem
    }
}

internal data class PaletteItem(
    //val image: Bitmap,
    val domainMeta: PaletteStyle.Identifier,
)