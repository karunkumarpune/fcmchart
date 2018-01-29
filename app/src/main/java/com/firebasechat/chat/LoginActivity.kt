package com.firebasechat.chat

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.StringRequestListener
import com.firebase.client.Firebase
import com.firebasechat.*
import com.firebasechat.list_user.UserLists
import com.immigration.appdata.Constant
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONException
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        Firebase.setAndroidContext(this)


        register.setOnClickListener {
            startActivity(Intent(this@LoginActivity, RegistrationActivity::class.java))
        }


        loginButton.setOnClickListener {
            val user_ = username.text.toString()
            val pass_ = password.text.toString()

            initJsonParse(user_, pass_)
        }
    }

    private fun initJsonParse(user: String, pass: String) {
        val pd = ProgressDialog(this@LoginActivity)
        pd.setMessage("Loading...")
        pd.show()


        AndroidNetworking.get(Constant.url_Login).build().getAsString(object : StringRequestListener {
            override fun onResponse(response: String?) {
                if (response == "null") {
                    Toast.makeText(this@LoginActivity, "user not found", Toast.LENGTH_LONG).show()
                } else {
                    try {
                        val obj = JSONObject(response)
                        if (!obj.has(user)) {
                            Toast.makeText(this@LoginActivity, "user not found", Toast.LENGTH_LONG).show()
                        } else if (obj.getJSONObject(user).getString("password") == pass) {
                            UserDetails.username = user
                            UserDetails.password = pass
                            startActivity(Intent(this@LoginActivity, UserLists::class.java))
                        } else {
                            Toast.makeText(this@LoginActivity, "incorrect password", Toast.LENGTH_LONG).show()
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
                pd.dismiss()

            }

            override fun onError(anError: ANError?) {
                pd.dismiss()
            }
        })
    }
}