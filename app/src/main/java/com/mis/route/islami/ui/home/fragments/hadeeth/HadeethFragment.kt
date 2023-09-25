package com.mis.route.islami.ui.home.fragments.hadeeth

import android.content.Intent
import android.content.res.Configuration
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mis.route.islami.R
import com.mis.route.islami.databinding.FragmentHadeethBinding
import com.mis.route.islami.ui.Constants
import com.mis.route.islami.ui.details.DetailsActivity
import com.mis.route.islami.ui.home.fragments.hadeeth.adapter.HadeethRecyclerviewAdapter

class HadeethFragment : Fragment() {
    companion object {
        const val HADEETH_TYPE_KEY = "HadeethTypeKey"
        const val HADEETH_POSITION_KEY = "HadeethObjectKey"
    }

    private lateinit var binding: FragmentHadeethBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHadeethBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        applyThemeOnViews()
        val hadeethList = prepareHadeethList()
        val hadeethRecyclerviewAdapter = HadeethRecyclerviewAdapter(hadeethList)
        hadeethRecyclerviewAdapter.setOnHadeethClickListener { position ->
            val intent = Intent(context, DetailsActivity::class.java)
            intent.putExtra(Constants.DETAILS_SENT_TYPE_KEY, HADEETH_TYPE_KEY)
            intent.putExtra(HADEETH_POSITION_KEY, position)
            startActivity(intent)
        }
        binding.hadeethFragmentRecyclerview.adapter = hadeethRecyclerviewAdapter
    }

    private fun applyThemeOnViews() {
        when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_NO -> {
                // light theme.
                val color = resources.getColor(R.color.md_theme_light_primary)
                binding.topDivider.dividerColor = color
                binding.bottomDivider.dividerColor = color
            }

            Configuration.UI_MODE_NIGHT_YES -> {
                // dark theme.
                val color = resources.getColor(R.color.md_theme_dark_primary)
                binding.topDivider.dividerColor = color
                binding.bottomDivider.dividerColor = color
            }
        }
    }

    private fun prepareHadeethList(): List<String> {
        val hadeethTitlesList = mutableListOf<String>()
        for (n in 1..50) hadeethTitlesList.add("حديث $n")
        return hadeethTitlesList
    }
}
