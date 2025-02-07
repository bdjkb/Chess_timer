package com.example.chess_timer

import android.media.MediaPlayer
import android.media.RingtoneManager
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.graphics.Color // Import this line
import androidx.core.content.ContextCompat
import com.example.chess_timer.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private var player1Time: Long = 600000 // 10 minutes
    private var player2Time: Long = 600000
    private var currentPlayer = 1
    private var isRunning = false
    private var isPaused = false

    private var player1Timer: CountDownTimer? = null
    private var player2Timer: CountDownTimer? = null

    private lateinit var binding: ActivityMainBinding
    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.player1Timer.text = "10:00"
        binding.player2Timer.text = "10:00"
        binding.pauseButton.text = "Pause"
        binding.resetButton.text = "Reset"

        isPaused = false // Initially not paused
        enableTimersAndReset() // Timers and reset enabled at the start

        updateTextView(player1Time, true)
        updateTextView(player2Time, false)
        updatePlayerColors()

        binding.root.setOnClickListener { switchPlayer() }
        binding.pauseButton.setOnClickListener { pauseTimers() }
        binding.resetButton.setOnClickListener { confirmReset() }

        binding.pauseButton.setOnClickListener {
            if (isPaused) {
                resumeTimers()
                binding.pauseButton.text = "Pause"
                binding.pauseButton.setBackgroundColor(ContextCompat.getColor(this, android.R.color.darker_gray))
                enableTimersAndReset() // Timers and reset enabled when resumed
            } else {
                pauseTimers()
                binding.pauseButton.text = "Resume"
                binding.pauseButton.setBackgroundColor(Color.BLUE)
                disableTimersAndReset() // Timers and reset disabled when paused
            }
        }
    }

    private fun resumeTimers() {
        isPaused = false
        if (currentPlayer == 1) {
            startPlayer1Timer()
        } else {
            startPlayer2Timer()
        }
        isRunning = true
    }

    private fun enableTimersAndReset() {
        binding.player1Timer.isEnabled = true
        binding.player2Timer.isEnabled = true
        binding.resetButton.isEnabled = true
        updatePlayerColors() // Update colors based on current player
    }

    private fun disableTimersAndReset() {
        binding.player1Timer.isEnabled = false
        binding.player2Timer.isEnabled = false
        // Gray out timers when disabled
        binding.player1Timer.setBackgroundColor(Color.GRAY)
        binding.player2Timer.setBackgroundColor(Color.GRAY)
    }

    private fun updateTextView(timeInMillis: Long, isPlayer1: Boolean) {
        val minutes = timeInMillis / 60000
        val seconds = (timeInMillis % 60000) / 1000
        val time = String.format("%02d:%02d", minutes, seconds)
        if (isPlayer1) {
            binding.player1Timer.text = time
        } else {
            binding.player2Timer.text = time
        }
    }

    private fun startPlayer1Timer() {
        cancelTimers() // Cancel any existing timer before starting a new one
        player1Timer = object : CountDownTimer(player1Time, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                player1Time = millisUntilFinished
                updateTextView(player1Time, true)
            }

            override fun onFinish() {
                binding.player1Timer.text = "Time's up!"
            }
        }.start()
        updatePlayerColors() // Ensure the current player's timer has the correct color
    }

    private fun startPlayer2Timer() {
        cancelTimers() // Cancel any existing timer before starting a new one
        player2Timer = object : CountDownTimer(player2Time, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                player2Time = millisUntilFinished
                updateTextView(player2Time, false)
            }

            override fun onFinish() {
                binding.player2Timer.text = "Time's up!"
            }
        }.start()
        updatePlayerColors() // Ensure the current player's timer has the correct color
    }

    private fun switchPlayer() {
        if (isPaused) return // Ne engedj váltást, ha szünetel
        playSwitchSound()

        // Leállítjuk az aktuális időzítőt
        cancelTimers()

        // Ha az első játékos van soron, indítjuk a második játékos időzítőjét, és fordítva
        if (currentPlayer == 1) {
            currentPlayer = 2
            startPlayer2Timer() // Indítjuk a második játékos időzítőjét
        } else {
            currentPlayer = 1
            startPlayer1Timer() // Indítjuk az első játékos időzítőjét
        }

        isRunning = true
        updatePlayerColors() // Frissítjük a háttérszíneket váltáskor
    }

    private fun pauseTimers() {
        if (!isRunning) return // Prevent pausing if timers haven't started

        cancelTimers() // Stop current timer
        isPaused = !isPaused // Toggle the paused state

        if (isPaused) {
            disableTimersAndReset()
        } else {
            enableTimersAndReset()
            if (currentPlayer == 1) {
                startPlayer1Timer()
            } else {
                startPlayer2Timer()
            }
        }
    }

    private fun confirmReset() {
        AlertDialog.Builder(this)
            .setTitle("Reset Timer")
            .setMessage("Biztosan visszaállítod az időzítőt?")
            .setPositiveButton("Igen") { _, _ -> resetTimers() }
            .setNegativeButton("Mégse", null)
            .show()
    }

    private fun resetTimers() {
        cancelTimers()
        player1Time = 600000
        player2Time = 600000
        updateTextView(player1Time, true)
        updateTextView(player2Time, false)
        isRunning = false
        isPaused = false
        updatePlayerColors() // Update colors after reset
        enableTimersAndReset() // Timers and reset enabled after reset
        binding.pauseButton.text = "Pause" // Reset button text
    }

    private fun cancelTimers() {
        player1Timer?.cancel()
        player2Timer?.cancel()
    }

    private fun updatePlayerColors() {
        // Ha az aktuális játékos 1, akkor a player1 aktív lesz, a player2 pedig szürke
        if (currentPlayer == 1) {
            // Világosabb kék háttér a player1-hez
            binding.player1Timer.setBackgroundColor(Color.parseColor("#ADD8E6")) // Light Sky Blue
            // Szürke háttér a player2-höz
            binding.player2Timer.setBackgroundColor(getColor(android.R.color.darker_gray)) // Inaktív
        } else {
            // Világosabb zöld háttér a player2-höz
            binding.player2Timer.setBackgroundColor(Color.parseColor("#90EE90")) // Pale Green
            // Szürke háttér a player1-hez
            binding.player1Timer.setBackgroundColor(getColor(android.R.color.darker_gray)) // Inaktív
        }
    }



    private fun playSwitchSound() {
        val notification: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        mediaPlayer?.release() // Release previous instance
        mediaPlayer = MediaPlayer().apply {
            setDataSource(applicationContext, notification)
            prepare()
            start()
        }
    }
}

