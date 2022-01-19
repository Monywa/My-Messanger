package com.appsnipp.messagnerTesting

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.appsnipp.messagnerTesting.RegisterActivity.PhoneValid.isPhoneNumber
import com.appsnipp.messagnerTesting.model.User
import com.appsnipp.messagnetTesting.R
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.layout_register.*
import org.jetbrains.anko.find
import org.jetbrains.anko.imageBitmap
import org.jetbrains.anko.toast
import java.util.*
import java.util.regex.Pattern


public class RegisterActivity : AppCompatActivity() {
    var uri: Uri? = null
    private var mAuth: FirebaseAuth? = null
    private var mStorageRef: StorageReference? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        Textview_already_account.setOnClickListener {
            startActivity(Intent(this@RegisterActivity,LoginActivity::class.java))
        }

        val emailId = findViewById<TextInputLayout>(R.id.EditText_Email)
        val passwordId = find<TextInputLayout>(R.id.EditText_Password)
        val nameId = find<TextInputLayout>(R.id.EditText_RegisterName)
        val phoneId = find<TextInputLayout>(R.id.EditText_Phone)

        mAuth = FirebaseAuth.getInstance()


        requestPermit()//photo storage permission

        profile_image.setOnClickListener {
            PickImage()// picking image profile
        }

        Button_Register.setOnClickListener {

            val email = emailId.editText?.text.toString().trim()
            val password = passwordId.editText?.text.toString().trim()
            val username = nameId.editText!!.text.toString()
            val phone = phoneId.editText!!.text.toString()




            if (CheckErroTextField(username, phone, email, password)) {
                H.l("Empty")
                ErrorMessage(username, phone, email, password)
            } else {

                clearTextFieldError()

                H.l(email)
                H.l(password)
                SignUpNewUsers(email, password)
            }
        }

    }

    private fun clearTextFieldError() {
        val uname = find<TextInputLayout>(R.id.EditText_RegisterName)
        val uphone = find<TextInputLayout>(R.id.EditText_Phone)
        val uemail = find<TextInputLayout>(R.id.EditText_Email)
        val upassword = find<TextInputLayout>(R.id.EditText_Password)

        uname.error = null
        uphone.error = null
        uemail.error = null
        upassword.error = null
    }

    object PhoneValid {
        val REG = "^(\\+91[\\-\\s]?)?[0]?(91)?[789]\\d{9}\$"
        var PATTERN: Pattern = Pattern.compile(REG)
        fun CharSequence.isPhoneNumber(): Boolean = PATTERN.matcher(this).find()
    }

    private fun CheckErroTextField(
        name: String,
        phone: String,
        email: String,
        Password: String
    ): Boolean {

        return name.isEmpty() || phone.isEmpty() || email.isEmpty() || Password.isEmpty() || uri == null || !phone.isPhoneNumber()


    }

    private fun ErrorMessage(name: String, phone: String, email: String, Password: String) {
        val uname = find<TextInputLayout>(R.id.EditText_RegisterName)
        val uphone = find<TextInputLayout>(R.id.EditText_Phone)
        val uemail = find<TextInputLayout>(R.id.EditText_Email)
        val upassword = find<TextInputLayout>(R.id.EditText_Password)
        H.l("Something empty")
        if (name.isEmpty()) {
            uname.error = "Please Fill Name"
            H.l("name is empty")

        }




        if (phone.isEmpty()) {
            uphone.error = "Please Fill Mobile Number"

        }


        if (!phone.isPhoneNumber()) {
            uphone.error = "Please fill correct phone number"
        }

        if (email.isEmpty()) {
            uemail.error = "Please Fill Email Address"
        }


        if (Password.isEmpty()) {
            upassword.error = "Please Fill Password"
        }

        if (uri == null)
            toast("Please Choose Profile Image")
    }

    private fun SignUpNewUsers(email: String, password: String) {

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {

                if (it.isSuccessful) {
//                    toast("Success Registration")


                    UserProfileUploadNow()
                    return@addOnCompleteListener

                } else
                    toast("Authentication Fail")

            }
            .addOnFailureListener {

                H.l(it.message.toString())
                toast(it.message.toString())
            }

    }

    private fun UserProfileUploadNow() {


        if (uri == null) return

        val filename = UUID.randomUUID().toString()

        mStorageRef = FirebaseStorage.getInstance().getReference("/ images/$filename")
        mStorageRef!!.putFile(uri!!)
            .addOnSuccessListener {
                mStorageRef!!.downloadUrl.addOnSuccessListener {

                    StoreUserToDatabase(it.toString())

                }
            }
            .addOnFailureListener {
                H.l(it.message.toString())
            }

    }

    private fun StoreUserToDatabase(photoUril: String) {

        val emailId = findViewById<TextInputLayout>(R.id.EditText_Email)
        val passwordId = find<TextInputLayout>(R.id.EditText_Password)
        val nameId = find<TextInputLayout>(R.id.EditText_RegisterName)
        val phoneId = find<TextInputLayout>(R.id.EditText_Phone)

        val uid = FirebaseAuth.getInstance().uid
        val username = nameId.editText!!.text.toString()
        val phone = phoneId.editText!!.text.toString()
        val email = emailId.editText!!.text.toString()


        val user = User(uid!!, username, phone, email, photoUril)

        val dataBase = FirebaseDatabase.getInstance().getReference("/users/$uid")
        dataBase.setValue(user)
            .addOnSuccessListener {
                H.l("Success Firebase Database")

                val intent=Intent(this@RegisterActivity,MainActivity::class.java)
                intent.flags=Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
            }
            .addOnFailureListener {
                H.l(it.message.toString())

            }


    }

    private fun requestPermit() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
            101
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {

            101 ->
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {

                    toast("Permission Denied")

                } else
                    toast("Permission Accept")


        }
    }

    private fun PickImage() {

        var intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(Intent.createChooser(intent, "Image Profile"), 100)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            100 ->
                if (resultCode == Activity.RESULT_OK && data != null) {
                    uri = data.data

                    var inputStram = contentResolver.openInputStream(uri!!)
                    var bitmap = BitmapFactory.decodeStream(inputStram)
                    profile_image.imageBitmap = bitmap

                }
        }
    }
}