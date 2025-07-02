package leegroup.module.alarm.worker

import android.content.Context
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import leegroup.module.alarm.filemanager.AlarmFileManager
import okhttp3.OkHttpClient
import okhttp3.Request

class AlarmDownloadAudioWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    private val alarmFileManager: AlarmFileManager = AlarmFileManager(applicationContext)

    override suspend fun doWork(): Result {
        val url = inputData.getString(KEY_URL) ?: return Result.failure()
        val file = alarmFileManager.getFile(url)

        return try {
            val request = Request.Builder().url(url).build()
            val client = OkHttpClient()

            val response = client.newCall(request).execute()
            if (!response.isSuccessful) return Result.failure()

            val inputStream = response.body?.byteStream()

            file.outputStream().use { output ->
                inputStream?.copyTo(output)
            }

            Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure()
        }
    }

    companion object {
        const val KEY_URL = "audio_url"

        private fun getWorkerId(url: String) = "preload_${url.hashCode()}"

        fun start(context: Context, url: String) {
            val request = OneTimeWorkRequestBuilder<AlarmDownloadAudioWorker>()
                .setInputData(workDataOf(KEY_URL to url))
                .setConstraints(
                    Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.CONNECTED)
                        .build()
                )
                .build()

            WorkManager.getInstance(context).enqueueUniqueWork(
                getWorkerId(url),
                ExistingWorkPolicy.KEEP,
                request
            )
        }

        fun isVideoPreloadFrameWorkerRunning(context: Context, url: String): Boolean {
            val workName = getWorkerId(url)
            val workInfos = WorkManager.getInstance(context)
                .getWorkInfosForUniqueWork(workName)
                .get()

            return workInfos.any { it.state == WorkInfo.State.RUNNING || it.state == WorkInfo.State.ENQUEUED }
        }
    }
}
