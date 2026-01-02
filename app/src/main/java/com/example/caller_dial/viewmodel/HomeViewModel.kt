package com.example.caller_dial.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.caller_dial.domain.repository.CallRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: CallRepository
) : ViewModel() {

    val callLists = repository.getAllLists()

    fun createList(name: String) {
        viewModelScope.launch {
            repository.createList(name)
        }
    }
}
