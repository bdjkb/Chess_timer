package com.example.chess_timer

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity : AppCompatActivity() {

    private lateinit var player1NameEditText: EditText
    private lateinit var player2NameEditText: EditText
    private lateinit var timerSpinner: Spinner
    private lateinit var addTimeCheckBox: CheckBox
    private lateinit var startButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        // Initialize views
        player1NameEditText = findViewById(R.id.player1NameEditText)
        player2NameEditText = findViewById(R.id.player2NameEditText)
        timerSpinner = findViewById(R.id.timerSpinner)
        addTimeCheckBox = findViewById(R.id.addTimeCheckBox)
        startButton = findViewById(R.id.startButton)

        // Set up start button click listener
        startButton.setOnClickListener {
            val player1Name = player1NameEditText.text.toString()
            val player2Name = player2NameEditText.text.toString()
            val selectedTime = timerSpinner.selectedItem.toString().toInt() * 60000 // Convert minutes to milliseconds
            val isAddTimeEnabled = addTimeCheckBox.isChecked

            // Pass data to the main game activity
            val intent = Intent(this, MainActivity::class.java).apply {
                putExtra("player1Name", player1Name)
                putExtra("player2Name", player2Name)
                putExtra("timerDuration", selectedTime)
                putExtra("isAddTimeEnabled", isAddTimeEnabled)
            }
            startActivity(intent)
        }
    }
}
