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
import com.hiskytech.portfolio.Models.AnnoucementModal
import com.hiskytech.portfolio.Models.CourseModal
import com.hiskytech.portfolio.R
import java.text.SimpleDateFormat
import java.util.Locale

class AnnoucementAdapter(
    private val context: Context,
    private val list: List<AnnoucementModal>, val listener: OnItemClickListener
) : RecyclerView.Adapter<AnnoucementAdapter.ViewHolder>() {
    interface OnItemClickListener {

        fun onItemClick(annoucementModal: AnnoucementModal)
        fun onDeleteClick(annoucementModal: AnnoucementModal)
        fun onEditClick(annoucementModal: AnnoucementModal)

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.annoucement_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val annoucement_title: TextView = itemView.findViewById(R.id.title_annoucement)
        private val edit: ImageView = itemView.findViewById(R.id.edit)
        private val deleteButton: ImageView = itemView.findViewById(R.id.delete)
        var annoucement_description:TextView = itemView.findViewById(R.id.annoucement_description)
        private val moredetail: TextView = itemView.findViewById(R.id.viewAnnoucement)
        private  var imageView: ImageView = itemView.findViewById(R.id.annoucementImage)
        private  var imageViewSecond: ImageView = itemView.findViewById(R.id.annoucementImageDisplay)

        init {
            moredetail.setOnClickListener { listener.onItemClick(list[adapterPosition]) }
            edit.setOnClickListener { listener.onEditClick(list[adapterPosition]) }
            deleteButton.setOnClickListener { listener.onDeleteClick(list[adapterPosition]) }
        }
        @SuppressLint("SuspiciousIndentation")
        fun bind(annoucementModal: AnnoucementModal) {

            annoucement_title.text = annoucementModal.title
    Glide.with(context)
        .load(annoucementModal.thumnail)
        .centerCrop()
        .placeholder(R.drawable.ic_launcher_background)
        .into(imageView)

    Glide.with(context)
        .load(annoucementModal.thumnailSecond)
        .centerCrop()
        .placeholder(R.drawable.ic_launcher_background)
        .into(imageViewSecond)



            val dateTimeFormat = SimpleDateFormat("dd MMMM yyyy, h:mm a", Locale.getDefault())
            val formattedDateTime = dateTimeFormat.format(annoucementModal.createdAt.toDate())
//            itemView.findViewById<TextView>(R.id.uploadedAt).text = formattedDateTime

        }

    }
}
