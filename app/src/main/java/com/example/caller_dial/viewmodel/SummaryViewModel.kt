package com.example.caller_dial.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.caller_dial.data.export.CsvExporter
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

    private val _exportResult = MutableStateFlow<Boolean?>(null)
    val exportResult: StateFlow<Boolean?> = _exportResult.asStateFlow()

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

    fun exportCsv(
        context: Context,
        fileNamePrefix: String
    ) {
        viewModelScope.launch {
            val result = CsvExporter.exportCallsToCsv(
                context = context,
                fileNamePrefix = fileNamePrefix,
                contacts = _contacts.value
            )
            _exportResult.value = result
        }
    }

    fun resetExportState() {
        _exportResult.value = null
    }
}
