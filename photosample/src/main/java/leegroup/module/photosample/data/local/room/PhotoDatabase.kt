package leegroup.module.photosample.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import leegroup.module.photosample.data.local.room.entities.PhotoEntity

@Database(entities = [PhotoEntity::class], version = 1)
internal abstract class PhotoDatabase : RoomDatabase() {
    abstract fun photoDao(): PhotoDao
}