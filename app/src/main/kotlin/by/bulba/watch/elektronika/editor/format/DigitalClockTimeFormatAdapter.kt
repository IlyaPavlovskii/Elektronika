package by.bulba.watch.elektronika.editor.format

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import by.bulba.watch.elektronika.data.watchface.DigitalClockTimeFormat
import by.bulba.watch.elektronika.databinding.TimeFormatItemBinding

internal class DigitalClockTimeFormatAdapter(
    private val callback: OnTimeFormatClickListener? = null
) : RecyclerView.Adapter<DigitalClockTimeFormatAdapter.Holder>() {

    private val items: MutableList<TimeFormatItem> = mutableListOf()

    fun interface OnTimeFormatClickListener {
        fun onItemClicked(menuItem: TimeFormatItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder = Holder(
        TimeFormatItemBinding.inflate(LayoutInflater.from(parent.context))
    ).apply {
        this.binding.root.setOnClickListener {
            this.getTag()?.also { item -> callback?.onItemClicked(item) }
        }
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = items.size

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(items: List<TimeFormatItem>) {
        this.items.clear()
        this.items.addAll(items)
        this.notifyDataSetChanged()
    }

    class Holder(
        internal val binding: TimeFormatItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: TimeFormatItem) {
            binding.radioButton.isChecked = item.selected
            binding.text.text = item.text
            binding.root.tag = item
        }

        fun getTag(): TimeFormatItem? = binding.root.tag as? TimeFormatItem
    }
}

internal data class TimeFormatItem(
    val text: String,
    val selected: Boolean,
    val domainMetaData: DigitalClockTimeFormat.Identifier,
)

