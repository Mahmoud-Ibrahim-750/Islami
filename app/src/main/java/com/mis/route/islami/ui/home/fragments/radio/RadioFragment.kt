package com.mis.route.islami.ui.home.fragments.radio

import android.content.res.ColorStateList
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import com.mis.route.islami.R
import com.mis.route.islami.databinding.FragmentRadioBinding

class RadioFragment : Fragment() {
    private lateinit var binding: FragmentRadioBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRadioBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        applyThemeOnViews()
    }

    private fun applyThemeOnViews() {
        val colorStateList: ColorStateList? = when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_NO -> // light theme.
                context?.let {
                    AppCompatResources.getColorStateList(requireContext(), R.color.md_theme_light_primary)
                }

            else -> // dark theme.
                context?.let {
                    AppCompatResources.getColorStateList(requireContext(), R.color.md_theme_dark_primary)
                }
        }

        binding.backwardButton.imageTintList =  colorStateList
        binding.playStopButton.imageTintList = colorStateList
        binding.forwardButton.imageTintList = colorStateList
    }
}