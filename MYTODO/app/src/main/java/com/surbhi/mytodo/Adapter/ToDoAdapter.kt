package com.surbhi.mytodo.Adapter
import android.R
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import com.surbhi.mytodo.MainActivity
import com.surbhi.mytodo.Utils.DatabaseHandler
import com.surbhi.mytodo.AddNewTask


class ToDoAdapter(private val db: DatabaseHandler, private val activity: MainActivity) :
    RecyclerView.Adapter<ToDoAdapter.ViewHolder>() {
    private var todoList: List<ToDoModel>? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.task_layout, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        db.openDatabase()
        val item = todoList!![position]
        holder.task.text = item.task
        holder.task.isChecked = toBoolean(item.status)
        holder.task.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                db.updateStatus(item.id, 1)
            } else {
                db.updateStatus(item.id, 0)
            }
        }
    }

    private fun toBoolean(n: Int): Boolean {
        return n != 0
    }

    override fun getItemCount(): Int {
        return todoList!!.size
    }

    val context: Context
        get() = activity

    fun setTasks(todoList: List<ToDoModel>?) {
        this.todoList = todoList
        notifyDataSetChanged()
    }

    fun deleteItem(position: Int) {
        val item = todoList!![position]
        db.deleteTask(item.id)
        todoList.removeAt(position)
        notifyItemRemoved(position)
    }

    fun editItem(position: Int) {
        val item = todoList!![position]
        val bundle = Bundle()
        bundle.putInt("id", item.id)
        bundle.putString("task", item.task)
        val fragment = AddNewTask()
        fragment.setArguments(bundle)
        fragment.show(activity.supportFragmentManager, AddNewTask.TAG)
    }

    class ViewHolder internal constructor(view: View) : RecyclerView.ViewHolder(view) {
        var task: CheckBox

        init {
            task = view.findViewById(R.id.todoCheckBox)
        }
    }
}