package vc.weather.feature.overview.ui

import kotlinx.coroutines.flow.StateFlow

interface ForecastDetailsViewModel {
    val uiState: StateFlow<ForecastDetailsUiState>
    fun onUiEvent(event: ForecastDetailsUiEvent)
}

data class ForecastDetailsUiState(
    val event: ForecastDetailsEvent? = null,
    val cityName: String = "",
    val weather: String = "",
    val iconUrl: String = "",
    val temperature: String = "",
    val feelsLike: String = ""
)

sealed class ForecastDetailsEvent {
    object GoBack: ForecastDetailsEvent()
}

sealed class ForecastDetailsUiEvent {
    object ResetEvent: ForecastDetailsUiEvent()
    object BackPressed: ForecastDetailsUiEvent()
}
