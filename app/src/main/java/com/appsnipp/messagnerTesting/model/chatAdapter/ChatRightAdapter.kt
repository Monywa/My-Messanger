package com.appsnipp.messagnerTesting.model.chatAdapter

import android.widget.TextView
import com.appsnipp.messagnerTesting.model.User
import com.appsnipp.messagnetTesting.R
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.activity_other_user_message_row.view.*

class ChatRightAdapter(val messageText:String,val user: User):Item<GroupieViewHolder>() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        val otherUser=viewHolder.itemView.findViewById<TextView>(R.id.textView_other_user_message)
        otherUser.text=messageText
        Picasso.get().load(user.photoUrl).into(viewHolder.itemView.Image_other_user_message_)
    }

    override fun getLayout(): Int {
        return R.layout.activity_other_user_message_row
    }
}