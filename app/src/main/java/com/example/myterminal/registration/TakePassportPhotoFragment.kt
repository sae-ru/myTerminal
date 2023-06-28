package com.example.myterminal.registration

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.myterminal.R
import com.example.myterminal.databinding.FragmentTakePassportPhotoBinding
import com.example.myterminal.model.DocViewModel
import java.text.SimpleDateFormat
import java.util.Locale

class TakePassportPhotoFragment : Fragment() {

    // Binding object instance corresponding to the fragment_choose_action.xml layout
    // This property is non-null between the onCreateView() and onDestroyView() lifecycle callbacks,
    // when the view hierarchy is attached to the fragment.
    private var binding: FragmentTakePassportPhotoBinding? = null

    // Create a ViewModel the first time the fragment is created.
    // If the fragment is re-created, it receives the same GameViewModel instance created by the
    // first fragment.
    private val viewModel: DocViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentBinding = FragmentTakePassportPhotoBinding
            .inflate(inflater, container, false)
        binding = fragmentBinding
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Request camera permissions
        if (allPermissionsGranted()) {
            takePassportPhoto()
        } else {
            requestPermissions()
        }

        binding?.retakePassportPhotoButton?.setOnClickListener { takePassportPhoto() }
        binding?.savePassportPhotoButton?.setOnClickListener { saveData() }
    }

    private lateinit var newPassportImageUri: Uri
    private lateinit var newPassportImageBitmap: Bitmap
    private var wasPassportImageInstalled: Boolean = false

    //TODO: delete empty images
//    private var currentImageUri: Uri? = null
//    private var wasCameraOpened: Boolean = false
//    private var wasOnPauseCalled: Boolean = false

    override fun onPause() {
        super.onPause()
//        wasOnPauseCalled = true
        Log.d("MYTAG", "called onPause()")
    }

    override fun onResume() {
        super.onResume()
        Log.d("MYTAG", "called onResume()")
//        deleteEmptyPhoto()
//        wasOnPauseCalled = false
//        wasCameraOpened = false
    }

    /**
     * This fragment lifecycle method is called when the view hierarchy associated with the fragment
     * is being removed. As a result, clear out the binding object.
     */
    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    /*private fun deleteEmptyPhoto() {
        Log.d("MYTAG", "wasCameraOpened: $wasCameraOpened")
        Log.d("MYTAG", "wasOnPauseCalled: $wasOnPauseCalled")
        if (!wasResultLauncherCalled && wasCameraOpened && wasOnPauseCalled) {
            Log.d("MYTAG", "currentImageUri: $currentImageUri")
            Log.d("MYTAG", "newImageUri: $newImageUri")
            val deletePath = getFilePath(newImageUri)
            Log.d("MYTAG", "getFilePath(imageUri): ${getFilePath(newImageUri)}")
            if (deletePath != null) {
                Log.d("MYTAG", "deletePath != null")
                val fileDelete = File(deletePath)
                Log.d("MYTAG", "fileDelete: $fileDelete")

//                if (fileDelete.exists())

//                    Log.d("MYTAG", "fileDelete.exists()")
                    if (fileDelete.delete()) {
                        Log.d("MYTAG", "File Deleted")
                        Toast.makeText(
                            context,
                            "File Deleted",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Log.d("MYTAG", "File not Deleted")
                        Toast.makeText(
                            context,
                            "File not Deleted",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

            }
        }
    }*/

    /*//getting real path from uri
    private fun getFilePath(uri: Uri): String? {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor: Cursor? = requireActivity().contentResolver.query(uri, projection, null, null, null)
        if (cursor != null) {
            cursor.moveToFirst()
            val columnIndex = cursor.getColumnIndex(projection[0])
            val picturePath = cursor.getString(columnIndex) // returns null
            cursor.close()
            return picturePath
        }
        return null
    }*/

    private fun saveData() {
        if (wasPassportImageInstalled) {
            viewModel.setPassportImageByteArray(viewModel.bitmapToByteArray(newPassportImageBitmap))

            //TODO: connect to internet and send photo
            //viewModel.postOCRData()

            findNavController().navigate(R.id.action_takePassportPhotoFragment_to_fillDataFragment)
        } else
            Toast.makeText(
                context,
                getString(R.string.empty_photo_toast_text),
                Toast.LENGTH_SHORT
            ).show()
    }

    private fun takePassportPhoto() {
        Log.d("MYTAG", "called takePhoto()")
        // Create time stamped name and MediaStore entry.
        val name = SimpleDateFormat(FILENAME_FORMAT, Locale.US)
            .format(System.currentTimeMillis())
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, name)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/CameraX-Image")
            }
        }

        newPassportImageUri = requireContext().contentResolver
            .insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)!!

//        wasCameraOpened = true
        wasPassportImageInstalled = false

        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        resultLauncher.launch(cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, newPassportImageUri))
    }

    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                try {
                    val bitmap = MediaStore.Images.Media
                        .getBitmap(requireActivity().contentResolver, newPassportImageUri)
                    newPassportImageBitmap = bitmap
                    Log.d("MYTAG", "called resultLauncher")
                    binding?.passportImageView?.setImageBitmap(bitmap)
                    wasPassportImageInstalled = true
//                    currentImageUri = newPassportImageUri
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

    private fun requestPermissions() {
        activityResultLauncher.launch(REQUIRED_PERMISSIONS)
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(requireContext(), it) == PackageManager.PERMISSION_GRANTED
    }

    companion object {
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private val REQUIRED_PERMISSIONS =
            mutableListOf(
                android.Manifest.permission.CAMERA
            ).apply {
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                    add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
            }.toTypedArray()
    }

    private val activityResultLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        )
        { permissions ->
            // Handle Permission granted/rejected
            var permissionGranted = true
            permissions.entries.forEach {
                if (it.key in REQUIRED_PERMISSIONS && !it.value)
                    permissionGranted = false
            }
            if (!permissionGranted) {
                Toast.makeText(
                    context,
                    "Permission request denied",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                takePassportPhoto()
            }
        }

}