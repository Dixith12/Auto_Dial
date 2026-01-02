package com.example.caller_dial.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.caller_dial.domain.model.CallContact
import com.example.caller_dial.domain.model.CallStatus
import com.example.caller_dial.domain.repository.CallRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CallerViewModel @Inject constructor(
    private val repository: CallRepository
) : ViewModel() {

    private val _contacts = MutableStateFlow<List<CallContact>>(emptyList())
    val contacts: StateFlow<List<CallContact>> = _contacts.asStateFlow()

    private val _currentIndex = MutableStateFlow(0)
    val currentIndex: StateFlow<Int> = _currentIndex.asStateFlow()

    private val _currentContact = MutableStateFlow<CallContact?>(null)
    val currentContact: StateFlow<CallContact?> = _currentContact.asStateFlow()

    fun loadContacts(listId: Long) {
        viewModelScope.launch {
            repository.getContacts(listId).collect { list ->
                _contacts.value = list
                if (list.isNotEmpty()) {
                    _currentContact.value = list.getOrNull(_currentIndex.value)
                }
            }
        }
    }

    fun markResult(
        status: CallStatus,
        note: String? = null
    ) {
        val contact = _currentContact.value ?: return

        viewModelScope.launch {
            repository.updateContact(
                contactId = contact.id,
                status = status,
                note = note,
                attempts = contact.attempts + 1
            )
            moveNext()
        }
    }

    private fun moveNext() {
        val next = _currentIndex.value + 1
        if (next < _contacts.value.size) {
            _currentIndex.value = next
            _currentContact.value = _contacts.value[next]
        } else {
            _currentContact.value = null
        }
    }
}
