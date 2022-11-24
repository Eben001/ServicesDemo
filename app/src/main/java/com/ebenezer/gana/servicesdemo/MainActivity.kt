package com.ebenezer.gana.servicesdemo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.playRingtone.setOnClickListener {
            startService(PLAY_KEY, RINGTONE.name)
        }
        binding.playAlarm.setOnClickListener {
            startService(PLAY_KEY, ALARM.name)

        }
        binding.stopPlaying.setOnClickListener {
            startService(ACTION_KEY, STOP.name)

        }
        binding.stopService.setOnClickListener {
            stopService(Intent(this, MyService::class.java))
        }
    }

    private fun startService(key: String, value: String) {
        startService(Intent(this, MyService::class.java).apply {
            putExtra(key, value)
        })
    }

}