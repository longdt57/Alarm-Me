package leegroup.modul.sample.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import leegroup.modul.sample.data.repositories.SampleRepository
import leegroup.modul.sample.data.repositories.impl.SampleRepositoryImpl

@Module
@InstallIn(ViewModelComponent::class)
internal interface RepositoryModule {

    @Binds
    fun bindGitUserRepository(repository: SampleRepositoryImpl): SampleRepository

}