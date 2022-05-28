package com.project.delcanteen.helper

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
}