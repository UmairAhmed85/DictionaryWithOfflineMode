package com.example.dictionarywithofflinemode.feature_dictionary.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dictionarywithofflinemode.core.util.Resource
import com.example.dictionarywithofflinemode.feature_dictionary.domain.use_case.GetWordInfoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WordInfoViewmodel @Inject constructor(
    private val getWordInfoUseCase: GetWordInfoUseCase
) : ViewModel() {

    private val _searchQuery = mutableStateOf("")
    val searchQuery: State<String> = _searchQuery

    private val _wordInfoState = mutableStateOf(WordInfoState())
    val wordInfoState: State<WordInfoState> = _wordInfoState

    private val _eventFlow = MutableSharedFlow<UIEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var searchJob: Job? = null
    fun onSearch(query: String) {
        _searchQuery.value = query
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(500L)
            getWordInfoUseCase(query).onEach { result ->
                when (result) {
                    is Resource.Loading -> {
                        _wordInfoState.value = wordInfoState.value.copy(
                            wordInfoItem = result.data ?: emptyList(),
                            isLoading = true
                        )
                    }
                    is Resource.Error -> {
                        _wordInfoState.value = wordInfoState.value.copy(
                            wordInfoItem = result.data ?: emptyList(),
                            isLoading = false
                        )
                        _eventFlow.emit(UIEvent.ShowSnackbar(result.message ?: "Something went wrong"))
                    }
                    is Resource.Success -> {
                        _wordInfoState.value = wordInfoState.value.copy(
                            wordInfoItem = result.data ?: emptyList(),
                            isLoading = false
                        )
                    }
                }
            }.launchIn(this)
        }
    }

    sealed class UIEvent {
        data class ShowSnackbar(val message: String) : UIEvent()
    }

}