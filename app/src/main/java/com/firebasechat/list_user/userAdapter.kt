package com.firebasechat.list_user

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.firebasechat.R
import com.firebasechat.chat.UserDetails
import com.firebasechat.chat.Chat
import kotlinx.android.synthetic.main.adapter_row.view.*


class userAdapter(val list:ArrayList<Model>): RecyclerView.Adapter<userAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): userAdapter.ViewHolder {
        val v=LayoutInflater.from(parent!!.context).inflate(R.layout.adapter_row,parent,false)
        return userAdapter.ViewHolder(v)
    }

    override fun onBindViewHolder(holder: userAdapter.ViewHolder, position: Int) {
         holder.bindItems(list[position])
    }

    override fun getItemCount(): Int {
       return list.size
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

           val context:Context=itemView.context
          fun bindItems(model: Model){
              val asubstring = model.name.substring(0, 1)
              itemView.txt_title.text=asubstring.toUpperCase()
              itemView.txt_message.text=asubstring.toUpperCase()+model.name.substring(1).toLowerCase();

              itemView.card_layout.setOnClickListener {
                  UserDetails.chatWith = model.name
                  context.startActivity(Intent(context, Chat::class.java))
              }

          }
    }

}

