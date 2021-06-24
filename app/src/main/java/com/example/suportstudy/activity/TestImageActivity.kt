package com.example.suportstudy.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.suportstudy.R
import com.example.suportstudy.until.ApiService
import com.example.suportstudy.until.Constrain
import com.example.suportstudy.until.Persmission
import kotlinx.android.synthetic.main.activity_test_image.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File


class TestImageActivity : AppCompatActivity() {
    var context = this@TestImageActivity
    var apiService: ApiService? = null

    private val STORAGE_REQUEST_CODE = 100
    private val IMAGE_PICK_GALLERY_CODE = 200
    var image_uri: Uri? = null
    var part_image: String? = null
    var storagePermission: Array<String>? = null
    var bitmap:Bitmap?=null

    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_image)

        storagePermission = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        apiService=Constrain.createRetrofit2(ApiService::class.java)

        btnLoad.setOnClickListener {
            if (!Persmission.checkStoragePermission(context)) {
                Persmission.requestStoragetPermission(context)
            } else {
                Persmission.pickFromGallery(context)
            }
        }
        loadserver.setOnClickListener {
               loadimgae()
        }

    }
    fun loadimgae(){
        var file=File(part_image)
        val reqFile: RequestBody = RequestBody.create(MediaType.parse("image/*"), file)
        val body = MultipartBody.Part.createFormData("myFile", file.getName(), reqFile)

        val req: Call<ResponseBody> = apiService!!.postImage(body)
        req.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {

                if (response.code() == 200) {
                    Constrain.showToast(context,"Thành công  " + response.toString())
                }
                Toast.makeText(
                    applicationContext,
                    response.code().toString() + " ",
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Constrain.showToast(context,"Thất bại")

                t.printStackTrace()
                Log.e("ERROR", t.toString())
            }
        })


    }

    private fun checkStoragePermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this@TestImageActivity,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private fun requestStoragetPermission() {
        requestPermissions(
            storagePermission!!,
            Constrain.STORAGE_REQUEST_CODE
        )
    }

    private fun pickFromGallery() {
        val galleryIntent = Intent(Intent.ACTION_PICK)
        galleryIntent.type = "image/*"
        startActivityForResult(
            galleryIntent,
            IMAGE_PICK_GALLERY_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            Constrain.STORAGE_REQUEST_CODE -> {
                if (grantResults.size > 0) {
                    val writeStorageAccpted = grantResults[0] == PackageManager.PERMISSION_GRANTED
                    if (writeStorageAccpted) {
                        pickFromGallery()
                    } else {
                        Constrain.showToast(context, "Bật quyen thu vien")
                    }
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == RESULT_OK) {
            if (requestCode ==IMAGE_PICK_GALLERY_CODE) {
                val pickedImage: Uri = data!!.data!!
                img.setImageURI(pickedImage)
               part_image=  getRealPathFromURI(pickedImage)
                Constrain.showToast(context, part_image.toString())
            }



        }
        super.onActivityResult(requestCode, resultCode, data)
    }
    fun getRealPathFromURI(contentUri: Uri?): String? {

        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val cursor: Cursor = managedQuery(
            contentUri,
            proj,  // Which columns to return
            null,  // WHERE clause; which rows to return (all rows)
            null,  // WHERE clause selection arguments (none)
            null
        ) // Order-by clause (ascending by name)
        val column_index: Int = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        return cursor.getString(column_index)
    }
}