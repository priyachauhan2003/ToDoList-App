package com.example.todolist

import android.content.DialogInterface
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    lateinit var item : EditText
    lateinit var add : Button
    lateinit var listView: ListView

    var itemList=ArrayList<String>()

    var fileHelper = FileHelper()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        item = findViewById(R.id.editText)
        add = findViewById(R.id.button)
        listView = findViewById(R.id.list)

        itemList=fileHelper.readData(this)

        var arrayAdapter=ArrayAdapter(this,android.R.layout.simple_list_item_1,android.R.id.text1,itemList)

        listView.adapter=arrayAdapter

        add.setOnClickListener {
            //1. take the item from edit text and assign it to string container
            var itemName:String=item.text.toString()
            //2. add item to the Array list
            itemList.add(itemName)
            //3. clear the text of edit text
            item.setText("")
            //4. if application is close then it will save the data
            fileHelper.writeData(itemList,applicationContext)
            //5. arry adpter hold this changes
            arrayAdapter.notifyDataSetChanged()
        }
        
        listView.setOnItemClickListener { adapterView, view, position, id ->
            var alert = AlertDialog.Builder(this)
            alert.setTitle("Delete")
            alert.setMessage("Do you really want to delete this item.")
            alert.setCancelable(false)
            alert.setNegativeButton("No",DialogInterface.OnClickListener { dialog, which ->
                dialog.cancel()
            })
            alert.setPositiveButton("Yes",DialogInterface.OnClickListener { dialog, which ->
                itemList.removeAt(position)
                //list vie is renewed we will do this by notifying adapter
                arrayAdapter.notifyDataSetChanged()
                //we have to write this new array list to file
                fileHelper.writeData(itemList,applicationContext)
            })
            alert.create().show()
        }
    }
}