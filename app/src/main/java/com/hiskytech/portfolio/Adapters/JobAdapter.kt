package com.hiskytech.portfolio.Adapters

import android.annotation.SuppressLint
import android.content.Context
import android.media.audiofx.AudioEffect.Descriptor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hiskytech.portfolio.Models.JobModal
import com.hiskytech.portfolio.R
import java.text.SimpleDateFormat
import java.util.Locale

class JobAdapter(
private val context: Context,
private val listjob: List<JobModal>, val listener: OnItemClickListener
) : RecyclerView.Adapter<JobAdapter.ViewHolder>() {

    interface OnItemClickListener {

        fun onItemClick(jobModal: JobModal)
        fun onDeleteClick(jobModal: JobModal)
        fun onEditClick(jobModal: JobModal)

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.list_of_jobs, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listjob[position])
    }

    override fun getItemCount(): Int {
        return listjob.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val jobTitle: TextView = itemView.findViewById(R.id.job_title)
        private val deleteButton: ImageView = itemView.findViewById(R.id.delete)
        private val moredetail: TextView = itemView.findViewById(R.id.view_job)
        private  var editButton: ImageView = itemView.findViewById(R.id.edit)

        init {
            moredetail.setOnClickListener { listener.onItemClick(listjob[adapterPosition]) }
            editButton.setOnClickListener { listener.onEditClick(listjob[adapterPosition]) }
            deleteButton.setOnClickListener { listener.onDeleteClick(listjob[adapterPosition]) }
        }
        @SuppressLint("SuspiciousIndentation")
        fun bind(jobModal: JobModal) {

            jobTitle.text = jobModal.title
            val dateTimeFormat = SimpleDateFormat("dd MMMM yyyy, h:mm a", Locale.getDefault())
            val formattedDateTime = dateTimeFormat.format(jobModal.createdAt.toDate()) // Assuming timestamp is a Firebase Timestamp
            itemView.findViewById<TextView>(R.id.uploadedAt).text = formattedDateTime

        }

    }
}
