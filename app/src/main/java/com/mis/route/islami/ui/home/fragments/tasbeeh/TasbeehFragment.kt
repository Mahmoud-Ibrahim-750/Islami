package com.mis.route.islami.ui.home.fragments.tasbeeh

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mis.route.islami.databinding.FragmentTasbeehBinding

class TasbeehFragment : Fragment() {

    private lateinit var binding: FragmentTasbeehBinding
    private var tasbeehCounter = 0
    private val azkarList = listOf("سبحان الله", "الحمد لله", "الله اكبر")
    private var currentZikrIndex = 0

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
        binding.tasbeehCounterTv.text = tasbeehCounter.toString()
        binding.title.text = azkarList[currentZikrIndex]

        binding.sebhaImageContainer.setOnClickListener {
            tasbeehCounter++
            binding.tasbeehCounterTv.text = tasbeehCounter.toString()
            binding.sebhaBodyImage.rotation += 30

            if (tasbeehCounter == 33) {
                tasbeehCounter = 0
                binding.tasbeehCounterTv.text = tasbeehCounter.toString()
                currentZikrIndex = if (currentZikrIndex == 2) 0 else ++currentZikrIndex
                binding.title.text = azkarList[currentZikrIndex]
            }
        }
    }
}
