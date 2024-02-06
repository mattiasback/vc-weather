package vc.weather.data.forecast.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import vc.weather.data.forecast.remote.ForecastRemoteDataSource
import vc.weather.data.forecast.remote.ForecastRemoteDataSourceImpl
import vc.weather.data.forecast.repository.ForecastRepository
import vc.weather.data.forecast.repository.ForecastRepositoryImpl

@InstallIn(SingletonComponent::class)
@Module
internal abstract class ForecastModule {

    @Binds
    abstract fun bindsForecastRemoteDataSource(implementation: ForecastRemoteDataSourceImpl): ForecastRemoteDataSource

    @Binds
    abstract fun bindsForecastRepository(implementation: ForecastRepositoryImpl): ForecastRepository
}