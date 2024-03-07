package com.hiskytech.portfolio.Adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hiskytech.portfolio.Models.CompletedprojectModal
import com.hiskytech.portfolio.Models.CourseModal
import com.hiskytech.portfolio.R
import java.text.SimpleDateFormat
import java.util.Locale

class CompletedProjectAdapter(
    private val context: Context,
    private val list: List<CompletedprojectModal>, val listener: OnItemClickListener
) : RecyclerView.Adapter<CompletedProjectAdapter.ViewHolder>() {
    interface OnItemClickListener {

        fun onItemClick(completedprojectModal: CompletedprojectModal)
        fun onDeleteClick(completedprojectModal: CompletedprojectModal)
        fun onEditClick(completedprojectModal: CompletedprojectModal)

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.completed_project_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val project_title: TextView = itemView.findViewById(R.id.title)
        private val edit: ImageView = itemView.findViewById(R.id.edit)
        private val deleteButton: ImageView = itemView.findViewById(R.id.delete)
        private val moredetail: TextView = itemView.findViewById(R.id.view_project)
        private val project_image:ImageView = itemView.findViewById(R.id.cardImage)

        init {
            moredetail.setOnClickListener { listener.onItemClick(list[adapterPosition]) }
            edit.setOnClickListener { listener.onEditClick(list[adapterPosition]) }
            deleteButton.setOnClickListener { listener.onDeleteClick(list[adapterPosition]) }
        }
        @SuppressLint("SuspiciousIndentation")
        fun bind(completedprojectModal: CompletedprojectModal) {

            project_title.text = completedprojectModal.project_title
            Glide.with(context)
                .load(completedprojectModal.thumnail)
                .centerCrop()
                .placeholder(R.drawable.ic_launcher_background)
                .into(project_image)
            val dateTimeFormat = SimpleDateFormat("dd MMMM yyyy, h:mm a", Locale.getDefault())
            val formattedDateTime = dateTimeFormat.format(completedprojectModal.createdAt.toDate()) // Assuming timestamp is a Firebase Timestamp
            itemView.findViewById<TextView>(R.id.uploadedAt).text = formattedDateTime

        }

    }
}
