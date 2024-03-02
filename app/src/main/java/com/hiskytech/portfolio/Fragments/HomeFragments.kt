package com.hiskytech.portfolio.Fragments

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.hiskytech.portfolio.Models.AnnoucementModal
import com.hiskytech.portfolio.R
import com.hiskytech.portfolio.databinding.FragmentHomeFragmentsBinding

class HomeFragments : Fragment() {
    private lateinit var binding: FragmentHomeFragmentsBinding
    private lateinit var dialog: Dialog
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentHomeFragmentsBinding.inflate(inflater,container,false)
        binding.detailFloating.setOnClickListener()
        {
            ShowDetaildialogue()
        }
        return binding.root
    }

    private fun ShowDetaildialogue() {
        dialog = Dialog(requireContext(),R.style.FullWidthDialog)
        dialog.setContentView(R.layout.dialog_detail_list)
        dialog.setCancelable(false)

        var add_annoucement = dialog.findViewById<Button>(R.id.add_annoucement)
        var add_course = dialog.findViewById<Button>(R.id.add_course)
        var add_completed_projects = dialog.findViewById<Button>(R.id.add_completed_projects)
        var add_team_member = dialog.findViewById<Button>(R.id.add_team_member)
        var add_job = dialog.findViewById<Button>(R.id.add_job)

        add_annoucement.setOnClickListener()
        {
            dialog = Dialog(requireContext(),R.style.FullWidthDialog)
            dialog.setContentView(R.layout.dialogue_add_annoucement)
            dialog.setCancelable(false)

            var annoucement_title = dialog.findViewById<EditText>(R.id.annoucement_title)
            var annoucement_des = dialog.findViewById<EditText>(R.id.annoucement_description)
            var annoucement_image = dialog.findViewById<EditText>(R.id.select_image)
            var annoucement_display_image = dialog.findViewById<EditText>(R.id.select_image_display)

            var next = dialog.findViewById<Button>(R.id.add_course)
            var cancel = dialog.findViewById<Button>(R.id.cancel)
            cancel.setOnClickListener()
            {
                dialog.dismiss()
            }
            next.setOnClickListener()
            {
                var annoucement_modal  = AnnoucementModal()
            }

            dialog.show()
        }
        add_course.setOnClickListener()
        {
            dialog = Dialog(requireContext(),R.style.FullWidthDialog)
            dialog.setContentView(R.layout.dialogue_add_course)
            dialog.setCancelable(false)
            dialog.show()
        }
        add_completed_projects.setOnClickListener()
        {
            dialog = Dialog(requireContext(),R.style.FullWidthDialog)
            dialog.setContentView(R.layout.dialogue_add_completed_projects)
            dialog.setCancelable(false)
            dialog.show()
        }
        add_team_member.setOnClickListener()
        {
            dialog = Dialog(requireContext(),R.style.FullWidthDialog)
            dialog.setContentView(R.layout.dialogue_add_team_member)
            dialog.setCancelable(false)
            dialog.show()
        }
        add_job.setOnClickListener()
        {  dialog = Dialog(requireContext(),R.style.FullWidthDialog)
            dialog.setContentView(R.layout.dialogue_add_new_job)
            dialog.setCancelable(false)
            dialog.show()

        }

        dialog.show()
    }
}