package com.airatlovesmusic.kompressorsample

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.airatlovesmusic.kompressor.ImageCompressor
import kotlinx.android.synthetic.main.activity_main.*
import java.io.ByteArrayOutputStream
import java.util.*

/**
 * Created by Airat Khalilov on 09/05/2020.
 */

class MainActivity: AppCompatActivity() {

    private val REQUEST_CAMERA_PERM = 1
    private val REQUEST_GALLERY_PERM = 2
    private val REQUEST_IMAGE_CAPTURE = 3
    private val REQUEST_GALLERY_PHOTO = 4

    private val imageCompressor = ImageCompressor()
    private var targetUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setOnClickListeners()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
            when (requestCode) {
                REQUEST_CAMERA_PERM -> dispatchTakePictureIntent()
                REQUEST_GALLERY_PERM -> dispatchGalleryIntent()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_CAPTURE && targetUri != null) {
                targetUri?.let { showImage(it) }
            } else if (requestCode == REQUEST_GALLERY_PHOTO && data != null) {
                data.data?.let { showImage(it) }
            }
        }
    }

    private fun showImage(contentUri: Uri) {
        val baos = ByteArrayOutputStream()
        imageCompressor.compress(
            inputStream = contentResolver.openInputStream(contentUri)!!,
            outputStream = baos,
            format = Bitmap.CompressFormat.JPEG,
            quality = 80
        )
        val byteArray = baos.toByteArray()
        iv_image.setImageBitmap(
            BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
        )
    }

    private fun setOnClickListeners() {
        btn_camera.setOnClickListener(::handleCameraClick)
        btn_gallery.setOnClickListener(::handleGalleryClick)
    }

    // Camera region start
    private fun handleCameraClick(v: View) {
        if (
            ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
        ) { dispatchTakePictureIntent() }
        else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_CAMERA_PERM)
        }
    }

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                targetUri = contentResolver.insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    ContentValues().apply {
                        put(MediaStore.Images.Media.TITLE, "${UUID.randomUUID()}.jpg")
                    }
                )
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, targetUri)
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            } ?: finish()
        }
    }
    // Camera region end

    // Gallery region start
    private fun handleGalleryClick(v: View) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            dispatchGalleryIntent()
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_GALLERY_PERM)
        }
    }

    private fun dispatchGalleryIntent() {
        with(Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)) {
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }.apply { startActivityForResult(this, REQUEST_GALLERY_PHOTO) }
    }
    // Gallery region end

}