package com.appsnipp.messagnerTesting

import android.util.Log
import android.widget.TextView
import android.widget.Toast
import com.appsnipp.messagnerTesting.model.User
import com.appsnipp.messagnetTesting.R
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.user_new_row.view.*

class UserAdapter(val user: User):Item<GroupieViewHolder>() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        val name=viewHolder.itemView.findViewById<TextView>(R.id.user_new_row_name)
        name.text=user.name

        Picasso.get().load(user.photoUrl).into(viewHolder.itemView.user_new_row_ImageProfile)


    }

    override fun getLayout(): Int {
        return R.layout.user_new_row
    }
}