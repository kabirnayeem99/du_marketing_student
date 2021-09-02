package io.github.kabirnayeem99.dumarketingstudent.ui.fragments

import android.os.Bundle
import android.view.View
import android.view.animation.ScaleAnimation
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import io.github.kabirnayeem99.dumarketingstudent.R
import io.github.kabirnayeem99.dumarketingstudent.databinding.FragmentImageDetailsBinding
import io.github.kabirnayeem99.dumarketingstudent.ui.base.BaseFragment
import io.github.kabirnayeem99.dumarketingstudent.util.showSnackBar
import io.github.kabirnayeem99.dumarketingstudent.viewmodel.GalleryViewModel
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class ImageDetailsFragment : BaseFragment<FragmentImageDetailsBinding>() {


    @Inject
    lateinit var scale: ScaleAnimation

    private val galleryViewModel: GalleryViewModel by activityViewModels()

    override val layout: Int
        get() = R.layout.fragment_image_details


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        galleryViewModel.selectedImageUrl.observe(viewLifecycleOwner, { imageUrl ->
            loadUi(imageUrl)
        })
        binding.ivImageDetails.doubleTapToZoom = true
    }

    private fun loadUi(imageUrl: String) {
        try {
            Glide.with(binding.root).load(imageUrl).into(binding.ivImageDetails)
            binding.ivImageDetails.startAnimation(scale)
        } catch (e: Exception) {
            showSnackBar("Could not load the image you have selected.")
            Timber.e(e)
        }
    }

}