package leegroup.module.alarm.support.helpers

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioManager.STREAM_ALARM
import android.media.MediaPlayer
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.core.net.toUri
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import leegroup.module.alarm.data.models.AlarmModel
import leegroup.module.alarm.support.extensions.audioManager
import leegroup.module.alarm.support.extensions.vibrator
import leegroup.module.core.extensions.orFalse
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext

@Singleton
internal class AlarmSoundAndVibrateHelper @Inject constructor(@ApplicationContext private val context: Context) :
    CoroutineScope {

    private val vibrator: Vibrator get() = context.vibrator
    private val audioManager get() = context.audioManager

    private var initialVolume = audioManager.getStreamVolume(STREAM_ALARM)
    private var mediaPlayer: MediaPlayer? = null
    private var volumeJob: Job? = null
    private val increaseVolumeGradually = true

    fun start(alarm: AlarmModel) {
        startSound(alarm)
        startVibration(alarm)
    }

    private fun startSound(alarm: AlarmModel) {
        if (alarm.soundUri.isBlank()) return

        try {
            if (increaseVolumeGradually) {
                initialVolume = audioManager.getStreamVolume(STREAM_ALARM)
                // Set to low volume before playing
                audioManager.setStreamVolume(STREAM_ALARM, MIN_ALARM_VOLUME.toInt(), 0)
            }

            mediaPlayer = MediaPlayer().apply {
                setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_ALARM)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .build()
                )
                setDataSource(context, alarm.soundUri.toUri())
                isLooping = true
                prepare()
                start()
            }

            if (increaseVolumeGradually) {
                startGradualVolumeIncrease(MAX_ALARM_VOLUME)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            mediaPlayer?.release()
            mediaPlayer = null
        }
    }

    private fun startGradualVolumeIncrease(maxVolume: Float) {
        volumeJob?.cancel()
        volumeJob = launch {
            var current = MIN_ALARM_VOLUME
            while (current < maxVolume) {
                delay(INCREASE_VOLUME_DELAY)
                current = (current + VOLUME_STEP).coerceAtMost(maxVolume)
                audioManager.setStreamVolume(STREAM_ALARM, current.toInt(), 0)
            }
        }
    }

    private fun startVibration(alarm: AlarmModel) {
        if (alarm.vibrate.orFalse) {
            vibrator.vibrate(
                VibrationEffect.createWaveform(
                    longArrayOf(VIBRATION_PATTERN, VIBRATION_PATTERN), 0
                )
            )
        }
    }

    fun stop() {
        mediaPlayer?.run {
            stop()
            release()
        }
        mediaPlayer = null

        vibrator.cancel()
        volumeJob?.cancel()

        if (increaseVolumeGradually) {
            audioManager.setStreamVolume(STREAM_ALARM, initialVolume, 0)
        }
    }

    companion object {
        private const val MAX_ALARM_VOLUME = 9f
        private const val INCREASE_VOLUME_DELAY = 300L
        private const val MIN_ALARM_VOLUME = 6f
        private const val VIBRATION_PATTERN = 500L
        private const val VOLUME_STEP = 0.1f
    }

    override val coroutineContext: CoroutineContext = SupervisorJob() + Dispatchers.IO
}