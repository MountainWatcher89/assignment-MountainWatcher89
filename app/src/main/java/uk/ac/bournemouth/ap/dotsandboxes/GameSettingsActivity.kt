package uk.ac.bournemouth.ap.dotsandboxes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_game_settings.*
import org.example.student.dotsboxgame.StudentDotsBoxGame

class GameSettingsActivity : AppCompatActivity(), SeekBar.OnSeekBarChangeListener {

    private lateinit var myGameView: DotsAndBoxesGameView
    lateinit var btn_start_game: Button
    lateinit var bar_width: SeekBar
    lateinit var bar_height: SeekBar
    lateinit var bar_difficulty: SeekBar
    lateinit var widthText: TextView
    lateinit var heightText: TextView
    lateinit var aiDificultyText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_settings)

        seekBarWidth.setOnSeekBarChangeListener(this)
        seekBarHeight.setOnSeekBarChangeListener(this)
        seekBarAIDifficulty.setOnSeekBarChangeListener(this)

        bar_width = findViewById(R.id.seekBarWidth)
        bar_height = findViewById(R.id.seekBarHeight)
        bar_difficulty = findViewById(R.id.seekBarAIDifficulty)
        widthText = findViewById(R.id.text_width_label)
        heightText = findViewById(R.id.text_height_label)
        aiDificultyText = findViewById(R.id.text_ai_difficulty_label)
        btn_start_game = findViewById(R.id.btn_start_game)

        btn_start_game.setOnClickListener()
        {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("ID_EXTRA", intArrayOf(bar_width.progress + 2, bar_height.progress + 2, bar_difficulty.progress))

            startActivity(intent)
        }

    }

    override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
        //Need to fix this so that the second text view gets updated
        when(seekBar.id)
        {
            R.id.seekBarWidth -> widthText.text = "Grid width: ${bar_width.progress + 2}"
            R.id.seekBarHeight -> heightText.text = "Grid height ${bar_height.progress + 2}"
            R.id.seekBarAIDifficulty ->
                if(bar_difficulty.progress == 0){
                aiDificultyText.text = "AI difficulty: Easy"
            }
            else if(bar_difficulty.progress == 1)
            {
                aiDificultyText.text = "AI difficulty: Medium"
            }
                else if(bar_difficulty.progress == 2) {
                    aiDificultyText.text = "AI difficulty: Hard"
                }
            else {
                    aiDificultyText.text = "Error"
                }

        }

/*
        if(seekBar.id == R.id.seekBarWidth)
        {
            widthText.text = "Grid width: ${bar_width.progress + 2}"
        }
        else if(seekBar.id == R.id.seekBarHeight)
        {
            heightText.text = "Grid height ${bar_height.progress + 2}"
        }
        */
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {

    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {

    }




}
