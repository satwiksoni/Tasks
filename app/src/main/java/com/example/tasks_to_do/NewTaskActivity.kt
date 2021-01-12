package com.example.tasks_to_do

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.CalendarContract
import android.view.View
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.room.Room
import kotlinx.android.synthetic.main.activity_new_task.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import kotlinx.coroutines.withContext
import java.util.*

const val DB_NAME:String="DB"
class NewTaskActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var myCalender: Calendar
    lateinit var dateSetListener:DatePickerDialog.OnDateSetListener
    lateinit var timeSetListener:TimePickerDialog.OnTimeSetListener
    private val lables=arrayListOf("Coding","Projects","Personal","Business","Insurance","Shopping","Banking")
    var finalDate=0L
    var finalTime=0L


    val db by lazy {
        AppDB.getDatabase(this)
     }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_task)

        date1.setOnClickListener(this)
        time1.setOnClickListener(this)
        save.setOnClickListener(this)
        setupSpinner()

    }

    private fun setupSpinner() {
        val adapter=ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,lables)
        lables.sort()
        spinner.adapter=adapter
    }
    override fun onClick(v: View?) {
        when(v?.id){
            R.id.date1->{
              setListener()
            }
            R.id.time1->{
                setTimeListener()
            }
            R.id.save->{
                saveListener()
            }
        }
    }

    private fun saveListener() {
        val category=spinner.selectedItem.toString()
        val title=et1.editableText.toString()
        val description= et2.editableText.toString()
        GlobalScope.launch(Dispatchers.Main) {
            val id= withContext(Dispatchers.IO)
            {
                return@withContext db.todoDao().Insert(TodoModel(
                        title,
                        description,
                        category,
                        finalDate,
                        finalTime
                 )
                )

            }
            finish()
        }

    }


    private fun setTimeListener() {

        myCalender= Calendar.getInstance()
        timeSetListener=TimePickerDialog.OnTimeSetListener { _: TimePicker, hourOfDay: Int, min: Int ->
            myCalender.set(Calendar.HOUR_OF_DAY,hourOfDay)
            myCalender.set(Calendar.MINUTE,min)
            updateTime()
        }
        val timePickerDialog= TimePickerDialog(
                this,timeSetListener,myCalender.get(Calendar.HOUR_OF_DAY)
                ,myCalender.get(Calendar.MINUTE),false)
        timePickerDialog.show()
    }

    private fun updateTime() {
        val myFormat="h:mm:a"
        val sdf= SimpleDateFormat(myFormat)
        finalTime=myCalender.time.time
        time1.setText(sdf.format(myCalender.time))
    }






    private fun setListener() {
   myCalender= Calendar.getInstance()

        dateSetListener=DatePickerDialog.OnDateSetListener { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            myCalender.set(Calendar.YEAR,year)
            myCalender.set(Calendar.MONTH,month)
            myCalender.set(Calendar.DAY_OF_MONTH,dayOfMonth)
            updateDate()
        }
        val datePickerDialog= DatePickerDialog(
                this,dateSetListener,myCalender.get(Calendar.YEAR)
        ,myCalender.get(Calendar.MONTH),myCalender.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.datePicker.minDate=System.currentTimeMillis()
        datePickerDialog.show()
    }

    private fun updateDate() {
    val myFormat="EEE,d MMM yyyy"
        val sdf= SimpleDateFormat(myFormat)
        date1.setText(sdf.format(myCalender.time))
        finalDate=myCalender.time.time
        time_layout.visibility=View.VISIBLE
    }
}