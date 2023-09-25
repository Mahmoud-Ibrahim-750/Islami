package com.mis.route.islami.ui.home.fragments.quran.adapter

import android.content.Context
import android.content.res.Configuration
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.divider.MaterialDivider
import com.mis.route.islami.R
import com.mis.route.islami.databinding.SurahItemBinding
import com.mis.route.islami.ui.home.fragments.quran.model.Surah

class QuranRecyclerviewAdapter(private val surahList: List<Surah>) : RecyclerView.Adapter<QuranRecyclerviewAdapter.ViewHolder>() {

    class ViewHolder(val binding: SurahItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = SurahItemBinding.inflate(
            LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val surah = surahList[position]
        holder.binding.surahName.text = surah.surahName
        holder.binding.verseCount.text = surah.verseCount.toString()
        holder.binding.root.setOnClickListener {
            onSurahClickListener.onClick(position, surah)
        }
        applyThemeOnViews(holder.binding.divider.context, holder.binding.divider)
    }

    private fun applyThemeOnViews(context: Context, divider: MaterialDivider) {
        var color: Int = -1
        when (context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_NO -> {
                // light theme
                color = context.resources.getColor(R.color.md_theme_light_primary)
            }

            Configuration.UI_MODE_NIGHT_YES -> {
                // dark theme.
                color = context.resources.getColor(R.color.md_theme_dark_primary)
            }
        }
        divider.dividerColor = color
    }

    override fun getItemCount() = surahList.size


    private lateinit var onSurahClickListener: OnSurahClickListener

    fun setOnSurahClickListener(listener: OnSurahClickListener) {
        onSurahClickListener = listener
    }

    fun interface OnSurahClickListener {
        fun onClick(position: Int, surah: Surah)
    }
}