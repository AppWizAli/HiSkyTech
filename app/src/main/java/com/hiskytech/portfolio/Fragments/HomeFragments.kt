package com.hiskytech.portfolio.Fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
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
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.hiskytech.portfolio.Adapters.AnnoucementAdapter
import com.hiskytech.portfolio.Adapters.CompletedProjectAdapter
import com.hiskytech.portfolio.Adapters.CoursesAdapter
import com.hiskytech.portfolio.Adapters.JobAdapter
import com.hiskytech.portfolio.Adapters.TeamAdapter
import com.hiskytech.portfolio.Data.Constants
import com.hiskytech.portfolio.Data.SharedPrefManager
import com.hiskytech.portfolio.Data.Utils
import com.hiskytech.portfolio.Models.AnnoucementModal
import com.hiskytech.portfolio.Models.CompletedprojectModal
import com.hiskytech.portfolio.Models.CourseModal
import com.hiskytech.portfolio.Models.JobModal
import com.hiskytech.portfolio.Models.TeamModal
import com.hiskytech.portfolio.R
import com.hiskytech.portfolio.ViewModels.CourseViewModal
import com.hiskytech.portfolio.ViewModels.UserViewModal
import com.hiskytech.portfolio.databinding.FragmentHomeFragmentsBinding

class HomeFragments : Fragment(), CoursesAdapter.OnItemClickListener , JobAdapter.OnItemClickListener,AnnoucementAdapter.OnItemClickListener, CompletedProjectAdapter.OnItemClickListener,TeamAdapter.OnItemClickListener{
    private lateinit var binding: FragmentHomeFragmentsBinding
    private lateinit var dialogDetail: Dialog
    private lateinit var dialog: Dialog
    private lateinit var courseViewModel: CourseViewModal
    private val IMAGE_PICKER_REQUEST_CODE = 123
    private lateinit var contants: Constants
    private lateinit var mContext: Context
    private var imageURI: Uri? = null
    private lateinit var  teamModal:TeamModal
    private var imageUriSecond: Uri? = null
    private lateinit var jobModal: JobModal
    private lateinit var annoucementModal: AnnoucementModal
    private var getText: String = "view all"
    private var imagecodeSecond: Int = 100
    private lateinit var completedprojectModal: CompletedprojectModal
    private lateinit var courseModal: CourseModal
    private var deleteDialog: AlertDialog? = null
    private lateinit var utils: Utils
    private lateinit var constants: Constants
    private val userViewModal : UserViewModal by viewModels()

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
        jobModal = JobModal()
        completedprojectModal = CompletedprojectModal()
        teamModal = TeamModal()


