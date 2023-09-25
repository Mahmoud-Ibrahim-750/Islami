package com.mis.route.islami.ui.home.fragments.quran

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mis.route.islami.R
import com.mis.route.islami.databinding.FragmentQuranBinding
import com.mis.route.islami.ui.Constants
import com.mis.route.islami.ui.details.DetailsActivity
import com.mis.route.islami.ui.home.fragments.quran.adapter.QuranRecyclerviewAdapter
import com.mis.route.islami.ui.home.fragments.quran.model.Surah

class QuranFragment : Fragment() {
    companion object {
        const val SURAH_TYPE_KEY = "SurahTypeKey"
        const val SURAH_OBJECT_KEY = "SurahObjectKey"
        const val SURAH_POSITION_KEY = "SurahPositionKey"
    }

    private lateinit var binding: FragmentQuranBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentQuranBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        applyThemeOnViews()
        val surahList = prepareSurahList()
        val quranRecyclerviewAdapter = QuranRecyclerviewAdapter(surahList)
        quranRecyclerviewAdapter.setOnSurahClickListener { position, surah ->
            val intent = Intent(context, DetailsActivity::class.java)
            intent.putExtra(Constants.DETAILS_SENT_TYPE_KEY, SURAH_TYPE_KEY)
            intent.putExtra(SURAH_OBJECT_KEY, surah)
            intent.putExtra(SURAH_POSITION_KEY, position)
            startActivity(intent)
        }
        binding.quranFragmentRecyclerview.adapter = quranRecyclerviewAdapter
    }

    private fun prepareSurahList() : MutableList<Surah> {
        val namesList = listOf("الفاتحه","البقرة","آل عمران","النساء","المائدة","الأنعام","الأعراف","الأنفال","التوبة","يونس","هود"
            ,"يوسف","الرعد","إبراهيم","الحجر","النحل","الإسراء","الكهف","مريم","طه","الأنبياء","الحج","المؤمنون"
            ,"النّور","الفرقان","الشعراء","النّمل","القصص","العنكبوت","الرّوم","لقمان","السجدة","الأحزاب","سبأ"
            ,"فاطر","يس","الصافات","ص","الزمر","غافر","فصّلت","الشورى","الزخرف","الدّخان","الجاثية","الأحقاف"
            ,"محمد","الفتح","الحجرات","ق","الذاريات","الطور","النجم","القمر","الرحمن","الواقعة","الحديد","المجادلة"
            ,"الحشر","الممتحنة","الصف","الجمعة","المنافقون","التغابن","الطلاق","التحريم","الملك","القلم","الحاقة","المعارج"
            ,"نوح","الجن","المزّمّل","المدّثر","القيامة","الإنسان","المرسلات","النبأ","النازعات","عبس","التكوير","الإنفطار"
            ,"المطفّفين","الإنشقاق","البروج","الطارق","الأعلى","الغاشية","الفجر","البلد","الشمس","الليل","الضحى","الشرح"
            ,"التين","العلق","القدر","البينة","الزلزلة","العاديات","القارعة","التكاثر","العصر",
            "الهمزة","الفيل","قريش","الماعون","الكوثر","الكافرون","النصر","المسد","الإخلاص","الفلق","الناس")

        val verseNumbersList = listOf(
            7,   286, 200, 176, 120, 165, 206, 75,  129, 109,
            123, 111, 43,  52,  99,  128, 111, 110, 98,  135,
            112, 78,  118, 64,  77,  227, 93,  88,  69,  60,
            34,  30,  73,  54,  45,  83,  182, 88,  75,  85,
            54,  53,  89,  59,  37,  35,  38,  29,  18,  45,
            60,  49,  62,  55,  78,  96,  29,  22,  24,  13,
            14,  11,  11,  18,  12,  12,  30,  52,  52,  44,
            28,  28,  20,  56,  40,  31,  50,  40,  46,  42,
            29,  19,  36,  25,  22,  17,  19,  26,  30,  20,
            15,  21,  11,  8,   8,   19,  5,   8,   8,   11,
            11,  8,   3,   9,   5,   4,   7,   3,   6,   3,
            5,   4,   5,   6
        )

        val surahList = mutableListOf<Surah>()
        for(n in 0..113) surahList.add(Surah(namesList[n], verseNumbersList[n]))
        return surahList
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        applyThemeOnViews()
    }

    private fun applyThemeOnViews() {
        when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_NO -> {
                // light theme.
                val color = resources.getColor(R.color.md_theme_light_primary)
                binding.topDivider.dividerColor = color
                binding.centerDivider.dividerColor = color
                binding.bottomDivider.dividerColor = color
            }

            Configuration.UI_MODE_NIGHT_YES -> {
                // dark theme.
                val color = resources.getColor(R.color.md_theme_dark_primary)
                binding.topDivider.dividerColor = color
                binding.centerDivider.dividerColor = color
                binding.bottomDivider.dividerColor = color
            }
        }
    }
}
