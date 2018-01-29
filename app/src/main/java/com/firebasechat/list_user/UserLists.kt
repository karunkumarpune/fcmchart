package com.firebasechat.list_user

import android.app.ProgressDialog
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.LinearLayout
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.StringRequestListener
import com.firebasechat.R
import com.firebasechat.chat.UserDetails
import com.immigration.appdata.Constant
import kotlinx.android.synthetic.main.activity_users_list.*
import org.json.JSONException
import org.json.JSONObject


class UserLists : AppCompatActivity() {

    var totalUsers = 0
    lateinit var pd: ProgressDialog
    lateinit var list: ArrayList<Model>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_users_list)

        pd = ProgressDialog(this@UserLists)
        pd.setMessage("Loading...")
        pd.show()

        list = ArrayList()
        recy.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        initJsonParse()
    }

    private fun initJsonParse() {
        AndroidNetworking.get(Constant.url_Registation).build().getAsString(object : StringRequestListener {
            override fun onResponse(response: String?) {
                if (response != null) {
                    doOnSuccess(response)
                }
            }

            override fun onError(anError: ANError?) {
                pd.dismiss()
            }
        })
    }


    fun doOnSuccess(s: String) {
        try {
            val obj = JSONObject(s)
            val i = obj.keys()
            var key = ""
            while (i.hasNext()) {
                key = i.next().toString()
                if (key != UserDetails.username) {
                    list.add(Model(key,""))
                }
                totalUsers++
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        if (totalUsers <= 1) {
            noUsersText.visibility = View.VISIBLE
            recy.visibility = View.GONE
        } else {
            noUsersText.visibility = View.GONE
            recy.visibility = View.VISIBLE


            val adp = userAdapter(list)
            recy.adapter = adp

        }
        pd.dismiss()
    }

}
