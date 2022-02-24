package com.malviscape.todo

import android.R
import android.app.Activity
import android.content.Context
import android.graphics.Typeface
import android.os.Bundle
import android.os.FileUtils
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.AdapterView.OnItemLongClickListener
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.malviscape.todo.R.string.*
import org.apache.commons.io.FileUtils.writeLines
import java.io.File
import java.io.IOException


class MainActivity : AppCompatActivity() {
    private var items: ArrayList<String>? = null
    private var itemsAdapter: ArrayAdapter<String>? = null
    private var ListItems: ListView? = null
    private var mCLayout: CoordinatorLayout? = null
    private var mTypeface: Typeface? = null
    private var mContext: Context? = null
    private var mActivity: Activity? = null
    fun Toolbar() {
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        val mTitle = toolbar.findViewById<View>(R.id.toolbar_title) as TextView
    }

    private fun readToDo() {
        val filesDir = filesDir
        val todoFile = File(filesDir, "todo_contents.cartel")
        try {
            items = ArrayList<String>(FileUtils.readLines(todoFile))
        } catch (e: IOException) {
            items = ArrayList()
        }
    }

    private fun saveToDo() {
        val filesDir = filesDir
        val todoFile = File(filesDir, "todo_contents.cartel")
        try {
            writeLines(todoFile, items)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mContext = applicationContext
        mActivity = this@MainActivity
        mCLayout = findViewById<View>(R.id.coordinator_layout) as CoordinatorLayout
        mTypeface = Typeface.createFromAsset(assets, "fonts/and_black.ttf")
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        ListItems = findViewById(R.id.ListItems)
        items = ArrayList()
        readToDo()
        itemsAdapter = object : ArrayAdapter<String?>(this,
            R.layout.simple_list_item_1, items!! as List<String?>
        ) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                // Cast the list view each item as text view
                val item = super.getView(position, convertView, parent) as TextView

                // Set the typeface/font for the current item
                item.setTypeface(mTypeface)

                // Set the font size.
                item.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16f)

                // return the view
                return item
            }
        }
        ListItems.setAdapter(itemsAdapter)
        setupListViewListener()
        Toolbar()
    }

    override fun onContentChanged() {
        super.onContentChanged()
        val emptyView = findViewById<ImageView>(R.id.animationView)
        val ListItems = findViewById<View>(R.id.ListItems) as ListView
        ListItems.emptyView = emptyView
    }

    fun fab(v: View?) {
        val editToDo = findViewById<EditText>(R.id.editToDo)
        val itemText = editToDo.text.toString()
        if (editToDo.text.toString().trim { it <= ' ' }.length <= 0) {
            Toast.makeText(this@MainActivity, toast_empty, Toast.LENGTH_SHORT).show()
        } else {
            itemsAdapter!!.add("- $itemText")
            editToDo.setText("")
        }
        saveToDo()
    }

    private fun setupListViewListener() {
        val touchListener = SwipeDismissListViewTouchListener(
            ListItems,
            object : DismissCallbacks() {
                fun canDismiss(position: Int): Boolean {
                    return true
                }

                fun onDismiss(listView: ListView?, reverseSortedPositions: IntArray) {
                    for (position in reverseSortedPositions) {
                        items!!.removeAt(position)
                        itemsAdapter!!.notifyDataSetChanged()
                        saveToDo()
                    }
                }
            })
        ListItems!!.setOnTouchListener(touchListener)

        // Add long click to remove too.
        ListItems!!.onItemLongClickListener =
            OnItemLongClickListener { adapter: AdapterView<*>?, item: View?, pos: Int, id: Long ->
                items!!.removeAt(pos)
                itemsAdapter!!.notifyDataSetChanged()
                saveToDo()
                true
            }
    }
}


class MainActivity : AppCompatActivity() {
    private var name: EditText? = null
    private var age: EditText? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        name = findViewById(R.id.edit1)
        age = findViewById(R.id.edit2)
    }

    // Fetch the stored data in onResume()
    // Because this is what will be called
    // when the app opens again
    override fun onResume() {
        super.onResume()

        // Fetching the stored data
        // from the SharedPreference
        val sh = getSharedPreferences("MySharedPref", MODE_PRIVATE)
        val s1 = sh.getString("name", "")
        val a = sh.getInt("age", 0)

        // Setting the fetched data
        // in the EditTexts
        name!!.setText(s1)
        age!!.setText(a.toString())
    }

    // Store the data in the SharedPreference
    // in the onPause() method
    // When the user closes the application
    // onPause() will be called
    // and data will be stored
    override fun onPause() {
        super.onPause()

        // Creating a shared pref object
        // with a file name "MySharedPref"
        // in private mode
        val sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE)
        val myEdit = sharedPreferences.edit()

        // write all the data entered by the user in SharedPreference and apply
        myEdit.putString("name", name!!.text.toString())
        myEdit.putInt("age", age!!.text.toString().toInt())
        myEdit.apply()
    }
}
