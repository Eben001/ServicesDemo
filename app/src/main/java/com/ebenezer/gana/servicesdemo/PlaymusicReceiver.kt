package com.ebenezer.gana.servicesdemo

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

const val ACTION_MUSIC_PLAYING = "playing_music"
const val ACTION_MUSIC_STOP_PLAYING = "stop_playing_music"
class PlaymusicReceiver(private inline val onMusicPlayed:() -> Unit,
private inline val onMusicStopped:() -> Unit):BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if(intent?.action == ACTION_MUSIC_PLAYING){
            onMusicPlayed.invoke()
        }else if(intent?.action ==ACTION_MUSIC_STOP_PLAYING){
            onMusicStopped.invoke()
        }
    }
}