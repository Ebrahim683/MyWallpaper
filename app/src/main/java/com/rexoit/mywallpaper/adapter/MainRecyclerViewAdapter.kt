package com.rexoit.mywallpaper.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rexoit.mywallpaper.R
import com.rexoit.mywallpaper.model.WallpaperModel
import com.rexoit.mywallpaper.util.RecyclerViewItemClick
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class MainRecyclerViewAdapter :
    ListAdapter<WallpaperModel, MainRecyclerViewAdapter.MainRecyclerViewHolder>(DIFF_UTIL_CALL_BACK) {

    var recyclerViewItemClick: RecyclerViewItemClick? = null

    fun onRecItemClick(recyclerViewItemClick: RecyclerViewItemClick) {
        this.recyclerViewItemClick = recyclerViewItemClick
    }

    class MainRecyclerViewHolder(itemView: View, val recyclerViewItemClick: RecyclerViewItemClick) :
        RecyclerView.ViewHolder(itemView) {
        val imageName: TextView = itemView.findViewById(R.id.sample_main_rec_text)
        val imageLink: CircleImageView = itemView.findViewById(R.id.sample_main_rec_image)

        fun bind(wallpaperModel: WallpaperModel) {
            imageLink.setImageResource(wallpaperModel.imageLink!!.toInt())
            imageName.text = wallpaperModel.imageName

            itemView.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    recyclerViewItemClick.onItemClick(adapterPosition, itemView)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainRecyclerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.main_rec_row, parent, false)
        return MainRecyclerViewHolder(view, recyclerViewItemClick!!)
    }

    override fun onBindViewHolder(holder: MainRecyclerViewHolder, position: Int) {
        val wallpaperModel = getItem(position)
        holder.bind(wallpaperModel)
    }

    companion object {
        val DIFF_UTIL_CALL_BACK = object : DiffUtil.ItemCallback<WallpaperModel>() {
            override fun areItemsTheSame(
                oldItem: WallpaperModel,
                newItem: WallpaperModel
            ): Boolean {
                return oldItem.imageName == newItem.imageName && oldItem.imageLink == newItem.imageLink
            }

            override fun areContentsTheSame(
                oldItem: WallpaperModel,
                newItem: WallpaperModel
            ): Boolean {
                return oldItem.imageName == newItem.imageName && oldItem.imageLink == newItem.imageLink
            }

        }
    }
}