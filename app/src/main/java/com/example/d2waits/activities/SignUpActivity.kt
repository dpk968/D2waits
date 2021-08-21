package com.example.d2waits.activities

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.d2waits.R
import com.example.d2waits.dataClasses.UserProfile
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity() {


    companion object{
        private const val RC_SIGN_IN = 120
    }
    private lateinit var mAuth: FirebaseAuth
    private lateinit var googleSignInClient:GoogleSignInClient
    private lateinit var database:DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        mAuth = FirebaseAuth.getInstance()
        googlebtnsignin.setOnClickListener{
            signIn()
        }
        s.setOnClickListener{
            val progressDialog = ProgressDialog(this)
            progressDialog.setMessage("uploading data......")
            progressDialog.setCancelable(false)
            progressDialog.show()
            when {
                signupEmal.text.toString().isNullOrEmpty() -> {
                    if (progressDialog.isShowing) progressDialog.dismiss()
                    Toast.makeText(this, "Email not provided", Toast.LENGTH_SHORT).show()
                }
                signuppass.text.toString().isNullOrEmpty() -> {
                    if (progressDialog.isShowing) progressDialog.dismiss()
                    Toast.makeText(this, "password not provided", Toast.LENGTH_SHORT).show()
                }
                UserName.text.toString().isNullOrEmpty()-> {
                    if (progressDialog.isShowing) progressDialog.dismiss()
                    Toast.makeText(this, " userName not provided", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    mAuth.createUserWithEmailAndPassword(signupEmal.text.toString(), signuppass.text.toString())
                        .addOnCompleteListener(this) { task ->
                            progressDialog.show()
                            if (task.isSuccessful) {
                                checkEmail()
                                if (progressDialog.isShowing) progressDialog.dismiss()
                                Toast.makeText(baseContext, "Sign Up succesfully please check your email", Toast.LENGTH_LONG).show()

                                startActivity(Intent(this, LoginActivity::class.java))
                            } else {
                                if (progressDialog.isShowing) progressDialog.dismiss()
                                Toast.makeText(baseContext, "Sign Up failed. Try after sometime", Toast.LENGTH_LONG).show()

                            }
                        }
                }
            }



        }

        login_txt.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

    }

    private fun addData() {
        database =FirebaseDatabase.getInstance().getReference("User")
        val mail = mAuth.currentUser?.email
        val user = UserProfile(UserName.text.toString(),mail,"E")

        mAuth.currentUser?.let {
            database.child(it.uid).setValue(user).addOnCompleteListener {
                Toast.makeText(this,"succesfull",Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun addDataForGoogle() {
        database =FirebaseDatabase.getInstance().getReference("User")
        val mail = mAuth.currentUser?.email
        val name = mAuth.currentUser?.displayName
        val user = UserProfile(name,mail,"G")

        mAuth.currentUser?.let {
            database.child(it.uid).setValue(user).addOnCompleteListener {
                Toast.makeText(this,"succesfull",Toast.LENGTH_LONG).show()
            }
        }
    }

    fun checkEmail(){
        val firebaseUser:FirebaseUser? = mAuth.currentUser
        firebaseUser?.sendEmailVerification()?.addOnCompleteListener { task ->
            if (task.isSuccessful){
                Toast.makeText(this,"Verification mail sent",Toast.LENGTH_SHORT).show()
                addData()

            }else{
                Toast.makeText(this,"some error occured",Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
//            val exception = task.exception
            if(task.isSuccessful){
                try {
                    // Google Sign In was successful, authenticate with Firebase
                    val account = task.getResult(ApiException::class.java)!!
//                    Log.d("MainActivity", "firebaseAuthWithGoogle:" + account.id)
                    firebaseAuthWithGoogle(account.idToken!!)

                } catch (e: ApiException) {
                    // Google Sign In failed, update UI appropriately
                    Log.w("MainActivity", "Google sign in failed", e)

                }
            }else{
//                Log.w("MainActivity",exception.toString())
                Toast.makeText(this,"error ouccured with google account",Toast.LENGTH_LONG).show()


            }

        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information

                        Toast.makeText(this,"Login Sussecfully",Toast.LENGTH_LONG).show()
                    val intent = Intent(this, userProfileActivity::class.java)
                    addDataForGoogle()
                    //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    finish()

                    //val user = mAuth.currentUser
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("MainActivity", "signInWithCredential:failure", task.exception)
                    startActivity(Intent(this,SignUpActivity::class.java))

                }
            }
    }


}