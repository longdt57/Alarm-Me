package leegroup.module.alarm.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import leegroup.module.alarm.domain.usecases.RescheduleEnabledAlarmUseCase
import leegroup.module.alarm.support.extensions.alarmEntryPoint

@HiltWorker
internal class RescheduleAlarmsWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        applicationContext.alarmEntryPoint.rescheduleEnabledAlarmUseCase().invoke()
        return Result.success()
    }

    companion object {
        fun startWorker(context: Context) {
            val workRequest = OneTimeWorkRequestBuilder<RescheduleAlarmsWorker>()
                .build()

            WorkManager.getInstance(context.applicationContext).enqueue(workRequest)
        }
    }
}
