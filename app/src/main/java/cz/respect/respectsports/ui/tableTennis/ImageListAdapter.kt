package cz.respect.respectsports.ui.tableTennis

import android.content.Context
import android.icu.text.SimpleDateFormat
import android.util.Log
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
            holder.homePlayerName = itemBinding.homePlayerName
            holder.visitorPlayerName = itemBinding.visitorPlayerName
            holder.result = itemBinding.result
            convertView.tag = holder
        } else {
            holder = convertView.tag as ItemViewHolder
        }

        //holder.id!!.text = this.itemList!![position].id
        holder.date!!.text = SimpleDateFormat("d.M.yyyy").format( this.itemList!![position].date.toDouble().toLong()).toString()//this.itemList!![position].date
        holder.homePlayerName!!.text = this.itemList!![position].homePlayerName
        holder.visitorPlayerName!!.text = this.itemList!![position].visitorPlayerName
        holder.result!!.text = this.itemList!![position].result
        return convertView
    }

    internal class ItemViewHolder {
        var id: TextView? = null
        var date: TextView? = null
        var homePlayerName: TextView? = null
        var visitorPlayerName: TextView? = null
        var result: TextView? = null
        //var icon: ImageView? = null
    }
}
