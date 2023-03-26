package com.example.dice

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import kotlin.random.Random

class ActivityGameWindow : AppCompatActivity() {

//    private var checkBox1: CheckBox? = null
//    private var checkBox2: CheckBox? = null
//    private var checkBox3: CheckBox? = null
//    private var checkBox4: CheckBox? = null
//    private var checkBox5: CheckBox? = null
//
//    private val checkBoxes = arrayOf<CheckBox>(
//        findViewById(R.id.radioDice01),
//        findViewById(R.id.radioDice02),
//        findViewById(R.id.radioDice03),
//        findViewById(R.id.radioDice04),
//        findViewById(R.id.radioDice05)
//    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_window)
        showTargetScorePopup()
        randomDice()

        val topLayout = findViewById<LinearLayout>(R.id.diceSelectorGroup)
        topLayout.bringToFront()

        scoreButton = findViewById(R.id.btnScore)
        scoreButton.setOnClickListener{
            scoreButton.isEnabled = false
            scoreCalculator()
        }
    }

    private lateinit var rollDiceButton: Button
    private lateinit var scoreButton: Button
    private var scoreClicked = true
    private var numOfRolls = 0
    private var targetScore: Int = 101 // default target score

    @SuppressLint("SetTextI18n")
    private fun showTargetScorePopup() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle("Set Target Score")

        // Set up the input field
        val input = EditText(this)
        input.inputType = InputType.TYPE_CLASS_NUMBER
        input.setText(targetScore.toString())
        builder.setView(input)

        val targetScoreText: TextView = findViewById(R.id.targetScore)

        // Set up the OK button
        builder.setPositiveButton("OK") { _, _ ->
            val newTargetScore = input.text.toString().toIntOrNull()
            if (newTargetScore != null && newTargetScore > 0) {
                targetScore = newTargetScore
                targetScoreText.text = "Target Score: " +targetScore
            } else {
                // handle invalid input
                Toast.makeText(this, "Invalid target score", Toast.LENGTH_SHORT).show()
                targetScoreText.text = "Target Score: " +targetScore
            }
        }
        // Set up the cancel button
        builder.setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }

        builder.show()
    }

    @SuppressLint("SetTextI18n")
    private fun randomDice (){

        val playerDice: Array<ImageView> = arrayOf(
            findViewById(R.id.playerDice1),
            findViewById(R.id.playerDice2),
            findViewById(R.id.playerDice3),
            findViewById(R.id.playerDice4),
            findViewById(R.id.playerDice5)
        )
        val botDice: Array<ImageView> = arrayOf(
            findViewById(R.id.botDice1),
            findViewById(R.id.botDice2),
            findViewById(R.id.botDice3),
            findViewById(R.id.botDice4),
            findViewById(R.id.botDice5)
        )

        rollDiceButton = findViewById(R.id.btnThrow)
        val playerDiceValue: TextView = findViewById(R.id.playerDiceValue) // Variable for show sum of 5 dices values of player for each new roll
        val botDiceValue : TextView = findViewById(R.id.botDiceValue)  // Variable for show sum of 5 dices values of bot for each new roll
        val botScore: TextView = findViewById(R.id.botScore)

        rollDiceButton.setOnClickListener {
            numOfRolls++
            // Generate random numbers for each die and set the corresponding ImageView
            for (die in playerDice) {
                val randomNumber = (1..6).random()
                val drawableResource = when (randomNumber) {
                    1 -> R.drawable.dice1
                    2 -> R.drawable.dice2
                    3 -> R.drawable.dice3
                    4 -> R.drawable.dice4
                    5 -> R.drawable.dice5
                    else -> R.drawable.dice6
                }
                die.setImageResource(drawableResource)
                die.contentDescription = randomNumber.toString() // Fix: set contentDescription to the random number
            }

            // Calculate and display the total score for player one
            val playerTotal = playerDice.map { die ->
                die.contentDescription.toString().toInt()
            }.sum()
            playerDiceValue.text =playerTotal.toString()

            // Generate random numbers for each die and set the corresponding ImageView
            for (die in botDice) {
                val randomNumber = (1..6).random()
                val drawableResource = when (randomNumber) {
                    1 -> R.drawable.dice1
                    2 -> R.drawable.dice2
                    3 -> R.drawable.dice3
                    4 -> R.drawable.dice4
                    5 -> R.drawable.dice5
                    else -> R.drawable.dice6
                }
                die.setImageResource(drawableResource)
                die.contentDescription = randomNumber.toString() // Fix: set contentDescription to the random number
            }

            // Calculate and display the total score for player two
            val botTotal = botDice.map { die ->
                die.contentDescription.toString().toInt()
            }.sum()
            botDiceValue.text = botTotal.toString()
            scoreCalculator()
            scoreButton.isEnabled = true
        }
    }


