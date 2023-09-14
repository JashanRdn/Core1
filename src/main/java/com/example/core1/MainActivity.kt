package com.example.core1

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.core.graphics.toColor
import kotlin.random.Random

const val KEY_ROLLED = "keyRolled"
const val KEY_CURRENT_COUNT = "keyCurrentCount"

const val LIFE_CYCLE = "LIFE_CYCLE"
const val USER_INPUT = "USER_INPUT"
const val APP_OPERATIONS = "APP_OPERATIONS"

class MainActivity : ComponentActivity() {

    //Values
    private var rolled = 0
    private var currentCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.i(LIFE_CYCLE, "App created.")

        //Visual feedback
        val countText = findViewById<TextView>(R.id.count)
        val diceImage = findViewById<ImageView>(R.id.dice)
        diceImage.imageAlpha = 0
        val diceImages : List<Int> = listOf(
            R.drawable.dice_1, R.drawable.dice_2,
            R.drawable.dice_3, R.drawable.dice_4,
            R.drawable.dice_5, R.drawable.dice_6)

        //Buttons
        val roll = findViewById<Button>(R.id.roll)
        val add = findViewById<Button>(R.id.add)
        val subtract = findViewById<Button>(R.id.subtract)
        val reset = findViewById<Button>(R.id.reset)

        fun resetRoll()
        {
            rolled  = 0
            diceImage.imageAlpha = 0
            Log.i(APP_OPERATIONS, "The Roll has been reset. Rolled: $rolled")
        }

        fun updateCountText()
        {
            countText.text = currentCount.toString()
            Log.i(APP_OPERATIONS, "Count text changed to ${countText.text}. Current Count is $currentCount.")

            var textColor = Color.BLACK
            when
            {
                currentCount == 20 -> textColor = Color.GREEN
                currentCount > 20 -> textColor = Color.RED
            }
            Log.i(APP_OPERATIONS, "Changed Count Text color to ${textColor.toColor()}")
            countText.setTextColor(textColor)
        }

        //Checking saved
        if (savedInstanceState != null)
        {
            Log.i(APP_OPERATIONS, "Found an saved instance state.")
            rolled = savedInstanceState.getInt(KEY_ROLLED)
            if (rolled != 0)
            {
                diceImage.setImageDrawable(getDrawable(diceImages[rolled - 1]))
                diceImage.imageAlpha = UByte.MAX_VALUE.toInt()
            }

            currentCount = savedInstanceState.getInt(KEY_CURRENT_COUNT)
            updateCountText()

            Log.i(APP_OPERATIONS, "Extracted Current Count: $currentCount. Extracted Rolled: $rolled")

        }

        val random = Random(1)
        roll.setOnClickListener()
        {
            Log.i(USER_INPUT, "Roll button clicked.")
            if (rolled == 0)
            {
                rolled = random.nextInt(1,7)
                Log.i(APP_OPERATIONS, "Rolled the dice for $rolled.")

                diceImage.setImageDrawable(getDrawable(diceImages[rolled - 1]))
                diceImage.imageAlpha = UByte.MAX_VALUE.toInt()
            }
            else
            {
                Log.i(USER_INPUT, "Cannot roll again yet. The user needs to add or subtract the current roll first.")
            }
        }

        add.setOnClickListener()
        {
            Log.i(USER_INPUT, "Add button clicked.")
            if (rolled != 0)
            {
                Log.i(APP_OPERATIONS, "Adding $rolled to $currentCount.")
                currentCount += rolled
                updateCountText()

                resetRoll()
            }
            else
            {
                Log.i(USER_INPUT, "Cannot add. The user needs to roll the dice first.")
            }
        }

        subtract.setOnClickListener()
        {
            Log.i(USER_INPUT, "Subtract button clicked.")
            if (rolled != 0 && currentCount != 0)
            {
                Log.i(APP_OPERATIONS, "Subtracting $rolled from $currentCount.")
                currentCount = if (currentCount < rolled) 0 else currentCount - rolled
                updateCountText()

                resetRoll()
            }
            else
            {
                Log.i(USER_INPUT, "Cannot subtract. The user needs to roll the dice first.")
            }
        }

        reset.setOnClickListener()
        {
            Log.i(USER_INPUT, "Reset button clicked.")
            currentCount = 0
            updateCountText()

            resetRoll()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.i(LIFE_CYCLE, "Saving data.")

        outState.putInt(KEY_CURRENT_COUNT, currentCount)
        outState.putInt(KEY_ROLLED, rolled)
        Log.i(APP_OPERATIONS, "Data Saved. Current Count: $currentCount. Rolled: $rolled.")
    }
}