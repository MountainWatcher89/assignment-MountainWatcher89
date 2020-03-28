package uk.ac.bournemouth.ap.dotsandboxes

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet

class DotsAndBoxesGameView
{
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) :
            super(context, attrs, defStyleAttr)
    private val colCount = 7
    private val rowCount = 7
    private var mGridPaint: Paint
    private var mNoPlayerPaint: Paint

    init {
        mGridPaint = Paint().apply {
            style = Paint.Style.FILL
            color = Color.BLUE
        }

        mNoPlayerPaint = Paint().apply {
            style = Paint.Style.FILL
            color = Color.WHITE
        }
    }
}