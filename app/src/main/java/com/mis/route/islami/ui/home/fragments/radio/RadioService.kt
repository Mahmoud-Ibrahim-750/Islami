package com.mis.route.islami.ui.home.fragments.radio

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.view.View
import android.widget.RemoteViews
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.mis.route.islami.R
import com.mis.route.islami.data.api.ApiManager
import com.mis.route.islami.data.api.Radio
import com.mis.route.islami.data.api.RadioResponse
import com.mis.route.islami.ui.Constants
import com.mis.route.islami.ui.home.HomeActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

const val RADIO_SERVICE_ID = 100
const val CLICK_ACTION = "ClickAction"
const val PLAY_ACTION = 1
const val NEXT_ACTION = 2
const val PREVIOUS_ACTION = 3
const val CLOSE_ACTION = 4
const val CHANNEL_ID = "RadioFragChannelId"
const val CHANNEL_NAME = "Radio Media Playback"
const val LOGGING_TAG = "RadioService"

class RadioService : Service() {

    private lateinit var customContentRV: RemoteViews
    private var _mediaPlayer: MediaPlayer? = null
    private val mediaPlayer: MediaPlayer get() = _mediaPlayer!!
    private var mediaPlayerAvailable = false
    private lateinit var radiosList: List<Radio>
    private var currentlyPlaying = false
    private var currentRadioIndex = 0

    private val iBinder: IBinder = LocalBinder()

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        intent?.getIntExtra(CLICK_ACTION, -1)?.let { clickAction ->
            when (clickAction) {
                PLAY_ACTION -> playOrPauseRadio()
                NEXT_ACTION -> playNextRadio()
                PREVIOUS_ACTION -> playPreviousRadio()
                CLOSE_ACTION -> stopService()
                else -> Log.d(LOGGING_TAG, "unknown action with code $clickAction")
            }
        }

