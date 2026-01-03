package com.example.caller_dial.data.export

import android.content.ContentValues
import android.content.Context
import android.provider.MediaStore
import com.example.caller_dial.domain.model.CallContact
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object CsvExporter {

    fun exportCallsToCsv(
        context: Context,
        fileNamePrefix: String,
        contacts: List<CallContact>
    ): Boolean {

        val timeStamp = SimpleDateFormat(
            "yyyyMMdd_HHmmss",
            Locale.getDefault()
        ).format(Date())

        val fileName = "${fileNamePrefix}_$timeStamp.csv"

        val resolver = context.contentResolver

        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
            put(MediaStore.MediaColumns.MIME_TYPE, "text/csv")
            put(
                MediaStore.MediaColumns.RELATIVE_PATH,
                "Documents/CallerDialer"
            )
        }

        val uri = resolver.insert(
            MediaStore.Files.getContentUri("external"),
            contentValues
        ) ?: return false

        return try {
            resolver.openOutputStream(uri)?.use { outputStream ->
                writeCsv(outputStream, contacts)
            }
            true
        } catch (e: Exception) {
            false
        }
    }

    private fun writeCsv(
        outputStream: OutputStream,
        contacts: List<CallContact>
    ) {
        outputStream.bufferedWriter().use { writer ->
            writer.appendLine(
                "Phone Number,Status,Attempts,Note,Timestamp"
            )

            contacts.forEach { contact ->
                writer.appendLine(
                    "${contact.phoneNumber}," +
                            "${contact.status.name}," +
                            "${contact.attempts}," +
                            "\"${contact.note ?: ""}\"," +
                            contact.timestamp
                )
            }
        }
    }
}
