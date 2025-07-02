package com.app.androidcompose.di

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.app.androidcompose.AppApplication
import com.app.androidcompose.MainActivity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import leegroup.module.alarm.BuildConfig
import leegroup.module.core.util.AppConfigProvider
import leegroup.module.core.util.DispatchersProvider
import leegroup.module.core.util.DispatchersProviderImpl

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    fun provideContext(@ApplicationContext context: Context): Context = context

    @Provides
    fun provideDispatchersProvider(): DispatchersProvider = DispatchersProviderImpl

    @Provides
    fun provideAppConfigProvider(): AppConfigProvider = object : AppConfigProvider {
        override val isDebug: Boolean get() = BuildConfig.DEBUG
        override val currentActivity: Activity? get() = AppApplication.currentActivity.get()

        override fun restartApp(context: Context) {
            context.startActivity(
                Intent(context, MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
            )
        }
    }
}
