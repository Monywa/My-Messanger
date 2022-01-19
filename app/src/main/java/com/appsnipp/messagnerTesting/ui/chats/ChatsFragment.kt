package com.appsnipp.messagnerTesting.ui.chats

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.appsnipp.messagnerTesting.ChartActivity
import com.appsnipp.messagnerTesting.H
import com.appsnipp.messagnerTesting.model.LatestChatRowAdapter
import com.appsnipp.messagnerTesting.model.MyMessage
import com.appsnipp.messagnerTesting.model.chatAdapter.ChatLeftAdapter
import com.appsnipp.messagnetTesting.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.fragment_message.*
import kotlinx.android.synthetic.main.fragment_message.view.*


class ChatsFragment : Fragment() {

    //    private lateinit var homeViewModel: ChatsViewModel
    val adapter1 = GroupAdapter<GroupieViewHolder>()
    val latestMessageMap = HashMap<String, MyMessage>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        homeViewModel =
//            ViewModelProvider(this).get(ChatsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_message, container, false)
//        val textView: TextView = root.findViewById(R.id.text_home)
//        homeViewModel.text.observe(viewLifecycleOwner, Observer {
//            textView.text = it
//        })
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recycler = view.findViewById<RecyclerView>(R.id.RecyclerView_LatestChatRow)
        recycler.adapter = adapter1

        adapter1.setOnItemClickListener { item, view ->
            val row=item as LatestChatRowAdapter
            val intent=Intent(view.context,ChartActivity::class.java)
            intent.putExtra("touser",row.friendUser)
            startActivity(intent)
        }
        LatestChatMessageRow()

        H.l("ChatFragment")


    }

    private fun refreshAdapter()
    {
        H.l("refreshAdapter")
        adapter1.clear()
        latestMessageMap.values.forEach {
            adapter1.add(LatestChatRowAdapter(it!!))
        }
    }

    private fun LatestChatMessageRow() {
        val fromId = FirebaseAuth.getInstance().uid

        val ref = FirebaseDatabase.getInstance().getReference("/Latest-Messages/$fromId")
        ref.addChildEventListener(object : ChildEventListener {


            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {


                val data = snapshot.getValue(MyMessage::class.java)
                if (data != null)
                    latestMessageMap[snapshot.key!!] = data!!
                refreshAdapter()

            }

            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                H.l("LatestCats")
                val data = snapshot.getValue(MyMessage::class.java)
                if (data != null)
                    latestMessageMap[snapshot.key!!] = data!!

                refreshAdapter()
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {

            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

    }


}