        return START_STICKY
    }

    private fun stopService() {
        this.stopSelf()
    }

    override fun onCreate() {
        super.onCreate()
        startForegroundServiceWithNotification()
        loadRadios()
    }

    private fun startForegroundServiceWithNotification() {
        try {
            createNotificationChannel()
            customContentRV = createNotificationCustomView()
            startForeground(RADIO_SERVICE_ID, createNotification(customContentRV))
        } catch (e: Exception) {
            Toast.makeText(
                this, "Error occurred: ${e.message}", Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun createNotification(customContent: RemoteViews): Notification {
        val intent = Intent(this, HomeActivity::class.java)

        val pendingIntent = PendingIntent.getActivity(
            this.applicationContext,
            1,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val notificationBuilder = NotificationCompat
            .Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.radio)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
            .setCustomContentView(customContent)
            .setOnlyAlertOnce(true)

        return notificationBuilder.build()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )

            (getSystemService(NOTIFICATION_SERVICE) as NotificationManager)
                .createNotificationChannel(channel)
        }
    }

    private fun createNotificationCustomView(): RemoteViews {
        val customContentRV = RemoteViews(this.packageName, R.layout.notification_collapsed_content)

        customContentRV.createCustomViewAction(this, R.id.notification_play, PLAY_ACTION)
        customContentRV.createCustomViewAction(this, R.id.notification_next, NEXT_ACTION)
        customContentRV.createCustomViewAction(this, R.id.notification_previous, PREVIOUS_ACTION)
        customContentRV.createCustomViewAction(this, R.id.notification_close, CLOSE_ACTION)
        val radioText = resources.getString(R.string.radio_default_title)
        customContentRV.setTextViewText(R.id.notification_title, radioText)

        return customContentRV
    }

    private fun RemoteViews.createCustomViewAction(
        context: Context,
        viewId: Int,
        actionCode: Int
    ) {
        val actionIntent = Intent(context, RadioService::class.java).apply {
            putExtra(CLICK_ACTION, actionCode)
        }

        val actionPendingIntent = PendingIntent.getService(
            context, actionCode, actionIntent, PendingIntent.FLAG_IMMUTABLE
        )

        this.setOnClickPendingIntent(viewId, actionPendingIntent)
    }

    private fun RemoteViews.updateText(viewId: Int, text: String?) {
        this.setTextViewText(viewId, text)

        (getSystemService(NOTIFICATION_SERVICE) as NotificationManager)
            .notify(RADIO_SERVICE_ID, createNotification(customContentRV))
    }

    private fun RemoteViews.updateImage(viewId: Int, imageResId: Int) {
        this.setImageViewResource(viewId, imageResId)

        (getSystemService(NOTIFICATION_SERVICE) as NotificationManager)
            .notify(RADIO_SERVICE_ID, createNotification(customContentRV))
    }

    private fun RemoteViews.togglePlayingVisibility(playing: Boolean) {
        val visibility = if (playing) View.VISIBLE else View.GONE
        val opposite = if (playing) View.GONE else View.VISIBLE
        this.setViewVisibility(R.id.notification_play, visibility)
        this.setViewVisibility(R.id.notification_loading_progress, opposite)

        (getSystemService(NOTIFICATION_SERVICE) as NotificationManager)
            .notify(RADIO_SERVICE_ID, createNotification(customContentRV))
    }

    private fun RemoteViews.togglePlayingStatus(currentlyPlaying: Boolean) {
        this@RadioService.currentlyPlaying = currentlyPlaying
        val resId = if (currentlyPlaying) R.drawable.baseline_play_arrow_24 else R.drawable.ic_pause
        this.setImageViewResource(R.id.notification_play, resId)

        (getSystemService(NOTIFICATION_SERVICE) as NotificationManager)
            .notify(RADIO_SERVICE_ID, createNotification(customContentRV))
    }

    override fun onDestroy() {
        super.onDestroy()
        // releasing resources
        mediaPlayer.release()
        _mediaPlayer = null
    }

    override fun onBind(intent: Intent?): IBinder {
        return iBinder
    }

    private fun playOrPauseRadio() {
        if (!mediaPlayerAvailable) {
            Toast.makeText(this, "media player not available", Toast.LENGTH_SHORT)
                .show()
            return
        }

        if (currentlyPlaying) {
            mediaPlayer.pause()
            customContentRV.togglePlayingStatus(false)
        } else {
            mediaPlayer.start()
            customContentRV.togglePlayingStatus(true)
        }
    }

    private fun playPreviousRadio() {
        mediaPlayerAvailable = false
        customContentRV.togglePlayingVisibility(false)
        customContentRV.togglePlayingStatus(false)

        currentRadioIndex = if (currentRadioIndex == 0) radiosList.size - 1 else --currentRadioIndex
        playRadioAtCurrentIndex()
    }

    private fun playNextRadio() {
        mediaPlayerAvailable = false
        customContentRV.togglePlayingVisibility(false)
        customContentRV.togglePlayingStatus(false)

        currentRadioIndex = if (currentRadioIndex == radiosList.size - 1) 0 else ++currentRadioIndex
        playRadioAtCurrentIndex()
    }

    private fun playRadioAtCurrentIndex() {
        mediaPlayer.apply {
            reset()
            setDataSource(radiosList[currentRadioIndex].url)
            prepareAsync()
            setOnPreparedListener {
                mediaPlayerAvailable = true
                customContentRV.updateText(
                    R.id.notification_title,
                    radiosList[currentRadioIndex].name
                )
                customContentRV.togglePlayingVisibility(true)
                start()
                customContentRV.togglePlayingStatus(true)
            }
        }

//        saveRadioData()
    }

    private fun getCurrentLanguageCode(): String {
        return if (resources.configuration.locales[0].language == "ar") Constants.ARABIC_LANG_CODE
        else Constants.ENGLISH_LANG_CODE
    }

    private fun loadRadios() {
        customContentRV.togglePlayingVisibility(false)

        ApiManager.getRadiosService()
            .getRadios(language = getCurrentLanguageCode())
            .enqueue(object : Callback<RadioResponse> {
                override fun onResponse(
                    call: Call<RadioResponse>,
                    response: Response<RadioResponse>
                ) {
                    if (response.isSuccessful) {
                        radiosList = response.body()?.radios ?: emptyList()
                        Log.d(LOGGING_TAG, "radios list ready")
                        if (_mediaPlayer == null) initMediaPlayer()
                    } else {
                        Log.d(LOGGING_TAG, "service error: ${response.errorBody().toString()}")
                    }
                }

                override fun onFailure(p0: Call<RadioResponse>, p1: Throwable) {
                    Log.d(LOGGING_TAG, p1.message ?: "service unknown error")
                }
            })
    }

    private fun initMediaPlayer(name: String? = null, url: String? = null) {
        _mediaPlayer = MediaPlayer().apply {
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build()
            )
            setDataSource(url ?: radiosList[currentRadioIndex].url)
            prepareAsync()
            setOnPreparedListener {
                mediaPlayerAvailable = true
                customContentRV.updateText(
                    R.id.notification_title,
                    radiosList[currentRadioIndex].name
                )
                customContentRV.togglePlayingVisibility(true)
                start()
                customContentRV.togglePlayingStatus(true)
            }
        }
    }


    inner class LocalBinder : Binder() {
        fun getService(): RadioService {
            return this@RadioService
        }
    }
}
