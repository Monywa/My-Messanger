package com.appsnipp.messagnerTesting.model.chatAdapter

import android.widget.EditText
import android.widget.TextView
import com.appsnipp.messagnerTesting.model.MyMessage
import com.appsnipp.messagnerTesting.model.User
import com.appsnipp.messagnetTesting.R
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.activity_current_user_message_row.view.*

class ChatLeftAdapter(val messagetext:String,val user: User):Item<GroupieViewHolder>() {
    override fun getLayout(): Int {
      return R.layout.activity_current_user_message_row
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
       val currentmessage=viewHolder.itemView.findViewById<TextView>(R.id.textView_current_user_message)

        currentmessage.text=messagetext

        Picasso.get().load(user.photoUrl).into(viewHolder.itemView.Image_current_user_message_)
    }
}