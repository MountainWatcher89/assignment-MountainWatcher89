package uk.ac.bournemouth.ap.dotsandboxes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.SeekBar
import kotlinx.android.synthetic.main.activity_game_settings.*
import org.example.student.dotsboxgame.StudentDotsBoxGame

class GameSettingsActivity : AppCompatActivity(), SeekBar.OnSeekBarChangeListener {

    private lateinit var myGameView: DotsAndBoxesGameView
    lateinit var btn_start_game: Button
    lateinit var bar_width: SeekBar
    lateinit var bar_height: SeekBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        myGameView = DotsAndBoxesGameView(this)

        setContentView(R.layout.activity_game_settings)
        seekBarWidth.setOnSeekBarChangeListener(this)

        bar_width = findViewById(R.id.seekBarWidth)
        bar_height = findViewById(R.id.seekBarHeight)
        btn_start_game = findViewById(R.id.btn_start_game)

        btn_start_game.setOnClickListener()
        {
            val intent = Intent(this, DotsAndBoxesGameView::class.java)

            myGameView.myGameInstance = StudentDotsBoxGame(bar_width.progress, bar_height.progress,
                                                           listOf(StudentDotsBoxGame.NamedHumanPlayer("Player 1"), StudentDotsBoxGame.EasyAI("Computer 1")))

            startActivity(intent)
        }

    }

    override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
        if(seekBar.id == R.id.seekBarWidth)
        {
            textView.setText("gameSize: ${seekBar.progress + 3}")
        }

    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {

    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {

    }




}
