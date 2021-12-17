package com.rexoit.mywallpaper.ui

import android.app.WallpaperManager
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.WindowManager
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.rexoit.mywallpaper.BuildConfig
import com.rexoit.mywallpaper.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_image_details.*
import java.io.File
import java.io.FileOutputStream

private const val TAG = "imageDetailsActivity"

class ImageDetailsActivity : AppCompatActivity() {

    private var outputStream: FileOutputStream? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_details)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        val image = intent.getStringExtra("image")
        val name = intent.getStringExtra("name")
        val sliderImageTitle = intent.getStringExtra("sliderImageTitle")

        //slider image

        when (name) {
            "SLIDER_IMAGE" -> {
                val sliderImage = intent.getStringExtra("sliderImage")
                Picasso.get().load(sliderImage).placeholder(R.drawable.loading).into(img_full)
                img_full_name.text = sliderImageTitle
            }
            else -> {
                Picasso.get().load(image).placeholder(R.drawable.loading).into(img_full)
                img_full_name.text = name


                img_full_download.setOnClickListener {
                    saveImage(image = img_full, name = name.toString(), quality = 100)
                }

                img_full_set.setOnClickListener {
                    setWallpaper(img_full)
                }

                img_full_share.setOnClickListener {
                    shareImage()
                }
            }
        }

    }

    //share image
    private fun shareImage() {
        try {
            val intent = Intent(Intent.ACTION_SEND)
            intent.apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_SUBJECT, "Try this amazing wallpaper")
                putExtra(
                    Intent.EXTRA_TEXT,
                    "https://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID}"
                )
            }
            startActivity(Intent.createChooser(intent, "Share via"))
        } catch (e: Exception) {
            Toast.makeText(this, "Fail to share", Toast.LENGTH_SHORT).show()
            Log.d(TAG, "shareImage: ${e.message}")
        }
    }

    //save image in storage
    private fun saveImage(image: ImageView, name: String, quality: Int) {
        val drawable = image.drawable as BitmapDrawable
        val bitmap = drawable.bitmap
        val sdCard =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        val directory =
            File("${sdCard.absolutePath}/${resources.getString(R.string.app_name)}")
        directory.mkdir()
        val fileName = String.format("$name%d.png", System.currentTimeMillis())
        val finalFile = File(directory, fileName)
        Toast.makeText(
            this,
            "Image Saved",
            Toast.LENGTH_SHORT
        ).show()

        try {
            outputStream = FileOutputStream(finalFile)
            bitmap.compress(Bitmap.CompressFormat.PNG, quality, outputStream)
            outputStream!!.flush()
            outputStream!!.close()
            val intent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
            intent.data = Uri.fromFile(finalFile)
            sendBroadcast(intent)
        } catch (e: Exception) {
            Log.d(TAG, "saveClick: $e")
        }
    }

    //set wallpaper
    private fun setWallpaper(image: ImageView) {
        val drawable = image.drawable as BitmapDrawable
        val bitmap = drawable.bitmap
        val wallpaperManager = WallpaperManager.getInstance(applicationContext)

        wallpaperManager.setBitmap(bitmap)
        Toast.makeText(this, "Wallpaper set successfully", Toast.LENGTH_SHORT).show()
    }

}