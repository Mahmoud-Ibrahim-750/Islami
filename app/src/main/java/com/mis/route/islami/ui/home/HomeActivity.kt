package com.mis.route.islami.ui.home

import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import com.mis.route.islami.R
import com.mis.route.islami.databinding.ActivityHomeBinding
import com.mis.route.islami.ui.home.fragments.hadeeth.HadeethFragment
import com.mis.route.islami.ui.home.fragments.quran.QuranFragment
import com.mis.route.islami.ui.home.fragments.radio.RadioFragment
import com.mis.route.islami.ui.home.fragments.tasbeeh.TasbeehFragment

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // set views properties according to theme
        applyThemeOnViews()

        // display default fragment
        showFragment(QuranFragment())

//        binding.contentHome.bottomNavView.setOnItemSelectedListener {
//            when (it.itemId) {
//                R.id.navigation_quran -> {
//                    showFragment(QuranFragment())
//                }
//                R.id.navigation_hadeeth -> {
//                    showFragment(HadeethFragment())
//                }
//                R.id.navigation_tasbeeh -> {
//                    showFragment(TasbeehFragment())
//                }
//                R.id.navigation_radio -> {
//                    showFragment(RadioFragment())
//                }
//            }
//            true
//        }

        binding.contentHome.bottomNavView.setOnItemSelectedListener {
            // TODO: check this later too
//            val tasbeehCounter = TasbeehCounter.getInstance()
//            tasbeehCounter.counter++
//            Log.d("CounterHome", tasbeehCounter.counter.toString())
            //
            showFragment(
                when (it.itemId) {
                R.id.navigation_quran -> QuranFragment()
                R.id.navigation_hadeeth -> HadeethFragment()
                R.id.navigation_tasbeeh -> TasbeehFragment()
                else -> RadioFragment()
            })
            true
        }

    }

    private fun showFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    // update views when theme is changed while app is running
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        applyThemeOnViews()
    }

    // set images according to the current theme
    private fun applyThemeOnViews() {
        when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_NO -> {
                // light theme.
                binding.root.setBackgroundResource(R.drawable.bg_light_main)
                binding.titleTextview.setTextColor(resources.getColor(R.color.md_theme_light_onBackground))
                binding.contentHome.bottomNavView.setBackgroundColor(resources.getColor(R.color.md_theme_light_primary))
                val colorStateList = AppCompatResources.getColorStateList(this, R.color.light_home_nav_color)
                binding.contentHome.bottomNavView.itemIconTintList = colorStateList
                binding.contentHome.bottomNavView.itemTextColor = colorStateList
            }

            Configuration.UI_MODE_NIGHT_YES -> {
                // dark theme.
                binding.root.setBackgroundResource(R.drawable.bg_dark_main)
                binding.titleTextview.setTextColor(resources.getColor(R.color.md_theme_dark_onBackground))
                binding.contentHome.bottomNavView.setBackgroundColor(resources.getColor(R.color.purple_dark))
                val colorStateList = AppCompatResources.getColorStateList(this, R.color.dark_home_nav_color)
                binding.contentHome.bottomNavView.itemIconTintList = colorStateList
                binding.contentHome.bottomNavView.itemTextColor = colorStateList
            }
        }
    }
}
