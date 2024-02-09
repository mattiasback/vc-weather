package vc.weather.feature.overview.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import vc.weather.data.forecast.models.City
import vc.weather.data.forecast.repository.ForecastRepository
import javax.inject.Inject

@HiltViewModel
class OverviewViewModelImpl @Inject constructor(
    private val forecastRepository: ForecastRepository,
) : ViewModel(), OverviewViewModel {

    private val _uiState = MutableStateFlow(OverviewUiState())
    private val _cities = forecastRepository.forecasts.map {
        it.map { forecast ->
            Timber.d(forecast.toString())
            UiCity(
                id = forecast.cityId,
                name = forecast.cityName,
                iconUrl = forecast.iconUrl
            )
        }
    }

    override val uiState = combine(_uiState, _cities) { state, cities ->
        state.copy(cities = cities)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), OverviewUiState())

    init {
        viewModelScope.launch {
            forecastRepository.fetchForecasts(City.entries)
        }
    }

    override fun onUiEvent(event: OverviewUiEvent) {
        when (event) {
            OverviewUiEvent.ResetEvent -> _uiState.update { it.copy(event = null) }
            is OverviewUiEvent.CityClicked -> {
                _uiState.update { it.copy(event = OverviewEvent.OpenForecast(cityId = event.cityId)) }
            }
            OverviewUiEvent.Refresh -> {
                viewModelScope.launch {
                    forecastRepository.fetchForecasts(City.entries)
                }
            }
        }
    }
}