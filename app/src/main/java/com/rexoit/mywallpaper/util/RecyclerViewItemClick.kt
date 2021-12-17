package com.rexoit.mywallpaper.util

import android.view.View

interface RecyclerViewItemClick {
    fun onItemClick(position: Int, view: View)
    fun saveClick(position: Int, view: View)
}