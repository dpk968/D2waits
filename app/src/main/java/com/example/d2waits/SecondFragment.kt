package com.example.d2waits

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.d2waits.databinding.FragmentFirstBinding
import com.example.d2waits.databinding.FragmentSecondBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_second.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SecondFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SecondFragment : Fragment() {


    private var storageReference: StorageReference?=null
    private lateinit var database : DatabaseReference
    private lateinit var mAuth: FirebaseAuth
    private var _binding: FragmentSecondBinding?= null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        mAuth = FirebaseAuth.getInstance()
//        firebaseStorage = FirebaseStorage.getInstance()
        storageReference = FirebaseStorage.getInstance().reference


        storageReference!!.child(mAuth.uid!!).child("image/Profile Pic")
            .downloadUrl.addOnSuccessListener { uri ->
                Picasso.get().load(uri).into(uImage)
            }

        database = FirebaseDatabase.getInstance().getReference("User")
        if (mAuth.uid!=null){
            database.child(mAuth.uid!!).get().addOnSuccessListener {
                if (it.exists()){
                    binding.uName.text = it.child("firstName").value.toString()


                }


            }
        }

        _binding = FragmentSecondBinding.inflate(inflater,container,false)
//        binding.uName.text = mAuth.uid
        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SecondFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic fun newInstance(param1: String, param2: String) =
                SecondFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}