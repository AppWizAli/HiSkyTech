package com.hiskytech.portfolio.Fragments

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.common.internal.Constants
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.hiskytech.portfolio.Adapters.AdapterAdmin
import com.hiskytech.portfolio.Adapters.JobAdapter
import com.hiskytech.portfolio.Models.JobModal
import com.hiskytech.portfolio.Models.TeamModal
import com.hiskytech.portfolio.Models.Usermodel
import com.hiskytech.portfolio.R
import com.hiskytech.portfolio.ViewModels.UserViewModal
import com.hiskytech.portfolio.databinding.FragmentUser1Binding


class User1Fragment : Fragment() , AdapterAdmin.OnItemClickListener{
    private lateinit var binding:FragmentUser1Binding
    private val db = Firebase.firestore
    private lateinit var dialog:Dialog
    private lateinit var usermodel: Usermodel
    private lateinit var mContext: Context
    private var deleteDialog: AlertDialog? = null
    private lateinit var constants: Constants
    private val userViewModal : UserViewModal by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentUser1Binding.inflate(inflater,container,false)
        mContext = requireContext()
        constants = Constants()
        usermodel = Usermodel()
        binding.floatingaction.setOnClickListener {
            showChoiceDialog()
        }


        setAdapter()

        return binding.root
    }

    @SuppressLint("SuspiciousIndentation")
    private fun showChoiceDialog() {
      var   builder = AlertDialog.Builder(requireContext())

        builder.setTitle("Select Option")
            .setPositiveButton("Add User") { dialog, which ->
                showDialogAdduser()
            }
            .setNegativeButton("Cancel") { dialog, which ->
                dialog.dismiss()

            }


        val dialog = builder.create()
        dialog.show()
    }

    private fun showDialogAdduser() {
         dialog= Dialog(requireContext(),R.style.FullWidthDialog)

        dialog.setContentView(R.layout.dialog_adduser)
        val email = dialog.findViewById<EditText>(R.id.userEmail)
        val password = dialog.findViewById<EditText>(R.id.userPassword)
        val name = dialog.findViewById<EditText>(R.id.userName)
        val next = dialog.findViewById<Button>(R.id.btnNext)
        val back = dialog.findViewById<ImageView>(R.id.back)
        dialog.setCancelable(false)


            back.setOnClickListener{
                dialog.dismiss()
            }
        next.setOnClickListener(){

            if (email.text.toString().isEmpty() || password.text.toString().isEmpty()|| name.text.toString().isEmpty()) {
                Toast.makeText(requireContext(), "Please Enter All fields", Toast.LENGTH_SHORT).show()
            }
            else{
                usermodel.name=name.text.toString()
                usermodel.password=password.text.toString()
                usermodel.email=email.text.toString()

                adduser(usermodel)
            }

        }
        dialog.show()
    }


    private fun adduser(usermodel: Usermodel) {
        userViewModal.adduser(usermodel).observe(requireActivity()){
          success->
            if (success){

                Toast.makeText(
                    requireContext(),
                    "Created successfully",
                    Toast.LENGTH_SHORT
                ).show()
                dialog.dismiss()
                setAdapter()
            }
            else {

                Toast.makeText(

                    requireContext(),
                    "Something went wrong",
                    Toast.LENGTH_SHORT
                ).show()
                // dialog.dismiss() // Assuming 'dialog' is defined elsewhere
            }
        }
    }

    private fun setAdapter() {
        val userList : ArrayList<Usermodel> = arrayListOf()
        userViewModal.get_data().addOnCompleteListener() { user->
            if (user.isSuccessful)
            {
                if (user.result.size()>0)
                {
                    for (document in user.result)
                    {
                        userList .add(document.toObject(Usermodel::class.java))
                    }

                }
                userList.sortBy { it.name }
                binding.rvusers.layoutManager = LinearLayoutManager(requireContext())
                binding.rvusers.adapter = AdapterAdmin(requireContext(),userList,this@User1Fragment)

            }
        }
    }


    override fun onUpdateButton(usermodel: Usermodel) {


        val dialog = Dialog(mContext, R.style.FullWidthDialog)
        dialog.setContentView(R.layout.dialog_adduser)

        val email = dialog.findViewById<EditText>(R.id.userEmail)
        val password = dialog.findViewById<EditText>(R.id.userPassword)
        val name = dialog.findViewById<EditText>(R.id.userName)
        val next = dialog.findViewById<Button>(R.id.btnNext)
        val back = dialog.findViewById<ImageView>(R.id.back)
        dialog.setCancelable(false)


        back.setOnClickListener {
            dialog.dismiss()
        }

        email.setText(usermodel.email)
        password.setText(usermodel.password)
        name.setText(usermodel.name)

        next.setOnClickListener {
            if (email.text.toString().isEmpty() || password.text.toString()
                    .isEmpty() || name.text.toString().isEmpty()
            ) {
                Toast.makeText(requireContext(), "Please Enter All fields", Toast.LENGTH_SHORT)
                    .show()
            } else {
                usermodel.name = name.text.toString()
                usermodel.password = password.text.toString()
                usermodel.email = email.text.toString()

                userViewModal.edit_user(usermodel)
                    .observe(requireActivity()) { success ->
                        if (success) {
                            Toast.makeText(
                                mContext,
                                " updated successfully",
                                Toast.LENGTH_SHORT
                            ).show()
                            setAdapter()
                            dialog.dismiss()

                        } else {
                            Toast.makeText(
                                mContext,
                                "Failed to update",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            }


        }
        dialog.show()
    }

    override fun onRemoveButton(usermodel: Usermodel) {
        val builder = AlertDialog.Builder(mContext)
        builder.setTitle("Confirmation")
            .setMessage("Are you sure you want to delete?")
            .setPositiveButton("Yes") { _, _ ->
                performDeleteActionTeamMember(usermodel)
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
        deleteDialog = builder.create()
        deleteDialog?.show()
    }
        private fun performDeleteActionTeamMember(usermodel: Usermodel)
        {
            userViewModal.delte_user(usermodel)
                .observe(this@User1Fragment) { success ->
                    if (success) {
                        Toast.makeText(
                            mContext,
                            "Team Deleted Successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                        deleteDialog?.dismiss() // Dismiss the dialog here
                        setAdapter()
                    } else {
                        Toast.makeText(
                            mContext,
                            "some thing went wrong ",
                            Toast.LENGTH_SHORT
                        ).show()
                        deleteDialog?.dismiss() // Dismiss the dialog here
                    }
                }
        }

    override fun onViewButton(usermodel: Usermodel) {

    }
}


