package com.immigration.appdata

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager


object Constant {


    const val BASE_URL = "https://chat-693c8.firebaseio.com/"

    const val url_Registation = BASE_URL + "users.json"

    const val url_Users = BASE_URL + "users"

    const val url_Login = BASE_URL + "users.json"

    const val url_Messages = BASE_URL + "messages/"


    /*  val url_Registation = BaseURL+"users.json"
      val url_Login = BaseURL+"users.json"


      */


    // https://chat-693c8.firebaseio.com/users.json

    //Custome hide keyboard


      @JvmStatic
      fun hideSoftKeyboad(context: Context, v: View) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(v.windowToken, 0)
    }

}
