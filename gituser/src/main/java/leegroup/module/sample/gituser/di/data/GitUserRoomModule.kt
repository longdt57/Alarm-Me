package leegroup.module.sample.gituser.di.data

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import leegroup.module.sample.gituser.data.local.room.GitUserDao
import leegroup.module.sample.gituser.data.local.room.GitUserDatabase
import leegroup.module.sample.gituser.data.local.room.GitUserDetailDao

private const val APP_DATABASE = "git-user-database"

@Module
@InstallIn(SingletonComponent::class)
internal class GitUserRoomModule {

    @Provides
    fun provideGitUserRoom(@ApplicationContext applicationContext: Context): GitUserDatabase {
        return Room.databaseBuilder(
            applicationContext,
            GitUserDatabase::class.java, APP_DATABASE
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideGitUserDao(database: GitUserDatabase): GitUserDao = database.gitUserDao()

    @Provides
    fun provideGitUserDetailDao(database: GitUserDatabase): GitUserDetailDao =
        database.gitUserDetailDao()
}