//    private fun getDiceImage(value: Int): Drawable {
//        return when (value) {
//            1 -> ContextCompat.getDrawable(this, R.drawable.dice1)!!
//            2 -> ContextCompat.getDrawable(this, R.drawable.dice2)!!
//            3 -> ContextCompat.getDrawable(this, R.drawable.dice3)!!
//            4 -> ContextCompat.getDrawable(this, R.drawable.dice4)!!
//            5 -> ContextCompat.getDrawable(this, R.drawable.dice5)!!
//            6 -> ContextCompat.getDrawable(this, R.drawable.dice6)!!
//            else -> throw IllegalArgumentException("Invalid dice value: $value")
//        }
//    }

    @SuppressLint("SetTextI18n")
    private fun scoreCalculator() {
        scoreButton.isEnabled = false

        val playerScore: TextView = findViewById(R.id.playerScore)
        val botScore: TextView = findViewById(R.id.botScore)

        // Get the current scores for both the player and the bot
        var currentHumanScore = playerScore.text.toString().substringAfter(": ").toInt()
        var currentBotScore = botScore.text.toString().substringAfter(": ").toInt()

        val playerDiceValue: TextView = findViewById(R.id.playerDiceValue)
        val botDiceValue: TextView = findViewById(R.id.botDiceValue)


        // Calculate the score for the player's current turn based on the sum of their dice values
        val playerTurnScore = playerDiceValue.text.toString().toInt()
        val botTurnScore = botDiceValue.text.toString().toInt()

        // If the player's score is zero, they can't score in this turn
        if (playerTurnScore == 0) {
            Toast.makeText(this, "You cannot score with a zero sum", Toast.LENGTH_SHORT).show()
            return
        }

        var playerScoreButton : Button = findViewById(R.id.btnScore)

        // Check if the player has used all three rolls or if they clicked on the Score button
        if (numOfRolls > 2|| playerScoreButton.isPressed) {
            scoreButton.isEnabled = false
            // Update the player's score for this turn and the overall score
            currentHumanScore += playerTurnScore
            playerScore.text = "Your Score: " + currentHumanScore.toString().substringAfter(": ")

            // Calculate the bot's score for this turn based on the current strategy
            currentBotScore += botTurnScore
            botScore.text = "Bot Score: " + currentBotScore.toString().substringAfter(": ")

            // Reset the number of rolls for the next turn
            numOfRolls = 0

            if(playerScore == botScore){ // roll until tie break
                randomDice()
            }
        }

        if (currentHumanScore > targetScore){
            if(playerScore == botScore){ // roll until tie break
                randomDice()
            }

            val builder = AlertDialog.Builder(this)
            val inflater = this.layoutInflater
            val view = inflater.inflate(R.layout.won_message_popup, null)
            builder.setView(view)
            val dialog = builder.create()
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.show()
            dialog.setCancelable(false)
            val wonOkButton : Button = view.findViewById(R.id.winnerOkButton)
            wonOkButton.setOnClickListener {
                finish()
                if (dialog != null && dialog.isShowing) {
                    dialog.dismiss()
                }
            }
        }

        else if (currentBotScore > targetScore){
            if(playerScore == botScore){ // roll until tie break
                randomDice()
            }

            val builder = AlertDialog.Builder(this)
            val inflater = this.layoutInflater
            val view = inflater.inflate(R.layout.lose_message_popup, null)
            builder.setView(view)
            val dialog = builder.create()
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.show()
            dialog.setCancelable(false)
            val loseOkButton : Button = view.findViewById(R.id.loseOkButton)
            loseOkButton.setOnClickListener {
                finish()
                if (dialog != null && dialog.isShowing) {
                    dialog.dismiss()
                }
            }
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setContentView(R.layout.activity_game_window)
            Log.i("OrientationChange", "landscape")
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            setContentView(R.layout.activity_game_window)
            Log.i("OrientationChange", "portrait")
        }

        // Initialize the views and assign the listeners
        val rollButton = findViewById<Button>(R.id.btnThrow)
        rollButton.setOnClickListener { randomDice() }
        val resetButton = findViewById<Button>(R.id.btnScore)
        resetButton.setOnClickListener { scoreCalculator() }
    }
}

