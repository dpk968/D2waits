package com.example.d2waits.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.d2waits.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_forget.*

class ForgetActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forget)

        mAuth = FirebaseAuth.getInstance()

        button_reset_password.setOnClickListener{
            Toast.makeText(this,reset_email_field.text.toString(),Toast.LENGTH_LONG).show()
            reset()

        }
    }

    private fun reset() {
        var emailR:String = reset_email_field.text.toString()
        if (emailR.isNullOrEmpty()){
            Toast.makeText(this,"Please enter Email",Toast.LENGTH_LONG).show()
        }
        mAuth.sendPasswordResetEmail(emailR).addOnCompleteListener { task ->
            if (task.isSuccessful){
                Toast.makeText(this,"reset Link is Send on your mail",Toast.LENGTH_LONG).show()
                startActivity(Intent(this, LoginActivity::class.java))
            }else{
                Toast.makeText(this,"reset Link is not send Please try after sometime",Toast.LENGTH_LONG).show()
            }

        }
    }
}