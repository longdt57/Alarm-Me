package leegroup.module.alarm.ui.screens.setup.components

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import leegroup.module.alarm.R
import leegroup.module.alarm.data.models.AlarmAudioModel
import leegroup.module.alarm.filemanager.AlarmFileManager
import leegroup.module.alarm.ui.screens.components.PrimaryBottomSheet

@Composable
fun AlarmSetupAudioBottomSheet(
    modifier: Modifier = Modifier,
    options: List<AlarmAudioModel>,
    selectedItem: AlarmAudioModel?,
    onItemClick: (AlarmAudioModel) -> Unit = {},
    onDismissed: () -> Unit = {},
) {
    val playPreview = rememberAudioPreviewPlayer()
    val context = LocalContext.current
    val alarmFileManager = remember { AlarmFileManager(context) }

    PrimaryBottomSheet(
        isVisible = true,
        modifier = modifier,
        onDismissed = {
            onDismissed()
        },
        dismissOnSelected = false,
        title = stringResource(R.string.sound).uppercase(),
        onItemClick = { title ->
            val selected = options.firstOrNull { it.title == title } ?: return@PrimaryBottomSheet
            onItemClick(selected)
            playPreview(alarmFileManager.getLocalOrDefault(selected.fileUrl.orEmpty()))
        },
        options = options.map { it.title.orEmpty() },
        selectedItems = selectedItem?.let { listOf(it.title.orEmpty()) } ?: emptyList()
    )
}


@Composable
fun rememberAudioPreviewPlayer(): (Uri) -> Unit {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var mediaPlayer by remember { mutableStateOf<MediaPlayer?>(null) }
    var runningJob by remember { mutableStateOf<Job?>(null) }
    val audioManager = remember {
        context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    }

    val originalVolume = remember { mutableIntStateOf(0) }
    val maxVolume = remember { audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC) }

    DisposableEffect(Unit) {
        onDispose {
            mediaPlayer?.stop()
            mediaPlayer?.release()
            mediaPlayer = null
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, originalVolume.intValue, 0)
        }
    }

    return remember {
        { uri: Uri ->
            runningJob?.cancel()
            runningJob = scope.launch(Dispatchers.IO) {
                try {
                    // Stop any current playback
                    mediaPlayer?.stop()
                    mediaPlayer?.release()

                    // Save & increase volume
                    originalVolume.intValue =
                        audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
                    val boostedVolume = (maxVolume * 0.8f).toInt().coerceAtMost(maxVolume)
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, boostedVolume, 0)

                    val player = MediaPlayer().apply {
                        setDataSource(context, uri)
                        setAudioAttributes(
                            AudioAttributes.Builder()
                                .setUsage(AudioAttributes.USAGE_MEDIA)
                                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                                .build()
                        )
                        isLooping = true

                        setOnPreparedListener {
                            it.start()
                        }

                        prepareAsync()
                    }

                    mediaPlayer = player

                    delay(10000)

                    withContext(Dispatchers.Main) {
                        mediaPlayer?.stop()
                        mediaPlayer?.release()
                        mediaPlayer = null

                        audioManager.setStreamVolume(
                            AudioManager.STREAM_MUSIC,
                            originalVolume.intValue,
                            0
                        )
                    }
                } catch (e: Exception) {
                    Log.e("AudioPreview", "Playback error: ${e.localizedMessage}")
                }
            }
        }
    }
}
