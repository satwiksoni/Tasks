package com.example.tasks_to_do

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_todo.view.*
import java.text.SimpleDateFormat
import java.util.*

class TodoAdaptor(val list:List<TodoModel>,private val listener:ItodoAdapter):RecyclerView.Adapter<TodoAdaptor.TodoViewhoder>() {
    class TodoViewhoder(itemView: View) :RecyclerView.ViewHolder(itemView) {
        val delete_button=itemView.findViewById<ImageView>(R.id.Delete_button)
        fun bind(todoModel: TodoModel) {
            with(itemView)
            {

                Title_txt.text=todoModel.title
                showTask_txt.text=todoModel.description
                Category_txt.text=todoModel.category
                updateTime(todoModel.time)
                updateDate(todoModel.date)
            }
        }

        private fun updateTime(time: Long) {
            val myFormate="h:mm:a"
            val sdf=SimpleDateFormat(myFormate)
            itemView.TimeAll_txt.text=sdf.format(Date(time))

        }

        private fun updateDate(date: Long) {
            val myFormate="EEE,d MMM yyyy"
            val sdf=SimpleDateFormat(myFormate)
            itemView.DateAll_txt.text=sdf.format(Date(date))

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewhoder {
        val todoViewHolder=TodoViewhoder(LayoutInflater.from(parent.context).inflate(R.layout.item_todo,parent,false))
        todoViewHolder.delete_button.setOnClickListener {
            listener.OnItemClicked(list[todoViewHolder.adapterPosition])
        }
        return todoViewHolder


    }

    override fun onBindViewHolder(holder: TodoViewhoder, position: Int) {
        holder.bind(list[position])

    }

    override fun getItemCount(): Int {
        return list.size
    }
}
interface ItodoAdapter
{
fun OnItemClicked(todo_model:TodoModel)
}

