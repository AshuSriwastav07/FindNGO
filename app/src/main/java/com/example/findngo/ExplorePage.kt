package com.example.findngo

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.tlc.findngo.R

class ExplorePage : AppCompatActivity() {

    private val handler = Handler(Looper.getMainLooper())
    private var currentIndex = 0
    private val imageList = arrayOf(
        R.drawable.explore1,
        R.drawable.explore2,
        R.drawable.explore3,
        R.drawable.explore4,
        R.drawable.explore5
    )

    private val slideshowRunnable = object : Runnable {
        override fun run() {
            val exploreImageView = findViewById<ImageView>(R.id.ExploreimageView)
            val explorePage = findViewById<RelativeLayout>(R.id.ExplorePage)

            if (exploreImageView != null && explorePage != null) {
                exploreImageView.setImageResource(imageList[currentIndex])
                explorePage.setBackgroundResource(imageList[currentIndex])
            }

            currentIndex = (currentIndex + 1) % imageList.size
            handler.postDelayed(this, 2000)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.explore_page_activity)

        val exploreButton: Button = findViewById(R.id.exploreButton)

        handler.post(slideshowRunnable)

        exploreButton.setOnClickListener {
            val intent = Intent(this, HomePage::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(slideshowRunnable)
    }
}