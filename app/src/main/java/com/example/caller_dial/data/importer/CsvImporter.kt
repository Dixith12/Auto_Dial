package com.example.caller_dial.data.importer
import android.content.Context
import android.net.Uri
import com.example.caller_dial.domain.model.CallContact
import com.example.caller_dial.domain.model.CallStatus
import java.io.BufferedReader
import java.io.InputStreamReader

object CsvImporter {

    fun importContacts(
        context: Context,
        uri: Uri,
        listId: Long
    ): List<CallContact> {

        val contacts = mutableListOf<CallContact>()

        val inputStream = context.contentResolver.openInputStream(uri)
            ?: return emptyList()

        BufferedReader(InputStreamReader(inputStream)).useLines { lines ->
            lines.drop(1).forEach { line ->
                val columns = line.split(",")

                if (columns.isNotEmpty()) {
                    val phoneNumber = columns[0].trim()

                    if (phoneNumber.isNotBlank()) {
                        contacts.add(
                            CallContact(
                                listId = listId,
                                phoneNumber = phoneNumber,
                                status = CallStatus.PENDING,
                                attempts = 0,
                                note = null
                            )
                        )
                    }
                }
            }
        }
        return contacts
    }
}
