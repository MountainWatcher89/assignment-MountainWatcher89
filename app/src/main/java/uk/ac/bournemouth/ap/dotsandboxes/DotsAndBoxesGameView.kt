package uk.ac.bournemouth.ap.dotsandboxes

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import org.example.student.dotsboxgame.StudentDotsBoxGame
import uk.ac.bournemouth.ap.dotsandboxeslib.DotsAndBoxesGame
import uk.ac.bournemouth.ap.dotsandboxeslib.Player

class DotsAndBoxesGameView : View
{
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) :
            super(context, attrs, defStyleAttr)

    //Default values for grid and player list
    private val gridHeight: Int get() = myGameInstance.myBoxes.height
    private val gridWidth: Int get() = myGameInstance.myBoxes.width
    val players: List<Player> get() = myGameInstance.players



    var winningPlayer: Player? = null

    //Set the view properties
    private var myBackgroundGridPaint: Paint
    private var myDotPaint: Paint
    private var myNeuturalBoxPaint: Paint
    private var myPlayer1BoxPaint: Paint
    private var myPlayer2BoxPaint: Paint
    private var myPlayer3BoxPaint: Paint
    private var myPlayer4BoxPaint: Paint
    private var myLineUndrawnPaint: Paint
    private var myLineDrawnByPlayerPaint: Paint
    private var myLineDrawnByComputerPaint: Paint
    private var myTextPaint: Paint

    var maxGridElementDiameter: Float = 0f
    //
    val myGameChangeListenerImp = object: DotsAndBoxesGame.GameChangeListener
    {
        override fun onGameChange(game: DotsAndBoxesGame) {
            invalidate()
        }
    }

    //Need to fix this
    val myGameOverListenerImp = object: DotsAndBoxesGame.GameOverListener
    {
        override fun onGameOver(game: DotsAndBoxesGame, playerScoreList: List<Pair<Player, Int>>)
        {
            invalidate()
        }
    }

    //Instantiate the StudentDotsBoxGame class. The height and width values can be overridden
    var myGameInstance: StudentDotsBoxGame =
        StudentDotsBoxGame(3, 3,
                           listOf(StudentDotsBoxGame.NamedHumanPlayer("Player 1"), StudentDotsBoxGame.EasyAI("Computer 1")))
    set(value) {
        field.removeOnGameChangeListener(myGameChangeListenerImp)
        field.removeOnGameOverListener(myGameOverListenerImp)
        field = value
        field.addOnGameChangeListener(myGameChangeListenerImp)
        field.addOnGameOverListener(myGameOverListenerImp)
    }




    init
    {
        myBackgroundGridPaint = Paint().apply {
            style = Paint.Style.FILL
            color = Color.DKGRAY
        }

        myDotPaint = Paint().apply {
            style = Paint.Style.FILL
            color = Color.LTGRAY
        }

        myNeuturalBoxPaint = Paint().apply {
            style = Paint.Style.FILL
            color = Color.WHITE
        }

        myPlayer1BoxPaint = Paint().apply {
            style = Paint.Style.FILL
            color = Color.RED
        }

        myPlayer2BoxPaint = Paint().apply {
            style = Paint.Style.FILL
            this.color = Color.rgb(127, 0, 255)
        }

        myPlayer3BoxPaint = Paint().apply {
            style = Paint.Style.FILL
            //Computer player 2 uses a blue paint
            this.color = Color.rgb(0, 0, 255)
        }

        myPlayer4BoxPaint = Paint().apply {
            style = Paint.Style.FILL
            //Computer player 3 uses an orange paint
            this.color = Color.rgb(255, 128, 0)
        }

        myLineUndrawnPaint = Paint().apply {
            style = Paint.Style.FILL
            color = Color.LTGRAY
        }

        myLineDrawnByPlayerPaint = Paint().apply {
            style = Paint.Style.FILL
            color = Color.rgb(128, 255, 0)
        }

        myLineDrawnByComputerPaint = Paint().apply {
            style = Paint.Style.FILL
            color = Color.rgb(0, 153, 76)
        }

        myTextPaint = Paint().apply {
            textAlign = Paint.Align.CENTER
            textSize = 25.toFloat()
            typeface = Typeface.SANS_SERIF
            color = Color.BLACK
        }

        myGameInstance.setGameChangeListener(myGameChangeListenerImp)
        myGameInstance.setGameOverListener(myGameOverListenerImp)
    }

    override fun onDraw(canvas: Canvas)
    {
        super.onDraw(canvas)

        var paint: Paint = myNeuturalBoxPaint
        var paintValue: Int = 0

        val viewWidth: Float = width.toFloat()
        val viewHeight: Float = height.toFloat()

        val diameterX: Float = viewWidth / gridWidth.toFloat()
        //There needs to be some room for the text that displays the game information
        val diameterY: Float = viewHeight / (gridHeight.toFloat() * 0.75f)

        // Choose the smallest of the two
        if (diameterX < diameterY)
            maxGridElementDiameter = diameterX
        else
            maxGridElementDiameter = diameterY

        // Draw the game board
        canvas.drawRect(0.toFloat(), 0.toFloat(), viewWidth, viewHeight, myBackgroundGridPaint)

        val gridElementRadius = maxGridElementDiameter / 2

        //Draw grid elements on the game board
        for(gridX in 0 until gridHeight)
        {
            for(gridY in 0 until gridWidth)
            {

                //If both the row and gridX are even, the grid element is a dot
                if((gridY % 2 == 0) && (gridX % 2 == 0))
                {
                    // Calculate the co-ordinates of the circle
                    val cx = maxGridElementDiameter * gridX + gridElementRadius
                    val cy = maxGridElementDiameter * gridY + gridElementRadius
                    canvas.drawCircle(cx, cy, gridElementRadius / 2, myDotPaint)
                }
                //If the row number is even, but the gridX number is odd, then the grid element is a horizontal line
                //else if((row % 2 == 0) && (gridX % 2 != 0))
                else if(myGameInstance.myLines[gridX, gridY].isHorizontal())
                {
                    // Calculate the co-ordinates of the horizontal line
                    val leftSideX = (maxGridElementDiameter * gridX)
                    val topY = (maxGridElementDiameter * gridY) + ((maxGridElementDiameter / 2) - (maxGridElementDiameter / 6))
                    val rightSideX = (maxGridElementDiameter * (gridX + 1))
                    val bottomY = (maxGridElementDiameter * gridY) + ((maxGridElementDiameter / 2) + (maxGridElementDiameter / 6))
                    //(maxGridElementDiameter * gridX) + ((gridElementRadius) - (maxGridElementDiameter * 0.15f))

                    if(myGameInstance.myLines[gridX, gridY].isDrawn)
                    {
                        paint = myLineDrawnByPlayerPaint
                    }
                    else
                    {
                        paint = myLineUndrawnPaint
                    }
                    canvas.drawRect(leftSideX, topY, rightSideX, bottomY, paint)
                }
                //If the row number is odd, but the gridX number is even, then the grid element is a vertical line
                else if(myGameInstance.myLines[gridX, gridY].isVertical())
                {
                    // Calculate the co-ordinates of the vertical line
                    val leftSideX = (maxGridElementDiameter * gridX) + ((maxGridElementDiameter / 2) - (maxGridElementDiameter / 6))
                    val topY = (maxGridElementDiameter * gridY)
                    val rightSideX = (maxGridElementDiameter * gridX) + ((maxGridElementDiameter / 2) + (maxGridElementDiameter / 6))
                    val bottomY = (maxGridElementDiameter * (gridY + 1))
                    //(maxGridElementDiameter * gridX) + ((gridElementRadius) - (maxGridElementDiameter * 0.15f))

                    if(myGameInstance.myLines[gridX, gridY].isDrawn)
                    {
                        paint = myLineDrawnByPlayerPaint
                    }
                    else
                    {
                        paint = myLineUndrawnPaint
                    }
                    canvas.drawRect(leftSideX, topY, rightSideX, bottomY, paint)
                }
                //If both the row number and gridX number are odd, then the grid element is a box
                else
                {
                    // Calculate the co-ordinates of the box
                    val leftSideX = (maxGridElementDiameter * gridX)
                    val topY = (maxGridElementDiameter * gridY)
                    val rightSideX = (maxGridElementDiameter * (gridX + 1))
                    val bottomY = (maxGridElementDiameter * (gridY + 1))

                    //Player index from the player list is retrieved
                    paintValue = calculateBoxPaint(gridX, gridY)

                    //Paint is set based on player index in the players list
                    if(paintValue == 0)
                    {
                        paint = myNeuturalBoxPaint
                    }
                    else if(paintValue == 1)
                    {
                        paint = myPlayer1BoxPaint
                    }
                    else if(paintValue == 2)
                    {
                        paint = myPlayer2BoxPaint
                    }
                    else if(paintValue == 3)
                    {
                        paint = myPlayer3BoxPaint
                    }
                    else if(paintValue == 4)
                    {
                        paint = myPlayer4BoxPaint
                    }
                    canvas.drawRect(leftSideX, topY, rightSideX, bottomY, paint)
                }
            }
        }

        //Draw the game text that displays all player scores
        var textSpace = 30
        var counter = 0
        val scores = myGameInstance.getScores()
        for(player in players)
        {
            if(player is StudentDotsBoxGame.NamedHumanPlayer)
            {
                canvas.drawText(
                    player.name + " score: " + scores[counter].toString(),
                                (viewWidth / 2f), (viewHeight.toFloat() * 0.75f) + textSpace, myTextPaint)
            }
            else if(player is StudentDotsBoxGame.EasyAI)
            {
                canvas.drawText(
                    player.name + " score: " + scores[counter].toString(),
                                (viewWidth / 2f), (viewHeight.toFloat() * 0.75f) + textSpace, myTextPaint)
            }
            counter++
            textSpace = textSpace + 60
        }

        if(myGameInstance.isFinished)
        {
            winningPlayer = myGameInstance.getWinner()
            if(winningPlayer is StudentDotsBoxGame.NamedHumanPlayer)
            {
                if(!myGameInstance.checkForDraw())
                {
                    canvas.drawText(
                        (winningPlayer as StudentDotsBoxGame.NamedHumanPlayer).name + " is the winner!",
                        (width.toFloat() / 2f), viewHeight.toFloat() - 30, myTextPaint)
                }
                else
                {
                    canvas.drawText("It's a draw!",
                        (width.toFloat() / 2f), viewHeight.toFloat() - 30, myTextPaint)
                }

            }
            else if(myGameInstance.getWinner() is StudentDotsBoxGame.EasyAI)
            {
                if(!myGameInstance.checkForDraw())
                {
                    canvas.drawText(
                        (winningPlayer as StudentDotsBoxGame.EasyAI).name + " is the winner!",
                        (width.toFloat() / 2f), viewHeight.toFloat() - 30, myTextPaint)
                }
                else
                {
                    canvas.drawText("It's a draw!",
                                    (width.toFloat() / 2f), viewHeight.toFloat() - 30, myTextPaint)
                }
            }
        }

    }
    //End of onDraw function

    private fun calculateBoxPaint(recRow: Int, recColumn: Int ): Int
    {
        //Select the correct colour for the box
        if(myGameInstance.players.size == 2)
        {
            if(myGameInstance.players.get(0) == myGameInstance.getTurnToken(recRow, recColumn))
            {
                return 1
            }
            else if(myGameInstance.players.get(1) == myGameInstance.getTurnToken(recRow, recColumn))
            {
                return 2
            }
            //Box does not have an owning player
            else
            {
                return 0
            }
        }
        else if(myGameInstance.players.size == 3)
        {
            if(myGameInstance.players.get(0) == myGameInstance.getTurnToken(recRow, recColumn))
            {
                return 1
            }
            else if(myGameInstance.players.get(1) == myGameInstance.getTurnToken(recRow, recColumn))
            {
                return 2
            }
            else if(myGameInstance.players.get(2) == myGameInstance.getTurnToken(recRow, recColumn))
            {
                return 3
            }
            //Box does not have an owning player
            else
            {
                return 0
            }
        }
        else if(myGameInstance.players.size == 4)
        {
            if(myGameInstance.players.get(0) == myGameInstance.getTurnToken(recRow, recColumn))
            {
                return 1
            }
            else if(myGameInstance.players.get(1) == myGameInstance.getTurnToken(recRow, recColumn))
            {
                return 2
            }
            else if(myGameInstance.players.get(2) == myGameInstance.getTurnToken(recRow, recColumn))
            {
                return 3
            }
            else if(myGameInstance.players.get(3) == myGameInstance.getTurnToken(recRow, recColumn))
            {
                return 4
            }
            //Box does not have an owning player
            else
            {
                return 0
            }
        }
        else
        {
            return 0
        }
    }


    private val myGestureDetector = GestureDetector(context, myGestureListener())

    override fun onTouchEvent(ev: MotionEvent): Boolean
    {
        return myGestureDetector.onTouchEvent(ev) || super.onTouchEvent(ev)
    }

    inner class myGestureListener: GestureDetector.SimpleOnGestureListener()
    {
        override fun onDown(ev: MotionEvent): Boolean {
            return !myGameInstance.isFinished
        }

        override fun onSingleTapUp(ev: MotionEvent): Boolean {
            //Calculate width of a grid element space in pixels
            //maxGridElementDiameter

            //Calculate the column the user has touched
            val columnTouch = (ev.x / maxGridElementDiameter).toInt()

            //Calculate the row the user has touched
            val rowTouch = (ev.y / maxGridElementDiameter).toInt()

            if(ev.x.toInt() < gridWidth * maxGridElementDiameter && ev.y.toInt() < gridHeight * maxGridElementDiameter)
            {
                //Tell the game logic that the user has chosen a line
                //Might need to change this to call the drawLine function
                myGameInstance.tryToDrawLine(columnTouch, rowTouch)

                //Get the computer players to make their moves

                return true
            }
            else
            {
                return false
            }
        }
    }


}