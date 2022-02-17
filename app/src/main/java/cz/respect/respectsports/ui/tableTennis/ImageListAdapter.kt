package cz.respect.respectsports.ui.tableTennis

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import cz.respect.respectsports.databinding.GridItemBinding
import cz.respect.respectsports.domain.Match


internal class ImageListAdapter internal constructor(
    context: Context,
    private val resource: Int,
    private val itemList: ArrayList<Match>?
) : ArrayAdapter<ImageListAdapter.ItemViewHolder>(context, resource) {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private lateinit var itemBinding: GridItemBinding

    override fun getCount(): Int {
        return if (this.itemList != null) this.itemList.size else 0
    }

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        var convertView = view
        val holder: ItemViewHolder
        if (convertView == null) {
            itemBinding = GridItemBinding.inflate(inflater)
            convertView = itemBinding.root
            holder = ItemViewHolder()
            //holder.id = itemBinding.id
            holder.date = itemBinding.date
            holder.homePlayer = itemBinding.homePlayer
            holder.visitorPlayer = itemBinding.visitorPlayer
            holder.result = itemBinding.result
            //holder.icon = itemBinding.icon
            convertView.tag = holder
        } else {
            holder = convertView.tag as ItemViewHolder
        }
        //holder.id!!.text = this.itemList!![position].id
        holder.date!!.text = this.itemList!![position].date
        holder.homePlayer!!.text = this.itemList!![position].homePlayer
        holder.visitorPlayer!!.text = this.itemList!![position].visitorPlayer
        holder.result!!.text = this.itemList!![position].result
        //holder.icon!!.setImageResource(R.mipmap.ic_launcher)
        return convertView
    }

    internal class ItemViewHolder {
        var id: TextView? = null
        var date: TextView? = null
        var homePlayer: TextView? = null
        var visitorPlayer: TextView? = null
        var result: TextView? = null
        //var icon: ImageView? = null
    }
}
