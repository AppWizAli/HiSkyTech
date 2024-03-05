package com.hiskytech.portfolio.Fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
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
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import com.hiskytech.portfolio.Adapters.CoursesAdapter
import com.hiskytech.portfolio.Data.Constants
import com.hiskytech.portfolio.Data.SharedPrefManager
import com.hiskytech.portfolio.Data.Utils
import com.hiskytech.portfolio.Models.AnnoucementModal
import com.hiskytech.portfolio.Models.CourseModal
import com.hiskytech.portfolio.R
import com.hiskytech.portfolio.ViewModels.CourseViewModal
import com.hiskytech.portfolio.databinding.FragmentHomeFragmentsBinding
import kotlinx.coroutines.launch
import java.util.UUID
import kotlin.properties.Delegates

class HomeFragments : Fragment(), CoursesAdapter.OnItemClickListener {
    private lateinit var binding: FragmentHomeFragmentsBinding
    private lateinit var dialogDetail: Dialog
    private lateinit var dialog: Dialog
    private val db = Firebase.firestore
   private lateinit var  courseViewModel: CourseViewModal
    private val IMAGE_PICKER_REQUEST_CODE = 123
    private lateinit var contants: Constants
    private lateinit var mContext: Context
    private var imageURI: Uri? = null
    private  var imageUriSecond: Uri? = null
    private lateinit var annoucementModal: AnnoucementModal
    private  var getText:String = "view all"
    private var imagecode:Int = 100
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
        annoucementModal = AnnoucementModal()


