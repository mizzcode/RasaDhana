package com.rasadhana.ui.photo

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.navigation.fragment.findNavController
import com.rasadhana.ui.main.MainActivity
import com.rasadhana.R
import com.rasadhana.databinding.FragmentPhotoBinding
import com.rasadhana.getImageUri
import com.rasadhana.reduceFileImage
import com.rasadhana.uriToFile
import org.koin.android.ext.android.inject
import com.rasadhana.data.Result
import com.rasadhana.ui.photo.CameraActivity.Companion.CAMERAX_RESULT

class PhotoFragment : Fragment() {
    private var _binding: FragmentPhotoBinding? = null
    private val binding get() = _binding!!

    private val photoViewModel: PhotoViewModel by inject()

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                showToast("Permission request granted")
            } else {
                showToast("Permission request denied")
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentPhotoBinding.inflate(inflater, container, false)
        val root: View = binding.root

        (requireActivity() as MainActivity).setBottomNavVisibility(false)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        }

        (activity as AppCompatActivity).supportActionBar?.apply {
            title = getString(R.string.photo_ingredient)
            setDisplayHomeAsUpEnabled(true)
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            findNavController().navigateUp()
        }

        binding.galleryButton.setOnClickListener { startGallery() }
        binding.cameraButton.setOnClickListener { startCameraX() }
//        binding.uploadButton.setOnClickListener { uploadImage() }
    }

    private fun startCameraX() {
        val intent = Intent(requireActivity(), CameraActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERAX_RESULT) {
            photoViewModel.currentImageUri = it.data?.getStringExtra(CameraActivity.EXTRA_CAMERAX_IMAGE)?.toUri()
            showImage()
        }
    }

//    private fun uploadImage() {
//        photoViewModel.currentImageUri?.let { uri ->
//            val imageFile = uriToFile(uri, requireContext()).reduceFileImage()
//            Log.d("Image File", "showImage: ${imageFile.path}")
//
//            photoViewModel.getSession().observe(viewLifecycleOwner) { user ->
//                Log.d("usesr", "$user")
//                    photoViewModel.uploadImage(imageFile, user.id).observe(viewLifecycleOwner) { result ->
//                        if (result != null) {
//                            when (result) {
//                                is Result.Error -> {
//                                    showLoading(false)
//                                    showToast(result.error)
//                                }
//                                Result.Loading -> {
//                                    showLoading(true)
//                                }
//                                is Result.Success -> {
//                                    showLoading(false)
//
//                                    if (result.data.success) {
//                                        val response = result.data
//                                        showToast(response.message)
//                                        Log.d("PhotoFragment", "Response: ${response.photoUrl}")
//                                    } else {
//                                        showToast(result.data.message)
//                                    }
//                                }
//                            }
//                        }
//                    }
//            }
//        } ?: showToast(getString(R.string.empty_image_warning))
//    }


    private fun allPermissionsGranted() =
        ContextCompat.checkSelfPermission(
            requireContext(),
            REQUIRED_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            photoViewModel.currentImageUri = uri
            showImage()
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private fun showImage() {
        photoViewModel.currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.previewImageView.setImageURI(it)
        }
    }

    private fun startCamera() {
        Log.d("Photo Fragment", photoViewModel.currentImageUri.toString())
        photoViewModel.currentImageUri = getImageUri(requireContext())
        launcherIntentCamera.launch(photoViewModel.currentImageUri!!)
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            showImage()
        } else {
            photoViewModel.currentImageUri = null
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        (requireActivity() as MainActivity).setBottomNavVisibility(true)
    }
}