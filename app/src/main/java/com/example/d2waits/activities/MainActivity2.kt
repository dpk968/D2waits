package com.example.d2waits.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.d2waits.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_first.*


class MainActivity2 : AppCompatActivity() {

//    private var firebaseStorage:FirebaseStorage?=null
//    private var storageReference:StorageReference?=null
//    private lateinit var database : DatabaseReference
//    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

//        mAuth = FirebaseAuth.getInstance()
//        firebaseStorage = FirebaseStorage.getInstance()
//        storageReference = FirebaseStorage.getInstance().reference




        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        val navController = findNavController(R.id.fragment)
//        val appBarConfiguration = AppBarConfiguration(setOf(R.id.firstFragment,R.id.secondFragment,R.id.thirdFragment))
//        setupActionBarWithNavController(navController,appBarConfiguration)

        bottomNavigationView.setupWithNavController(navController)

//        firebaseAuth = FirebaseAuth.getInstance()
//        firebaseStorage = FirebaseStorage.getInstance()
//        storageReference!!.child(mAuth!!.uid!!).child("image/Profile Pic")
//            .downloadUrl.addOnSuccessListener { uri ->
//                Picasso.get().load(uri).into(uImage)
//            }


    }
}