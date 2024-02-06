package vc.weather.data.forecast.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.Instant

@Serializable
internal data class RemoteForecast(
    @SerialName("coord")
    val coordinate: RemoteCoordinate,
    val id: Int? = null,
    val name: String? = null,
    val weather: List<RemoteWeather>,
    val base: String,
    val main: RemoteMain? = null,
    val visibility: Int = 0,
    val wind: RemoteWind? = null,
    val clouds: RemoteClouds? = null,
    val rain: RemoteRain? = null,
    val snow: RemoteSnow? = null,
    @SerialName("dt")
    val datetime: Long,
    val sys: RemoteSystem? = null,
    val timezone: Int,
    val cod: Int,
) {
    val calculatedAt: Instant
        get() = Instant.ofEpochSecond(datetime)
}

@Serializable
internal data class RemoteWeather(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
)

@Serializable
internal data class RemoteCoordinate(
    @SerialName("lon")
    val longitude: Float,
    @SerialName("lat")
    val latitude: Float
)

@Serializable
internal data class RemoteMain(
    val temp: Float,
    @SerialName("feels_like")
    val feelsLike: Float,
    @SerialName("temp_min")
    val tempMin: Float,
    @SerialName("temp_max")
    val tempMax: Float,
    val pressure: Int,
    val humidity: Int,
    @SerialName("sea_level")
    val seaLevel: Int? = null,
    @SerialName("grnd_level")
    val groundLevel: Int? = null,
)

@Serializable
internal data class RemoteWind(
    val speed: Float = 0f,
    val deg: Int = 0,
    val gust: Float = 0f
)

@Serializable
internal data class RemoteClouds(
    val all: Int = 0,
)

@Serializable
internal data class RemoteRain(
    @SerialName("1h")
    val oneHour: Float = 0f,
    @SerialName("3h")
    val threeHour: Float = 0f,
)

@Serializable
internal data class RemoteSnow(
    @SerialName("1h")
    val oneHour: Float = 0f,
    @SerialName("3h")
    val threeHour: Float = 0f,
)

@Serializable
internal data class RemoteSystem(
    val type: Int? = null,
    val id: Int? = null,
    val country: String,
    val sunrise: Long,
    val sunset: Long
)
