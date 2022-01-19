package com.appsnipp.messagnerTesting

import android.content.Intent
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.appsnipp.messagnerTesting.model.User
import com.appsnipp.messagnetTesting.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {

    lateinit var navController:NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
       val navView: BottomNavigationView = findViewById(R.id.nav_view)
//
        navController = Navigation.findNavController(this,R.id.nav_host_fragment)

////        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
//         navController = navHostFragment.navController
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
//        val appBarConfiguration = AppBarConfiguration(
//            setOf(
//                R.id.navigation_chats, R.id.navigation_people
//            )
//        )
//        this.setupActionBarWithNavController(navController, appBarConfiguration)
//        navView.setupWithNavController(navController)// we do not use as support action bar

        navView.let{
            NavigationUI.setupWithNavController(it,navController)
        }
        checkUserAuth()
        fretchUsers()
        H.l("MainActivity")
    }

    private fun fretchUsers() {
        var uid=FirebaseAuth.getInstance().uid
        var ref=FirebaseDatabase.getInstance().getReference("/users/$uid")
        ref.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                H.currentUser=snapshot.getValue(User::class.java)

            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    private fun checkUserAuth() {
        var uid=FirebaseAuth.getInstance().uid
        if(uid==null){
            var intent=Intent(this@MainActivity,LoginActivity::class.java)
            intent.flags=Intent.FLAG_ACTIVITY_CLEAR_TASK or (Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }
    }
}