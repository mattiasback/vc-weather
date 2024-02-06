package vc.weather.feature.overview.ui

import kotlinx.coroutines.flow.StateFlow

interface OverviewViewModel {
    val uiState: StateFlow<OverviewUiState>
    fun onUiEvent(event: OverviewUiEvent)
}

data class OverviewUiState(
    val event: OverviewEvent? = null,
    val cities: List<UiCity> = emptyList()
)

sealed class OverviewEvent {
    data class OpenForecast(val cityId: Int) : OverviewEvent()
}

sealed class OverviewUiEvent {
    object ResetEvent : OverviewUiEvent()
    data class CityClicked(val cityId: Int) : OverviewUiEvent()
    object Refresh : OverviewUiEvent()
}

data class UiCity(
    val id: Int,
    val name: String,
    val iconUrl: String
)