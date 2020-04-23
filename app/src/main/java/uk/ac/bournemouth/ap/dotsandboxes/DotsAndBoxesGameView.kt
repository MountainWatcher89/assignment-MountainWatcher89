package uk.ac.bournemouth.ap.dotsandboxes

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Build
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.annotation.RequiresApi
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
    private var gridColumnCount: Int = 7
    private var gridRowCount: Int = 7
    var players: List<Player> = listOf(HumanPlayer(), StudentDotsBoxGame.easyAI())

    //Secondary constructor that accepts new grid size and player list parameters
    constructor(context: Context?,
                recColumnCount: Int, recRowCount: Int, recPlayers: List<Player>) : super(context) {
        this.gridColumnCount = recColumnCount
        this.gridRowCount = recRowCount
        this.players = recPlayers
    }

    private var myBackgroundGridPaint: Paint
    private var myDotPaint: Paint
    private var myNeuturalBoxPaint: Paint
    private var myPlayerBoxPaint: Paint
    private var myComputerBoxPaint: Paint
    private var myLineUndrawnPaint: Paint
    private var myLineDrawnPaint: Paint

    //
    var myListenerImp = object: DotsAndBoxesGame.GameChangeListener
    {
        override fun onGameChange(game: DotsAndBoxesGame) {
            invalidate()
        }
    }

    //Instantiate the StudentDotsBoxGame class
    private val myGameInstance: StudentDotsBoxGame =
        StudentDotsBoxGame(3,3, players)

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

        myPlayerBoxPaint = Paint().apply {
            style = Paint.Style.FILL
            color = Color.RED
        }

        myComputerBoxPaint = Paint().apply {
            style = Paint.Style.FILL
            this.color = Color.rgb(127, 0, 255)
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

    private val myGestureDetector = GestureDetector(context, myGestureListener())

    override fun onDraw(canvas: Canvas)
    {
        super.onDraw(canvas)

        val maxGridElementDiameter: Float
        var tokenAtPos: Int
        var paint: Paint

        val viewWidth: Float = width.toFloat()
        val viewHeight: Float = height.toFloat()

        val diameterX: Float= viewWidth / gridColumnCount.toFloat()
        val diameterY: Float= viewHeight / gridRowCount.toFloat()

        // Choose the smallest of the two
        if (diameterX < diameterY)
            maxGridElementDiameter = diameterX
        else
            maxGridElementDiameter = diameterY

        // Draw the game board
        canvas.drawRect(0.toFloat(), 0.toFloat(), viewWidth, viewHeight, myBackgroundGridPaint)

        val gridElementRadius = maxGridElementDiameter / 2

        //Draw gid elements on the game board
        for(row in 0 until gridRowCount)
        {
            for(column in 0 until gridColumnCount)
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
                //else if((row % 2 == 0) || (column % 2 != 0))
                else if((row % 2 == 0) && (column % 2 != 0))
                {
                    // Calculate the co-ordinates of the horizontal line
                    val leftSideX = (maxGridElementDiameter * column)
                    val topY = (maxGridElementDiameter * row) + ((maxGridElementDiameter / 2) - (maxGridElementDiameter / 6))
                    val rightSideX = (maxGridElementDiameter * (column + 1))
                    val bottomY = (maxGridElementDiameter * row) + ((maxGridElementDiameter / 2) + (maxGridElementDiameter / 6))
                    //(maxGridElementDiameter * column) + ((gridElementRadius) - (maxGridElementDiameter * 0.15f))
                    canvas.drawRect(leftSideX, topY, rightSideX, bottomY, myLineUndrawnPaint)
                }
                //If the row number is odd, but the column number is even, then the grid element is a vertical line
                else if((row % 2 != 0) && (column % 2 == 0))
                {
                    // Calculate the co-ordinates of the vertical line
                    val leftSideX = (maxGridElementDiameter * column) + ((maxGridElementDiameter / 2) - (maxGridElementDiameter / 6))
                    val topY = (maxGridElementDiameter * row)
                    val rightSideX = (maxGridElementDiameter * column) + ((maxGridElementDiameter / 2) + (maxGridElementDiameter / 6))
                    val bottomY = (maxGridElementDiameter * (row + 1))
                    //(maxGridElementDiameter * column) + ((gridElementRadius) - (maxGridElementDiameter * 0.15f))
                    canvas.drawRect(leftSideX, topY, rightSideX, bottomY, myLineUndrawnPaint)

                }
                //If both the row number and column number are odd, then the grid element is a box
                else
                {
                    // Calculate the co-ordinates of the box
                    val leftSideX = (maxGridElementDiameter * column)
                    val topY = (maxGridElementDiameter * row)
                    val rightSideX = (maxGridElementDiameter * (column + 1))
                    val bottomY = (maxGridElementDiameter * (row + 1))
                    canvas.drawRect(leftSideX, topY, rightSideX, bottomY, myNeuturalBoxPaint)
                }

            }
        }
    }
    //End of onDraw function

    override fun onTouchEvent(ev: MotionEvent): Boolean
    {
        return myGestureDetector.onTouchEvent(ev) || super.onTouchEvent(ev)
    }

    inner class myGestureListener: GestureDetector.SimpleOnGestureListener()
    {
        override fun onDown(ev: MotionEvent): Boolean {
            return true
        }

        override fun onSingleTapUp(e: MotionEvent?): Boolean {
            //Calculate what the coordinates of the touch event are

            return true
        }
    }


}