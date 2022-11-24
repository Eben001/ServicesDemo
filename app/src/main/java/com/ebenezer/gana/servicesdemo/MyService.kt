package com.ebenezer.gana.servicesdemo

import android.app.*
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.os.IBinder
import android.provider.Settings
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat

private const val CHANNEL_ID = "1000"

class MyService:Service() {

    private var player:MediaPlayer? = null

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createChannel() {
        val mNotificationManager = applicationContext
            .getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val name:String = getString(R.string.app_name)
        //The user-visible name of the channel
        // The user-visible description of the channel
        val description = "Service example notification channel"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val mChannel = NotificationChannel(CHANNEL_ID,
            name, importance)

        //Configure the notification channel
        mChannel.description = description
        mChannel.setShowBadge(false)
        mChannel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
        mNotificationManager.createNotificationChannel(mChannel)
    }

    override fun onCreate() {
        super.onCreate()
        //API 26
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            createChannel()
        }
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent,
        0)
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(getString(R.string.my_service_running))
            .setContentText(getString(R.string.app_name))
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentIntent(pendingIntent)
            .build()

        //needs to be called within 5 seconds of call to foreground service
        startForeground(1,notification)
    }

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