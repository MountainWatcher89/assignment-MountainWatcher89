package uk.ac.bournemouth.ap.dotsandboxes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import org.example.student.dotsboxgame.StudentDotsBoxGame
import uk.ac.bournemouth.ap.dotsandboxeslib.*

class MainActivity() : AppCompatActivity() {

    var width = 2
    var height = 2
    private var aiPlayer: ComputerPlayer? = null

    private lateinit var myGameView: DotsAndBoxesGameView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if(intent.getIntArrayExtra("ID_EXTRA") != null)
        {
            val tempArray = intent.getIntArrayExtra("ID_EXTRA")
            //Temporary experiment
            width = tempArray[0]
            height = tempArray[1]

            if(tempArray[2] == 0)
            {
                aiPlayer = StudentDotsBoxGame.EasyAI("Easy AI 1")
            }
            else if(tempArray[2] == 1)
            {
                aiPlayer = StudentDotsBoxGame.MediumAI("Medium AI 1")
            }
            else if(tempArray[2] == 2)
            {
                aiPlayer = StudentDotsBoxGame.HardAI("Hard AI 1")
            }
            else
            {
                aiPlayer = StudentDotsBoxGame.EasyAI("Easy AI 1")
            }
        }

        myGameView = DotsAndBoxesGameView(this)

        myGameView.myGameInstance = StudentDotsBoxGame(width, height,
                                                           listOf(StudentDotsBoxGame.NamedHumanPlayer("Player 1"), aiPlayer!!))

        setContentView(myGameView)
    }
}
