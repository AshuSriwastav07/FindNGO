package com.example.findngo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.tlc.findngo.R

class ExplorePage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Thread.sleep(3000) //set 3s to sleep
        installSplashScreen() //set splash screen before main screen

        setContentView(R.layout.explore_page_activity)

        val ExploreImageView=findViewById<ImageView>(R.id.ExploreimageView)  //get imageview
        val ExploreButton=findViewById<Button>(R.id.exploreButton)          //get button for goto next screen
        val ExplorePage=findViewById<RelativeLayout>(R.id.ExplorePage)

        val ImageList= arrayOf(R.drawable.explore1,R.drawable.explore2,R.drawable.explore3,R.drawable.explore4,R.drawable.explore5)  //set images name in array for change

        var currentIndex = 0   // count image index value for changeing and

        val handler = android.os.Handler() //allow us to set and do something in future using time in millisecond

        val runnable = object : Runnable {  //make object which contains the code to be executed by the Handler. Inside the run()
            override fun run() {
                ExploreImageView.setImageResource(ImageList[currentIndex])  // set image by index value
                ExplorePage.setBackgroundResource(ImageList[currentIndex])  //set imageList Image in background

                currentIndex = (currentIndex + 1) % ImageList.size   //change index value and if it is > or = to ImageList.size set to 0
                handler.postDelayed(this, 1500) // Set delay for slideshow (in milliseconds)  //call run function after 1.5sec each and change image
            }
        }

        // Start the slideshow
        handler.post(runnable)   // call runnable object in handler and start image slideshow

        ExploreButton.setOnClickListener{
            intent = Intent(this,HomePage::class.java)
            startActivity(intent)
        }
val key=intent.extras?.getString("key")
        Log.d("Message", key.toString())

    }
}