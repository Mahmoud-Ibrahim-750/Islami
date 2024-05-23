package com.mis.route.islami.ui.home.fragments.radio

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.mis.route.islami.R
import com.mis.route.islami.databinding.FragmentRadioBinding
import com.mis.route.islami.ui.Constants


class RadioFragment : Fragment() {
    private lateinit var binding: FragmentRadioBinding

    private lateinit var sharedPreferences: SharedPreferences

    private lateinit var radioPlayerService: RadioPlayerService
    private var isRadioPlayerServiceBound: Boolean = false


    private val connection = object : ServiceConnection {

        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance.
            val binder = service as RadioPlayerService.LocalBinder
            radioPlayerService = binder.getService()
            isRadioPlayerServiceBound = true
            defineRadioPlayerServiceContract()
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            isRadioPlayerServiceBound = false
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStart() {
        super.onStart()
        Intent(requireContext(), RadioPlayerService::class.java).also { intent ->
            requireActivity().startForegroundService(intent)
            requireActivity().bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }
    }

    override fun onStop() {
        super.onStop()
        requireActivity().unbindService(connection)
        isRadioPlayerServiceBound = false
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

//        retrieveSavedData()

        requireActivity().startForegroundService(
            Intent(
                requireContext(),
                RadioPlayerService::class.java
            )
        )

        binding.playStopButton.setOnClickListener { toggleRadioPlayer() }
        binding.forwardButton.setOnClickListener { playNextRadio() }
        binding.backwardButton.setOnClickListener { playPreviousRadio() }
    }

    private fun defineRadioPlayerServiceContract() {
        radioPlayerService.defineRadioMediaPlayerContract(object :
            RadioPlayerService.RadioMediaPlayerContract {
            override fun onPlayed() {
                togglePlayingVisibility(true)
                togglePlayingStatus(true)
            }

            override fun onPaused() {
                togglePlayingVisibility(true)
                togglePlayingStatus(false)
            }

            override fun onNextPlayed() {
                togglePlayingVisibility(true)
                togglePlayingStatus(true)
            }

            override fun onPreviousPlayed() {
                togglePlayingVisibility(true)
                togglePlayingStatus(true)
            }

            override fun onLoading() {
                togglePlayingVisibility(false)
            }
        })
    }

    private fun toggleRadioPlayer() {
        if (isRadioPlayerServiceBound) {
            radioPlayerService.playOrPauseRadio()
        }
    }

    private fun playPreviousRadio() {
        if (isRadioPlayerServiceBound) {
            radioPlayerService.playPreviousRadio()
            togglePlayingVisibility(false)
            togglePlayingStatus(false)
        }
    }

    private fun playNextRadio() {
        if (isRadioPlayerServiceBound) {
            radioPlayerService.playNextRadio()
            togglePlayingVisibility(false)
            togglePlayingStatus(false)
        }
    }

    private fun togglePlayingVisibility(playing: Boolean) {
        binding.playStopButton.isVisible = playing
        binding.loadingProgress.isVisible = !playing
    }

    private fun togglePlayingStatus(currentlyPlaying: Boolean) {
        if (currentlyPlaying) {
            binding.playStopButton.setImageResource(R.drawable.ic_play_gold)
            binding.playStopButton.scaleType = ImageView.ScaleType.CENTER
        } else {
            binding.playStopButton.setImageResource(R.drawable.ic_pause)
            binding.playStopButton.scaleType = ImageView.ScaleType.CENTER_CROP
        }
    }
}