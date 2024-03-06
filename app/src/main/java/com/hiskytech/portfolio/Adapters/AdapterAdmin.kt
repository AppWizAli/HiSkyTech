package com.hiskytech.portfolio.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hiskytech.portfolio.Data.Constants
import com.hiskytech.portfolio.Data.SharedPrefManager
import com.hiskytech.portfolio.databinding.ItemUserBinding
import com.hiskytech.portfolio.Models.ModelUser
import com.hiskytech.portfolio.Models.Usermodel
import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Locale

class AdapterAdmin(var context: Context, val list: ArrayList<Usermodel>, val listener: OnItemClickListener) : RecyclerView.Adapter<AdapterAdmin.ViewHolder>(){
var sharedPrefManager= SharedPrefManager(context)

    var constant= Constants()

    interface OnItemClickListener {
        fun onUpdateButton(usermodel: Usermodel )
        fun onRemoveButton(usermodel: Usermodel)
        fun onViewButton(usermodel: Usermodel)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemBinding = ItemUserBinding.inflate(inflater, parent, false)
        return ViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) { holder.bind(list[position]) }
    override fun getItemCount(): Int { return list.size }
    inner class ViewHolder(val itemBinding: ItemUserBinding) : RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(usermodel: Usermodel) {
            itemBinding.updateUser.setOnClickListener {
                listener.onUpdateButton(usermodel )
            }
            itemBinding.removeUser.setOnClickListener {
                listener.onRemoveButton(usermodel)
            }
            itemBinding.view.setOnClickListener {
                listener.onViewButton(usermodel)
            }
            itemBinding.userName.text = usermodel.name
            val dateTimeFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
            val formattedDateTime = dateTimeFormat.format(usermodel.createdAt.toDate()) // Assuming timestamp is a Firebase Timestamp
            itemBinding.uploadedAt.text = formattedDateTime
        }
    }


}