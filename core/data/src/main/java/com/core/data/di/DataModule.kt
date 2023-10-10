package com.core.data.di

import android.app.Application
import androidx.room.Room
import com.core.data.data_source.RentCountDatabase
import com.core.data.repository.AnalyticsRepositoryImpl
import com.core.data.repository.HomeRepositoryImpl
import com.core.data.repository.TransactionRepositoryImpl
import com.feature_analytics.domain.repository.AnalyticsRepository
import com.feature_home.domain.repository.HomeRepository
import com.feature_transactions.domain.repository.TransactionRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideRentDatabase(
        app: Application
    ): RentCountDatabase {
        return Room.databaseBuilder(
            app,
            RentCountDatabase::class.java,
            RentCountDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideAnalyticsRepository(db: RentCountDatabase): AnalyticsRepository {
        return AnalyticsRepositoryImpl(dao = db.rentCountDao)
    }

    @Provides
    @Singleton
    fun provideHomeRepository(db: RentCountDatabase): HomeRepository {
        return HomeRepositoryImpl(dao = db.rentCountDao)
    }

    @Provides
    @Singleton
    fun provideTransactionRepository(db: RentCountDatabase): TransactionRepository {
        return TransactionRepositoryImpl(dao = db.rentCountDao)
    }

}