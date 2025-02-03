package com.example.chess_timer

import android.os.Bundle
import android.os.CountDownTimer
import androidx.appcompat.app.AppCompatActivity
import com.example.chess_timer.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private var player1Time = 600000L // 10 perc
    private var player2Time = 600000L
    private var currentPlayer = 1
    private var isRunning = false

    private var player1Timer: CountDownTimer? = null
    private var player2Timer: CountDownTimer? = null

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fun updateTextView(timeInMillis: Long, isPlayer1: Boolean) {
            val minutes = timeInMillis / 60000
            val seconds = (timeInMillis % 60000) / 1000
            if (isPlayer1) {
                binding.player1Timer.text = String.format("%02d:%02d", minutes, seconds)
            } else {
                binding.player2Timer.text = String.format("%02d:%02d", minutes, seconds)
            }
        }

        fun startPlayer1Timer() {
            player1Timer?.cancel()
            player1Timer = object : CountDownTimer(player1Time, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    player1Time = millisUntilFinished
                    updateTextView(player1Time, true)
                }

                override fun onFinish() {
                    binding.player1Timer.text = "Time's up!"
                }
            }.start()
        }

        fun startPlayer2Timer() {
            player2Timer?.cancel()
            player2Timer = object : CountDownTimer(player2Time, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    player2Time = millisUntilFinished
                    updateTextView(player2Time, false)
                }

                override fun onFinish() {
                    binding.player2Timer.text = "Time's up!"
                }
            }.start()
        }

        binding.player1Button.setOnClickListener {
            if (!isRunning || currentPlayer == 2) {
                player2Timer?.cancel()
                startPlayer1Timer()
                currentPlayer = 1
                isRunning = true
            }
        }

        binding.player2Button.setOnClickListener {
            if (!isRunning || currentPlayer == 1) {
                player1Timer?.cancel()
                startPlayer2Timer()
                currentPlayer = 2
                isRunning = true
            }
        }

        binding.resetButton.setOnClickListener {
            player1Timer?.cancel()
            player2Timer?.cancel()
            player1Time = 600000L
            player2Time = 600000L
            updateTextView(player1Time, true)
            updateTextView(player2Time, false)
            isRunning = false
        }

        // Frissítsd az időzítő szövegeket az induláskor
        updateTextView(player1Time, true)
        updateTextView(player2Time, false)
    }
}
