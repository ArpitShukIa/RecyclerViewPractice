// https://youtu.be/rcSNkSJ624U  : Swipe Feature

package com.example.recyclerviewpractice

import android.graphics.Canvas
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), RecyclerViewAdapter.OnNameListener {

    private lateinit var recyclerViewAdapter: RecyclerViewAdapter
    private lateinit var data : ArrayList<Names>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        initRecyclerView()
        addDataSet()

    }

    fun addDataSet() {

        data = DataSource.createDataSet()
        recyclerViewAdapter.submitList(data)

    }

    private fun initRecyclerView() {

        recycler_view.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)

            recyclerViewAdapter = RecyclerViewAdapter(recycler_view, this@MainActivity)

            val callback : ItemTouchHelper.Callback = MyItemTouchHelper(recyclerViewAdapter)
            val itemTouchHelper = ItemTouchHelper(callback)
            recyclerViewAdapter.setItemTouchHelper(itemTouchHelper)
            itemTouchHelper.attachToRecyclerView(recycler_view)

            adapter = recyclerViewAdapter

        }

    }

    override fun onNoteClick(position: Int) {

        Toast.makeText(this,data[position].friend_name,Toast.LENGTH_SHORT).show()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        val menuInflater : MenuInflater = getMenuInflater()
        menuInflater.inflate(R.menu.main_menu,menu)

        val searchItem : MenuItem = menu!!.findItem(R.id.action_search)
        val searchView : SearchView = searchItem.actionView as SearchView

        searchView.setOnQueryTextListener( object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                recyclerViewAdapter.filter.filter(newText)
                return false
            }

        })

        return true
    }
}
