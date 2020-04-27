package uk.ac.bournemouth.ap.dotsandboxes

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import org.example.student.dotsboxgame.StudentDotsBoxGame
import uk.ac.bournemouth.ap.dotsandboxeslib.DotsAndBoxesGame
import uk.ac.bournemouth.ap.dotsandboxeslib.Player
import uk.ac.bournemouth.ap.dotsandboxeslib.HumanPlayer

class DotsAndBoxesGameView : View
{
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) :
            super(context, attrs, defStyleAttr)

    //Default values for grid and player list
    private var gridHeight: Int = 7
    private var gridWidth: Int = 7
    var players: List<Player> = listOf(HumanPlayer(), StudentDotsBoxGame.easyAI(""))

    //Secondary constructor that accepts new grid size and player list parameters
    constructor(context: Context?,
                recColumnCount: Int, recRowCount: Int, recPlayers: List<Player>) : super(context) {

        //If the game detects a grid height less than 3 or greater than 9, it will set the grid height to 7
        if(this.gridHeight >= 3 && this.gridHeight <= 9)
        {
            this.gridHeight = recColumnCount
        }

        //If the game detects a grid width less than 3 or greater than 9, it will set the grid width to 7
        if(this.gridWidth >= 3 && this.gridWidth <= 9)
        {
            this.gridWidth = recRowCount
        }

        //If the game detects a list of players that is too small or too large, it will start the
        //game with its default player list
        if (myGameInstance.players.size >= 2 && myGameInstance.players.size <= 4)
        {
            this.players = recPlayers
        }



    }

    //Set the view properties
    private var myBackgroundGridPaint: Paint
    private var myDotPaint: Paint
    private var myNeuturalBoxPaint: Paint
    private var myPlayer1BoxPaint: Paint
    private var myPlayer2BoxPaint: Paint
    private var myPlayer3BoxPaint: Paint
    private var myPlayer4BoxPaint: Paint
    private var myLineUndrawnPaint: Paint
    private var myLineDrawnPaint: Paint

    var maxGridElementDiameter: Float = 0f
    //
    var myListenerImp = object: DotsAndBoxesGame.GameChangeListener
    {
        override fun onGameChange(game: DotsAndBoxesGame) {
            invalidate()
        }
    }

    //Instantiate the StudentDotsBoxGame class
    private val myGameInstance: StudentDotsBoxGame =
        StudentDotsBoxGame(gridWidth,gridHeight, players)

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

        myLineDrawnPaint = Paint().apply {
            style = Paint.Style.FILL
            color = Color.GREEN
        }

        myGameInstance.setGameChangeListener(myListenerImp)
    }

    override fun onDraw(canvas: Canvas)
    {
        super.onDraw(canvas)

        var paint: Paint = myNeuturalBoxPaint
        var paintValue: Int = 0

        val viewWidth: Float = width.toFloat()
        val viewHeight: Float = height.toFloat()

        val diameterX: Float= viewWidth / gridHeight.toFloat()
        val diameterY: Float= viewHeight / gridWidth.toFloat()

        // Choose the smallest of the two
        if (diameterX < diameterY)
            maxGridElementDiameter = diameterX
        else
            maxGridElementDiameter = diameterY

        // Draw the game board
        canvas.drawRect(0.toFloat(), 0.toFloat(), viewWidth, viewHeight, myBackgroundGridPaint)

        val gridElementRadius = maxGridElementDiameter / 2

        //Draw gid elements on the game board
        for(row in 0 until gridWidth)
        {
            for(column in 0 until gridHeight)
            {

                //If both the row and column are even, the grid element is a dot
                if((row % 2 == 0) && (column % 2 == 0))
                {
                    // Calculate the co-ordinates of the circle
                    val cx = maxGridElementDiameter * column + gridElementRadius
                    val cy = maxGridElementDiameter * row + gridElementRadius
                    canvas.drawCircle(cx, cy, gridElementRadius / 2, myDotPaint)
                }
                //If the row number is even, but the column number is odd, then the grid element is a horizontal line
                //else if((row % 2 == 0) && (column % 2 != 0))
                else if(myGameInstance.lines[row, column].isHorizontal())
                {
                    // Calculate the co-ordinates of the horizontal line
                    val leftSideX = (maxGridElementDiameter * column)
                    val topY = (maxGridElementDiameter * row) + ((maxGridElementDiameter / 2) - (maxGridElementDiameter / 6))
                    val rightSideX = (maxGridElementDiameter * (column + 1))
                    val bottomY = (maxGridElementDiameter * row) + ((maxGridElementDiameter / 2) + (maxGridElementDiameter / 6))
                    //(maxGridElementDiameter * column) + ((gridElementRadius) - (maxGridElementDiameter * 0.15f))

                    if(myGameInstance.lines[row, column].isDrawn)
                    {
                        paint = myLineDrawnPaint
                    }
                    else
                    {
                        paint = myLineUndrawnPaint
                    }
                    canvas.drawRect(leftSideX, topY, rightSideX, bottomY, paint)
                }
                //If the row number is odd, but the column number is even, then the grid element is a vertical line
                else if(myGameInstance.lines[row, column].isVertical())
                {
                    // Calculate the co-ordinates of the vertical line
                    val leftSideX = (maxGridElementDiameter * column) + ((maxGridElementDiameter / 2) - (maxGridElementDiameter / 6))
                    val topY = (maxGridElementDiameter * row)
                    val rightSideX = (maxGridElementDiameter * column) + ((maxGridElementDiameter / 2) + (maxGridElementDiameter / 6))
                    val bottomY = (maxGridElementDiameter * (row + 1))
                    //(maxGridElementDiameter * column) + ((gridElementRadius) - (maxGridElementDiameter * 0.15f))

                    if(myGameInstance.lines[row, column].isDrawn)
                    {
                        paint = myLineDrawnPaint
                    }
                    else
                    {
                        paint = myLineUndrawnPaint
                    }
                    canvas.drawRect(leftSideX, topY, rightSideX, bottomY, paint)
                }
                //If both the row number and column number are odd, then the grid element is a box
                else
                {
                    // Calculate the co-ordinates of the box
                    val leftSideX = (maxGridElementDiameter * column)
                    val topY = (maxGridElementDiameter * row)
                    val rightSideX = (maxGridElementDiameter * (column + 1))
                    val bottomY = (maxGridElementDiameter * (row + 1))

                    //Player index from the player list is retrieved
                    paintValue = calculateBoxPaint(row, column)

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
            return true
        }

        override fun onSingleTapUp(ev: MotionEvent): Boolean {
            //Calculate width of a grid element space in pixels
            val elementWidth = width / maxGridElementDiameter

            //Calculate the column the user has touched
            val columnTouch = (ev.x / elementWidth).toInt()

            //Calculate the row the user has touched
            val rowTouch = (ev.y / elementWidth).toInt()

            //Tell the game logic that the user has chosen a line
            myGameInstance.playTurnToken(columnTouch, rowTouch)

            //Get the computer players to make their moves
            myGameInstance.playComputerTurns()

            return true
        }
    }


}