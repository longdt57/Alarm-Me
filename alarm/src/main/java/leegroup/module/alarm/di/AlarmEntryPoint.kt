package leegroup.module.alarm.di

import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import leegroup.module.alarm.domain.usecases.AlarmCleanupUseCase
import leegroup.module.alarm.domain.usecases.RescheduleEnabledAlarmUseCase
import leegroup.module.alarm.support.helpers.AlarmNotificationHelper
import leegroup.module.alarm.support.helpers.AlarmSoundAndVibrateHelper
import leegroup.module.core.util.AppConfigProvider

@EntryPoint
@InstallIn(SingletonComponent::class)
internal interface AlarmEntryPoint {
    fun alarmNotificationHelper(): AlarmNotificationHelper
    fun alarmSoundHelper(): AlarmSoundAndVibrateHelper
    fun appConfigProvider(): AppConfigProvider
    fun alarmCleanupUseCase(): AlarmCleanupUseCase
    fun rescheduleEnabledAlarmUseCase(): RescheduleEnabledAlarmUseCase
}
