package com.app.androidcompose.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import leegroup.module.analytics.AnalyticsManager
import leegroup.module.analytics.clients.ClientType
import leegroup.module.analytics.clients.FirebaseClient

@Module
@InstallIn(SingletonComponent::class)
class AnalyticModule {

    @Provides
    fun provideAnalytics(
        firebaseClient: FirebaseClient
    ): AnalyticsManager {
        return AnalyticsManager(
            listOf(firebaseClient),
            defaultLogEventClientType = ClientType.FIREBASE
        )
    }
}