package com.rexoit.mywallpaper.ui

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.rexoit.mywallpaper.R
import com.rexoit.mywallpaper.adapter.ImageListAdapter
import com.rexoit.mywallpaper.model.WallpaperModel
import com.rexoit.mywallpaper.util.RecyclerViewItemClick
import kotlinx.android.synthetic.main.activity_image_list.*
import kotlinx.android.synthetic.main.image_list_row.view.*
import java.io.File
import java.io.FileOutputStream
import java.util.*
import kotlin.collections.ArrayList

private const val TAG = "imageListActivity"

class ImageListActivity : AppCompatActivity() {
    private lateinit var imageListAdapter: ImageListAdapter
    private lateinit var arrayList: ArrayList<WallpaperModel>
    private lateinit var filterList: ArrayList<WallpaperModel>
    private lateinit var name: String
    private lateinit var toolbar: Toolbar
    private var outputStream: FileOutputStream? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_list)

        name = intent.getStringExtra("category")!!

        toolbar = findViewById(R.id.toolbar_image_list)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = name
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(resources.getDrawable(R.drawable.ic_baseline_arrow_back_ios_24))

        Log.d(TAG, "onCreate: $name")
        arrayList = ArrayList()
        filterList = ArrayList()
        imageListAdapter = ImageListAdapter(this)
        setUpWallpaper(name)

        swipe_refresh_list.setOnRefreshListener {
            arrayList.clear()
            setUpWallpaper(name)
            swipe_refresh_list.isRefreshing = false
        }

        //recycler view item click
        imageListAdapter.onClick(object : RecyclerViewItemClick {
            override fun onItemClick(position: Int, view: View) {
                val intent = Intent(this@ImageListActivity, ImageDetailsActivity::class.java)
                intent.apply {
                    putExtra("name", arrayList[position].imageName)
                    putExtra("image", arrayList[position].imageLink)
                }
                startActivity(intent)
            }

            //save image in storage
            override fun saveClick(position: Int, view: View) {
                val drawable = view.sample_image_list.drawable as BitmapDrawable
                val bitmap = drawable.bitmap
                val sdCard =
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                val directory =
                    File("${sdCard.absolutePath}/${resources.getString(R.string.app_name)}")
                directory.mkdir()
                val fileName = String.format(
                    "${filterList[position].imageName}.png",
                    System.currentTimeMillis()
                )
                val finalFile = File(directory, fileName)
                Toast.makeText(
                    this@ImageListActivity,
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
                    sendBroadcast(intent)
                } catch (e: Exception) {
                    Log.d(TAG, "saveClick: $e")
                }
            }

        })
        //edit text search view
        search_image_list.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                filterData(s.toString())
            }
        })

        image_list_rec.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@ImageListActivity)
            adapter = imageListAdapter
        }

    }

    private fun setUpWallpaper(name: String) {

        when (name) {
            "Islamic" -> islamicWallpaper()
            "Galaxy" -> galaxyWallpaper()
            "Ocean" -> oceanWallpaper()
            "Nature" -> natureWallpaper()
            "Flower" -> flowerWallpaper()
            "Sky" -> skyWallpaper()
            "Cars" -> carsWallpaper()
            "Hill" -> hillWallpaper()
            "Others" -> otherWallpaper()
        }

    }

    private fun islamicWallpaper() {
        val firestore = Firebase.firestore
        firestore.collection("islamic")
            .get()
            .addOnSuccessListener { data ->
                val list: List<DocumentSnapshot> = data.documents
                list.forEach {
                    val wallpaperModel = it.toObject(WallpaperModel::class.java)
                    arrayList.add(wallpaperModel!!)
                    filterList.add(wallpaperModel)
                    shimmer_list.stopShimmer()
                    shimmer_list.setShimmer(null)
                    shimmer_list.visibility = View.INVISIBLE
                }
                imageListAdapter.submitList(arrayList)
                imageListAdapter.notifyDataSetChanged()
            }.addOnFailureListener { e ->
                Log.d(TAG, "islamicWallpaper: ${e.message}")
            }
    }

    private fun galaxyWallpaper() {
        val firestore = Firebase.firestore
        firestore.collection("galaxy")
            .get()
            .addOnSuccessListener { data ->
                val list: List<DocumentSnapshot> = data.documents
                list.forEach {
                    val wallpaperModel = it.toObject(WallpaperModel::class.java)
                    arrayList.add(wallpaperModel!!)
                    filterList.add(wallpaperModel)
                    shimmer_list.stopShimmer()
                    shimmer_list.setShimmer(null)
                    shimmer_list.visibility = View.INVISIBLE
                }
                imageListAdapter.submitList(arrayList)
                imageListAdapter.notifyDataSetChanged()
            }.addOnFailureListener { e ->
                Log.d(TAG, "islamicWallpaper: ${e.message}")
            }
    }

    private fun natureWallpaper() {
        val firestore = Firebase.firestore
        firestore.collection("nature")
            .get()
            .addOnSuccessListener { data ->
                val list: List<DocumentSnapshot> = data.documents
                list.forEach {
                    val wallpaperModel = it.toObject(WallpaperModel::class.java)
                    arrayList.add(wallpaperModel!!)
                    filterList.add(wallpaperModel)
                    shimmer_list.stopShimmer()
                    shimmer_list.setShimmer(null)
                    shimmer_list.visibility = View.INVISIBLE
                }
                imageListAdapter.submitList(arrayList)
                imageListAdapter.notifyDataSetChanged()
            }.addOnFailureListener { e ->
                Log.d(TAG, "natureWallpaper: ${e.message}")
            }
    }

    private fun oceanWallpaper() {
        val firestore = Firebase.firestore
        firestore.collection("ocean")
            .get()
            .addOnSuccessListener { data ->
                val list: List<DocumentSnapshot> = data.documents
                list.forEach {
                    val wallpaperModel = it.toObject(WallpaperModel::class.java)
                    arrayList.add(wallpaperModel!!)
                    filterList.add(wallpaperModel)
                    shimmer_list.stopShimmer()
                    shimmer_list.setShimmer(null)
                    shimmer_list.visibility = View.INVISIBLE
                }
                imageListAdapter.submitList(arrayList)
                imageListAdapter.notifyDataSetChanged()
            }.addOnFailureListener { e ->
                Log.d(TAG, "islamicWallpaper: ${e.message}")
            }
    }

    private fun flowerWallpaper() {
        val firestore = Firebase.firestore
        firestore.collection("flower")
            .get()
            .addOnSuccessListener { data ->
                val list: List<DocumentSnapshot> = data.documents
                list.forEach {
                    val wallpaperModel = it.toObject(WallpaperModel::class.java)
                    arrayList.add(wallpaperModel!!)
                    filterList.add(wallpaperModel)
                    shimmer_list.stopShimmer()
                    shimmer_list.setShimmer(null)
                    shimmer_list.visibility = View.INVISIBLE
                }
                imageListAdapter.submitList(arrayList)
                imageListAdapter.notifyDataSetChanged()
            }.addOnFailureListener { e ->
                Log.d(TAG, "flowerWallpaper: ${e.message}")
            }
    }

    private fun skyWallpaper() {
        val firestore = Firebase.firestore
        firestore.collection("sky")
            .get()
            .addOnSuccessListener { data ->
                val list: List<DocumentSnapshot> = data.documents
                list.forEach {
                    val wallpaperModel = it.toObject(WallpaperModel::class.java)
                    arrayList.add(wallpaperModel!!)
                    filterList.add(wallpaperModel)
                    shimmer_list.stopShimmer()
                    shimmer_list.setShimmer(null)
                    shimmer_list.visibility = View.INVISIBLE
                }
                imageListAdapter.submitList(arrayList)
                imageListAdapter.notifyDataSetChanged()
            }.addOnFailureListener { e ->
                Log.d(TAG, "skyWallpaper: ${e.message}")
            }
    }

    private fun carsWallpaper() {
        val firestore = Firebase.firestore
        firestore.collection("car")
            .get()
            .addOnSuccessListener { data ->
                val list: List<DocumentSnapshot> = data.documents
                list.forEach {
                    val wallpaperModel = it.toObject(WallpaperModel::class.java)
                    arrayList.add(wallpaperModel!!)
                    filterList.add(wallpaperModel)
                    shimmer_list.stopShimmer()
                    shimmer_list.setShimmer(null)
                    shimmer_list.visibility = View.INVISIBLE
                }
                imageListAdapter.submitList(arrayList)
                imageListAdapter.notifyDataSetChanged()
            }.addOnFailureListener { e ->
                Log.d(TAG, "carsWallpaper: ${e.message}")
            }
    }

    private fun hillWallpaper() {
        val firestore = Firebase.firestore
        firestore.collection("hill")
            .get()
            .addOnSuccessListener { data ->
                val list: List<DocumentSnapshot> = data.documents
                list.forEach {
                    val wallpaperModel = it.toObject(WallpaperModel::class.java)
                    arrayList.add(wallpaperModel!!)
                    filterList.add(wallpaperModel)
                    shimmer_list.stopShimmer()
                    shimmer_list.setShimmer(null)
                    shimmer_list.visibility = View.INVISIBLE
                }
                imageListAdapter.submitList(arrayList)
                imageListAdapter.notifyDataSetChanged()
            }.addOnFailureListener { e ->
                Log.d(TAG, "hillWallpaper: ${e.message}")
            }
    }

    private fun otherWallpaper() {
        val firestore = Firebase.firestore
        firestore.collection("others")
            .get()
            .addOnSuccessListener { data ->
                val list: List<DocumentSnapshot> = data.documents
                list.forEach {
                    val wallpaperModel = it.toObject(WallpaperModel::class.java)
                    arrayList.add(wallpaperModel!!)
                    filterList.add(wallpaperModel)
                    shimmer_list.stopShimmer()
                    shimmer_list.setShimmer(null)
                    shimmer_list.visibility = View.INVISIBLE
                }
                imageListAdapter.submitList(arrayList)
                imageListAdapter.notifyDataSetChanged()
            }.addOnFailureListener { e ->
                Log.d(TAG, "otherWallpaper: ${e.message}")
            }
    }

    //filter data for search
    private fun filterData(text: String) {
        arrayList.clear()
        val searchText = text!!.toLowerCase(Locale.getDefault())
        if (searchText.isNotEmpty()) {
            filterList.forEach {
                if (it.imageName!!.toLowerCase(Locale.getDefault()).contains(searchText)) {
                    arrayList.add(it)
                }
            }
            imageListAdapter.notifyDataSetChanged()
        } else {
            arrayList.clear()
            arrayList.addAll(filterList)
            imageListAdapter.notifyDataSetChanged()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                super.onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }

}