package com.project.delcanteen.helper

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import java.text.NumberFormat
import java.util.*

class Helper {
    fun changeRupiah(string: String): String {
        return NumberFormat.getCurrencyInstance(Locale("in", "ID")).format(Integer.valueOf(string))
    }
    fun changeRupiah(value: Int): String {
        return NumberFormat.getCurrencyInstance(Locale("in", "ID")).format(value)
    }
    fun changeRupiah(value: Boolean): String {
        return NumberFormat.getCurrencyInstance(Locale("in", "ID")).format(value)
    }

    fun setToolbar(activity: Activity, toolbar: Toolbar, title:String){
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        activity.supportActionBar!!.title = title
        activity.supportActionBar!!.setDisplayShowHomeEnabled(true)
        activity.supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }
}