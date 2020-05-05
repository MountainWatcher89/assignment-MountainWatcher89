package uk.ac.bournemouth.ap.dotsandboxes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import org.example.student.dotsboxgame.StudentDotsBoxGame

class MainActivity() : AppCompatActivity() {

    var width = 2
    var height = 2


    private lateinit var myGameView: DotsAndBoxesGameView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if(intent.getIntArrayExtra("ID_EXTRA") != null)
        {
            val tempArray = intent.getIntArrayExtra("ID_EXTRA")
            width = tempArray[0]
            height = tempArray[1]
        }

        myGameView = DotsAndBoxesGameView(this)
        myGameView.myGameInstance = StudentDotsBoxGame(width,height,
                                                       listOf(StudentDotsBoxGame.NamedHumanPlayer("Player 1"), StudentDotsBoxGame.EasyAI("Computer 1")))
        setContentView(myGameView)
    }
}
