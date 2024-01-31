package com.movieapp.ui.main.add_movie.dialog

import android.Manifest
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.fondesa.kpermissions.allGranted
import com.fondesa.kpermissions.extension.permissionsBuilder
import com.fondesa.kpermissions.extension.send
import com.movieapp.R
import com.movieapp.api.Status
import com.movieapp.databinding.LayoutAddMovieModalBinding
import com.movieapp.interfaces.DialogListener
import com.movieapp.ui.main.add_movie.adapter.AddMovieImageListAdapter
import com.movieapp.ui.main.add_movie.viewmodel.AddMovieViewModal
import com.movieapp.ui.main.movie_details.activity.MovieDetailsActivity
import com.movieapp.utils.CommonUtils
import com.movieapp.utils.isConnected
import com.nareshchocha.filepickerlibrary.models.PickMediaConfig
import com.nareshchocha.filepickerlibrary.models.PickMediaType
import com.nareshchocha.filepickerlibrary.models.PopUpConfig
import com.nareshchocha.filepickerlibrary.models.PopUpType
import com.nareshchocha.filepickerlibrary.ui.FilePicker
import com.nareshchocha.filepickerlibrary.utilities.appConst.Const
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody


class AddMovieModal(
    val categoryName: String,
    val activity: MovieDetailsActivity,
    val movieId: Int,
) : DialogFragment() {

    private lateinit var binding: LayoutAddMovieModalBinding
    private lateinit var addMovieImageListAdapter: AddMovieImageListAdapter
    private var movieRatingList = ArrayList<Int>()
    private var imageList = ArrayList<Uri>()
    private var selectedRating = -1
    private val viewModel: AddMovieViewModal by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = LayoutAddMovieModalBinding.inflate(layoutInflater)
        setupToolbar()
        setAdapter()
        setListener()
        setUpObserver()
        return binding.root
    }

    private fun setListener() {
        binding.btnClose.setOnClickListener {
            dismiss()
        }

        binding.etMovieRating.setOnItemClickListener { parent, view, position, id ->
            selectedRating = movieRatingList[position]
            binding.movieRatingTextInput.isErrorEnabled = false
        }

        binding.cardMovieImage.setOnClickListener {
            permissionsBuilder(Manifest.permission.CAMERA).build().send { result ->
                if (result.allGranted()) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        permissionsBuilder(Manifest.permission.READ_MEDIA_IMAGES).build()
                            .send { result ->
                                if (result.allGranted()) {
                                    captureImageResultLauncher.launch(
                                        FilePicker.Builder(requireContext())
                                            .setPopUpConfig()
                                            .addImageCapture()
                                            .addPickMedia()
                                            .build(),
                                    )

                                }
                            }
                    } else {
                        permissionsBuilder(Manifest.permission.READ_EXTERNAL_STORAGE).build()
                            .send { result ->
                                if (result.allGranted()) {
                                    captureImageResultLauncher.launch(
                                        FilePicker.Builder(requireContext())
                                            .setPopUpConfig()
                                            .addImageCapture()
                                            .addPickMedia()
                                            .build(),
                                    )
                                }
                            }
                    }
                }
            }

        }

        binding.btnConfirm.setOnClickListener {
            val title = binding.etMovieName.text.toString()
            val description = binding.etMovieDescription.text.toString()
            if (TextUtils.isEmpty(title)) {
                binding.movieNameTextInput.error = "movie is name required"
            } else if (TextUtils.isEmpty(description)) {
                binding.movieDescriptionTextInput.error = "description is required"
            } else if (selectedRating == -1) {
                binding.movieRatingTextInput.error = "please select rating"
            } else if (imageList.isEmpty()) {
                Toast.makeText(requireContext(), "select one image", Toast.LENGTH_SHORT)
                    .show()
            } else {

                /* api call */
                val params = mapOf(
                    "category_id" to movieId.toString()
                        .toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                    "movie_name" to title.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                    "movie_description" to description.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                    "movie_rating" to selectedRating.toString()
                        .toRequestBody("multipart/form-data".toMediaTypeOrNull())
                )

                viewModel.addMovie(params, imageList[0], activity)
            }
        }


        binding.etMovieName.doOnTextChanged { text, start, before, count ->
            binding.movieNameTextInput.isErrorEnabled = false
        }

        binding.etMovieDescription.doOnTextChanged { text, start, before, count ->
            binding.movieDescriptionTextInput.isErrorEnabled = false
        }


    }

    private val captureImageResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

            if (result != null && result.resultCode == RESULT_OK) {
                imageList.clear()

                if (result.data?.data != null) {
                    result.data?.data?.let {
                        imageList.add(it)
                    }
                }

                addMovieImageListAdapter.notifyDataSetChanged()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Something went wrong",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }


    private fun setAdapter() {

        /* set adapter and layout manager for recyclerview */
        val layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvImages.layoutManager = layoutManager
        addMovieImageListAdapter = AddMovieImageListAdapter(imageList) { pos: Int -> onRemove(pos) }
        binding.rvImages.adapter = addMovieImageListAdapter

        /* set movie rating adapter */
        movieRatingList.clear()
        movieRatingList.addAll(listOf(1, 2, 3, 4, 5))
        val adapter = ArrayAdapter(
            requireContext(), R.layout.dropdown_item, movieRatingList
        )
        binding.etMovieRating.setAdapter(adapter)
    }

    private fun onRemove(pos: Int) {
        imageList.removeAt(pos)
        addMovieImageListAdapter.notifyDataSetChanged()
    }

    private fun setUpObserver() {
        viewModel.addMovieDetailsLiveData.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    activity.dismissProgress()
                    dismiss()
                    listener?.onCloseDialog()


                }

                Status.ERROR -> {
                    activity.dismissProgress()
                    if (requireActivity().isConnected) {
                        CommonUtils.showErrorSnackBar(
                            it.message.toString(), binding.root, requireActivity()
                        )
                    } else {
                        CommonUtils.showErrorSnackBar(
                            getString(R.string.please_check_internet),
                            binding.root,
                            requireActivity()
                        )
                    }

                }

                Status.LOADING -> {
                    activity.showProgress()
                }

            }
        }
    }

    private fun setupToolbar() {
        binding.toolbar.toolbarTitle.text = getString(R.string.add_movie)
        binding.toolbar.toolbar.apply {
            title = ""
            setNavigationIcon(R.drawable.ic_baseline_close_24)
            setNavigationOnClickListener { dismiss() }
        }
        binding.tvCategory.text = categoryName
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        dialog?.window?.apply {
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            setBackgroundDrawable(ColorDrawable(Color.WHITE))

        }
    }


    override fun getTheme(): Int {
        return R.style.FullScreenDialog
    }


    private var listener: DialogListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as DialogListener
        } catch (e: ClassCastException) {
            throw ClassCastException("$context must implement DialogListener")
        }
    }


}