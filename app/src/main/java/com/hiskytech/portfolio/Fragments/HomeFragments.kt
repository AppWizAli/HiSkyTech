package com.hiskytech.portfolio.Fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.Application
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.hiskytech.portfolio.Adapters.CoursesAdapter
import com.hiskytech.portfolio.Data.Constants
import com.hiskytech.portfolio.Data.SharedPrefManager
import com.hiskytech.portfolio.Data.Utils
import com.hiskytech.portfolio.Models.AnnoucementModal
import com.hiskytech.portfolio.Models.CourseModal
import com.hiskytech.portfolio.R
import com.hiskytech.portfolio.ViewModels.CourseViewModal
import com.hiskytech.portfolio.databinding.FragmentHomeFragmentsBinding
import java.util.UUID

class HomeFragments : Fragment(), CoursesAdapter.OnItemClickListener {
    private lateinit var binding: FragmentHomeFragmentsBinding
    private lateinit var dialog: Dialog
    private val db = Firebase.firestore
   private lateinit var  courseViewModel: CourseViewModal
    private val IMAGE_PICKER_REQUEST_CODE = 123
    private lateinit var contants: Constants
    private lateinit var mContext: Context
    private var imageURI: Uri? = null
    private lateinit var courseModal :CourseModal
    private var private = 110
    private var deleteDialog: AlertDialog? = null
    private lateinit var thumnailview: ImageView
    private lateinit var utils: Utils
    private lateinit var constants: Constants
    private lateinit var sharedPrefManager: SharedPrefManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentHomeFragmentsBinding.inflate(inflater, container, false)
        contants = Constants()
        mContext = requireContext()
        courseViewModel = ViewModelProvider(this@HomeFragments).get(CourseViewModal::class.java)
        courseModal = CourseModal()


