package com.mis.route.islami.ui.details

import android.content.res.ColorStateList
import android.content.res.Configuration
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContentProviderCompat.requireContext
import com.mis.route.islami.R
import com.mis.route.islami.databinding.ActivityDetailsBinding
import com.mis.route.islami.ui.Constants
import com.mis.route.islami.ui.home.fragments.hadeeth.HadeethFragment
import com.mis.route.islami.ui.home.fragments.hadeeth.model.Hadeeth
import com.mis.route.islami.ui.home.fragments.quran.QuranFragment
import com.mis.route.islami.ui.home.fragments.quran.model.Surah
import java.io.BufferedReader
import java.io.InputStreamReader

class DetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailsBinding

    private fun getSurahContentByPosition(position: Int): String {
        val inputStream = assets.open("quran/${position + 1}.txt")
        // the InputStreamReader converts byte stream into character data
        val bufferedReader = BufferedReader(InputStreamReader(inputStream))
        val stringBuilder = StringBuilder()
        var line: String?
        while (bufferedReader.readLine().also { line = it } != null) {
            stringBuilder.append("$line\n")
        }
        bufferedReader.close()
        return stringBuilder.toString()
    }

    private fun getHadeethByPosition(position: Int): Hadeeth {
        val inputStream = assets.open("hadeeth/h${position + 1}.txt")
        val bufferedReader = BufferedReader(InputStreamReader(inputStream))
        val titleLine = bufferedReader.readLine() // first line is the title
        val contentLines = StringBuilder()
        var line: String?
        while (bufferedReader.readLine().also { line = it } != null) {
            contentLines.append("$line\n")
        }
        bufferedReader.close()
        return Hadeeth(titleLine, contentLines.toString())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        applyThemeOnViews()

        when (intent?.getStringExtra(Constants.DETAILS_SENT_TYPE_KEY)) {
            HadeethFragment.HADEETH_TYPE_KEY -> {
                val hadeeth = getHadeethByPosition(
                    intent.getIntExtra(
                        HadeethFragment.HADEETH_POSITION_KEY, 0
                    )
                )
                binding.titleTextview.text = hadeeth.title
                binding.contentTextview.text = hadeeth.content
            }

            QuranFragment.SURAH_TYPE_KEY -> {
                val surah = getSurahObject()
                binding.titleTextview.text = surah?.surahName
                binding.contentTextview.text = getSurahContentByPosition(
                    intent.getIntExtra(
                        QuranFragment.SURAH_POSITION_KEY,
                        0
                    )
                )
                getSurahContentByPosition(intent.getIntExtra(QuranFragment.SURAH_POSITION_KEY, 0))
            }
        }

        binding.backArrowButton.setOnClickListener {
            finish()
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        applyThemeOnViews()
    }

    private fun getSurahObject(): Surah? {
        return if (VERSION.SDK_INT >= VERSION_CODES.TIRAMISU)
            intent.getParcelableExtra(QuranFragment.SURAH_OBJECT_KEY, Surah::class.java)
        else intent.getParcelableExtra(QuranFragment.SURAH_OBJECT_KEY)
    }

    private fun applyThemeOnViews() {
        when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_NO -> {
                // light theme.
                val colorStateList = AppCompatResources.getColorStateList(this, R.color.md_theme_light_primary)
                val color = resources.getColor(R.color.gold_light)
                binding.root.background = resources.getDrawable(R.drawable.bg_light_main)
                binding.backArrowButton.imageTintList = colorStateList
                binding.titleTextview.setTextColor(color)
                binding.contentTextview.setTextColor(color)
                binding.playButton.imageTintList = colorStateList
                binding.divider.dividerColor = color
            }

            Configuration.UI_MODE_NIGHT_YES -> {
                // dark theme.
                val colorStateList = AppCompatResources.getColorStateList(this, R.color.md_theme_dark_primary)
                val color = resources.getColor(R.color.gold_dark)
                binding.root.background = resources.getDrawable(R.drawable.bg_dark_main)
                binding.backArrowButton.imageTintList = colorStateList
                binding.titleTextview.setTextColor(color)
                binding.contentTextview.setTextColor(color)
                binding.playButton.imageTintList = colorStateList
                binding.divider.dividerColor = color
            }
        }
    }
}
