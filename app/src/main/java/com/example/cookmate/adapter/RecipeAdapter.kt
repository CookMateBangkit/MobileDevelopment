package com.example.cookmate.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.cookmate.databinding.ItemResepBinding
import com.example.cookmate.response.DataItem
import com.example.cookmate.response.RecipeResponse

class RecipeAdapter(private val data:MutableList<DataItem> = mutableListOf(), private val listener:(DataItem) -> Unit):
    RecyclerView.Adapter<RecipeAdapter.StoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder =
        StoryViewHolder(ItemResepBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item)
        holder.itemView.setOnClickListener{
            listener(item)
        }
    }

    override fun getItemCount(): Int = data.size

    class StoryViewHolder(private val v: ItemResepBinding): RecyclerView.ViewHolder(v.root) {
        fun bind(itemStoryBinding: DataItem){
            v.ivResep.load(itemStoryBinding.picsUrl)
            v.tvResep.text = itemStoryBinding.name
        }
    }

    fun setData(data: MutableList<DataItem>){
        this.data.clear()
        this.data.addAll(data)
        notifyDataSetChanged()
    }
}