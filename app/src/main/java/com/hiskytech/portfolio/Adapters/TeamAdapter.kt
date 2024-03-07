package com.hiskytech.portfolio.Adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hiskytech.portfolio.Models.JobModal
import com.hiskytech.portfolio.Models.TeamModal
import com.hiskytech.portfolio.R
import java.text.SimpleDateFormat
import java.util.Locale

class TeamAdapter (
    private val context: Context,
    private val listjob: List<TeamModal>, val listener: OnItemClickListener
) : RecyclerView.Adapter<TeamAdapter.ViewHolder>() {

    interface OnItemClickListener {

        fun onItemClick(teamModal: TeamModal)
        fun onDeleteClick(teamModal: TeamModal)
        fun onEditClick(teamModal: TeamModal)

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.team_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listjob[position])
    }

    override fun getItemCount(): Int {
        return listjob.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val member_name: TextView = itemView.findViewById(R.id.person_name)
        private val member_phone:TextView = itemView.findViewById(R.id.peron_phone)
        private val deleteButton: ImageView = itemView.findViewById(R.id.delete)
        private val moredetail: TextView = itemView.findViewById(R.id.view_member)
        private  var editButton: ImageView = itemView.findViewById(R.id.edit)

        init {
            moredetail.setOnClickListener { listener.onItemClick(listjob[adapterPosition]) }
            editButton.setOnClickListener { listener.onEditClick(listjob[adapterPosition]) }
            deleteButton.setOnClickListener { listener.onDeleteClick(listjob[adapterPosition]) }
        }
        @SuppressLint("SuspiciousIndentation")
        fun bind(teamModal: TeamModal) {
            member_name.text = teamModal.member_name
            member_phone.text = teamModal.member_Phone
            val dateTimeFormat = SimpleDateFormat("dd MMMM yyyy, h:mm a", Locale.getDefault())
            val formattedDateTime = dateTimeFormat.format(teamModal.createdAt.toDate()) // Assuming timestamp is a Firebase Timestamp
            itemView.findViewById<TextView>(R.id.uploadedAt).text = formattedDateTime

        }

    }
}
