package com.example.suportstudy.fragment.addNewsFeed

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.suportstudy.R
import com.example.suportstudy.extensions.onClick
import com.example.suportstudy.extensions.pop
import com.example.suportstudy.extensions.visible
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.fragment_add_news_feed.*
import java.io.ByteArrayOutputStream


class AddNewsFeedFragment : Fragment() {

    private var cameraPermission: Array<String>? = null
    private var storagePermission: Array<String>? = null
    var image_uri: Uri? = null

    private val CAMERA_REQUEST_CODE = 200
    private val STORAGE_REQUEST_CODE = 300
    private val IMAGE_PICK_GALLERY_CODE = 400
    private val IMAGE_PICK_CAMERA_CODE = 500

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        initViewModel here
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view =inflater.inflate(R.layout.fragment_add_news_feed, container, false)
        return view
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cameraPermission = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        storagePermission = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        //onlick, initView
        backIv.onClick {
            pop()
        }
        linearAnh.onClick {
            showImagePickDialog()
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun showImagePickDialog() {
        val bottomSheetDialog = activity?.let {
            BottomSheetDialog(
                it, R.style.BottomSheetDialogTheme
            )
        }
        val bottomSheetView: View = LayoutInflater.from(context).inflate(
            R.layout.dialog_choose_image,
            bottomSheetDialog!!.findViewById<View>(R.id.bottomSheetContainer) as LinearLayout?
        )
        val params = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        params.setMargins(50, 0, 50, 0)
        bottomSheetView.layoutParams = params
        bottomSheetDialog!!.setCancelable(false)
        bottomSheetView.findViewById<View>(R.id.cameraBtn).setOnClickListener {
            if (!checkCameraPermission()) {
                requestCameraPermission()
            } else {
                pickFromCamera()
            }
            bottomSheetDialog.dismiss()
        }
        bottomSheetView.findViewById<View>(R.id.galleryBtn).setOnClickListener {
            if (!checkStoragePermission()) {
                requestStoragetPermission()
            } else {
                pickFromGallery()
            }
            bottomSheetDialog.dismiss()
        }
        bottomSheetView.findViewById<View>(R.id.cancelBtn)
            .setOnClickListener { bottomSheetDialog.dismiss() }
        bottomSheetDialog.setContentView(bottomSheetView)
        bottomSheetDialog.show()
    }

    private fun checkStoragePermission(): Boolean {
        return activity?.let {
            ContextCompat.checkSelfPermission(
                it,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        } == PackageManager.PERMISSION_GRANTED
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private fun requestStoragetPermission() {
        storagePermission?.let {
            requestPermissions(
                it,
                STORAGE_REQUEST_CODE
            )
        }
    }

    private fun checkCameraPermission(): Boolean {
        val result = activity?.let {
            ContextCompat.checkSelfPermission(
                it,
                Manifest.permission.CAMERA
            )
        } == PackageManager.PERMISSION_GRANTED
        val result1 = activity?.let {
            ContextCompat.checkSelfPermission(
                it,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        } == PackageManager.PERMISSION_GRANTED
        return result && result1
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private fun requestCameraPermission() {
        cameraPermission?.let { requestPermissions(it, CAMERA_REQUEST_CODE) }
    }

    private fun pickFromGallery() {
        val galleryIntent = Intent(Intent.ACTION_PICK)
        galleryIntent.type = "image/*"
        startActivityForResult(
            galleryIntent,
            IMAGE_PICK_GALLERY_CODE
        )
    }

    private fun pickFromCamera() {
        val contentValues = ContentValues()
        contentValues.put(MediaStore.Images.Media.TITLE, "Temp Pick")
        contentValues.put(MediaStore.Images.Media.DESCRIPTION, "Temp Descr")
        image_uri = context?.contentResolver?.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues
        )
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri)
        startActivityForResult(intent, IMAGE_PICK_CAMERA_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            CAMERA_REQUEST_CODE -> {
                if (grantResults.size > 0) {
                    val cameraAccept = grantResults[0] == PackageManager.PERMISSION_GRANTED
                    val storageAccept = grantResults[1] == PackageManager.PERMISSION_GRANTED
                    if (cameraAccept && storageAccept) {
                        pickFromCamera()
                    } else {
                        Toast.makeText(
                            context,
                            R.string.Please_enable_camera_permissions,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
            STORAGE_REQUEST_CODE -> {
                if (grantResults.size > 0) {
                    val writeStorageAccpted = grantResults[0] == PackageManager.PERMISSION_GRANTED
                    if (writeStorageAccpted) {
                        pickFromGallery()
                    } else {
                        Toast.makeText(
                            context,
                            R.string.Please_enable_gallery_permissions,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == IMAGE_PICK_GALLERY_CODE) {
                image_uri = data!!.data
                pImageIv.setImageURI(image_uri)
                pImageIv.visible()
                Log.d("son", "uri $image_uri")
            }
            if (requestCode == IMAGE_PICK_CAMERA_CODE) {
                pImageIv.setImageURI(image_uri)
                pImageIv.visible()
                Log.d("son", "uri $image_uri")
            }

        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}