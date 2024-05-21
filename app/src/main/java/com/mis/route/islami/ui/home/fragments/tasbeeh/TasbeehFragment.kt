package com.mis.route.islami.ui.home.fragments.tasbeeh

import android.content.res.ColorStateList
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import com.mis.route.islami.R
import com.mis.route.islami.databinding.FragmentTasbeehBinding

class TasbeehFragment : Fragment() {

    private lateinit var binding: FragmentTasbeehBinding
    private var tasbeehCounter = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTasbeehBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        applyThemeOnViews()
        binding.tasbeehCountTextview.text = tasbeehCounter.toString()

        binding.counterButton.setOnClickListener {
            tasbeehCounter++
            binding.tasbeehCountTextview.text = tasbeehCounter.toString()
            binding.sebhaBodyImage.rotation += 30
        }
    }

    private fun applyThemeOnViews() {
        var colorStateList: ColorStateList? = null
        var color: Int? = null
        when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_NO -> {
                // light theme.
                context?.let {
                    colorStateList = AppCompatResources.getColorStateList(requireContext(), R.color.md_theme_light_primary)
                    color = resources.getColor(R.color.gold_light)

                }
            }

            Configuration.UI_MODE_NIGHT_YES -> {
                // dark theme.
                context?.let {
                    colorStateList = AppCompatResources.getColorStateList(requireContext(), R.color.md_theme_dark_primary)
                    color = resources.getColor(R.color.purple_dark)
                }
            }
        }

        binding.sebhaHeadImage.imageTintList =  colorStateList
        binding.sebhaBodyImage.imageTintList = colorStateList
        binding.tasbeehCountTextview.setBackgroundColor(color!!)
    }
}
