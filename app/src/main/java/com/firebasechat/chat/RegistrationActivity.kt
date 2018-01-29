package com.firebasechat.chat

import android.app.ProgressDialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.StringRequestListener
import com.firebase.client.Firebase
import com.firebasechat.R
import com.firebasechat.list_user.UserLists
import com.immigration.appdata.Constant.url_Registation
import com.immigration.appdata.Constant.url_Users
import kotlinx.android.synthetic.main.activity_register.*
import org.json.JSONException
import org.json.JSONObject

class RegistrationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        Firebase.setAndroidContext(this)



        registerButton.setOnClickListener {
            val user_ = username.text.toString()
            val pass_ = password.text.toString()

            initJsonParse(user_, pass_)
        }
    }

    private fun initJsonParse(user: String, pass: String) {
        val pd = ProgressDialog(this@RegistrationActivity)
        pd.setMessage("Loading...")
        pd.show()





        AndroidNetworking.get(url_Registation).build().getAsString(object : StringRequestListener {
            override fun onResponse(response: String?) {

                val reference = Firebase(url_Users)
                if (response == "null") {
                    reference.child(user).child("name").setValue(user)
                    reference.child(user).child("password").setValue(pass)
                    reference.child(user).child("image").setValue("http://1.bp.blogspot.com/-EUVJei-f0qY/ToFgC_gcEHI/AAAAAAAAAJY/wTQZlnsjbNg/s1600/kaju.jpg")

                    startActivity(Intent(this@RegistrationActivity, UserLists::class.java))
                    Toast.makeText(this@RegistrationActivity, "registration successful 1", Toast.LENGTH_LONG).show()
                } else {
                    try {
                        val obj = JSONObject(response)
                        if (!obj.has(user)) {
                            reference.child(user).child("name").setValue(user)
                            reference.child(user).child("password").setValue(pass)
                            reference.child(user).child("image").setValue("http://1.bp.blogspot.com/-EUVJei-f0qY/ToFgC_gcEHI/AAAAAAAAAJY/wTQZlnsjbNg/s1600/kaju.jpg")
                            startActivity(Intent(this@RegistrationActivity, UserLists::class.java))
                            Toast.makeText(this@RegistrationActivity, "registration successful", Toast.LENGTH_LONG).show()
                        } else {
                            Toast.makeText(this@RegistrationActivity, "username already exists", Toast.LENGTH_LONG).show()
                        }

                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                    pd.dismiss()
                }
            }

            override fun onError(anError: ANError?) {
                pd.dismiss()
            }
        })
    }
}
