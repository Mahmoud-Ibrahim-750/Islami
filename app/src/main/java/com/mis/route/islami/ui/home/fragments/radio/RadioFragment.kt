package com.mis.route.islami.ui.home.fragments.radio

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.mis.route.islami.R
import com.mis.route.islami.databinding.FragmentRadioBinding
import com.mis.route.islami.ui.Constants


class RadioFragment : Fragment() {
    private lateinit var binding: FragmentRadioBinding

    private lateinit var sharedPreferences: SharedPreferences


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStart() {
        super.onStart()
        Intent(requireContext(), RadioService::class.java).also { intent ->
            requireActivity().startForegroundService(intent)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRadioBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPreferences = requireActivity().getSharedPreferences(
            Constants.SETTINGS_FILE_NAME,
            Context.MODE_PRIVATE
        )

        retrieveSavedData()

        requireActivity().startForegroundService(Intent(requireContext(), RadioService::class.java))

        binding.playStopButton.setOnClickListener {
            toggleRadioPlayer()
        }

        binding.forwardButton.setOnClickListener { playNextRadio() }
        binding.backwardButton.setOnClickListener { playPreviousRadio() }
    }

    private fun toggleRadioPlayer() {

    }

    private fun retrieveSavedData() {
        val name =
            sharedPreferences.getString(Constants.SAVED_RADIO_NAME, "إذاعـة القـرآن الـكـريـم")
        val url: String = sharedPreferences.getString(Constants.SAVED_RADIO_URL, "") ?: ""
        Log.d("tt", "name $name, url $url")
//        if (url.isNotEmpty()) initMediaPlayer(name, url)
    }

    private fun playPreviousRadio() {
        togglePlayingVisibility(false)
        togglePlayingStatus(false)
    }

    private fun playNextRadio() {
        togglePlayingVisibility(false)
        togglePlayingStatus(false)
    }

    private fun getCurrentLanguageCode(): String {
        return if (resources.configuration.locales[0].language == "ar") Constants.ARABIC_LANG_CODE
        else Constants.ENGLISH_LANG_CODE
    }

    private fun togglePlayingVisibility(playing: Boolean) {
        binding.playStopButton.isVisible = playing
        binding.loadingProgress.isVisible = !playing
    }

    private fun togglePlayingStatus(currentlyPlaying: Boolean) {
        if (currentlyPlaying) binding.playStopButton.setImageResource(R.drawable.baseline_play_arrow_24)
        else binding.playStopButton.setImageResource(R.drawable.ic_pause)
    }
}