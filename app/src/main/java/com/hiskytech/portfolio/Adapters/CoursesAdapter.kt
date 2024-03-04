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
import com.hiskytech.portfolio.Models.CourseModal
import com.hiskytech.portfolio.R
import java.text.SimpleDateFormat
import java.util.Locale

class CoursesAdapter(
    private val context: Context,
    private val list: List<CourseModal>, val listener: OnItemClickListener
) : RecyclerView.Adapter<CoursesAdapter.ViewHolder>() {
    interface OnItemClickListener {

        fun onItemClick(coursemodal: CourseModal)
        fun onDeleteClick(coursemodal: CourseModal)
        fun onEditClick(coursemodal: CourseModal)

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.list_of_courses, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val course_title: TextView = itemView.findViewById(R.id.course_title)
        private val course_des: TextView = itemView.findViewById(R.id.course_description)
        private val edit: ImageView = itemView.findViewById(R.id.edit)
        private val deleteButton: ImageView = itemView.findViewById(R.id.delete)
        private val moredetail:TextView = itemView.findViewById(R.id.viewCourse)
        private  var imageView: ImageView = itemView.findViewById(R.id.course_image)

        @SuppressLint("SuspiciousIndentation")
        fun bind(coursemodal: CourseModal) {

            course_title.text = coursemodal.title
            course_des.text = coursemodal.description
            moredetail.setOnClickListener{ listener.onItemClick(coursemodal)}
            edit.setOnClickListener{ listener.onEditClick(coursemodal)}
           deleteButton.setOnClickListener{ listener.onDeleteClick(coursemodal)}
            Glide.with(context).load(coursemodal.thumbnail).centerCrop().placeholder(R.drawable.ic_launcher_background)
                .into(imageView)

            val dateTimeFormat = SimpleDateFormat("dd MMMM yyyy, h:mm a", Locale.getDefault())
            val formattedDateTime = dateTimeFormat.format(coursemodal.createdAt.toDate()) // Assuming timestamp is a Firebase Timestamp
            imageView.findViewById<TextView>(R.id.uploadedAt).text = formattedDateTime

        }

    }
}
