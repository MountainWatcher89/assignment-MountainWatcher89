package org.example.student.dotsboxgame

import uk.ac.bournemouth.ap.dotsandboxeslib.*
import uk.ac.bournemouth.ap.dotsandboxeslib.matrix.Matrix
import uk.ac.bournemouth.ap.dotsandboxeslib.matrix.MutableMatrix
import java.lang.IllegalStateException
import kotlin.Pair

class StudentDotsBoxGame(
    receivedGridWidth: Int,
    receivedGridHeight: Int,
    receivedPlayerList: List<Player>
                        ) : AbstractDotsAndBoxesGame() {

    //The width and height values include spaces for lines between the boxes

    private val gridWidth: Int = receivedGridWidth * 2 + 1
    private val gridHeight: Int = receivedGridHeight * 2 + 1
    override val players: List<Player> = receivedPlayerList.toList()
    override var isFinished: Boolean = false

    private var currentPlayerIndex: Int = 0
    override val currentPlayer: Player get() = players[currentPlayerIndex]

    // NOTE: you may want to me more specific in the box type if you use that type in your class

    override val boxes: List<StudentBox> get() = myBoxes.filter { it.isValid() }

    val myBoxes: Matrix<StudentBox> =
        MutableMatrix<StudentBox>(gridWidth, gridHeight, ::StudentBox)

    override val lines: List<StudentLine> get() = myLines.filter { it.isValid() } //MutableMatrix<StudentLine>(gridWidth, gridHeight, ::StudentLine)

    val myLines: Matrix<StudentLine> =
        MutableMatrix<StudentLine>(gridWidth, gridHeight, ::StudentLine)

    private fun getFinalScores(): List<Pair<Player, Int>> {
        return  players.map{ player ->
            val score = boxes.count(){ it.owningPlayer == player}
            player to score
        }
    }

    fun getWinner(): Player {
        val recScores = getFinalScores()
        var highestScore: Int = 0
        var highestScoringPlayer: Player = recScores.first().first
        for (i in 0 until recScores.size) {
            if (recScores[i].second > highestScore) {
                highestScoringPlayer = recScores[i].first
                highestScore = recScores[i].second
            }
        }

        return highestScoringPlayer
    }

    //A list of all of the un-drawn lines of the game, and their coordinates on the grid
    var unDrawnLines = createDrawnLines()

    fun createDrawnLines(): MutableList<MutableList<Pair<Int, Int>>> {
        val retVal = mutableListOf<MutableList<Pair<Int, Int>>>()
        //The 2D array is filled with zeroes, indicating that none of the lines have been drawn yet
        for (gridX in 0 until gridWidth) {
            val individualColumn = mutableListOf<Pair<Int, Int>>()
            for (gridY in 0 until gridHeight) {
                //Line will only be added if it has valid coordinates
                if (myLines[gridX, gridY].isValid()) {
                    individualColumn.add(Pair(gridX, gridY))
                }
            }
            //Add the current column to the 2D array
            retVal.add(individualColumn)
        }
        return retVal
    }

    fun printUndrawnLines() {
        for (element in unDrawnLines) {
            println(element)
        }
    }

    //https://www.youtube.com/channel/UCV7dSg_qGYnuwMZ8BNO-FQQ/videos


    fun getTurnToken(recColumn: Int, recRow: Int): Player? {
        return myBoxes[recColumn, recRow].owningPlayer
    }

    fun playTurnToken(recGridX: Int, recGridY: Int): Boolean
    {
        var boxCompleted = false
        val selectedLine = myLines[recGridX, recGridY]

        if (currentPlayerIndex < 0) {
            throw IllegalArgumentException("Current player index cannot be less than 0")
        }
        //If the line from the grid of lines selected is a valid line,
        // draw the line
        if (!selectedLine.isDrawn && selectedLine.isValid()) {
            //Set the line's state on the logical game grid to drawn
            selectedLine.isDrawn = true

            //printUndrawnLines()
            println("Coordinates of line selected to be drawn are : <" + recGridX + ", " + recGridY + ">")

            unDrawnLines[recGridX].removeAll{ it.first == recGridX && it.second == recGridY}

            //Check for box completion. If one or both of the boxes adjacent to the drawn line are
            //completed, the current player is granted an additional turn

            if (selectedLine.adjacentBoxes.first != null) {
                if (selectedLine.adjacentBoxes.first!!.checkBoxCompletion()) {
                    boxCompleted = true
                }
            }
            if (selectedLine.adjacentBoxes.second != null) {
                if (selectedLine.adjacentBoxes.second!!.checkBoxCompletion()) {
                    boxCompleted = true
                }
            }

            fireMyGameChange()
            fireGameChange()

            if (boxCompleted)
            {
                //Check if all lines have been drawn
                if (myLines.all{ !it.isValid() || it.isDrawn}) {
                    //Fire the game over event
                    isFinished = true
                    fireMyGameOver(getFinalScores())
                    fireGameOver(getFinalScores())
                }
            }
            else
            {
                //In the event of a player not completing a box on their turn, Increment the current
                //player variable, so that the next player will get their turn
                if (currentPlayerIndex < (players.size - 1)) {
                    //Increment current player by one
                    currentPlayerIndex += 1
                } else {
                    //Loop back around to the first player in the player list
                    currentPlayerIndex = 0
                }

            }

        }
        return boxCompleted
    }


    // Variable that holds the reference to the 'onGameChange' function in the game view class
    var onGameChangeListener: DotsAndBoxesGame.GameChangeListener? = null

    fun setGameChangeListener(myGameChangeListenerImp: DotsAndBoxesGame.GameChangeListener) {
        onGameChangeListener = myGameChangeListenerImp
    }

    fun fireMyGameChange() {
        onGameChangeListener?.onGameChange(this)
    }

    //Variable that holds the refernce to the 'onGameOver' function in the game view class
    var onGameOverListener: DotsAndBoxesGame.GameOverListener? = null

    fun setGameOverListener(myGameOverListenerImp: DotsAndBoxesGame.GameOverListener) {
        onGameOverListener = myGameOverListenerImp
    }

    fun fireMyGameOver(playerScores: List<Pair<Player, Int>>) {
        onGameOverListener?.onGameOver(this, playerScores)
    }

    override fun playComputerTurns() {
        while (currentPlayer is ComputerPlayer && !isFinished) {
            (currentPlayer as ComputerPlayer).makeMove(this)
        }
    }

    init {

        //Need to call the method that plays all computer turns, in the event the first player in
        //the player list is a computer player
        playComputerTurns()
    }

    /**
     * This is an inner class as it needs to refer to the game to be able to look up the correct
     * lines and boxes. Alternatively you can have a game property that does the same thing without
     * it being an inner class.
     */

    fun tryToDrawLine(recLineX: Int, recLineY: Int)
    {
        if(!myLines[recLineY,  recLineY].isDrawn)
        {
            myLines[recLineX, recLineY].drawLine()
        }
    }

    inner class StudentLine(lineX: Int, lineY: Int) : AbstractLine(lineX, lineY) {
        override var isDrawn: Boolean = false

        fun isValid(): Boolean {
            var result = false
            //The line is only valid if its X or Y coordinates are greater than 0, and less than
            if ((this.lineX >= 0 && this.lineX < gridWidth) && (this.lineY >= 0 && this.lineY < gridHeight)) {
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
        //Need to add checks so that a line on the edge of the grid only has one adjacent box
        override var adjacentBoxes: Pair<StudentBox?, StudentBox?> = Pair(null, null)
            get() {
                if (this.isValid()) {
                    //Line is horizontal
                    if (isHorizontal() && (this.lineY == 0)) {
                        //Get the box below the line
                        field = Pair(null, myBoxes[this.lineX, this.lineY + 1])
                    } else if (isHorizontal() && (this.lineY == (gridHeight - 1))) {
                        //Get the box above the line
                        field = Pair(myBoxes[this.lineX, this.lineY - 1], null)
                    } else if (isHorizontal()) {
                        //Get the boxes below and above the line
                        field = Pair(
                            myBoxes[this.lineX, this.lineY - 1],
                            myBoxes[this.lineX, this.lineY + 1]
                                    )
                    }
                    //Line is horizontal
                    else if (isVertical() && (this.lineX == 0)) {
                        //Get the box to the right of the line
                        field = Pair(null, myBoxes[this.lineX + 1, this.lineY])
                    } else if (isVertical() && (this.lineX == (gridWidth - 1))) {
                        //Get the box to the left of the line
                        field = Pair(myBoxes[this.lineX - 1, this.lineY], null)
                    } else if (isVertical()) {
                        //Get the boxes to the left and right of the line
                        field = Pair(
                            myBoxes[this.lineX - 1, this.lineY],
                            myBoxes[this.lineX + 1, this.lineY])
                    }
                }

                return field
            }

        fun isHorizontal(): Boolean {
            if ((this.lineX % 2 != 0) && (this.lineY % 2 == 0))
                return true
            else
                return false
        }

        fun isVertical(): Boolean {
            if ((this.lineX % 2 == 0) && (this.lineY % 2 != 0))
                return true
            else
                return false
        }

        override fun drawLine() {
            //isDrawn = true
            if(isDrawn)
            {
                throw IllegalStateException("Line already drawn")
            }

            val boxWasCompleted = playTurnToken(lineX, lineY)

            //If a human player draws a line but it does not complete a box, the next player, a
            //computer player, gets their turn.
            if(!boxWasCompleted && (currentPlayer is ComputerPlayer) || (currentPlayer is EasyAI))
            {
                playComputerTurns()
            }

            //fireMyGameChange()
            //playComputerTurns()
            // NOTE read the documentation in the interface, you must also update the current player.
        }

        override fun toString(): String {
            return "StudentLine($lineX, $lineY, isDrawn=$isDrawn)"
        }


    }

    inner class StudentBox(boxX: Int, boxY: Int) : AbstractBox(boxX, boxY) {

        override var owningPlayer: Player? = null
            get() {
                return field
            }
            set(value) {
                field = value
            }

        /**
         * This must be lazy or a getter, otherwise there is a chicken/egg problem with the boxes
         */
        //Look up the correct lines from the game outer class
        override val boundingLines: List<StudentLine>
            get() = listOf(
                (myLines[boxX, boxY - 1]),
                (myLines[boxX, boxY + 1]),
                (myLines[boxX - 1, boxY]),
                (myLines[boxX + 1, boxY])
                          )

        fun checkBoxCompletion(): Boolean {
            //If all surrounding lines of the box has been drawn, the box now belongs to the player
            //that drew the current line
            if (boundingLines.all{ it.isDrawn }) {
                this.owningPlayer = currentPlayer;
                return true
            }
            else {
                return false
            }
        }

        fun isValid(): Boolean {
            if ((this.boxX % 2 != 0) && (this.boxY % 2 != 0))
                return true
            else
                return false
        }
    }

    class EasyAI(val recName: String) : ComputerPlayer() {
        public var name: String = ""

        init {
            this.name = recName
        }

        override fun makeMove(gameRef: DotsAndBoxesGame) {

            val line = gameRef.lines.filter { !it.isDrawn }.random()
            line.drawLine()

            /*
               if (gameRef is StudentDotsBoxGame) {
                //Select a random column of the grid
                val chosenColumn: MutableList<Pair<Int, Int>> = gameRef.unDrawnLines.random()

                //Select a random line from the column
                val chosenColumnLine = chosenColumn.random()

                //Invoke the playTurnToken method using the selected line
                gameRef.playTurnToken(chosenColumnLine.first, chosenColumnLine.second)
            }*/
        }
    }

    class NamedHumanPlayer(recName: String) : HumanPlayer() {
        public var name: String = ""
            get() {
                return field
            }
            set(value) {
                field = value
            }

        init {
            this.name = recName
        }
    }

}