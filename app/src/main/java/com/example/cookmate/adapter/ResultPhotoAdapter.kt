package com.example.cookmate.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.cookmate.databinding.ItemResepBinding
import com.example.cookmate.databinding.ItemSavedBinding
import com.example.cookmate.response.DataItem
import com.example.cookmate.response.DataPhotoItem

class ResultPhotoAdapter(private val data:MutableList<DataPhotoItem> = mutableListOf(), private val listener:(DataPhotoItem) -> Unit)
    : RecyclerView.Adapter<ResultPhotoAdapter.ResultPhotoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultPhotoViewHolder =
        ResultPhotoViewHolder(ItemSavedBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ResultPhotoViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item)
        holder.itemView.setOnClickListener{
            listener(item)
        }
    }

    override fun getItemCount(): Int = data.size

    class ResultPhotoViewHolder(private val v: ItemSavedBinding): RecyclerView.ViewHolder(v.root) {
        fun bind(itemStoryBinding1: DataPhotoItem){
            v.ivResep.load(itemStoryBinding1.picsUrl)
            v.tvResep.text = itemStoryBinding1.name
        }
    }

    fun setData(data: MutableList<DataPhotoItem>){
        this.data.clear()
        this.data.addAll(data)
        notifyDataSetChanged()
    }
}