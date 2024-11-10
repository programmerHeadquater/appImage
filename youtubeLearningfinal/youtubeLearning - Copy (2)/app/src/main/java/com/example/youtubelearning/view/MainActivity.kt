package com.example.youtubelearning.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.youtubelearning.adapter.MyImageAdapter
import com.example.youtubelearning.databinding.ActivityMainBinding
import com.example.youtubelearning.room.MyImageDao
import com.example.youtubelearning.viewModel.MyImageViewModel
import com.example.youtubelearning.room.MyImagesDatabase
import androidx.activity.viewModels

class MainActivity : AppCompatActivity() {
    //this is test purpose
    private lateinit var db: MyImagesDatabase



    lateinit var mainBinding : ActivityMainBinding
    lateinit var myImageAdapter: MyImageAdapter
    private lateinit var myImageDao: MyImageDao
    private val myImagesViewModel: MyImageViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)



        mainBinding.recyclerview.layoutManager = LinearLayoutManager(this)
        myImageAdapter = MyImageAdapter()
        mainBinding.recyclerview.adapter = myImageAdapter

        myImagesViewModel.getAllImages().observe(this,{ images ->
            myImageAdapter.setImage(images)
        })

        mainBinding.add.setOnClickListener{
            val intent = Intent(this,AddImageActivity::class.java)
            startActivity(intent)
        }



    }



}