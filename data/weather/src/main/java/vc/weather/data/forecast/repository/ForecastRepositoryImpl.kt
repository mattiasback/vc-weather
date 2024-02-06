package vc.weather.data.forecast.repository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext
import timber.log.Timber
import vc.weather.data.forecast.models.City
import vc.weather.data.forecast.models.Forecast
import vc.weather.data.forecast.models.toForecast
import vc.weather.data.forecast.remote.ForecastRemoteDataSource
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
internal class ForecastRepositoryImpl @Inject constructor(
    private val _forecastRemoteDataSource: ForecastRemoteDataSource,
    @Named("ioDispatcher") private val _ioDispatcher: CoroutineDispatcher
) : ForecastRepository {

    private val _forecasts = MutableStateFlow<List<Forecast>>(emptyList())
    override val forecasts: StateFlow<List<Forecast>> = _forecasts.asStateFlow()

    override suspend fun fetchForecasts(cities: List<City>) =
        withContext(_ioDispatcher) {
            val forecastRequests = cities.map {
                async {
                    val response = _forecastRemoteDataSource.getForecast(it)
                    Timber.d(response.toString())
                    response?.toForecast()
                }
            }
            val forecasts = forecastRequests.awaitAll().filterNotNull()

            _forecasts.update { forecasts }
        }
}