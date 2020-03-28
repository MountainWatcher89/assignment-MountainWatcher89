package uk.ac.bournemouth.ap.dotsandboxes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {

    private var myGameView: DotsAndBoxesGameView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        myGameView = DotsAndBoxesGameView(this)
        setContentView(myGameView)
    }
}
