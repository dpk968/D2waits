package com.example.d2waits.activities


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar

import android.widget.Toast
import com.example.d2waits.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        


        mAuth = FirebaseAuth.getInstance()
        val pb = findViewById<ProgressBar>(R.id.progressBar)
        
        signInBtn.setOnClickListener{

            pb.visibility = View.VISIBLE

            when {
                signInEmail.text.toString().isNullOrEmpty() -> {
                    pb.visibility = View.GONE
                    Toast.makeText(this, "Email not provided", Toast.LENGTH_SHORT).show()
                }
                signInpass.text.toString().isNullOrEmpty() -> {
                    pb.visibility = View.GONE
                    Toast.makeText(this, "password not provided", Toast.LENGTH_LONG).show()
                }
                else -> {
                    mAuth.signInWithEmailAndPassword(signInEmail.text.toString(),signInpass.text.toString())
                        .addOnCompleteListener(this){ task->
                            if (task.isSuccessful){
                                pb.visibility = View.GONE
                                if(verifyEmail()){
                                    val intent =Intent(this, userProfileActivity::class.java)
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                                    startActivity(intent)
                                    finish()
                                }
                                else{
                                    startActivity(Intent(this, LoginActivity::class.java))
                                }

                            }else{
                                pb.visibility = View.GONE
                                Toast.makeText(baseContext,"Register First",Toast.LENGTH_SHORT).show()
                            }


                        }
                }
            }
        }


        signUPActivityLink.setOnClickListener {
            Toast.makeText(baseContext, "Jump to Signup Activity", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, SignUpActivity::class.java))
        }

        signup_txt.setOnClickListener {
            Toast.makeText(baseContext, "Jump to Signup Activity", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, SignUpActivity::class.java))
        }

        forgetlink.setOnClickListener{
            Toast.makeText(this,"Jump to forget activity",Toast.LENGTH_LONG).show()
            startActivity(Intent(this, ForgetActivity::class.java))

        }

    }



    fun verifyEmail(): Boolean {
        val firebaseUser:FirebaseUser?= FirebaseAuth.getInstance().currentUser
        val vemail:Boolean? = firebaseUser?.isEmailVerified
        if (vemail!!){
            Toast.makeText(this,"Login Successful",Toast.LENGTH_LONG).show()
            return true
        }else{
            Toast.makeText(this,"Please verify email first",Toast.LENGTH_LONG).show()
            return false
        }
    }




}
