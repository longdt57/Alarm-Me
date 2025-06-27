package leegroup.module.sample.gituser.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import leegroup.module.sample.gituser.data.models.GitUser
import leegroup.module.sample.gituser.data.models.GitUserDetail

@Database(entities = [GitUser::class, GitUserDetail::class], version = 1)
internal abstract class GitUserDatabase : RoomDatabase() {
    abstract fun gitUserDao(): GitUserDao
    abstract fun gitUserDetailDao(): GitUserDetailDao
}