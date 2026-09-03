package com.example.findngo

import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tlc.findngo.R
import com.tlc.findngo.databinding.ActivityHomePageBinding

class HomePage : AppCompatActivity() {

    private lateinit var binding : ActivityHomePageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomePageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        replace_fragment(HomePageFragment())

        binding.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.bottom_nav_home -> replace_fragment(HomePageFragment())
                R.id.bottom_nav_bookmark -> replace_fragment(BookmarkPageFragment())
                R.id.bottom_nav_donation -> replace_fragment(DonationPageFragment())
                R.id.bottom_nav_setting -> replace_fragment(SettingPageFragment())
                else -> {}
            }
            true
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val currentFragment = supportFragmentManager.findFragmentById(R.id.MainFrameLayout)
                if (currentFragment !is HomePageFragment) {
                    binding.bottomNavigationView.selectedItemId = R.id.bottom_nav_home
                } else {
                    isEnabled = false
                    onBackPressedDispatcher.onBackPressed()
                }
            }
        })
    }

    private fun replace_fragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.MainFrameLayout, fragment)
        fragmentTransaction.commit()
    }
}