package leegroup.module.photosample.di

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import leegroup.module.core.util.SavedStateProvider
import leegroup.module.photosample.ui.screens.main.PhotoDetailNav

@Module
@InstallIn(SingletonComponent::class)
class PhotoModule {

    @Provides
    internal fun providePhotoSavedStateProvider(): SavedStateProvider<PhotoDetailNav> =
        object : SavedStateProvider<PhotoDetailNav> {
            override fun toRoute(savedStateHandle: SavedStateHandle): PhotoDetailNav {
                return savedStateHandle.toRoute()
            }
        }
}