package com.example.youtubelearning.adapter

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.youtubelearning.databinding.ImageItemBinding
import com.example.youtubelearning.models.MyImages
import java.io.File
import android.util.Base64
import com.example.youtubelearning.room.MyImagesDatabase
import com.example.youtubelearning.view.DetailActivity
import com.example.youtubelearning.view.updateImageActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MyImageAdapter: RecyclerView.Adapter<MyImageAdapter.MyImageViewHolder>() {

    var imageList: MutableList<MyImages> = mutableListOf()

    fun setImage(images: List<MyImages>){
        this.imageList = images.toMutableList()
        notifyDataSetChanged()
    }


    class MyImageViewHolder(val itemBinding: ImageItemBinding) : RecyclerView.ViewHolder(itemBinding.root){

        fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyImageViewHolder {
            val view = ImageItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
            return MyImageViewHolder(view)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyImageViewHolder {
        val view = ImageItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MyImageViewHolder(view)
    }


    override fun getItemCount(): Int {
        return imageList.size
    }

    override fun onBindViewHolder(holder: MyImageViewHolder, position: Int) {
        var myImage = imageList[position]
        with(holder){
            val uri = Uri.fromFile(File(myImage.image))
            val imageId = myImage.imageId.toString()
            itemBinding.imageView.setImageURI(uri)
            itemBinding.textViewTitle.setText(myImage.imageTittle)


            itemBinding.textViewDescription.setText(myImage.imageDescription)

            itemBinding.Detail.setOnClickListener{
                val intent = Intent(itemBinding.root.context, DetailActivity::class.java)
                intent.putExtra("imageId", myImage.imageId.toString())
                intent.putExtra("imageTitle", myImage.imageTittle)
                intent.putExtra("description",myImage.imageDescription )
                intent.putExtra("date", myImage.date)
                intent.putExtra("location", myImage.loction)
                intent.putExtra("image", myImage.image)


                itemBinding.root.context.startActivity(intent)
            }
            itemBinding.update.setOnClickListener{
                val intent = Intent(itemBinding.root.context, updateImageActivity::class.java)
                intent.putExtra("imageId", myImage.imageId.toString())
                intent.putExtra("imageTitle", myImage.imageTittle)
                intent.putExtra("description",myImage.imageDescription )
                intent.putExtra("date", myImage.date)
                intent.putExtra("location", myImage.loction)
                intent.putExtra("image", myImage.image)
                itemBinding.root.context.startActivity(intent)
            }
            itemBinding.delete.setOnClickListener {

                val context = itemBinding.root.context
                val db = MyImagesDatabase.getDatabase(context)
                val imageDao = db.myImageDao()
                CoroutineScope(Dispatchers.IO).launch {
                    imageDao.deleteById(myImage.imageId)  // Directly delete by ID from the DB
                    imageList.removeAt(position)  // Remove from the list
                    withContext(Dispatchers.Main) {
                        notifyItemRemoved(position)  // Notify the adapter
                    }
                }

            }



        }
    }
    fun convertStringToBitMap(base64StringOfImage :String): Bitmap? {
        return try {
            val decodedByteArray = Base64.decode(base64StringOfImage, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.size)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
    fun convertToUri(imageString: String): Uri{
        return try {
            // Check if the imagePath is a valid URI string or a file path
            if (imageString.startsWith("content://") || imageString.startsWith("file://")) {
                // It's already a URI
                Uri.parse(imageString)
            } else {
                // It's a file path, so convert it to a URI
                Uri.fromFile(File(imageString))
            }
        } finally {
            //nothing

        }
    }
    fun updateData(newImages: List<MyImages>) {
        (newImages as MutableList).clear()
        newImages.addAll(newImages)
        notifyDataSetChanged()
    }
}