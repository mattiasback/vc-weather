package vc.weather.data.forecast.remote

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import timber.log.Timber
import vc.weather.data.forecast.models.City
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
internal class ForecastRemoteDataSourceImpl @Inject constructor(
    @Named("ioDispatcher")
    private val ioDispatcher: CoroutineDispatcher,
) : ForecastRemoteDataSource {

    private val apiKey = "6902c566c255ef948b6354e94c238d0c" //TODO this shall rather be a secret

    private val forecastApi: ForecastApi by lazy {
        val json = Json { ignoreUnknownKeys = true }
        val contentType = "application/json".toMediaType()
        Retrofit.Builder()
            .addConverterFactory(json.asConverterFactory(contentType))
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .build()
            .create(ForecastApi::class.java)
    }

    override suspend fun getForecast(city: City): RemoteForecast? =
        withContext(ioDispatcher) {
            try {
                forecastApi.getForecast(
                    cityId = city.id.toString(),
                    apiKey = apiKey
                )
            } catch (e: Exception) {
                Timber.e(e, "Forecast API error")
                null
            }
        }

}

