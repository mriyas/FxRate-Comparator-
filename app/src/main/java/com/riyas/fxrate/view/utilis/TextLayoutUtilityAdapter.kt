package com.riyas.fxrate.view.utilis

import android.widget.Button
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputLayout
import java.text.SimpleDateFormat
import java.util.*
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.adapters.TextViewBindingAdapter.setText
import androidx.appcompat.widget.AppCompatEditText



@BindingAdapter("errorText")
fun setError(view: TextInputLayout, error: String?){
    view.error = error
}

@BindingAdapter("formattedDate")
fun setFormattedDate(view: TextView, dateTime: String?) {
    var time = dateTime?.toLong()
    time = time?.div(1000)
    val formatter = SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a")
    val date = formatter.format(time?.let { Date(it) })
    view.text = date
}
@BindingAdapter("formattedDate")
fun setFormattedDate(view: TextView, time: Long?) {
   val timeUpdated= time?.times(1000)
    val formatter = SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a")
    val date = formatter.format(timeUpdated?.let { Date(it) })
    view.text = date
}

@BindingAdapter("setButtonState")
fun setButtonState(view: Button, state: Boolean) {
   if(!state!!){
       view.alpha=1.0f
       view.isEnabled=true
   }else{
       view.alpha=0.3f
       view.isEnabled=false

   }
}


