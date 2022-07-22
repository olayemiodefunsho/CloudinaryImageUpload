package com.example.imagefromcameragallery

import android.Manifest
import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.example.imagefromcameragallery.databinding.FragmentFirstBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null
    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>
    private lateinit var imageCropperLauncher: ActivityResultLauncher<CropImageContractOptions>

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)

        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { responseMap ->
            if(responseMap[Manifest.permission.CAMERA] == true && responseMap[Manifest.permission.READ_EXTERNAL_STORAGE] == true) {
                imageCropperLauncher.launch(CropImageContractOptions(null, CropImageOptions()))
            }
            else {
                showMessageDialog("No enough permissions to upload the image")
            }
        }

        imageCropperLauncher = registerForActivityResult(CropImageContract()) {
            if(it.isSuccessful) {
                binding.imageviewImage.setImageURI(it.uriContent)
            }
            else {
                showMessageDialog("There was a problem, please try later")
            }
        }

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonFirst.setOnClickListener {
            permissionLauncher.launch(arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE))
            //findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun showMessageDialog(message: String) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setMessage(message)
            .setPositiveButton("OK") { dialog, id -> }
        val alert = builder.create()
        alert.show()
        val positiveButton = alert.getButton(DialogInterface.BUTTON_POSITIVE)
        positiveButton?.setTextColor(ContextCompat.getColor(requireContext(), R.color.purple_200))
    }
}