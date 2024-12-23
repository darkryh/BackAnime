package com.ead.project.backanime.presentation.main

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ead.lib.anime.core.core.service.Service
import com.ead.lib.source.jkanime.JkAnimeService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel : ViewModel() {

    private val service : Service = JkAnimeService()

    private val _result: MutableState<String> = mutableStateOf("")
    val result: State<String> = _result

    init {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _result.value = service.getEpisodes("naruto-shippuden").toString()
            }
        }
    }
}