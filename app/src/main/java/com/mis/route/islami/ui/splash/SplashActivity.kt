package com.mis.route.islami.ui.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import com.mis.route.islami.R
import com.mis.route.islami.ui.home.HomeActivity
import com.mis.route.islami.databinding.ActivitySplashBinding
import java.util.Timer
import java.util.TimerTask

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        applyThemeOnViews()

        // Handler
//        Handler().postDelayed(Runnable { -> doSomething }, 3000)
//        Handler().postDelayed({
//                val intent = Intent(this, MainActivity::class.java)
//                startActivity(intent)
//                finish() }, 3000)

        // Timer
        Timer().schedule(object : TimerTask() {
            override fun run() {
                val intent = Intent(this@SplashActivity, HomeActivity::class.java)
                startActivity(intent)
                finish()
            }
        }, 3000)

         Timer().schedule(object:TimerTask(){
             override fun run() {

             }

         }, 3000) // TODO: check what is wrong here?
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        applyThemeOnViews()
    }

    private fun applyThemeOnViews() {
        when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_NO -> {
                // light theme.
                binding.root.setBackgroundResource(R.drawable.bg_light_empty)
                val colorStateList = AppCompatResources.getColorStateList(this, R.color.md_theme_light_primary)
                binding.splashImage.imageTintList = colorStateList
                binding.routeLogo.imageTintList = colorStateList
                binding.supervisorTextview.setTextColor(resources.getColor(R.color.md_theme_light_primary))

            }

            Configuration.UI_MODE_NIGHT_YES -> {
                // dark theme.
                binding.root.setBackgroundResource(R.drawable.bg_dark_empty)
                val colorStateList = AppCompatResources.getColorStateList(this, R.color.md_theme_dark_primary)
                binding.splashImage.imageTintList = colorStateList
                binding.routeLogo.imageTintList = colorStateList
                binding.supervisorTextview.setTextColor(resources.getColor(R.color.md_theme_dark_primary))
            }
        }
    }
}
