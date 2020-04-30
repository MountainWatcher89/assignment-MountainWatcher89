package uk.ac.bournemouth.ap.dotsandboxes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import org.example.student.dotsboxgame.StudentDotsBoxGame
import uk.ac.bournemouth.ap.dotsandboxeslib.ComputerPlayer
import uk.ac.bournemouth.ap.dotsandboxeslib.HumanPlayer
import uk.ac.bournemouth.ap.dotsandboxeslib.Player

class MainActivity : AppCompatActivity() {

    private var myGameView: DotsAndBoxesGameView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        myGameView = DotsAndBoxesGameView(
            this, 7, 7,
            listOf(StudentDotsBoxGame.namedHumanPlayer("Player 1"), StudentDotsBoxGame.easyAI("Computer 1")))
        setContentView(myGameView)
    }
}
