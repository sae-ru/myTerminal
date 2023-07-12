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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.myterminal.R
import com.example.myterminal.databinding.FragmentTakeFacePhotoBinding
import com.example.myterminal.model.ApiStatus
import com.example.myterminal.model.DocViewModel
import com.example.myterminal.model.toBase64
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * Fragment for taking a face photo and sending data to the ESM server
 */
class TakeFacePhotoFragment : Fragment() {

    // Binding object instance corresponding to the fragment_choose_action.xml layout
    // This property is non-null between the onCreateView() and onDestroyView() lifecycle callbacks,
    // when the view hierarchy is attached to the fragment.
    private var binding: FragmentTakeFacePhotoBinding? = null

    // Create a ViewModel the first time the fragment is created.
    // If the fragment is re-created, it receives the same GameViewModel instance created by the
    // first fragment.
    private val viewModel: DocViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val fragmentBinding = FragmentTakeFacePhotoBinding
            .inflate(inflater, container, false)
        binding = fragmentBinding
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Request camera permissions
        if (allPermissionsGranted()) {
            takeFacePhoto()
        } else {
            requestPermissions()
        }

        binding?.apply {
            retakeFacePhotoButton.setOnClickListener { takeFacePhoto() }
            saveFacePhotoButton.setOnClickListener { savePhoto() }
        }
    }

    /**
     * This fragment lifecycle method is called when the view hierarchy associated with the fragment
     * is being removed. As a result, clear out the binding object.
     */
    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    private lateinit var newFacePhotoUri: Uri
    private lateinit var newFacePhotoBitmap: Bitmap
    private var wasFacePhotoInstalled: Boolean = false

    private fun savePhoto() {
        if (wasFacePhotoInstalled) {
            viewModel.setPassportFacePhotoBase64String(
                viewModel.bitmapToByteArray(newFacePhotoBitmap).toBase64()
            )
            sendAllData()
        } else
            Toast.makeText(
                context,
                getString(R.string.empty_photo_toast_text),
                Toast.LENGTH_SHORT
            ).show()
    }

    private fun sendAllData() {
        //connect to ESM server and send all passport data
        viewModel.postAuthPassportData()

        viewModel.authPassportDataPostStatus.observe(viewLifecycleOwner) { postDataStatus ->
            when (postDataStatus) {
                ApiStatus.ERROR -> {
                    Toast.makeText(
                        context,
                        getString(R.string.postAuthPassportData_error_toast_text),
                        Toast.LENGTH_SHORT
                    ).show()
                    binding?.saveFacePhotoButton!!.apply {
                        text = getString(R.string.try_again_button_text)
                        isEnabled = true
                    }
                }
                ApiStatus.DONE -> {
                    binding?.saveFacePhotoButton!!.apply {
                        text = getString(R.string.continue_button_text)
                        isEnabled = true
                    }
//                    findNavController().navigate(R.id.action_takeFacePhotoFragment_to_endRegistrationFragment)
                }
                else -> {
                    binding?.saveFacePhotoButton!!.apply {
                        text = getString(R.string.data_processed_button_text)
                        isEnabled = false
                    }
                }
            }
        }
    }

    private fun takeFacePhoto() {
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

        wasFacePhotoInstalled = false

        newFacePhotoUri = requireContext().contentResolver
            .insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)!!

        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        resultLauncher.launch(cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, newFacePhotoUri))
    }

    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                try {
                    val bitmap = MediaStore.Images.Media
                        .getBitmap(requireActivity().contentResolver, newFacePhotoUri)
                    binding?.faceImageView?.setImageBitmap(bitmap)
                    newFacePhotoBitmap = bitmap
                    wasFacePhotoInstalled = true
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
                takeFacePhoto()
            }
        }
}