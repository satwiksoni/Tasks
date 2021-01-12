package com.example.tasks_to_do

import android.content.Intent
import android.graphics.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView

import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainActivity : AppCompatActivity(), ItodoAdapter {
    val db by lazy {
        AppDB.getDatabase(this)
    }

    val list = arrayListOf<TodoModel>()
    var adapter = TodoAdaptor(list,this)
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //tolbar setting
        setSupportActionBar(toolbar)

        //Adapter ka kaam
        todoRv.apply {
            layoutManager=LinearLayoutManager(this@MainActivity)
            adapter=this@MainActivity.adapter
        }


        db.todoDao().getTask().observe(this, Observer {
            if(!it.isNullOrEmpty())
            {
                list.clear()
                list.addAll(it)
                adapter.notifyDataSetChanged()
            }
        })
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        val item = menu.findItem(R.id.search)
        val searchView = item.actionView as SearchView
        item.setOnActionExpandListener(object :MenuItem.OnActionExpandListener{
            override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
                displayTodo()
                return true
            }

            override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                displayTodo()
                return true
            }

        })
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if(!newText.isNullOrEmpty()){
                    displayTodo(newText)
                }
                return true
            }

        })

        return super.onCreateOptionsMenu(menu)
    }


    fun displayTodo(newText: String = "") {
        db.todoDao().getTask().observe(this, Observer {
            if(it.isNotEmpty()){
                list.clear()
                list.addAll(
                        it.filter { todo ->
                            todo.title.contains(newText,true)
                        }
                )
                adapter.notifyDataSetChanged()
            }
        })
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean
    {
        when(item.itemId)
        {
            R.id.history->{
                startActivity(Intent(this,HistoryActivity::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun FloatingActionOnclick(view: View) {
        val intent=Intent(this,NewTaskActivity::class.java)
        startActivity(intent)
    }

    override fun OnItemClicked(todo_model: TodoModel)
    {
        GlobalScope.launch(Dispatchers.Main) {
            withContext(Dispatchers.IO)
            {
                return@withContext db.todoDao().delete(todo_model)
            }
        }
    }


}
