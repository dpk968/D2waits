package com.example.d2waits.activities

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.d2waits.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_user_profile.*

class userProfileActivity : AppCompatActivity() {

    private lateinit var database : DatabaseReference
    private lateinit var mAuth: FirebaseAuth
    private var PICK_IMAGE_REQUEST = 123
    private var imagePath :Uri?=null
    private var firebaseStorage:FirebaseStorage?=null
    private var storageReference:StorageReference?=null




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        mAuth = FirebaseAuth.getInstance()
        firebaseStorage = FirebaseStorage.getInstance()
        storageReference = FirebaseStorage.getInstance().reference
        val userPic = mAuth.currentUser?.photoUrl
        val uid = mAuth.currentUser?.uid
        var courseBsa:String?
//        var semesterBsa:String?
//        var branchBsa:String?


        database = FirebaseDatabase.getInstance().getReference("User")
        if (uid != null) {
            database.child(uid).get().addOnSuccessListener {
                if (it.exists()){
                    var firstName = it.child("firstName").value.toString()
                    val email = it.child("email").value.toString()
                    val type = it.child("type").value.toString()


                    if(firstName.contains(" ")){
                        firstName = firstName.replace(" "," \n")
                    }
                    if (type=="G"){
                        Glide.with(this).load(userPic).into(profile_image)
                        Toast.makeText(this,"google image",Toast.LENGTH_LONG).show()
                    }else{
                        profile_image.setImageResource(R.drawable.ic_user_defualt)
                    }
                    nameOfUser.text = firstName
                    emailOfUser.text = email
                    Toast.makeText(this,"succses",Toast.LENGTH_LONG).show()
                }
            }.addOnFailureListener {
                Toast.makeText(this,"sorry",Toast.LENGTH_LONG).show()
            }
        }


        profile_image.setOnClickListener {
            filechoser()

        }



//        button.setOnClickListener {
//            val progressDialog = ProgressDialog(this)
//            progressDialog.setMessage("uploading file......")
//            progressDialog.setCancelable(false)
//            progressDialog.show()
//
//            val fm = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault())
//            val now = Date()
//            val fileName = fm.format(now)
//            val sr = FirebaseStorage.getInstance().getReference("images/$fileName")
//
//            sr.putFile(ImageUri).addOnSuccessListener {
//                profile_image.setImageURI(null)
//                Toast.makeText(this,"succesfull",Toast.LENGTH_LONG).show()
//                if (progressDialog.isShowing) progressDialog.dismiss()
//            }
//
//        }

        button.setOnClickListener {



            val imgRef = storageReference!!.child(mAuth.uid!!).child("image").child("Profile Pic")

            val uploadImage:UploadTask = imgRef.putFile(imagePath!!)

            val progressDialog = ProgressDialog(this)
            progressDialog.setMessage("uploading data......")
            progressDialog.setCancelable(false)
            progressDialog.show()

            uploadImage.addOnFailureListener{
                if (progressDialog.isShowing) progressDialog.dismiss()
                Toast.makeText(this,"error Ocurred",Toast.LENGTH_LONG).show()
            }.addOnSuccessListener {
                if (progressDialog.isShowing) progressDialog.dismiss()
                startActivity(Intent(this, MainActivity2::class.java))
                finish()
            }



        }

        var course = listOf("B.tech","M.tech","B.pharma","D.pharma")
        val adapter = ArrayAdapter(this, R.layout.dropdown_item,course)
        autoComplete1.setAdapter(adapter)


        autoComplete1.onItemClickListener = AdapterView.OnItemClickListener{
                parent, view, position, id ->
            val selectedItem = parent.getItemAtPosition(position).toString()
            courseBsa = selectedItem

            mAuth.currentUser?.let {
                database.child(it.uid).child("Course").setValue(courseBsa).addOnCompleteListener {
                    Toast.makeText(this,courseBsa,Toast.LENGTH_LONG).show()
                }
            }
            Toast.makeText(applicationContext, selectedItem,Toast.LENGTH_LONG).show()
            if (courseBsa == "B.tech"||courseBsa == "M.tech"){
                show_second.visibility = View.VISIBLE
                val Branch = listOf("CSE","Mechanical","Civil","Electrical")
                val adapter2 = ArrayAdapter(this, R.layout.dropdown_item,Branch)
                autoComplete2.setAdapter(adapter2)
                autoComplete2.onItemClickListener = AdapterView.OnItemClickListener{
                        parent, view, position, id ->
                    val selectedItem = parent.getItemAtPosition(position).toString()
                    mAuth.currentUser?.let {
                        database.child(it.uid).child("Branch").setValue(selectedItem).addOnCompleteListener {
                            Toast.makeText(this,selectedItem,Toast.LENGTH_LONG).show()
                        }
                    }

                    
                }
            }
            else{
                show_second.visibility = View.GONE
            }

        }

        val Semester = listOf("1","2","3","4","5","6","7","8")
        val adapter3 = ArrayAdapter(this, R.layout.dropdown_item,Semester)
        autoComplete3.setAdapter(adapter3)
        autoComplete3.onItemClickListener = AdapterView.OnItemClickListener{
                parent, view, position, id ->
            val selectedItem = parent.getItemAtPosition(position).toString()

            mAuth.currentUser?.let {
                database.child(it.uid).child("Semester").setValue(selectedItem).addOnCompleteListener {
                    Toast.makeText(this,selectedItem,Toast.LENGTH_LONG).show()
                }
            }




        }







    }



//    private fun selectImage() {
//        val intent = Intent()
//        intent.type = "image/*"
//        intent.action = Intent.ACTION_GET_CONTENT
//        startActivityForResult(intent,PICK_IMAGE_REQUEST)
//    }

    private fun filechoser() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent,PICK_IMAGE_REQUEST)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data !=null&&data.data!=null){
            imagePath =data.data
            Picasso.get().load(imagePath).into(profile_image)
        }
    }


}

