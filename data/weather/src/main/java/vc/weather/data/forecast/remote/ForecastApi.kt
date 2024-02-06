package vc.weather.data.forecast.remote

import retrofit2.http.GET
import retrofit2.http.Query

internal interface ForecastApi {
    @GET("weather")
    suspend fun getForecast(
        @Query("id") cityId: String,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric",
        @Query("lang") language: String = "en"
    ): RemoteForecast
}