package net.yuzumone.bergamio.view

import android.content.Context
import android.support.annotation.UiThread
import android.support.v7.widget.RecyclerView
import java.util.*

abstract class ArrayRecyclerAdapter<T, VH : RecyclerView.ViewHolder>(val context: Context) :
        RecyclerView.Adapter<VH>(), Iterable<T> {

    protected val list: ArrayList<T> = ArrayList()

    @UiThread
    fun reset(items: Collection<T>) {
        clear()
        addAll(items)
        notifyDataSetChanged()
    }

    fun clear() {
        list.clear()
    }

    fun addAll(items: Collection<T>) {
        list.addAll(items)
    }

    fun getItem(position: Int): T {
        return list.get(position)
    }

    fun addItem(item: T) {
        list.add(item)
    }

    fun addAll(position: Int, items: Collection<T>) {
        list.addAll(position, items)
    }

    @UiThread
    fun addAllWithNotify(items: Collection<T>) {
        val position = itemCount
        addAll(items)
        notifyItemInserted(position)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun iterator(): Iterator<T> {
        return list.iterator()
    }
}