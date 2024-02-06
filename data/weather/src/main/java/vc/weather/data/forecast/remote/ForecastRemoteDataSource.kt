package vc.weather.data.forecast.remote

import vc.weather.data.forecast.models.City

internal interface ForecastRemoteDataSource {
    suspend fun getForecast(city: City): RemoteForecast?
}