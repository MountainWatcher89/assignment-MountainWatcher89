package org.example.student.dotsboxgame

import uk.ac.bournemouth.ap.dotsandboxeslib.*
import uk.ac.bournemouth.ap.dotsandboxeslib.matrix.Matrix
import uk.ac.bournemouth.ap.dotsandboxeslib.matrix.MutableMatrix

class StudentDotsBoxGame(givenGridWidth: Int, givenGridHeight: Int, receivedPlayerList: List<Player>) : AbstractDotsAndBoxesGame() {

    //The width and height values include spaces for lines between the boxes

    private val gridWidth: Int = givenGridWidth
    private val gridHeight: Int = givenGridHeight

    override val players: List<Player> = receivedPlayerList

    private var currentPlayerIndex: Int = 0

    override val currentPlayer: Player = players[0]


    // NOTE: you may want to me more specific in the box type if you use that type in your class

    //override val boxes: Matrix<DotsAndBoxesGame.Box> = TODO("Create a matrix initialized with your own box type")
    override val boxes: Matrix<StudentBox> = MutableMatrix<StudentBox>(gridWidth, gridHeight, ::StudentBox)
//https://www.youtube.com/channel/UCV7dSg_qGYnuwMZ8BNO-FQQ/videos

    override val lines: Matrix<StudentLine> = MutableMatrix<StudentLine>(gridWidth, gridHeight, ::StudentLine)

    override val isFinished: Boolean = false

    //Need to add gesture-related variables


    fun getTurnToken(recColumn: Int, recRow: Int): Player?
    {
        return boxes[recColumn, recRow].owningPlayer
    }

    fun playTurnToken(recColumn: Int, recRow: Int): Boolean
    {
        if(currentPlayerIndex < 0)
        {
            throw IllegalArgumentException("Current player index cannot be less than 0")
        }

        for (line in lines)
        {
            if(lines[recColumn, recRow].isDrawn == false)
            {
                lines[recColumn, recRow].isDrawn = true
                return true
            }
        }
        return false
    }

    // Variable that holds the reference to the 'onGameChange' function in the game view class
    var onGameChangeListener: DotsAndBoxesGame.GameChangeListener? = null

    fun setGameChangeListener(myListenerImp: DotsAndBoxesGame.GameChangeListener)
    {
        onGameChangeListener = myListenerImp
    }

    override fun fireGameChange()
    {
        onGameChangeListener?.onGameChange(this)
    }

    override fun playComputerTurns() {
        var current = currentPlayer
        while (current is ComputerPlayer && ! isFinished) {
            current.makeMove(this)
            current = currentPlayer
        }
    }

    /**
     * This is an inner class as it needs to refer to the game to be able to look up the correct
     * lines and boxes. Alternatively you can have a game property that does the same thing without
     * it being an inner class.
     */
    inner class StudentLine(lineX: Int, lineY: Int) : AbstractLine(lineX, lineY) {
        override var isDrawn: Boolean = false

        fun isValid(): Boolean {
            var result = false
            //The line is only valid if its X or Y coordinates are greater than 0, and less than
            if((this.lineX >= 0 && this.lineX < gridWidth) && (this.lineY >= 0 && this.lineY < gridHeight))
            {
                //Line is horizontal
                if (isHorizontal())
                    result = true
                //Line is vertical
                else if (isVertical())
                    result = true
            }
            return result
        }

        //"You need to look up the correct boxes for this to work")
        override var adjacentBoxes: Pair<StudentBox?, StudentBox?> = Pair(null, null)
            get() {
                if (this.isValid())
                {
                    //Line is horizontal
                    if (isHorizontal())
                        //Get the box above and below the line
                        field = Pair(boxes[this.lineX, this.lineY - 1], boxes[this.lineX, this.lineY + 1])
                    //Line is vertical
                    else if (isVertical())
                        //Get the box to the left and right of the line
                        field = Pair(boxes[this.lineX - 1, this.lineY], boxes[this.lineX + 1, this.lineY])
                }

                return field
            }

        fun isHorizontal(): Boolean
        {
            if((this.lineX % 2 != 0) && (this.lineY % 2 == 0))
                return true
            else
                return false
        }

        fun isVertical(): Boolean
        {
            if((this.lineX % 2 == 0) && (this.lineY % 2 != 0))
                return true
            else
                return false
        }

        override fun drawLine() {
            TODO("Implement the logic for a player drawing a line. Don't forget to inform the listeners (fireGameChange, fireGameOver)")
            // NOTE read the documentation in the interface, you must also update the current player.
        }
    }

    inner class StudentBox(boxX: Int, boxY: Int) : AbstractBox(boxX, boxY) {

        override var owningPlayer: Player? = null
            get()
            {
                return field
            }
            set(value)
            {
                field = value
            }

        /**
         * This must be lazy or a getter, otherwise there is a chicken/egg problem with the boxes
         */
        //Look up the correct lines from the game outer class
        override val boundingLines: MutableList<StudentLine> = mutableListOf()
            get()
            {
                return field
            }

        fun setBoundingLines()
        {
            //Get the line above the box
            boundingLines.add(lines[this.boxX, this.boxY - 1])

            //Get the line below the box
            boundingLines.add(lines[this.boxX, this.boxY + 1])

            //Get the line to the left of the box
            boundingLines.add(lines[this.boxX - 1, this.boxY])

            //Get the line to the right of the box
            boundingLines.add(lines[this.boxX + 1, this.boxY])
        }

        fun isValid(): Boolean {
            if ((this.boxX % 2 != 0) && (this.boxY % 2 != 0))
                return true
            else
                return false
        }
    }

    class easyAI(): ComputerPlayer()
    {
        override fun makeMove(game: DotsAndBoxesGame) {
            TODO("Not yet implemented")
        }
    }

    //Need to add another inner class here for gesture events
}