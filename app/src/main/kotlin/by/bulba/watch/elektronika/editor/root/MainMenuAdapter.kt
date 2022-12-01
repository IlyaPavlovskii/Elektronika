package by.bulba.watch.elektronika.editor.root

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import by.bulba.watch.elektronika.R
import by.bulba.watch.elektronika.databinding.MenuItemBinding
import by.bulba.watch.elektronika.utils.wrapper.DrawableWrapper
import by.bulba.watch.elektronika.utils.wrapper.TextWrapper
import by.bulba.watch.elektronika.utils.wrapper.setImageDrawable
import by.bulba.watch.elektronika.utils.wrapper.setText

internal class MainMenuAdapter(
    argItems: List<MenuItem> = enumValues<MenuItem>().toList(),
    private val callback: AdapterCallback? = null
) : RecyclerView.Adapter<MainMenuAdapter.Holder>() {

    private val items: MutableList<MenuItem> = mutableListOf(*argItems.toTypedArray())

    fun interface AdapterCallback {
        fun onItemClicked(menuItem: MenuItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder = Holder(
        MenuItemBinding.inflate(LayoutInflater.from(parent.context))
    ).apply {
        this.binding.root.setOnClickListener {
            this.getTag()?.also { menuItem -> callback?.onItemClicked(menuItem) }
        }
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(items: List<MenuItem>) {
        this.items.clear()
        this.items.addAll(items)
        this.notifyDataSetChanged()
    }

    class Holder(internal val binding: MenuItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MenuItem) {
            binding.icon.setImageDrawable(item.icon)
            binding.text.setText(item.text)
            binding.root.tag = item
        }

        fun getTag(): MenuItem? = binding.root.tag as? MenuItem
    }
}

internal enum class MenuItem(
    val icon: DrawableWrapper,
    val text: TextWrapper,
) {
    WATCH_FACE(
        icon = DrawableWrapper.Id(R.drawable.baseline_watch_24),
        text = TextWrapper.Id(R.string.elektronika__menu_item_watch_face),
    ),
    TIME_FORMAT(
        icon = DrawableWrapper.Id(R.drawable.baseline_watch_later_24),
        text = TextWrapper.Id(R.string.elektronika__menu_item_time_format),
    ),
    ;
}