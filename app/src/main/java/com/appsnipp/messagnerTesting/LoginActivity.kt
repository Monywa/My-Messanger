package com.appsnipp.messagnerTesting

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.appsnipp.messagnetTesting.R
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.layout_login.*
import org.jetbrains.anko.find
import org.jetbrains.anko.toast


class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val loginEmail = find<TextInputLayout>(R.id.textInputEmail)
        val loginPassword = find<TextInputLayout>(R.id.textInputPassword)
        if(FirebaseAuth.getInstance().currentUser!=null){
            val intent = Intent(this@LoginActivity, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            this.finish()
        }else

        LoginButton.setOnClickListener {

            val email = loginEmail.editText?.text.toString()
            val password = loginPassword.editText?.text.toString()

            loginEmail.error = null
            loginPassword.error = null

            if (email.isEmpty() || password.isEmpty()) {
                if (email.isEmpty()) loginEmail.error = "Please Fill Email"
              if(password.isEmpty())loginPassword.error = "Please Fill Password"
            } else if (password.length < 6) {
                loginEmail.error = null
                loginPassword.error = "Please Enter at least 6 characters"
            } else {
                loginEmail.error = null
                loginPassword.error = null
                StartLogin(email, password)
            }


        }




        TextView_ToRegister.setOnClickListener {
            var intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun StartLogin(email: String, password: String) {
        val auth = FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
        auth.addOnCompleteListener {
            if (it.isSuccessful) {
                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
            }

        }
            .addOnFailureListener {
                toast(it.message.toString())
            }
    }

}