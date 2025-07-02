package leegroup.module.alarm.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import leegroup.module.alarm.support.extensions.ALARM_ID
import leegroup.module.alarm.support.extensions.alarmEntryPoint

@HiltWorker
internal class AlarmCleanupWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        val alarmId = inputData.getInt(ALARM_ID, -1)
        if (alarmId == -1) return Result.failure()

        applicationContext.alarmEntryPoint.alarmCleanupUseCase().invoke(alarmId)

        return Result.success()
    }

    companion object {
        fun startWorker(context: Context, alarmId: Int) {
            val workRequest = OneTimeWorkRequestBuilder<AlarmCleanupWorker>()
                .setInputData(workDataOf(ALARM_ID to alarmId))
                .build()

            WorkManager.getInstance(context.applicationContext).enqueue(workRequest)
        }
    }
}
