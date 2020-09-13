package com.example.recyclerviewpractice

import android.util.Log
import android.view.*
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar


class RecyclerViewAdapter(val view: View, val mOnNameListener: OnNameListener) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>(), Filterable, ItemTouchHelperAdapter {

    var items: MutableList<Names> = ArrayList()
    var items2: MutableList<Names> = ArrayList()
    val archivedNames = ArrayList<Names>()
    lateinit var touchHelper: ItemTouchHelper

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.layout_names, parent, false),
            mOnNameListener,
            touchHelper

        )

    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when (holder) {
            is ViewHolder -> {
                holder.bind(items[position])
            }
        }

    }


    fun setItemTouchHelper(touchHelper: ItemTouchHelper) {
        this.touchHelper = touchHelper
    }

    fun submitList(namesList: MutableList<Names>) {
        items = namesList
        items2 = ArrayList<Names>(namesList)
    }

    class ViewHolder(
        itemView: View,
        var onNameListener: OnNameListener,
        val touchHelper: ItemTouchHelper
    ) : RecyclerView.ViewHolder(itemView),
        View.OnTouchListener,
        GestureDetector.OnGestureListener {

        var name: TextView = itemView.findViewById(R.id.text_name)
        val gestureDetector = GestureDetector(itemView.context, this)

        fun bind(PersonName: Names) {

            name.text = PersonName.friend_name
            itemView.setOnTouchListener(this)

        }

        override fun onTouch(v: View?, event: MotionEvent?): Boolean {
            gestureDetector.onTouchEvent(event)
            return true
        }

        override fun onShowPress(e: MotionEvent?) {

        }

        override fun onSingleTapUp(e: MotionEvent?): Boolean {
            onNameListener.onNoteClick(adapterPosition)
            return true
        }

        override fun onDown(e: MotionEvent?): Boolean {
            return false
        }

        override fun onFling(
            e1: MotionEvent?,
            e2: MotionEvent?,
            velocityX: Float,
            velocityY: Float
        ): Boolean {
            return true
        }

        override fun onScroll(
            e1: MotionEvent?,
            e2: MotionEvent?,
            distanceX: Float,
            distanceY: Float
        ): Boolean {
            return false
        }

        override fun onLongPress(e: MotionEvent?) {
            touchHelper.startDrag(this)
        }

    }

    interface OnNameListener {
        fun onNoteClick(position: Int)
    }

    override fun getFilter(): Filter {
        return listFilter
    }

    private val listFilter: Filter = object : Filter() {
        override fun performFiltering(constraint: CharSequence): FilterResults {
            val filteredList: MutableList<Names> = ArrayList()
            if (constraint.isEmpty()) {
                filteredList.addAll(items2)
            } else {
                val filterPattern = constraint.toString().toLowerCase().trim { it <= ' ' }
                for (item in items2) {
                    if (item.friend_name.toLowerCase().contains(filterPattern)) {
                        filteredList.add(item)
                    }
                }
            }
            val results = FilterResults()
            results.values = filteredList
            return results
        }

        override fun publishResults(constraint: CharSequence, results: FilterResults) {
            items.clear()
            items.addAll(results.values as List<Names>)
            notifyDataSetChanged()
        }
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int) {
        val fromName = items[fromPosition]
        items.removeAt(fromPosition)
        items.add(toPosition, fromName)
        notifyItemMoved(fromPosition, toPosition)
    }

    override fun onItemSwiped(position: Int, direction: Int) {
        when (direction) {
            ItemTouchHelper.LEFT -> {
                Log.d("RecyclerView :", " onitem swiped inside delete")
                val deletedName = items[position]

                items.removeAt(position)
                notifyItemRemoved(position)

                Snackbar.make(view, deletedName.friend_name, Snackbar.LENGTH_LONG)
                    .setAction("Undo") {
                        items.add(position, deletedName)
                        notifyItemInserted(position)
                    }
                    .show()
            }

            ItemTouchHelper.RIGHT -> {
                val archivedName = items[position]
                archivedNames.add(archivedName)

                items.removeAt(position)
                notifyItemRemoved(position)

                Snackbar.make(view, archivedName.friend_name + ", Archived", Snackbar.LENGTH_LONG)
                    .setAction("Undo") {
                        items.add(position, archivedName)
                        notifyItemInserted(position)

                        archivedNames.removeAt(archivedNames.lastIndex)
                    }
                    .show()
            }
            else -> Log.d("RevyclerView :", " onitem swiped inside else")
        }

    }

}