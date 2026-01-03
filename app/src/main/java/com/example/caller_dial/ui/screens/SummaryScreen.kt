package com.example.caller_dial.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.PhoneMissed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.caller_dial.domain.model.CallContact
import com.example.caller_dial.domain.model.CallStatus
import com.example.caller_dial.viewmodel.SummaryViewModel

@Composable
fun SummaryScreen(
    listId: Long,
    onBackToHome: () -> Unit,
    viewModel: SummaryViewModel = hiltViewModel()
) {

    val context = LocalContext.current

    val contacts by viewModel.contacts.collectAsStateWithLifecycle()
    val summary by viewModel.summary.collectAsStateWithLifecycle()
    val exportResult by viewModel.exportResult.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(listId) {
        viewModel.loadSummary(listId)
    }

    LaunchedEffect(exportResult) {
        exportResult?.let { success ->
            snackbarHostState.showSnackbar(
                if (success) "CSV exported successfully"
                else "CSV export failed"
            )
            viewModel.resetExportState()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color(0xFF0F2027),
                            Color(0xFF203A43),
                            Color(0xFF2C5364)
                        )
                    )
                )
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(24.dp))
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                tint = Color(0xFF00E676),
                modifier = Modifier.size(96.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Calling Completed",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(32.dp))

            summary?.let {
                Card(
                    shape = RoundedCornerShape(24.dp),
                    elevation = CardDefaults.cardElevation(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .background(
                                Brush.verticalGradient(
                                    listOf(
                                        Color(0xFF1F4037),
                                        Color(0xFF99F2C8)
                                    )
                                )
                            )
                            .padding(24.dp),
                        verticalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        SummaryRow(Icons.Default.Phone, "Total Calls", it.total.toString())
                        SummaryRow(Icons.Default.CheckCircle, "Answered", it.answered.toString(), Color(0xFF00E676))
                        SummaryRow(Icons.Default.PhoneMissed, "Unanswered", it.unanswered.toString(), Color(0xFFFF5252))
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            Card(
                shape = RoundedCornerShape(26.dp),
                elevation = CardDefaults.cardElevation(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                Column(
                    modifier = Modifier
                        .background(
                            Brush.verticalGradient(
                                listOf(
                                    Color(0xFF232526),
                                    Color(0xFF414345)
                                )
                            )
                        )
                        .padding(16.dp)
                ) {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Call Status",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            modifier = Modifier.weight(1f)
                        )

                        ExportMenu {
                            viewModel.exportCsv(
                                context = context,
                                fileNamePrefix = "call_report_$listId"
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(contacts) { contact ->
                            CallStatusRowPremium(contact)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onBackToHome,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black
                )
            ) {
                Text(
                    text = "Back to Home",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
private fun SummaryRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String,
    valueColor: Color = Color.White
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, null, tint = Color.White)
        Spacer(modifier = Modifier.width(12.dp))
        Text(label, color = Color.White, modifier = Modifier.weight(1f))
        Text(value, fontWeight = FontWeight.Bold, color = valueColor)
    }
}

@Composable
private fun CallStatusRowPremium(contact: CallContact) {

    val statusColor = when (contact.status) {
        CallStatus.ANSWERED -> Color(0xFF00E676)
        CallStatus.UNANSWERED -> Color(0xFFFF5252)
        CallStatus.DND -> Color(0xFFFFC107)
        else -> Color.Gray
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White.copy(alpha = 0.08f), RoundedCornerShape(16.dp))
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            text = contact.phoneNumber,
            color = Color.White,
            modifier = Modifier.weight(1f),
            fontWeight = FontWeight.Medium
        )

        Box(
            modifier = Modifier
                .border(1.dp, statusColor, RoundedCornerShape(50))
                .padding(horizontal = 14.dp, vertical = 6.dp)
        ) {
            Text(
                text = contact.status.name,
                color = statusColor,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun ExportMenu(onExportClick: () -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        IconButton(onClick = { expanded = true }) {
            Icon(Icons.Default.MoreVert, null, tint = Color.White)
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text("Export CSV") },
                onClick = {
                    expanded = false
                    onExportClick()
                }
            )
        }
    }
}