        binding.detailFloating.setOnClickListener()
        {
            ShowDetaildialogue()
        }
        return binding.root
    }

    @SuppressLint("CutPasteId")
    private fun ShowDetaildialogue() {
        dialog = Dialog(requireContext(), R.style.FullWidthDialog)
        dialog.setContentView(R.layout.dialog_detail_list)
        dialog.setCancelable(false)

        var add_annoucement = dialog.findViewById<Button>(R.id.add_annoucement)
        var add_Course = dialog.findViewById<Button>(R.id.add_course)
        var add_completed_projects = dialog.findViewById<Button>(R.id.add_completed_projects)
        var add_team_member = dialog.findViewById<Button>(R.id.add_team_member)
        var add_job = dialog.findViewById<Button>(R.id.add_job)

        add_annoucement.setOnClickListener()
        {
            dialog = Dialog(requireContext(), R.style.FullWidthDialog)
            dialog.setContentView(R.layout.dialogue_add_annoucement)
            dialog.setCancelable(false)

            var annoucement_title = dialog.findViewById<EditText>(R.id.annoucement_title)
            var annoucement_des = dialog.findViewById<EditText>(R.id.annoucement_description)
            var annoucement_image = dialog.findViewById<TextView>(R.id.select_image)
            var annoucement_display_image = dialog.findViewById<TextView>(R.id.select_image_display)

            var next = dialog.findViewById<Button>(R.id.add)
            var cancel = dialog.findViewById<Button>(R.id.cancel)
            cancel.setOnClickListener()
            {
                dialog.dismiss()
            }
            next.setOnClickListener()
            {
                var annoucement_modal = AnnoucementModal()
            }

            dialog.show()
        }


        ////////////////////////////////////////////
        add_Course.setOnClickListener()
        {
            dialog = Dialog(requireContext(), R.style.FullWidthDialog)
            dialog.setContentView(R.layout.dialogue_add_course)
            dialog.setCancelable(false)

            var course_title = dialog.findViewById<EditText>(R.id.course_title)
            var course_des = dialog.findViewById<EditText>(R.id.course_description)
            var course_image = dialog.findViewById<TextView>(R.id.select_image_course)
            var course_language = dialog.findViewById<EditText>(R.id.course_language)
            var add = dialog.findViewById<Button>(R.id.add)
            var cancl = dialog.findViewById<Button>(R.id.cancel)

            course_image.setOnClickListener()
            {
                val pickImage =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(pickImage, IMAGE_PICKER_REQUEST_CODE)
            }
            cancl.setOnClickListener()
            {
                dialog.dismiss()
            }
            add.setOnClickListener()
            {
                courseModal.title = course_title.text.toString()
                courseModal.description = course_des.text.toString()
                courseModal.language = course_language.text.toString()

                if (course_title.text.toString().isEmpty() || course_des.text.toString().isEmpty()
                ) {
                    Toast.makeText(mContext, "Please Enter All fields", Toast.LENGTH_SHORT).show()
                } else {

                    handleUploadButtonClick()
                }
            }
        dialog.show()
        }
        add_completed_projects.setOnClickListener()
        {
            dialog = Dialog(requireContext(), R.style.FullWidthDialog)
            dialog.setContentView(R.layout.dialogue_add_completed_projects)
            dialog.setCancelable(false)
            dialog.show()
        }
        add_team_member.setOnClickListener()
        {
            dialog = Dialog(requireContext(), R.style.FullWidthDialog)
            dialog.setContentView(R.layout.dialogue_add_team_member)
            dialog.setCancelable(false)
            dialog.show()
        }
        add_job.setOnClickListener()
        {
            dialog = Dialog(requireContext(), R.style.FullWidthDialog)
            dialog.setContentView(R.layout.dialogue_add_new_job)
            dialog.setCancelable(false)
            dialog.show()

        }

        dialog.show()
    }

    private fun setAdapter() {
        val list = ArrayList<CourseModal>()
       courseViewModel.getCourseList().addOnSuccessListener { taskResult->

            if (taskResult != null) {
                // Check if the task result is not null, indicating a successful result
                if (taskResult.size() > 0) {
                    for (document in taskResult) {
                        list.add(document.toObject(CourseModal::class.java))
                    }
                }
                val sortedList = list.sortedBy { it.title?.toIntOrNull() ?: 0 }
                if (list.isEmpty()) {
                    binding.nothing.visibility = View.VISIBLE
                    binding.recyclerView.layoutManager = LinearLayoutManager(mContext)
                    binding.recyclerView.adapter = CoursesAdapter(mContext, sortedList, this@HomeFragments)
                } else {
                    binding.nothing.visibility = View.GONE
                    binding.recyclerView.layoutManager = LinearLayoutManager(mContext)
                    binding.recyclerView.adapter = CoursesAdapter(mContext, sortedList, this@HomeFragments)

                }

            } else {
                Toast.makeText(mContext, constants.SOMETHING_WENT_WRONG_MESSAGE, Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_PICKER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            imageURI = data?.data


        }
    }
    private fun handleUploadButtonClick() {
        courseViewModel.add_course(courseModal).observe(requireActivity()) { success ->
            if (success) {
                Toast.makeText(
                    mContext,
                    "Created successfully",
                    Toast.LENGTH_SHORT
                ).show()
                // dialog.dismiss() // Assuming 'dialog' is defined elsewhere

                // Call the method to set the adapter
//                setAdapter()
            } else {
                Toast.makeText(
                    mContext,
                    "Something went wrong",
                    Toast.LENGTH_SHORT
                ).show()
                // dialog.dismiss() // Assuming 'dialog' is defined elsewhere
            }
        }
//        if (imageURI != null) {
//            // Upload the thumbnail image to Firebase Storage
//            uploadImage(imageURI!!)
//        } else {
//            Toast.makeText(
//                mContext,
//                "Please select a thumbnail image.",
//                Toast.LENGTH_SHORT
//            ).show()
//        }
    }

//    private fun uploadThumbnailImage(imageUri: Uri, callback: (String?) -> Unit) {
//        val storageRef = Firebase.storage.reference.child("images/${UUID.randomUUID()}.jpg")
//        val uploadTask = storageRef.putFile(imageUri)
//
//        uploadTask.addOnSuccessListener { taskSnapshot ->
//            // Upload successful, get download URL
//            taskSnapshot.storage.downloadUrl.addOnSuccessListener { downloadUrl ->
//                callback(downloadUrl.toString())
//            }.addOnFailureListener { exception ->
//                // Handle download URL failure (even if upload succeeded)
//                Toast.makeText(mContext, "Failed to get download URL: $exception", Toast.LENGTH_SHORT).show()
//                callback(null)
//            }
//        }
//    }
//    private fun uploadThumbnailImage(imageUri: Uri, callback: (String?) -> Unit) {
//        val storageRef = Firebase.storage.reference.child("thumbnails/${System.currentTimeMillis()}_${imageUri.lastPathSegment}")
//        storageRef.putFile(imageUri)
//            .addOnSuccessListener { taskSnapshot ->
//                taskSnapshot.storage.downloadUrl.addOnSuccessListener() { downloadUrlTask ->
//                        Toast.makeText(requireContext(), ""+downloadUrlTask, Toast.LENGTH_SHORT).show()
//                        val downloadUrl = downloadUrlTask.toString()
//                        callback(downloadUrl)
//                }
//            }
//            .addOnFailureListener {
//                callback(null)
//            }
//    }


    private fun uploadImage(imageUri: Uri) {
        // Use a unique path for each image using UUID.randomUUID()
        val path = "images/${UUID.randomUUID()}.jpg"

        // Use the correct function parameter name imageUri instead of imageURI
        uploadImageToFirebase(path, imageUri) { imageRef ->
            // Image uploaded successfully, now get the download URL
            getImageDownloadUrl(imageRef)
        }
    }

    private fun uploadImageToFirebase(path: String, imageUri: Uri, onSuccess: (StorageReference) -> Unit) {
        val storageRef = FirebaseStorage.getInstance().reference
        val imageRef = storageRef.child(path)

        // Proceed with the upload
        imageRef.putFile(imageUri)
            .addOnSuccessListener { taskSnapshot ->
                onSuccess(storageRef.child(path))
            }
            .addOnFailureListener { exception ->
                Toast.makeText(requireContext(), "Upload failed: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }

// Remove the unnecessary path and imageUri definitions here
// ...

// Call uploadImage with the actual image Uri when needed
// val imageUri = // your image Uri here
// uploadImage(imageUri)

    private fun getImageDownloadUrl(imageRef: StorageReference) {
        imageRef.downloadUrl
            .addOnSuccessListener { uri ->
                // Now 'uri' contains the download URL, you can store it in Firestore
                storeImageUrlInFirestore(uri.toString())
            }
            .addOnFailureListener { exception ->
                // Handle failure to get download URL
                Toast.makeText(requireContext(), "Failed to get download URL: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun storeImageUrlInFirestore(downloadUrl: String) {

        courseModal.thumbnail = downloadUrl.toString()
        // Assuming you have a function named add_course in your courseViewModel
        courseViewModel.add_course(courseModal).observe(requireActivity()) { success ->
            if (success) {
                Toast.makeText(
                    mContext,
                    "Created successfully",
                    Toast.LENGTH_SHORT
                ).show()
                // dialog.dismiss() // Assuming 'dialog' is defined elsewhere

                // Call the method to set the adapter
                setAdapter()
            } else {
                Toast.makeText(
                    mContext,
                    "Something went wrong",
                    Toast.LENGTH_SHORT
                ).show()
                // dialog.dismiss() // Assuming 'dialog' is defined elsewhere
            }
        }
    }



    override fun onItemClick(coursemodal: CourseModal) {

    }

    override fun onDeleteClick(coursemodal: CourseModal) {

    }

    override fun onEditClick(coursemodal: CourseModal) {

    }
}