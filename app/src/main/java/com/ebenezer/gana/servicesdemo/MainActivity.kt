package com.ebenezer.gana.servicesdemo

import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.ebenezer.gana.servicesdemo.MusicAction.*
import com.ebenezer.gana.servicesdemo.MusicPlay.*
import com.ebenezer.gana.servicesdemo.databinding.ActivityMainBinding

const val ACTION_KEY = "ACTION_KEY"

enum class MusicAction {
    STOP
}

const val PLAY_KEY = "PLAY_KEY"

enum class MusicPlay {
    RINGTONE,
    ALARM
}

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val receiver by lazy {
        PlaymusicReceiver(onMusicPlayed = {
            Toast.makeText(this, "Music Playing...", Toast.LENGTH_SHORT).show()

        }, onMusicStopped = {
            Toast.makeText(this, "Music Stopped Playing...", Toast.LENGTH_SHORT).show()

        })

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.playRingtone.setOnClickListener {
            startForegroundService(PLAY_KEY, RINGTONE.name)
        }
        binding.playAlarm.setOnClickListener {
            startForegroundService(PLAY_KEY, ALARM.name)

        }
        binding.stopPlaying.setOnClickListener {
            startForegroundService(ACTION_KEY, STOP.name)

        }
        binding.stopService.setOnClickListener {
            stopService(Intent(this, MyService::class.java))
        }
    }

    private fun startForegroundService(key: String, value: String) {
        ContextCompat.startForegroundService(this, Intent(this, MyService::class.java).apply {
            putExtra(key, value)
        })
    }

    override fun onStart() {
        super.onStart()
        registerReceiver(receiver, IntentFilter().apply {
            addAction(ACTION_MUSIC_PLAYING)
            addAction(ACTION_MUSIC_STOP_PLAYING)
        })
    }

    override fun onStop() {
        unregisterReceiver(receiver)
        super.onStop()
    }

}