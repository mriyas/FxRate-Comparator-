package com.riyas.fxrate.model

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.riyas.fxrate.BR

/**
 * An observer class to observe the state of app, to identify like when no connectivity of api is calling..etc
 * @param msg : an info message
 * @param state : a code to identify the state
 */
data class AppState(var msg:String?): BaseObservable() {
    var state: Int=0
        @Bindable
        get() = field
        set(value) {
            if(field!=value){
                field = value

            }
        }

}