        binding.detailFloating.setOnClickListener()
        {
            ShowDetaildialogue()
        }
        setAdapterAnnoucement()
        setAdapter()
        setAdapterJobs()
        setCompletedProjectAdapter()
        setTeamAdapter()

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
                imagecodeSecond = 120
                val pickImage =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(pickImage, IMAGE_PICKER_REQUEST_CODE)
            }

            next.setOnClickListener()
            {
                annoucementModal.title = annoucement_title.text.toString()
                annoucementModal.description = annoucement_des.text.toString()
                if (annoucement_title.text.toString().isEmpty() || annoucement_des.text.toString()
                        .isEmpty()
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
            var Project_title = dialog.findViewById<EditText>(R.id.project_title_editText)
            var project_description = dialog.findViewById<EditText>(R.id.project_description_editText)
            var Project_Duration = dialog.findViewById<EditText>(R.id.project_duration_editText)
            var Client_FeedBack = dialog.findViewById<EditText>(R.id.client_feedback_editText)
            var Achievement = dialog.findViewById<EditText>(R.id.achevement)
            var projectImage = dialog.findViewById<TextView>(R.id.project_image)
            var back = dialog.findViewById<Button>(R.id.back)
            var Add = dialog.findViewById<Button>(R.id.add)
            projectImage.setOnClickListener()
            {
                val pickImage =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(pickImage, IMAGE_PICKER_REQUEST_CODE)
            }

            back.setOnClickListener(){dialog.dismiss()}
            Add.setOnClickListener(){

                if (Project_title.text.toString().isEmpty() ||project_description.text.toString().isEmpty()||  Project_Duration.text.toString().isEmpty()|| Client_FeedBack.text.toString().isEmpty()||Achievement.text.toString().isEmpty() )
                {
                    Toast.makeText(mContext, "Please fill all the fields", Toast.LENGTH_SHORT).show()
                }
                else{
                    completedprojectModal.project_title = Project_title.text.toString()
                    completedprojectModal.project_description =
                        project_description.text.toString()
                    completedprojectModal.project_duration =
                        Project_Duration.text.toString()
                    completedprojectModal.client_feedback =
                        Client_FeedBack.text.toString()
                    completedprojectModal.acievement = Achievement.text.toString()
                    if (imageURI != null)
                    {
                        uploadThumbnailImage(imageURI!!) { thumbnailUrl ->
                            if (thumbnailUrl != null) {
                                completedprojectModal.thumnail = thumbnailUrl

                                courseViewModel.add_completed_course(completedprojectModal)
                                    .observe(requireActivity())
                                    { success ->
                                        if (success) {
                                            Toast.makeText(mContext, "Added", Toast.LENGTH_SHORT)
                                                .show()
                                            dialog.dismiss()
                                            setCompletedProjectAdapter()
                                        }

                                    }
                            }
                        }

                    }


                }
            }

            dialog.show()
        }
        add_team_member.setOnClickListener()
        {
            dialogDetail.dismiss()
            dialog = Dialog(requireContext(), R.style.FullWidthDialog)
            dialog.setContentView(R.layout.dialogue_add_team_member)
            dialog.setCancelable(false)

            var member_name = dialog.findViewById<EditText>(R.id.editname)
            var member_email = dialog.findViewById<EditText>(R.id.editemail)
            var member_phone_number = dialog.findViewById<EditText>(R.id.editPhone)
            var member_address = dialog.findViewById<EditText>(R.id.edit_adress)
            var cancel = dialog.findViewById<Button>(R.id.cancel_btn)
            var add = dialog.findViewById<Button>(R.id.add_btn)
            var member_image = dialog.findViewById<ImageView>(R.id.imageprof)
            cancel.setOnClickListener(){dialog.dismiss()}
            member_image.setOnClickListener()
            {
                val pickImage =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(pickImage, IMAGE_PICKER_REQUEST_CODE)
            }
            add.setOnClickListener()
            {
                if (member_name.text.toString().isEmpty() ||member_email.text.toString().isEmpty()||  member_phone_number.text.toString().isEmpty()|| member_address.text.toString().isEmpty() )
                {
                    Toast.makeText(mContext, "Please fill all the fields", Toast.LENGTH_SHORT).show()
                }
                else{

                    teamModal.member_name = member_name.text.toString()
                    teamModal.member_email = member_email.text.toString()
                    teamModal.member_Phone = member_phone_number.text.toString()
                    teamModal.member_address = member_address.text.toString()
                    if (imageURI != null)
                    {
                        uploadThumbnailImage(imageURI!!) { thumbnailUrl ->
                            if (thumbnailUrl != null) {
                                teamModal.thumnail = thumbnailUrl

                                userViewModal.add_team_member(teamModal)
                                    .observe(requireActivity())
                                    { success ->
                                        if (success) {
                                            Toast.makeText(mContext, "Added", Toast.LENGTH_SHORT)
                                                .show()
                                            dialog.dismiss()
                                            setTeamAdapter()
                                        }

                                    }
                            }
                        }

                    }
                    else{
                        Toast.makeText(mContext, "uri is null ", Toast.LENGTH_SHORT).show()
                    }


                }

            }
            dialog.show()
        }
        add_job.setOnClickListener()
        {
            dialogDetail.dismiss()
            dialog = Dialog(requireContext(), R.style.FullWidthDialog)
            dialog.setContentView(R.layout.dialogue_add_new_job)
            dialog.setCancelable(false)
            var job_title = dialog.findViewById<EditText>(R.id.Title_job)
            var description = dialog.findViewById<EditText>(R.id.Job_Description)
            var salary = dialog.findViewById<EditText>(R.id.salary)
            var company = dialog.findViewById<EditText>(R.id.company_name)
            var location = dialog.findViewById<EditText>(R.id.location)
            var dcancel = dialog.findViewById<Button>(R.id.cancel)
            var add = dialog.findViewById<Button>(R.id.add)
            dcancel.setOnClickListener() { dialog.dismiss() }
            add.setOnClickListener()
            {
                jobModal.title = job_title.text.toString()
                jobModal.description = description.text.toString()
                jobModal.sallary = salary.text.toString()
                jobModal.companyName = company.text.toString()
                jobModal.location = location.text.toString()

                if (job_title.text.toString().isEmpty() || description.text.toString()
                        .isEmpty() || salary.text.toString().isEmpty() || company.text.toString()
                        .isEmpty() || location.text.toString().isEmpty()
                ) {
                    Toast.makeText(mContext, "Please Enter All fields", Toast.LENGTH_SHORT).show()
                } else {

                    courseViewModel.add_Job(jobModal).observe(requireActivity()) { success ->
                        if (success) {
                            Toast.makeText(requireContext(), "added", Toast.LENGTH_SHORT).show()
                            setAdapterJobs()
                            dialog.dismiss()

                        } else {
                            Toast.makeText(requireContext(), "failed", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
            dialog.show()

        }

        dialogDetail.show()
    }
    private fun setTeamAdapter()
    {
        val listjob = ArrayList<TeamModal>()

        userViewModal.get_team_memeber_list().addOnSuccessListener() { taskResult ->
            if (taskResult != null) {
                if (taskResult.size() > 0) {
                    for (document in taskResult) {
                        listjob.add(document.toObject(TeamModal::class.java))
                        listjob.sortBy { it.member_name }
                    }
                }
                binding.recyclerViewTeam.layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false)
                binding.recyclerViewTeam.adapter = TeamAdapter(mContext, listjob.take(2), this@HomeFragments)
                binding.viewAllmember.setOnClickListener()
                {
                    var showText = binding.viewAllmember.text
                    if (showText == getText) {
                        binding.recyclerViewTeam.adapter = TeamAdapter(mContext, listjob, this@HomeFragments)
                        binding.viewAllmember.setText("Merge All")
                    } else {
                        binding.recyclerViewTeam.adapter = TeamAdapter(mContext, listjob.take(2), this@HomeFragments)
                        binding.viewAllmember.setText(getText)
                    }

                }


            }
        }
    }

    private fun setCompletedProjectAdapter(){
        val listjob = ArrayList<CompletedprojectModal>()

        courseViewModel.get_complted_project_list().addOnSuccessListener() { taskResult ->
            if (taskResult != null) {
                if (taskResult.size() > 0) {
                    for (document in taskResult) {
                        listjob.add(document.toObject(CompletedprojectModal::class.java))
                        listjob.sortBy { it.project_title }
                    }
                }
                binding.rvCompltedProjects.layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false)
                binding.rvCompltedProjects.adapter = CompletedProjectAdapter(mContext, listjob.take(2), this@HomeFragments)
                binding.viewallcompletedProjects.setOnClickListener()
                {
                    var showText = binding.viewallcompletedProjects.text
                    if (showText == getText) {
                        binding.rvCompltedProjects.adapter = CompletedProjectAdapter(mContext, listjob, this@HomeFragments)
                        binding.viewallcompletedProjects.setText("Merge All")
                    } else {
                        binding.rvCompltedProjects.adapter = CompletedProjectAdapter(mContext, listjob.take(2), this@HomeFragments)
                        binding.viewallcompletedProjects.setText(getText)
                    }

                }


            }
        }

    }


    private fun setAdapterJobs() {
        val listjob = ArrayList<JobModal>()

        courseViewModel.get_job_list().addOnSuccessListener() { taskResult ->
            if (taskResult != null) {
                if (taskResult.size() > 0) {
                    for (document in taskResult) {
                        listjob.add(document.toObject(jobModal::class.java))
                        listjob.sortBy { it.title }
                    }
                }
                binding.rvJob.layoutManager = LinearLayoutManager(mContext)
                binding.rvJob.adapter = JobAdapter(mContext, listjob.take(2), this@HomeFragments)
                binding.viewall.setOnClickListener()
                {
                    var showText = binding.viewall.text
                    if (showText == getText) {
                        binding.rvJob.adapter = JobAdapter(mContext, listjob, this@HomeFragments)
                        binding.viewall.setText("Merge All")
                    } else {
                        binding.rvJob.adapter =
                            JobAdapter(mContext, listjob.take(2), this@HomeFragments)
                        binding.viewall.setText(getText)
                    }

                }


            }
        }

    }
    private fun setAdapterAnnoucement() {
        val listAnnoucement = ArrayList<AnnoucementModal>()

        courseViewModel.get_Annoucement_list().addOnSuccessListener() { taskResult ->
            if (taskResult != null) {
                if (taskResult.size() > 0) {
                    for (document in taskResult) {
                        listAnnoucement.add(document.toObject(AnnoucementModal::class.java))
                        listAnnoucement.sortBy { it.title }
                    }
                }
                binding.recyclerViewAnnouncements.layoutManager = LinearLayoutManager(mContext)
                binding.recyclerViewAnnouncements.adapter = AnnoucementAdapter(mContext, listAnnoucement.take(1), this@HomeFragments)
                binding.viewAllAnnouncements.setOnClickListener()
                {
                    var showText = binding.viewAllAnnouncements.text
                    if (showText == getText) {
                        binding.recyclerViewAnnouncements.adapter = AnnoucementAdapter(mContext, listAnnoucement, this@HomeFragments)
                        binding.viewAllAnnouncements.setText("Merge All")
                    } else {
                        binding.recyclerViewAnnouncements.adapter =
                            AnnoucementAdapter(mContext, listAnnoucement.take(1), this@HomeFragments)
                        binding.viewAllAnnouncements.setText(getText)
                    }

                }


            }
        }

    }

    private fun setAdapter() {
        val list = ArrayList<CourseModal>()
        courseViewModel.getCourseList().addOnSuccessListener { taskResult ->

            if (taskResult != null) {
                if (taskResult.size() > 0) {
                    for (document in taskResult) {
                        list.add(document.toObject(CourseModal::class.java))
                        list.sortBy { it.title }
                    }
                }

                binding.recyclerView.layoutManager = LinearLayoutManager(mContext)
                binding.recyclerView.adapter =
                    CoursesAdapter(mContext, list.take(2), this@HomeFragments)
                binding.viewAll.setOnClickListener()
                {
                    var showText = binding.viewAll.text
                    if (showText == getText) {
                        binding.recyclerView.adapter =
                            CoursesAdapter(mContext, list, this@HomeFragments)
                        binding.viewAll.setText("Merge All")
                    } else {
                        binding.recyclerView.adapter =
                            CoursesAdapter(mContext, list.take(2), this@HomeFragments)
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
            imageURI = data?.data
            if (imagecodeSecond == 120) {
                imageUriSecond = data?.data
            } else {
                imageURI = data?.data
                val imageView = dialog.findViewById<ImageView>(R.id.imageprof)
                context?.let {
                    Glide.with(it)
                        .load(imageURI)
                        .into(imageView)
                }

            }


        }
    }

    private fun annoucementAdd() {
        if (imageURI != null) {
            if (imageUriSecond != null) {
                uploadThumbnailImage(imageURI!!) { thumbnailUrl ->
                    if (thumbnailUrl != null) {
                        annoucementModal.thumnail = thumbnailUrl

                        uploadThumbnailImage(imageUriSecond!!) {
                            if (thumbnailUrl != null) {
                                annoucementModal.thumnailSecond = thumbnailUrl
                                courseViewModel.addAnnoucement(annoucementModal)
                                    .observe(requireActivity()) { success ->
                                        if (success) {
                                            Toast.makeText(
                                                mContext,
                                                "Created successfully",
                                                Toast.LENGTH_SHORT
                                            ).show()

                                            setAdapterAnnoucement()
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
            } else {
                Toast.makeText(
                    requireContext(),
                    "image uri display is not selected",
                    Toast.LENGTH_SHORT
                ).show()
            }
        } else {
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
        } else {
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
                } else {
                    Toast.makeText(
                        requireContext(),
                        "image uri is not selected",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
        dialog.show()
    }

    private fun uploadThumbnailImage(imageUri: Uri, callback: (String?) -> Unit) {
        val storageRef =
            Firebase.storage.reference.child("thumbnails/${System.currentTimeMillis()}_${imageUri.lastPathSegment}")
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

    override fun onItemClick(jobModal: JobModal) {


    }


    override fun onDeleteClick(jobModal: JobModal) {
        val builder = AlertDialog.Builder(mContext)
        builder.setTitle("Confirmation")
            .setMessage("Are you sure you want to delete?")
            .setPositiveButton("Yes") { _, _ ->
                performDeleteActionJob(jobModal)
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
        deleteDialog = builder.create()
        deleteDialog?.show()
    }

    private fun performDeleteActionJob(jobModal: JobModal) {
        courseViewModel.deleteJob(jobModal)
            .observe(this@HomeFragments) { success ->
                if (success) {
                    Toast.makeText(
                        mContext,
                        "JOB Deleted Successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                    deleteDialog?.dismiss() // Dismiss the dialog here
                    setAdapterJobs()
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

        override fun onEditClick(jobModal: JobModal) {
            showJobEditDialog(jobModal)
            Toast.makeText(requireContext(), "clicked", Toast.LENGTH_SHORT).show()
        }

        private fun showJobEditDialog(jobModal: JobModal) {

            val dialog = Dialog(mContext, R.style.FullWidthDialog)
            dialog.setContentView(R.layout.dialogue_add_new_job)

            var job_title = dialog.findViewById<EditText>(R.id.Title_job)
            var job_description = dialog.findViewById<EditText>(R.id.Job_Description)
            var Salary = dialog.findViewById<EditText>(R.id.salary)
            var CompanyName = dialog.findViewById<EditText>(R.id.company_name)
            var Location = dialog.findViewById<EditText>(R.id.location)
            var add = dialog.findViewById<Button>(R.id.add)
            var cancl = dialog.findViewById<Button>(R.id.cancel)

            job_title.setText(jobModal.title)
            job_description.setText(jobModal.description)
            Salary.setText(jobModal.sallary)
            CompanyName.setText(jobModal.companyName)
            Location.setText(jobModal.location)

            dialog.setCancelable(false)

            cancl.setOnClickListener { dialog.dismiss() }

            add.setOnClickListener {
                val jobTitle = job_title.text.toString()
                val jobDescription = job_description.text.toString()
                val salary = Salary.text.toString()
                var companyName = CompanyName.text.toString()
                var location = Location.text.toString()

                if (jobTitle.isEmpty() || jobDescription.isEmpty() || salary.toString()
                        .isEmpty() || companyName.isEmpty() || location.isEmpty()
                ) {
                    Toast.makeText(mContext, "Please Enter All fields", Toast.LENGTH_SHORT).show()
                } else {
                    jobModal.title = jobTitle
                    jobModal.description = jobDescription
                    jobModal.sallary = salary
                    jobModal.companyName = companyName
                    jobModal.location = location

                    courseViewModel.updatejob(jobModal)
                        .observe(requireActivity()) { success ->
                            if (success) {
                                Toast.makeText(
                                    mContext,
                                    "Job updated successfully",
                                    Toast.LENGTH_SHORT
                                ).show()
                                setAdapterJobs()
                                dialog.dismiss()

                            } else {
                                utils.endLoadingAnimation()
                                Toast.makeText(
                                    mContext,
                                    "Failed to update the Job",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                }
            }

            dialog.show()

    }

    override fun onItemClick(annoucementModal: AnnoucementModal) {

    }

    override fun onDeleteClick(annoucementModal: AnnoucementModal) {

    }

    override fun onEditClick(annoucementModal: AnnoucementModal) {

    }

    override fun onItemClick(completedprojectModal: CompletedprojectModal) {

    }

    override fun onDeleteClick(completedprojectModal: CompletedprojectModal) {
        val builder = AlertDialog.Builder(mContext)
        builder.setTitle("Confirmation")
            .setMessage("Are you sure you want to delete?")
            .setPositiveButton("Yes") { _, _ ->
                performDeleteActionProject(completedprojectModal)
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
        deleteDialog = builder.create()
        deleteDialog?.show()
    }
    private fun performDeleteActionProject(completedprojectModal: CompletedprojectModal)
    {
        courseViewModel.deleteproject(completedprojectModal)
            .observe(this@HomeFragments) { success ->
                if (success) {
                    Toast.makeText(
                        mContext,
                        "Project Deleted Successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                    deleteDialog?.dismiss() // Dismiss the dialog here
                    setAdapterJobs()
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

    override fun onEditClick(completedprojectModal: CompletedprojectModal) {

        val dialog = Dialog(mContext, R.style.FullWidthDialog)
        dialog.setContentView(R.layout.dialogue_add_completed_projects)

        var Project_title = dialog.findViewById<EditText>(R.id.project_title_editText)
        var project_description = dialog.findViewById<EditText>(R.id.project_description_editText)
        var Project_Duration = dialog.findViewById<EditText>(R.id.project_duration_editText)
        var Client_FeedBack = dialog.findViewById<EditText>(R.id.client_feedback_editText)
        var Achievement = dialog.findViewById<EditText>(R.id.achevement)
        var projectImage = dialog.findViewById<TextView>(R.id.project_image)
        var back = dialog.findViewById<Button>(R.id.back)
        var Add = dialog.findViewById<Button>(R.id.add)

        Project_title.setText(completedprojectModal.project_title)
        project_description.setText(completedprojectModal.project_description)
        Project_Duration.setText(completedprojectModal.project_duration)
        Client_FeedBack.setText(completedprojectModal.client_feedback)
        Achievement.setText(completedprojectModal.acievement)

        dialog.setCancelable(false)

        back.setOnClickListener { dialog.dismiss() }

        projectImage.setOnClickListener {
            val pickImage =
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(pickImage, IMAGE_PICKER_REQUEST_CODE)
        }

        Add.setOnClickListener {
            completedprojectModal.project_title = Project_title.text.toString()
            completedprojectModal.project_description =
                project_description.text.toString()
            completedprojectModal.project_duration =
                Project_Duration.text.toString()
            completedprojectModal.client_feedback =
                Client_FeedBack.text.toString()
            completedprojectModal.acievement = Achievement.text.toString()

            if (Project_title.text.toString().isEmpty() ||project_description.text.toString().isEmpty()||  Project_Duration.text.toString().isEmpty()|| Client_FeedBack.text.toString().isEmpty()||Achievement.text.toString().isEmpty() )
            {
                Toast.makeText(mContext, "Please fill all the fields", Toast.LENGTH_SHORT).show()
            }
            else {
                if (imageURI != null) {
                    uploadThumbnailImage(imageURI!!) { thumbnailUrl ->
                        if (thumbnailUrl != null) {
                            completedprojectModal.thumnail = thumbnailUrl
                            courseViewModel.edit_completed_project(completedprojectModal)
                                .observe(requireActivity()) { success ->
                                    if (success) {
                                        Toast.makeText(
                                            mContext,
                                            " updated successfully",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        setCompletedProjectAdapter()
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
                } else {
                    Toast.makeText(
                        requireContext(),
                        "image uri is not selected",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
        dialog.show()

    }

    override fun onItemClick(teamModal: TeamModal) {

    }

    override fun onDeleteClick(teamModal: TeamModal) {
        val builder = AlertDialog.Builder(mContext)
        builder.setTitle("Confirmation")
            .setMessage("Are you sure you want to delete?")
            .setPositiveButton("Yes") { _, _ ->
                performDeleteActionTeamMember(teamModal)
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
        deleteDialog = builder.create()
        deleteDialog?.show()
    }
    private fun performDeleteActionTeamMember(teamModal: TeamModal)
    {
        userViewModal.deleteTeamMember(teamModal)
            .observe(this@HomeFragments) { success ->
                if (success) {
                    Toast.makeText(
                        mContext,
                        "Team Deleted Successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                    deleteDialog?.dismiss() // Dismiss the dialog here
                    setTeamAdapter()
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

    override fun onEditClick(teamModal: TeamModal) {

        val dialog = Dialog(mContext, R.style.FullWidthDialog)
        dialog.setContentView(R.layout.dialogue_add_team_member)

        var member_name = dialog.findViewById<EditText>(R.id.editname)
        var member_email = dialog.findViewById<EditText>(R.id.editemail)
        var member_phone_number = dialog.findViewById<EditText>(R.id.editPhone)
        var member_address = dialog.findViewById<EditText>(R.id.edit_adress)
        var cancel = dialog.findViewById<Button>(R.id.cancel_btn)
        var add = dialog.findViewById<Button>(R.id.add_btn)
        var member_image = dialog.findViewById<ImageView>(R.id.imageprof)
        cancel.setOnClickListener(){dialog.dismiss()}
        member_image.setOnClickListener()
        {
            val pickImage =
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(pickImage, IMAGE_PICKER_REQUEST_CODE)
        }
        member_name.setText(teamModal.member_name)
        member_email.setText(teamModal.member_email)
        member_phone_number.setText(teamModal.member_Phone)
        member_address.setText(teamModal.member_address)

        dialog.setCancelable(false)

        add.setOnClickListener {
            teamModal.member_name = member_name.text.toString()
            teamModal.member_email = member_email.text.toString()
            teamModal.member_Phone = member_phone_number.text.toString()
            teamModal.member_address = member_address.text.toString()

            if (member_name.text.toString().isEmpty() ||member_email.text.toString().isEmpty()||  member_phone_number.text.toString().isEmpty()|| member_address.text.toString().isEmpty() )
            {
                Toast.makeText(mContext, "Please fill all the fields", Toast.LENGTH_SHORT).show()
            }
            else{
                if (imageURI != null) {
                    uploadThumbnailImage(imageURI!!) { thumbnailUrl ->
                        if (thumbnailUrl != null) {
                            teamModal.thumnail = thumbnailUrl
                            userViewModal.edit_Team_member(teamModal)
                                .observe(requireActivity()) { success ->
                                    if (success) {
                                        Toast.makeText(
                                            mContext,
                                            " updated successfully",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        setTeamAdapter()
                                        dialog.dismiss()

                                    } else {
                                        utils.endLoadingAnimation()
                                        Toast.makeText(
                                            mContext,
                                            "Failed to update",
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
                } else {
                    Toast.makeText(
                        requireContext(),
                        "image uri is not selected",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
        dialog.show()

    }
}