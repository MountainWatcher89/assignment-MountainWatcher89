package uk.ac.bournemouth.ap.dotsandboxes

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class DotsAndBoxesGameView: View
{
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) :
            super(context, attrs, defStyleAttr)

    //Will need to be changed to be variable
    private val gridColumnCount = 7
    private val gridRowCount = 7

    private var myBackgroundGridPaint: Paint
    private var myDotPaint: Paint
    private var myNeuturalBoxPaint: Paint
    private var myPlayerBoxPaint: Paint
    private var myComputerBoxPaint: Paint
    private var myLineUndrawnPaint: Paint
    private var myLineDrawnPaint: Paint

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
    }

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
        for(row in 0 until gridColumnCount)
        {
            for(column in 0 until gridRowCount)
            {
                //If both the row and column are even, the grid element is a dot
                if((row % 2 == 0) || (column % 2 == 0))
                {
                    // Calculate the co-ordinates of the circle
                    val cx = maxGridElementDiameter * column + gridElementRadius
                    val cy = maxGridElementDiameter * row + gridElementRadius
                    canvas.drawCircle(cx, cy, gridElementRadius, myDotPaint)
                }
                //If the row number is even, but the column number is odd, then the grid element is a horizontal line
                else if((row % 2 == 0) || (column % 2 != 0))
                {
                    // Calculate the co-ordinates of the horizontal line
                    val coordinateX = (maxGridElementDiameter * column) + ((gridElementRadius) - (maxGridElementDiameter * 0.15f))
                    val coordinateY = maxGridElementDiameter * row

                    canvas.drawRect(coordinateX, coordinateY, maxGridElementDiameter, (maxGridElementDiameter * 0.3f), myNeuturalBoxPaint)
                }
                //If the row number is odd, but the column number is even, then the grid element is a vertical line
                else if((row % 2 != 0) || (column % 2 == 0))
                {
                    // Calculate the co-ordinates of the vertical line


                }
                //If both the row number and column number are odd, then the grid element is a box
                else
                {
                    // Calculate the co-ordinates of the box
                    val coordinateX = maxGridElementDiameter * column + gridElementRadius
                    val coordinateY = maxGridElementDiameter * row + gridElementRadius

                    canvas.drawRect(coordinateX, coordinateY, maxGridElementDiameter, maxGridElementDiameter, myNeuturalBoxPaint)
                }
            }
        }
    }
}