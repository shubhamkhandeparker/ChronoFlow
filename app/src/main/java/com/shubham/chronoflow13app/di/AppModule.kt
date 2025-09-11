package com.shubham.chronoflow13app.di

import android.content.Context
import com.shubham.chronoflow13app.domain.timer.TimerAlarmScheduler
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton

    fun provideTimerAlarmScheduler(@ApplicationContext context: Context): TimerAlarmScheduler {
        return TimerAlarmScheduler(context)
    }
}