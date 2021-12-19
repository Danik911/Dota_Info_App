package com.example.dota_infoapp.di

import android.app.Application
import com.example.hero_use_cases.HeroUseCases
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HeroUseCasesModule {

    @Provides
    @Singleton
    @Named("heroAndroidSqlDriver")// in case you have another SQLDelight db
    fun provideAndroidDriver(app: Application): SqlDriver {
        return AndroidSqliteDriver(
            schema = HeroUseCases.schema,
            context = app,
            name = HeroUseCases.dbName
        )
    }
    /**
     * Provide all the UseCases in hero-Use-Cases module
     */
    @Provides
    @Singleton
    fun provideHeroUseCases(
        @Named("heroAndroidSqlDriver")sqlDriver: SqlDriver,
    ): HeroUseCases{
        return HeroUseCases.build(sqlDriver = sqlDriver)
    }
}