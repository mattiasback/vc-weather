package vc.weather.feature.overview.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import vc.weather.data.forecast.repository.ForecastRepository
import javax.inject.Inject

@HiltViewModel
class ForecastDetailsViewModelImpl @Inject constructor(
    forecastRepository: ForecastRepository,
    savedStateHandle: SavedStateHandle = SavedStateHandle(),
) : ViewModel(), ForecastDetailsViewModel {

    private val _cityId: Int? = savedStateHandle["cityId"]

    private val _forecast = forecastRepository.forecasts.map {
        it.firstOrNull { forecast -> forecast.cityId == _cityId }
    }

    private val _uiState = MutableStateFlow(ForecastDetailsUiState())
    override val uiState = combine(_uiState, _forecast) { state, forecast ->
        state.copy(
            cityName = forecast?.cityName.orEmpty(),
            iconUrl = forecast?.iconUrl.orEmpty(),
            weather = forecast?.description.orEmpty().capitalize(),
            temperature = forecast?.temperature.orEmpty(),
            feelsLike = forecast?.feelsLike.orEmpty()
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), ForecastDetailsUiState())

    override fun onUiEvent(event: ForecastDetailsUiEvent) {
        when (event) {
            ForecastDetailsUiEvent.ResetEvent -> _uiState.update { it.copy(event = null) }
            ForecastDetailsUiEvent.BackPressed -> _uiState.update { it.copy(event = ForecastDetailsEvent.GoBack) }
        }
    }
}
