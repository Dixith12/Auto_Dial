package com.example.caller_dial.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.caller_dial.domain.model.CallContact
import com.example.caller_dial.domain.model.CallSummary
import com.example.caller_dial.domain.repository.CallRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SummaryViewModel @Inject constructor(
    private val repository: CallRepository
) : ViewModel() {

    private val _contacts = MutableStateFlow<List<CallContact>>(emptyList())
    val contacts: StateFlow<List<CallContact>> = _contacts.asStateFlow()

    private val _summary = MutableStateFlow<CallSummary?>(null)
    val summary: StateFlow<CallSummary?> = _summary.asStateFlow()

    fun loadSummary(listId: Long) {
        viewModelScope.launch {
            repository.getContacts(listId).collect {
                _contacts.value = it
            }
        }

        viewModelScope.launch {
            _summary.value = repository.getSummary(listId)
        }
    }
}
