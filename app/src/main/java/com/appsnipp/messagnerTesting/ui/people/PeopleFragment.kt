package com.appsnipp.messagnerTesting.ui.people

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.appsnipp.messagnerTesting.H
import com.appsnipp.messagnerTesting.ChartActivity
import com.appsnipp.messagnerTesting.UserAdapter
import com.appsnipp.messagnerTesting.model.User

import com.appsnipp.messagnetTesting.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder

class PeopleFragment : Fragment() {

//    private lateinit var peopleViewModel: PeopleViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        peopleViewModel =
//            ViewModelProvider(this).get(PeopleViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_people, container, false)
//        val textView: TextView = root.findViewById(R.id.text_notifications)
////        notificationsViewModel.text.observe(viewLifecycleOwner, Observer {
////            textView.text = it
////        })
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fetchUserAll(view)

    }

    private fun fetchUserAll(view: View) {
        val ref = FirebaseDatabase.getInstance().getReference("/users")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var adapter = GroupAdapter<GroupieViewHolder>()
                snapshot.children.forEach {
                    val user = it.getValue(User::class.java)
                    H.l("sart")
                    H.l(user!!.name.toString())
                    H.l(user!!.uid.toString())

                    if (user != null && user.name != H.currentUser?.name) {
                        adapter.add(UserAdapter(user!!))
                    }


                }

                adapter.setOnItemClickListener { item, view ->

                    val touser=item as UserAdapter
                    val intent=Intent(view.context,ChartActivity::class.java)
                    intent.putExtra("touser",touser.user)
                    startActivity(intent)
                }
                val recycler = view.findViewById<RecyclerView>(R.id.fragment_people_recycler_row)
                recycler.adapter = adapter


            }

            override fun onCancelled(error: DatabaseError) {
                H.l(error.message)
            }

        })
    }
}