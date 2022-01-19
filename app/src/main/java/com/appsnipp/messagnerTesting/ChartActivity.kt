package com.appsnipp.messagnerTesting

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import com.appsnipp.messagnerTesting.model.MyMessage
import com.appsnipp.messagnerTesting.model.User
import com.appsnipp.messagnerTesting.model.chatAdapter.ChatLeftAdapter
import com.appsnipp.messagnerTesting.model.chatAdapter.ChatRightAdapter
import com.appsnipp.messagnetTesting.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.activity_chart.*
import org.jetbrains.anko.toast

class ChartActivity : AppCompatActivity() {
    var toUser: User? = null
    var adapter = GroupAdapter<GroupieViewHolder>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chart)

        toUser = intent.getParcelableExtra("touser")
        toast(toUser!!.name)


        val send = findViewById<Button>(R.id.button_user_send_text)
        send.setOnClickListener {
            SendMessgae()
        }
        CheckMesssage()
        RecyclerView_ChatActvity.adapter = adapter
        RecyclerView_ChatActvity.scrollToPosition(adapter.itemCount)

    }

    private fun CheckMesssage() {
        val fromId = H.currentUser!!.uid
        val toId = toUser!!.uid
        val ref = FirebaseDatabase.getInstance().getReference("/messages/$fromId/$toId")
        ref.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val datachild = snapshot.getValue(MyMessage::class.java)

                if (FirebaseAuth.getInstance().uid == datachild!!.fromid) {
                    adapter.add(ChatLeftAdapter(datachild.message, H.currentUser!!))

                }// current user chat log
                else {
                    adapter.add(ChatRightAdapter(datachild.message, toUser!!))

                }//other user chat log
                RecyclerView_ChatActvity.scrollToPosition(adapter.itemCount - 1)
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onChildRemoved(snapshot: DataSnapshot) {

            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    private fun SendMessgae() {
        val editextsend = findViewById<EditText>(R.id.editText_user_send_text)
        var message = editextsend.text.toString()
        val fromId = H.currentUser!!.uid
        val toId = toUser!!.uid

        val toRef = FirebaseDatabase.getInstance().getReference("/messages/$fromId/$toId").push()
        val fromRef = FirebaseDatabase.getInstance().getReference("messages/$toId/$fromId").push()


        val messageText =
            MyMessage(toRef.key!!, message, fromId, toId, System.currentTimeMillis() / 1000)

        toRef.setValue(messageText).addOnSuccessListener {
            H.l("Succes Message")
        }

        fromRef.setValue(messageText).addOnSuccessListener {

        }

        val fromLatestChatRef =
            FirebaseDatabase.getInstance().getReference("/Latest-Messages/$fromId/$toId")
        val toLatestChatRef =
            FirebaseDatabase.getInstance().getReference("/Latest-Messages/$toId/$fromId")

        fromLatestChatRef.setValue(messageText).addOnSuccessListener { toast("sucess latest") }
        toLatestChatRef.setValue(messageText).addOnSuccessListener { toast("sucess latest") }

        editextsend.text.clear()

    }
}