package com.appsnipp.messagnerTesting.model

import android.widget.TextView
import com.appsnipp.messagnerTesting.H
import com.appsnipp.messagnetTesting.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.latest_chat_row.view.*

class LatestChatRowAdapter(val message: MyMessage) : Item<GroupieViewHolder>() {

    var friendUser:User?=null
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {



        val friId: String = if (FirebaseAuth.getInstance().uid == message.fromid) {
            message.toid
        } else message.fromid


        val ref = FirebaseDatabase.getInstance().getReference("/users/$friId")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                friendUser = snapshot.getValue(User::class.java)
                Picasso.get().load(friendUser!!.photoUrl)
                    .into(viewHolder.itemView.imageView_LatestChatRow)
                H.l("Photo ${friendUser!!.photoUrl}")
                val titleName=   viewHolder.itemView.findViewById<TextView>(R.id.textView_LatestChatRow_Title)
                val body=   viewHolder.itemView.findViewById<TextView>(R.id.textView_LatestChatRow_Message)

                titleName.text= friendUser!!.name
                H.l("title Name $titleName")
              body.text = message.message
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

    }

    override fun getLayout(): Int {
        return R.layout.latest_chat_row
    }
}