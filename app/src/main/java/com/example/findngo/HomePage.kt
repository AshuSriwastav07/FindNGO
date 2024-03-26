package com.example.findngo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.tlc.findngo.R
import com.tlc.findngo.databinding.ActivityHomePageBinding

class HomePage : AppCompatActivity() {

    private lateinit var binding : ActivityHomePageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityHomePageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        replace_fragment(HomePageFragment())

        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.bottom_nav_home -> replace_fragment(HomePageFragment())
                R.id.bottom_nav_bookmark -> replace_fragment(BookmarkPageFragment())
                R.id.bottom_nav_donation -> replace_fragment(DonationPageFragment())
                R.id.bottom_nav_setting -> replace_fragment(SettingPageFragment())


                else -> {}
            }

            true

    }}

    private fun replace_fragment(fragment: Fragment){

        val fragmentManager=supportFragmentManager
        val fragmentTransaction=fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.MainFrameLayout,fragment)
        fragmentTransaction.commit()

    }

    override fun onBackPressed() {
        // Get the current fragment
        val currentFragment = supportFragmentManager.findFragmentById(R.id.MainFrameLayout)  //get fragment contaner

        // Navigate to Fragment1 if the current fragment is not Fragment1
        if (currentFragment !is HomePageFragment) {
            navigateToFragment1()
        } else {
            // If the current fragment is Fragment1, let the system handle the back press
            super.onBackPressed()
        }
    }

    private fun navigateToFragment1() {
        // Clear the back stack
        supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)

        // Replace the current fragment with Fragment1
        supportFragmentManager.beginTransaction()
            .replace(R.id.MainFrameLayout, HomePageFragment())
            .commit()
    }


}