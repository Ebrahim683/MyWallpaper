package com.rexoit.mywallpaper.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rexoit.mywallpaper.R
import com.rexoit.mywallpaper.model.WallpaperModel
import com.rexoit.mywallpaper.util.RecyclerViewItemClick
import kotlinx.android.synthetic.main.image_list_row.view.*

class ImageListAdapter(var context: Context? = null) :
    ListAdapter<WallpaperModel, ImageListAdapter.ImageListHolder>(
        DIFF_UTIL_CALL_BACK
    ) {
    var recyclerViewItemClick: RecyclerViewItemClick? = null

    fun onClick(recyclerViewItemClick: RecyclerViewItemClick) {
        this.recyclerViewItemClick = recyclerViewItemClick
    }

    class ImageListHolder(itemView: View, val recyclerViewItemClick: RecyclerViewItemClick) :
        RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.sample_image_list)
        val imageName: TextView = itemView.findViewById(R.id.list_image_name)
        val btnDownload: Button = itemView.findViewById(R.id.image_list_download)

        fun bind(wallpaperModel: WallpaperModel, context: Context?) {
//            Picasso.get().load(wallpaperModel.imageLink).placeholder(R.drawable.loading).into(image)
            Glide.with(context!!).load(wallpaperModel.imageLink).placeholder(R.drawable.loading)
                .into(image)
            imageName.text = wallpaperModel.imageName

            itemView.r_id.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    recyclerViewItemClick.onItemClick(adapterPosition, itemView)
                }
            }
            btnDownload.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    recyclerViewItemClick.saveClick(adapterPosition, itemView)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageListHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.image_list_row, parent, false)
        return ImageListHolder(view, recyclerViewItemClick!!)
    }

    override fun onBindViewHolder(holder: ImageListHolder, position: Int) {
        val wallpaperModel = getItem(position)
        holder.bind(wallpaperModel, context)
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