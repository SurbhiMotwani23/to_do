package com.surbhi.mytodo
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.surbhi.mytodo.Adapter.ToDoAdapter
import com.surbhi.mytodo.Utils.DatabaseHandler
import java.util.*


class MainActivity : AppCompatActivity(), DialogCloseListener {
    private var db: DatabaseHandler? = null
    private var tasksRecyclerView: RecyclerView? = null
    private var tasksAdapter: ToDoAdapter? = null
    private var fab: FloatingActionButton? = null
    private var taskList: List<ToDoModel?>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Objects.requireNonNull(supportActionBar).hide()
        db = DatabaseHandler(this)
        db!!.openDatabase()
        tasksRecyclerView = findViewById(R.id.tasksRecyclerView)
        tasksRecyclerView.setLayoutManager(LinearLayoutManager(this))
        tasksAdapter = ToDoAdapter(db, this@MainActivity)
        tasksRecyclerView.setAdapter(tasksAdapter)
        val itemTouchHelper = ItemTouchHelper(RecyclerItemTouchHelper(tasksAdapter))
        itemTouchHelper.attachToRecyclerView(tasksRecyclerView)
        fab = findViewById(R.id.fab)
        taskList = db.getAllTasks()
        Collections.reverse(taskList)
        tasksAdapter.setTasks(taskList)
        fab.setOnClickListener(View.OnClickListener {
            AddNewTask.newInstance().show(supportFragmentManager, AddNewTask.TAG)
        })
    }

    fun handleDialogClose(dialog: DialogInterface?) {
        taskList = db.getAllTasks()
        Collections.reverse(taskList)
        tasksAdapter.setTasks(taskList)
        tasksAdapter.notifyDataSetChanged()
    }
}