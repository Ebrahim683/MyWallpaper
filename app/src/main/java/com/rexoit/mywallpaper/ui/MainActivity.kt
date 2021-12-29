package com.rexoit.mywallpaper.ui

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.GridLayoutManager
import com.afdhal_fa.imageslider.model.SlideUIModel
import com.denzcoskun.imageslider.interfaces.ItemClickListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.rexoit.mywallpaper.R
import com.rexoit.mywallpaper.adapter.MainRecyclerViewAdapter
import com.rexoit.mywallpaper.model.WallpaperModel
import com.rexoit.mywallpaper.util.RecyclerViewItemClick
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.image_list_row.*
import kotlinx.coroutines.*
import java.util.*
import kotlin.collections.ArrayList

private const val TAG = "mainActivity"

class MainActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var arrayList: ArrayList<WallpaperModel>
    private lateinit var filterList: ArrayList<WallpaperModel>
    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    private lateinit var mainRecyclerViewAdapter: MainRecyclerViewAdapter
    private lateinit var firestore: FirebaseFirestore
    private lateinit var sliderList: ArrayList<SlideUIModel>
    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar = findViewById(R.id.toolbar_main)
        setSupportActionBar(toolbar)
        supportActionBar?.title = ""
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        drawerLayout = findViewById(R.id.drawer_layout)
        actionBarDrawerToggle =
            ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        nav_id.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.m_setting -> {
                    Toast.makeText(this, "Setting", Toast.LENGTH_SHORT).show()
                }
                R.id.m_email->{
                    Toast.makeText(this, "Email", Toast.LENGTH_SHORT).show()
                }
                R.id.m_terms -> {
                    Toast.makeText(this, "Terms and condition", Toast.LENGTH_SHORT).show()
                }
                R.id.m_privacy -> {
                    Toast.makeText(this, "Privacy policy", Toast.LENGTH_SHORT).show()
                }
            }
            drawerLayout.closeDrawers()
            true
        }

        runTimePermission()
        socialMedia()
        imageSlider()

        firestore = FirebaseFirestore.getInstance()
        mainRecyclerViewAdapter = MainRecyclerViewAdapter()
        mainRecyclerViewAdapter.submitList(loadImages())

        //recyclerview item click
        mainRecyclerViewAdapter.onRecItemClick(object : RecyclerViewItemClick {
            override fun onItemClick(position: Int, view: View) {
                val intent = Intent(this@MainActivity, ImageListActivity::class.java)
                when (position) {
                    0 -> {
//                        val islamicBundle = Bundle()
//                        islamicBundle.putParcelableArrayList("islamic_wallpaper", islamicImage())
                        intent.apply {
                            putExtra("category", "Islamic")
//                            putExtras(islamicBundle)
                        }
                        startActivity(intent)
                    }
                    1 -> {
                        intent.apply {
                            putExtra("category", "Galaxy")
                        }
                        startActivity(intent)
                    }
                    2 -> {
                        intent.apply {
                            putExtra("category", "Ocean")
                        }
                        startActivity(intent)
                    }
                    3 -> {
                        intent.apply {
                            putExtra("category", "Nature")
                        }
                        startActivity(intent)
                    }
                    4 -> {
                        intent.apply {
                            putExtra("category", "Flower")
                        }
                        startActivity(intent)
                    }
                    5 -> {
                        intent.apply {
                            putExtra("category", "Sky")
                        }
                        startActivity(intent)
                    }
                    6 -> {
                        intent.apply {
                            putExtra("category", "Cars")
                        }
                        startActivity(intent)
                    }
                    7 -> {
                        intent.apply {
                            putExtra("category", "Hill")
                        }
                        startActivity(intent)
                    }
                    8 -> {
                        intent.apply {
                            putExtra("category", "Others")
                        }
                        startActivity(intent)
                    }
                }
            }

            override fun saveClick(position: Int, view: View) {}
        })
        rec_main.apply {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(this@MainActivity, 3)
            adapter = mainRecyclerViewAdapter
        }

        //edit text search view
        search_main.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                filter(s.toString())
            }
        })

        swipe_refresh_id.setOnRefreshListener {
            imageSlider()
            swipe_refresh_id.isRefreshing = false
        }

    }

    //filter data for search
    @SuppressLint("NotifyDataSetChanged")
    private fun filter(text: String) {
        filterList.clear()
        val searchText = text.lowercase(Locale.getDefault())
        if (searchText.isNotEmpty()) {
            arrayList.forEach {
                if (it.imageName!!.lowercase(Locale.getDefault()).contains(searchText)) {
                    filterList.add(it)
                }
            }
            mainRecyclerViewAdapter.notifyDataSetChanged()
        } else {
            filterList.clear()
            filterList.addAll(arrayList)
            mainRecyclerViewAdapter.notifyDataSetChanged()
        }
    }

    //grid list
    private fun loadImages(): ArrayList<WallpaperModel> {
        arrayList = ArrayList()
        filterList = ArrayList()

        arrayList.add(
            WallpaperModel(
                imageName = "Islamic",
                imageLink = R.drawable.islam.toString()
            )
        )

        arrayList.add(
            WallpaperModel(
                imageName = "Galaxy",
                imageLink = R.drawable.galaxy.toString()
            )
        )

        arrayList.add(
            WallpaperModel(
                imageName = "Ocean",
                imageLink = R.drawable.ocean.toString()
            )
        )

        arrayList.add(
            WallpaperModel(
                imageName = "Nature",
                imageLink = R.drawable.nature.toString()
            )
        )

        arrayList.add(
            WallpaperModel(
                imageName = "Flower",
                imageLink = R.drawable.flower.toString()
            )
        )

        arrayList.add(
            WallpaperModel(
                imageName = "Sky",
                imageLink = R.drawable.sky.toString()
            )
        )

        arrayList.add(
            WallpaperModel(
                imageName = "Cars",
                imageLink = R.drawable.car.toString()
            )
        )

        arrayList.add(
            WallpaperModel(
                imageName = "Hill",
                imageLink = R.drawable.hill.toString()
            )
        )

        arrayList.add(
            WallpaperModel(
                imageName = "Others",
                imageLink = R.drawable.others.toString()
            )
        )

        filterList.addAll(arrayList)
        return filterList
    }

    // social media
    private fun socialMedia() {
        facebook.setOnClickListener {
            val companyFB = "https://www.facebook.com/rexoit"
            val intent =
                Intent(Intent.ACTION_VIEW, Uri.parse("fb://facewebmodal/f?href=$companyFB"))
            startActivity(intent)
        }

        whatsapp.setOnClickListener {
            val number = "0174989506"
            val intent =
                Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/send?phone=$number"))
            startActivity(intent)
        }

        twitter.setOnClickListener {
            val twUserName = "ebrahim683"
            try {
                val intent =
                    Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?screen_name=$twUserName"))
                startActivity(intent)
            } catch (e: Exception) {
                val intent =
                    Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/#!/$twUserName"))
                startActivity(intent)
                Log.d(TAG, "socialMedia: ${e.message}")
            }
        }

        instagram.setOnClickListener {
            val name = "ebrahim683"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("http://instagram.com/_u/$name"))
            intent.setPackage("com.instagram.android")
            try {
                startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("http://instagram.com/$name")
                    )
                )
            }
        }

        youtube.setOnClickListener {
            val channelName = "Ummah Network"
            val intent = Intent(Intent.ACTION_VIEW)
            try {
                intent.data = Uri.parse("http://www.youtube.com/$channelName")
                startActivity(intent)
            } catch (e: Exception) {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("http://www.youtube.com/$channelName")
                    )
                )
            }
        }

        website.setOnClickListener {
            val url = "https://www.rexoit.com/"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        }
    }

    private fun imageSlider() {
        sliderList = ArrayList()
        sliderList.add(SlideUIModel(R.drawable.islamic_music.toString(), "Islamic Music"))
        image_slider.setImageList(sliderList)
        val firebaseDatabase = FirebaseDatabase.getInstance()
        firebaseDatabase.reference.child("Slider")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (dataSnapshot in snapshot.children) {
                            sliderList.add(
                                SlideUIModel(
                                    dataSnapshot.child("imageUrl").value.toString(),
                                    dataSnapshot.child("title").value.toString()
                                )
                            )
                            image_slider.setImageList(sliderList)

                            image_slider.setItemClickListener(object : ItemClickListener,
                                com.afdhal_fa.imageslider.`interface`.ItemClickListener {
                                override fun onItemClick(model: SlideUIModel, position: Int) {
                                    val intent =
                                        Intent(this@MainActivity, ImageDetailsActivity::class.java)
                                    intent.apply {
                                        putExtra("name", "SLIDER_IMAGE")
                                        putExtra("sliderImage", sliderList[position].imageUrl)
                                        putExtra("sliderImageTitle", sliderList[position].title)
                                    }
                                    startActivity(intent)
                                }

                                override fun onItemSelected(position: Int) {}

                            })
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d(TAG, "onCancelled: ${error.message}")
                }
            })
    }

    private fun runTimePermission() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED -> {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    1
                )
            }
            ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED -> {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                    2
                )
            }
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

}