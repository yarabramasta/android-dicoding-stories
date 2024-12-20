package com.dicoding.stories.di

import android.content.Context
import androidx.datastore.core.DataStore
import com.dicoding.stories.features.auth.data.local.SessionManager
import com.dicoding.stories.features.auth.data.local.sessionDataStore
import com.dicoding.stories.features.auth.domain.models.Session
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalDataModule {
  @Provides
  @Singleton
  fun provideSessionDataStore(
    @ApplicationContext context: Context,
  ): DataStore<Session> = context.sessionDataStore

  @Provides
  @Singleton
  fun provideSessionManager(
    dataStore: DataStore<Session>,
  ): SessionManager = SessionManager(dataStore)
}
