package com.nandini.android.dobcalc

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.graphics.Typeface
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.time.Duration
import java.util.*

class MainActivity : AppCompatActivity() {

   private var dateTv : TextView?=null
    private var minTv : TextView?=null
    private var year_tv:TextView?=null
    private var month_tv : TextView?=null
    private var day_tv : TextView?=null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnDatePicker : Button = findViewById(R.id.btnDatePicker)
        dateTv=findViewById(R.id.date_tv)
        minTv=findViewById(R.id.min_tv)
        year_tv=findViewById(R.id.year_tv)
        month_tv=findViewById(R.id.month_tv)
        day_tv = findViewById(R.id.day_tv)

        btnDatePicker.setOnClickListener {
         datePicker()
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("ResourceAsColor")
    private fun datePicker ()
    {
        val myCalender = Calendar.getInstance()
        val year = myCalender.get(Calendar.YEAR)
        val month = myCalender.get(Calendar.MONTH)
        val day = myCalender.get(Calendar.DAY_OF_MONTH)
        val dpd = DatePickerDialog(this,R.style.datepicker,
            { _, selectedYear, selectedMonth, selectedDay ->
                Toast.makeText(this,"Year was $selectedYear , ${selectedMonth+1}'s $selectedDay day.",Toast.LENGTH_SHORT).show()

                var sm = "${selectedMonth+1}"
                if(selectedMonth +1 < 10 )
                {
                    sm = "0${selectedMonth+1}"
                }

                var sd ="$selectedDay"
                if(selectedDay<10)
                {
                    sd="0$selectedDay"
                }
                val selectedDate="$sd.${sm}.$selectedYear "
                dateTv?.text = selectedDate
                val sdf= SimpleDateFormat("dd.MM.yyyy ",Locale.ENGLISH)
                val theDate=sdf.parse(selectedDate)

                theDate?.let {

                    val selectedDateInMin=theDate.time / 60000
                    val selectedDateInYear=(theDate.time/ 31536000000)
                    val selectedDateInMonth=(theDate.time)/2629746000
                    val selectedDateInDay=(theDate.time)/86400000
                    val currentDate=sdf.parse(sdf.format(System.currentTimeMillis()))
                    currentDate?.let {

                        val currentDateInMin=currentDate.time/60000
                        val currentDateInYear=(currentDate.time/ 31536000000)
                        val currentDateInMonth=(currentDate.time)/2629746000
                                val currentDateInDay=(currentDate.time)/86400000
                        val differenceInMin = currentDateInMin-selectedDateInMin
                        val differenceInYear=currentDateInYear-selectedDateInYear-1
                        // 24 leap years in 100 years
                        // 1->24/100
                        // 19->19*(24/100)
                        val leap_days=(differenceInYear*(0.24)).toInt()
                        val differenceInMonth=currentDateInMonth-selectedDateInMonth-(differenceInYear*12)-1
//                        val differenceInDay=currentDateInDay-selectedDateInDay-(differenceInYear*365)-(differenceInMonth*31)-leap_days
                        val differenceInDay=Duration.between(theDate.toInstant(),currentDate.toInstant()).toDays()-(differenceInYear*365+differenceInMonth*31+leap_days)

                        minTv?.text=differenceInMin.toString()
                        year_tv?.text=differenceInYear.toString()
                        month_tv?.text=differenceInMonth.toString()
                        day_tv?.text=differenceInDay.toString()
                    }

                } },year,month,day)
            dpd.datePicker.maxDate=System.currentTimeMillis()-86400000
            dpd.show()
        dpd.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(R.color.black)
        dpd.getButton(DatePickerDialog.BUTTON_POSITIVE).setTypeface(Typeface.DEFAULT_BOLD)

        dpd.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(R.color.black)
        dpd.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTypeface(Typeface.DEFAULT_BOLD)

    }
}