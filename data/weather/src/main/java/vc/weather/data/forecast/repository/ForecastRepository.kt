package vc.weather.data.forecast.repository

import kotlinx.coroutines.flow.StateFlow
import vc.weather.data.forecast.models.City
import vc.weather.data.forecast.models.Forecast

interface ForecastRepository {
    val forecasts: StateFlow<List<Forecast>>
    suspend fun fetchForecasts(cities: List<City>)
}

