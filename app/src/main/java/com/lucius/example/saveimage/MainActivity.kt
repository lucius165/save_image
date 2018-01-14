package com.lucius.example.saveimage

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.hoangtp165.quickcall.Helper
import java.io.File
import java.io.FileOutputStream

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (Helper().requestPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE, {
            Toast.makeText(this, "haloo", Toast.LENGTH_SHORT).show()
        }, 11)) {
            val intent = Intent()

            intent.action = Intent.ACTION_GET_CONTENT
            intent.type = "image/*"
            startActivityForResult(intent, 10)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 10 && resultCode == Activity.RESULT_OK) {
            if (data == null) return
            Log.e("log", data.data.toString())
            val stream = contentResolver.openInputStream(data.data)
            val bitmap = BitmapFactory.decodeStream(stream)
            val file = createImageFile()
            val outStream = FileOutputStream(file)

            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream)

            Log.e("log", "done 1")

            val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
            val contentUri = Uri.fromFile(File(data.data.path))
            mediaScanIntent.data = contentUri
            sendBroadcast(mediaScanIntent)
            MediaScannerConnection.scanFile(this, arrayOf(file.path), arrayOf("image/jpeg"), null)
            Log.e("log", "done 2")
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            11 -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    val intent = Intent()

                    intent.action = Intent.ACTION_GET_CONTENT
                    intent.type = "image/*"
                    startActivityForResult(intent, 10)

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                }
                return
            }
            else -> {
                // Ignore all other requests.
            }
        }
    }

    fun createImageFile(): File {
        // Create an image file name
        val storageDir = File(Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString() + "/Camera/")
        if (!storageDir.exists())
            storageDir.mkdirs()
        return File.createTempFile(
                "haloo", /* prefix */
                ".jpeg", /* suffix */
                storageDir                   /* directory */
        )
    }
}
