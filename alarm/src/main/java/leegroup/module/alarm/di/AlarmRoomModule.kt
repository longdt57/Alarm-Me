package leegroup.module.alarm.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import leegroup.module.alarm.data.local.room.AlarmAudioDao
import leegroup.module.alarm.data.local.room.AlarmDao
import leegroup.module.alarm.data.local.room.AlarmDatabase

@Module
@InstallIn(SingletonComponent::class)
internal class AlarmRoomModule {

    @Provides
    fun provideGitUserRoom(@ApplicationContext applicationContext: Context): AlarmDatabase {
        return Room.databaseBuilder(
            applicationContext,
            AlarmDatabase::class.java, "alarm-database"
        )
            .fallbackToDestructiveMigration(true)
            .build()
    }

    @Provides
    fun provideAlarmModelDao(database: AlarmDatabase): AlarmDao = database.alarmDao()

    @Provides
    fun provideAlarmAudioDao(database: AlarmDatabase): AlarmAudioDao = database.alarmAudioDao()
}