        binding.detailFloating.setOnClickListener()
        {
            ShowDetaildialogue()
        }
        setAdapter()
        return binding.root
    }

    @SuppressLint("CutPasteId")
    private fun ShowDetaildialogue() {
        dialogDetail = Dialog(requireContext(), R.style.FullWidthDialog)
        dialogDetail.setContentView(R.layout.dialog_detail_list)
        dialogDetail.setCancelable(false)

        var add_annoucement = dialogDetail.findViewById<Button>(R.id.add_annoucement)
        var add_Course = dialogDetail.findViewById<Button>(R.id.add_course)
        var add_completed_projects = dialogDetail.findViewById<Button>(R.id.add_completed_projects)
        var add_team_member = dialogDetail.findViewById<Button>(R.id.add_team_member)
        var add_job = dialogDetail.findViewById<Button>(R.id.add_job)
        var back = dialogDetail.findViewById<ImageView>(R.id.back)
        back.setOnClickListener()
        {
            dialogDetail.dismiss()
        }

        add_annoucement.setOnClickListener()
        {
            dialogDetail.dismiss()
            dialog = Dialog(requireContext(), R.style.FullWidthDialog)
            dialog.setContentView(R.layout.dialogue_add_annoucement)
            dialog.setCancelable(false)

            var annoucement_title = dialog.findViewById<EditText>(R.id.annoucement_title)
            var annoucement_des = dialog.findViewById<EditText>(R.id.annoucement_description)
            var annoucement_image = dialog.findViewById<TextView>(R.id.select_image)
            var annoucement_display_image = dialog.findViewById<TextView>(R.id.select_image_display)

            var next = dialog.findViewById<Button>(R.id.add)
            var cancel = dialog.findViewById<Button>(R.id.cancel)
            cancel.setOnClickListener() { dialog.dismiss() }
            annoucement_image.setOnClickListener()
            {
                val pickImage =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(pickImage, IMAGE_PICKER_REQUEST_CODE)
            }
            annoucement_display_image.setOnClickListener()
            {
                imagecode = 120
                val pickImage =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(pickImage, IMAGE_PICKER_REQUEST_CODE)
            }

            next.setOnClickListener()
            {
               annoucementModal.title = annoucement_title.text.toString()
                annoucementModal.description = annoucement_des.text.toString()
                if (annoucement_title.text.toString().isEmpty() || annoucement_des.text.toString().isEmpty()
                ) {
                    Toast.makeText(mContext, "Please Enter All fields", Toast.LENGTH_SHORT).show()
                } else {

                    annoucementAdd()
                }
            }

            dialog.show()
        }


        ////////////////////////////////////////////
        add_Course.setOnClickListener()
        {
            dialogDetail.dismiss()
            dialog = Dialog(requireContext(), R.style.FullWidthDialog)
            dialog.setContentView(R.layout.dialogue_add_course)
            dialog.setCancelable(false)

            var course_title = dialog.findViewById<EditText>(R.id.course_title)
            var course_des = dialog.findViewById<EditText>(R.id.course_description)
            var course_image = dialog.findViewById<TextView>(R.id.select_image_course)
            var course_language = dialog.findViewById<EditText>(R.id.course_language)
            var add = dialog.findViewById<Button>(R.id.add)
            var cancl = dialog.findViewById<Button>(R.id.cancel)

            course_image.setOnClickListener() {
                val pickImage =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(pickImage, IMAGE_PICKER_REQUEST_CODE)
            }
            cancl.setOnClickListener() { dialog.dismiss() }
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
            dialogDetail.dismiss()
            dialog = Dialog(requireContext(), R.style.FullWidthDialog)
            dialog.setContentView(R.layout.dialogue_add_completed_projects)
            dialog.setCancelable(false)
            dialog.show()
        }
        add_team_member.setOnClickListener()
        {
            dialogDetail.dismiss()
            dialog = Dialog(requireContext(), R.style.FullWidthDialog)
            dialog.setContentView(R.layout.dialogue_add_team_member)
            dialog.setCancelable(false)
            dialog.show()
        }
        add_job.setOnClickListener()
        {
            dialogDetail.dismiss()
            dialog = Dialog(requireContext(), R.style.FullWidthDialog)
            dialog.setContentView(R.layout.dialogue_add_new_job)
            dialog.setCancelable(false)
            dialog.show()

        }

        dialogDetail.show()
    }

    private fun setAdapter() {
        val list = ArrayList<CourseModal>()
       courseViewModel.getCourseList().addOnSuccessListener { taskResult->

            if (taskResult != null) {
                if (taskResult.size() > 0) {
                    for (document in taskResult) {
                        list.add(document.toObject(CourseModal::class.java))
                        list.sortBy { it.title }
                    }
                }

                    binding.recyclerView.layoutManager = LinearLayoutManager(mContext)
                    binding.recyclerView.adapter = CoursesAdapter(mContext, list.take(2), this@HomeFragments)
                binding.viewAll.setOnClickListener()
                {
                   var showText = binding.viewAll.text
                    if (showText == getText)
                    {
                        binding.recyclerView.adapter = CoursesAdapter(mContext, list, this@HomeFragments)
                        binding.viewAll.setText("Merge All")
                    }
                    else{
                        binding.recyclerView.adapter = CoursesAdapter(mContext, list.take(2), this@HomeFragments)
                        binding.viewAll.setText(getText)
                    }

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
            if (imagecode == 120)
            {
                imageUriSecond =  data?.data
            }
            else
            {
                imageURI = data?.data
            }



        }
    }
    private fun annoucementAdd()
    {
        if (imageURI != null) {
            if (imageUriSecond != null) {
                uploadThumbnailImage(imageURI!!) { thumbnailUrl ->
                    if (thumbnailUrl != null) {
                        annoucementModal.thumnail = thumbnailUrl

                        uploadThumbnailImage(imageUriSecond!!){
                            if (thumbnailUrl != null)
                            {
                                annoucementModal.thumnailSecond = thumbnailUrl
                                courseViewModel.addAnnoucement(annoucementModal)
                                    .observe(requireActivity()) { success ->
                                        if (success) {
                                            Toast.makeText(
                                                mContext,
                                                "Created successfully",
                                                Toast.LENGTH_SHORT
                                            ).show()

                                            setAdapter()
                                            dialog.dismiss()
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
                        }
                    } else {
                        Toast.makeText(
                            mContext,
                            "Failed to upload the thumbnail image.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
            else{
                Toast.makeText(requireContext(), "image uri display is not selected", Toast.LENGTH_SHORT).show()
            }
        }
        else{
            Toast.makeText(requireContext(), "image uri is not selected", Toast.LENGTH_SHORT).show()
        }

    }
    private fun handleUploadButtonClick() {
        if (imageURI != null) {
            uploadThumbnailImage(imageURI!!) { thumbnailUrl ->
                if (thumbnailUrl != null) {
                    courseModal.thumbnail = thumbnailUrl
                    courseViewModel.add_course(courseModal).observe(requireActivity()) { success ->
                        if (success) {
                            Toast.makeText(
                                mContext,
                                "Created successfully",
                                Toast.LENGTH_SHORT
                            ).show()

                            setAdapter()
                            dialog.dismiss()
                        } else {
                            Toast.makeText(
                                mContext,
                                "Something went wrong",
                                Toast.LENGTH_SHORT
                            ).show()
                            // dialog.dismiss() // Assuming 'dialog' is defined elsewhere
                        }
                    }
                } else {
                    Toast.makeText(
                        mContext,
                        "Failed to upload the thumbnail image.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
        else{
            Toast.makeText(requireContext(), "image uri is not selected", Toast.LENGTH_SHORT).show()
        }
    }
    ////////////////////////View item code //////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    override fun onItemClick(coursemodal: CourseModal) {

        Toast.makeText(requireActivity(), "view", Toast.LENGTH_SHORT).show()
    }

    ////////////////////////Delete code //////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    override fun onDeleteClick(coursemodal: CourseModal) {
        val builder = AlertDialog.Builder(mContext)
        builder.setTitle("Confirmation")
            .setMessage("Are you sure you want to delete?")
            .setPositiveButton("Yes") { _, _ ->
                performDeleteAction(coursemodal)
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
        deleteDialog = builder.create()
        deleteDialog?.show()
    }
    private fun performDeleteAction(coursemodal: CourseModal) {
        courseViewModel.deleteDrama(coursemodal)
            .observe(this@HomeFragments) { success ->
                if (success) {
                    Toast.makeText(
                        mContext,
                        "Drama Deleted Successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                    deleteDialog?.dismiss() // Dismiss the dialog here
                    setAdapter()
                } else {
                    Toast.makeText(
                        mContext,
                        constants.SOMETHING_WENT_WRONG_MESSAGE,
                        Toast.LENGTH_SHORT
                    ).show()
                    deleteDialog?.dismiss() // Dismiss the dialog here
                }
            }
    }

    ////////////////////////Edit code //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    override fun onEditClick(courseModal: CourseModal) {
        showEditDialog(courseModal)
        Toast.makeText(requireContext(), "clicked", Toast.LENGTH_SHORT).show()
    }
    @SuppressLint("SuspiciousIndentation")
    private fun showEditDialog(courseModal: CourseModal) {

        val dialog = Dialog(mContext, R.style.FullWidthDialog)
        dialog.setContentView(R.layout.dialogue_add_course)

        var course_title = dialog.findViewById<EditText>(R.id.course_title)
        var course_des = dialog.findViewById<EditText>(R.id.course_description)
        var course_image = dialog.findViewById<TextView>(R.id.select_image_course)
        var course_language = dialog.findViewById<EditText>(R.id.course_language)
        var add = dialog.findViewById<Button>(R.id.add)
        var cancl = dialog.findViewById<Button>(R.id.cancel)

        course_title.setText(courseModal.title)
        course_des.setText(courseModal.description)
        course_language.setText(courseModal.language)

        dialog.setCancelable(false)

        cancl.setOnClickListener { dialog.dismiss() }

        course_image.setOnClickListener {
             val pickImage =
                 Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
             startActivityForResult(pickImage, IMAGE_PICKER_REQUEST_CODE)
        }

        add.setOnClickListener {
            val courseTitle = course_title.text.toString()
            val courseDes = course_des.text.toString()
            val courselan = course_language.text.toString()

            if (courseTitle.isEmpty() || courseDes.isEmpty() || imageURI.toString()
                    .isEmpty()
            ) {
                Toast.makeText(mContext, "Please Enter All fields", Toast.LENGTH_SHORT).show()
            } else {
                courseModal.title = courseTitle
                courseModal.description = courseDes
                courseModal.language = courselan
                if (imageURI != null) {
                    uploadThumbnailImage(imageURI!!) { thumbnailUrl ->
                        if (thumbnailUrl != null) {
                            courseModal.thumbnail = thumbnailUrl
                                courseViewModel.updateCourse(courseModal)
                                    .observe(requireActivity()) { success ->
                                        if (success) {
                                            Toast.makeText(
                                                mContext,
                                                "Course updated successfully",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            setAdapter()
                                            dialog.dismiss()

                                        } else {
                                            utils.endLoadingAnimation()
                                            Toast.makeText(
                                                mContext,
                                                "Failed to update the Course",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
                        } else {
                            Toast.makeText(
                                mContext,
                                "Failed to upload the thumbnail image.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
                else{
                    Toast.makeText(requireContext(), "image uri is not selected", Toast.LENGTH_SHORT).show()
                }
            }
        }
        dialog.show()
    }
    private fun uploadThumbnailImage(imageUri: Uri, callback: (String?) -> Unit) {
        val storageRef = Firebase.storage.reference.child("thumbnails/${System.currentTimeMillis()}_${imageUri.lastPathSegment}")
        storageRef.putFile(imageUri)
            .addOnSuccessListener { taskSnapshot ->
                taskSnapshot.storage.downloadUrl.addOnCompleteListener { downloadUrlTask ->
                    if (downloadUrlTask.isSuccessful) {
                        val downloadUrl = downloadUrlTask.result.toString()
                        callback(downloadUrl)
                    } else {
                        callback(null)
                    }
                }
            }
            .addOnFailureListener {
                callback(null)
            }
    }
}