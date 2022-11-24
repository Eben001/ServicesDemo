package com.ebenezer.gana.servicesdemo

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import android.provider.Settings

class MyService:Service() {

    private var player:MediaPlayer? = null

    override fun onBind(intent: Intent?): IBinder? {
        //this is usually for bound services, we won't use this
        return null
    }
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        //service runs in Main thread so for heavy work, offload to background thread

        if(intent?.extras?.containsKey(ACTION_KEY) == true){
            val action = intent.getStringExtra(ACTION_KEY)?.let { MusicAction.valueOf(it) }
            if(action == MusicAction.STOP){
                player?.stop()
                //services can be stopped by itself by calling stopSelf()
            }
        }else if(intent?.extras?.containsKey(PLAY_KEY) == true){
            val play = intent.getStringExtra(PLAY_KEY)?.let { MusicPlay.valueOf(it) }
            playMusic(play)
        }

        //Restart the service if system kills it for some reason
        return START_STICKY
    }

    private fun playMusic(musicType: MusicPlay?) {
        val uri = when(musicType){
            MusicPlay.ALARM -> Settings.System.DEFAULT_ALARM_ALERT_URI
            else -> Settings.System.DEFAULT_RINGTONE_URI
        }
        if(player?.isPlaying == true){
            player?.stop()
        }
        player = MediaPlayer.create(this, uri).apply {
            isLooping = true
            start()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        player?.let {
            it.stop()
            it.release()
        }
    }


}