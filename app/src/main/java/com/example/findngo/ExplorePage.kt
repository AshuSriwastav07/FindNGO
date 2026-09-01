package com.example.findngo

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.tlc.findngo.R

class ExplorePage : AppCompatActivity() {

    private val handler = Handler(Looper.getMainLooper())
    private var currentIndex = 0

    private val slides = listOf(
        Slide(
            R.drawable.explore1,
            "Discover verified NGOs",
            "Explore a curated list of organizations with verified NGO Darpan IDs, ensuring your support reaches legitimate causes."
        ),
        Slide(
            R.drawable.explore2,
            "Direct & Transparent Impact",
            "Connect directly with registered trusts, societies, and foundations across India with verified contact information."
        ),
        Slide(
            R.drawable.explore3,
            "Support Active Causes",
            "Browse active donation drives, humanitarian relief campaigns, and educational missions across all sectors."
        )
    )

    private val slideshowRunnable = object : Runnable {
        override fun run() {
            updateSlide(currentIndex)
            currentIndex = (currentIndex + 1) % slides.size
            handler.postDelayed(this, 3500)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.explore_page_activity)

        findViewById<Button>(R.id.exploreButton)?.setOnClickListener { navigateHome() }
        findViewById<Button>(R.id.skipButton)?.setOnClickListener { navigateHome() }

        updateSlide(0)
        handler.postDelayed(slideshowRunnable, 3500)
    }

    private fun updateSlide(index: Int) {
        val slide = slides[index % slides.size]
        findViewById<ImageView>(R.id.ExploreimageView)?.setImageResource(slide.imageRes)
        findViewById<TextView>(R.id.onboardingTitle)?.text = slide.title
        findViewById<TextView>(R.id.onboardingSubtitle)?.text = slide.subtitle

        val dot1 = findViewById<View>(R.id.dot1)
        val dot2 = findViewById<View>(R.id.dot2)
        val dot3 = findViewById<View>(R.id.dot3)

        dot1?.backgroundTintList = ContextCompat.getColorStateList(this, if (index % 3 == 0) R.color.primary else R.color.outline_variant)
        dot2?.backgroundTintList = ContextCompat.getColorStateList(this, if (index % 3 == 1) R.color.primary else R.color.outline_variant)
        dot3?.backgroundTintList = ContextCompat.getColorStateList(this, if (index % 3 == 2) R.color.primary else R.color.outline_variant)
    }

    private fun navigateHome() {
        val intent = Intent(this, HomePage::class.java)
        startActivity(intent)
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(slideshowRunnable)
    }

    private data class Slide(
        val imageRes: Int,
        val title: String,
        val subtitle: String
    )
}