package uk.ac.bournemouth.ap.dotsandboxes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import org.example.student.dotsboxgame.StudentDotsBoxGame

class MainActivity : AppCompatActivity() {

    private lateinit var myGameView: DotsAndBoxesGameView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        myGameView = DotsAndBoxesGameView(this)
        myGameView.myGameInstance = StudentDotsBoxGame(2,2,
                                                       listOf(StudentDotsBoxGame.NamedHumanPlayer("Player 1"), StudentDotsBoxGame.EasyAI("Computer 1")))
        setContentView(myGameView)
    }
}
