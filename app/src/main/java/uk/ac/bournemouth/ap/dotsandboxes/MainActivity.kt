package uk.ac.bournemouth.ap.dotsandboxes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import org.example.student.dotsboxgame.StudentDotsBoxGame

class MainActivity : AppCompatActivity() {

    private var myGameView: DotsAndBoxesGameView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        myGameView = DotsAndBoxesGameView(
            this, 7, 7,
            listOf(StudentDotsBoxGame.NamedHumanPlayer("Player 1"), StudentDotsBoxGame.EasyAI("Computer 1")))
        setContentView(myGameView)
    }
}
