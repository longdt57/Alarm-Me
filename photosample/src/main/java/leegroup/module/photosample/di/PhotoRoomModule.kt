package leegroup.module.photosample.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import leegroup.module.photosample.data.local.room.PhotoDao
import leegroup.module.photosample.data.local.room.PhotoDatabase

private const val APP_DATABASE = "photo-database"

@Module
@InstallIn(SingletonComponent::class)
internal class PhotoRoomModule {

    @Provides
    fun providePhotoRoom(@ApplicationContext applicationContext: Context): PhotoDatabase {
        return Room.databaseBuilder(
            applicationContext,
            PhotoDatabase::class.java,
            APP_DATABASE
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun providePhotoDao(database: PhotoDatabase): PhotoDao = database.photoDao()

}