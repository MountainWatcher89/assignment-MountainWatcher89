package org.example.student.dotsboxgame

import uk.ac.bournemouth.ap.dotsandboxeslib.*
import uk.ac.bournemouth.ap.dotsandboxeslib.matrix.Matrix
import uk.ac.bournemouth.ap.dotsandboxeslib.matrix.MutableMatrix
import uk.ac.bournemouth.ap.dotsandboxeslib.matrix.SparseMatrix

class StudentDotsBoxGame(gridWidth: Int, gridHeight: Int, receivedPlayerList: List<Player>) : AbstractDotsAndBoxesGame() {

    //Need to decide if the width and height values include spaces for lines between the boxes

    override val players: List<Player> = receivedPlayerList

    private var currentPlayerIndex: Int = 0

    override val currentPlayer: Player
        get() = players[currentPlayerIndex]


    // NOTE: you may want to me more specific in the box type if you use that type in your class

    //override val boxes: Matrix<DotsAndBoxesGame.Box> = TODO("Create a matrix initialized with your own box type")
    override val boxes: Matrix<StudentBox> = MutableMatrix<StudentBox>(gridWidth, gridHeight, ::StudentBox)
//https://www.youtube.com/channel/UCV7dSg_qGYnuwMZ8BNO-FQQ/videos

    override val lines: Matrix<StudentLine> = MutableMatrix<StudentLine>(gridWidth, gridHeight, ::StudentLine)

    //Need to add gesture-related variables

    override val isFinished: Boolean
        get() = TODO("Provide this getter. Note you can make it a var to do so")

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
        override val isDrawn: Boolean
            get() = TODO("Provide this getter. Note you can make it a var to do so")


        override val adjacentBoxes: Pair<StudentBox?, StudentBox?>
            get() {
                TODO("You need to look up the correct boxes for this to work")
            }

        override fun drawLine() {
            TODO("Implement the logic for a player drawing a line. Don't forget to inform the listeners (fireGameChange, fireGameOver)")
            // NOTE read the documentation in the interface, you must also update the current player.
        }
    }

    inner class StudentBox(boxX: Int, boxY: Int) : AbstractBox(boxX, boxY) {

        override val owningPlayer: Player?
            get() = TODO("Provide this getter. Note you can make it a var to do so")

        /**
         * This must be lazy or a getter, otherwise there is a chicken/egg problem with the boxes
         */
        override val boundingLines: Iterable<DotsAndBoxesGame.Line>
            get() = TODO("Look up the correct lines from the game outer class")

    }

    //Need to add another inner class here for gesture events
}