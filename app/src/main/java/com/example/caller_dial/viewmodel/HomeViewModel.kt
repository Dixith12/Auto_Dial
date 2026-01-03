package com.example.caller_dial.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.caller_dial.data.importer.CsvImporter
import com.example.caller_dial.domain.repository.CallRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: CallRepository
) : ViewModel() {

    val callLists = repository.getAllLists()

    fun importCsv(
        context: Context,
        uri: Uri,
        listName: String
    ) {
        viewModelScope.launch {
            val listId = repository.createList(listName)

            val contacts = CsvImporter.importContacts(
                context = context,
                uri = uri,
                listId = listId
            )

            repository.insertContacts(listId, contacts)
        }
    }

    fun deleteList(listId: Long) {
        viewModelScope.launch {
            repository.deleteList(listId)
        }
    }

}
