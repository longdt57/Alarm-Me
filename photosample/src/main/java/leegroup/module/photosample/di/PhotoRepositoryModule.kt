package leegroup.module.photosample.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import leegroup.module.photosample.data.repositories.PhotoFavoriteRepositoryImpl
import leegroup.module.photosample.data.repositories.PhotoListFavoriteFilterRepositoryImpl
import leegroup.module.photosample.data.repositories.PhotoListRepositoryImpl
import leegroup.module.photosample.domain.repositories.PhotoFavoriteRepository
import leegroup.module.photosample.domain.repositories.PhotoListFavoriteFilterRepository
import leegroup.module.photosample.domain.repositories.PhotoListRepository

@Module
@InstallIn(ViewModelComponent::class)
internal interface PhotoRepositoryModule {

    @Binds
    fun bindPhotoListRepository(repository: PhotoListRepositoryImpl): PhotoListRepository

    @Binds
    fun bindPhotoFavoriteRepository(repository: PhotoFavoriteRepositoryImpl): PhotoFavoriteRepository

    @Binds
    fun bindPhotoListFavoriteFilterRepository(
        repository: PhotoListFavoriteFilterRepositoryImpl
    ): PhotoListFavoriteFilterRepository
}
