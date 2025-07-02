package leegroup.module.alarm.filemanager

import android.content.Context
import android.net.Uri
import androidx.core.net.toUri
import dagger.hilt.android.qualifiers.ApplicationContext
import leegroup.module.alarm.worker.AlarmDownloadAudioWorker
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AlarmFileManager @Inject constructor(
    @ApplicationContext private val appContext: Context
) {

    // Path to save video first frame
    fun getFile(url: String): File {
        val name = url.hashCode().toString()
        val videoDir = File(appContext.cacheDir, GACKT_ALARM_AUDIO_DIR)
        if (!videoDir.exists()) {
            videoDir.mkdirs()
        }
        return File(videoDir, "$name.jpeg")
    }

    fun getLocalOrDefault(url: String): Uri {
        if (AlarmDownloadAudioWorker.isVideoPreloadFrameWorkerRunning(appContext, url)) {
            return url.toUri()
        }
        if (isCached(url)) {
            return getFile(url).toUri()
        }
        return url.toUri()
    }


    fun isCached(url: String): Boolean {
        val localFile = getFile(url)
        return localFile.exists()
    }

    fun clear() {
        val videoDir = File(appContext.cacheDir, GACKT_ALARM_AUDIO_DIR)
        if (videoDir.exists()) {
            videoDir.deleteRecursively()
        }
    }

    companion object {
        const val GACKT_ALARM_AUDIO_DIR = "gackt_alarm_audio"
    }
}