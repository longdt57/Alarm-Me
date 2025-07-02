package leegroup.module.alarm.filemanager

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import leegroup.module.alarm.worker.AlarmDownloadAudioWorker
import javax.inject.Inject

class AlarmAudioDownloadManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val alarmFileManager: AlarmFileManager,
) {

    fun preloadAudios(urls: List<String>) {
        urls.forEach { url ->
            preloadVideo(url)
        }
    }

    private fun preloadVideo(url: String) {
        if (alarmFileManager.isCached(url)) return
        if (AlarmDownloadAudioWorker.isVideoPreloadFrameWorkerRunning(context, url)) return

        AlarmDownloadAudioWorker.start(context, url)
    }
}