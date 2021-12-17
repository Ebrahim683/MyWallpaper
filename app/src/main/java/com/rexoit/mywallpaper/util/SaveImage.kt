package com.rexoit.mywallpaper.util

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.Toast
import com.rexoit.mywallpaper.model.WallpaperModel
import kotlinx.android.synthetic.main.image_list_row.view.*
import java.io.File
import java.io.FileOutputStream

private const val TAG = "SaveImage"

class SaveImage {

    fun saveImage(
        context: Context,
        view: View,
        position: Int,
        resources: String,
        filterList: ArrayList<WallpaperModel>
    ) {
        var outputStream: FileOutputStream? = null
        val drawable = view.sample_image_list.drawable as BitmapDrawable
        val bitmap = drawable.bitmap
        val sdCard =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        val directory =
            File("${sdCard.absolutePath}/$resources")
        directory.mkdir()
        val fileName = String.format(
            "${filterList[position].imageName}.png",
            System.currentTimeMillis()
        )
        val finalFile = File(directory, fileName)
        Toast.makeText(
            context,
            "Image Saved",
            Toast.LENGTH_SHORT
        ).show()

        try {
            outputStream = FileOutputStream(finalFile)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            outputStream!!.flush()
            outputStream!!.close()
            val intent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
            intent.data = Uri.fromFile(finalFile)
            context.sendBroadcast(intent)
        } catch (e: Exception) {
            Log.d(TAG, "saveClick: $e")
        }
    }

}