package com.example.caller_dial.ui.screens

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PhoneInTalk
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.caller_dial.domain.model.CallContact
import com.example.caller_dial.domain.model.CallStatus
import com.example.caller_dial.viewmodel.CallerViewModel

@Composable
fun CallerScreen(
    listId: Long,
    onFinishCalling: () -> Unit,
    viewModel: CallerViewModel = hiltViewModel()
) {

    var callInProgress by remember { mutableStateOf(false) }

    var resultMarked by remember { mutableStateOf(false) }
    var callRequested by remember { mutableStateOf(false) }


    val currentContact by viewModel.currentContact.collectAsStateWithLifecycle()

    LaunchedEffect(listId) {
        viewModel.loadContacts(listId)
    }

    val context = LocalContext.current
    val callStateListener = remember {
        com.example.caller_dial.service.CallStateListener(context) {
            if (!resultMarked) {
                viewModel.markResult(CallStatus.UNANSWERED)
            }
            resultMarked = false
        }
    }
    fun startCall(contact: CallContact) {
        callStateListener.start()
        val intent = Intent(Intent.ACTION_CALL).apply {
            data = Uri.parse("tel:${contact.phoneNumber}")
        }
        context.startActivity(intent)
    }



    val permissionLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->

            val callGranted = permissions[Manifest.permission.CALL_PHONE] == true
            val phoneStateGranted = permissions[Manifest.permission.READ_PHONE_STATE] == true

            if (callGranted && phoneStateGranted) {

                currentContact?.let { startCall(it) }

            }

        }

    LaunchedEffect(callRequested) {
        if (!callRequested) return@LaunchedEffect

        val contact = currentContact ?: return@LaunchedEffect

        val hasCallPermission =
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CALL_PHONE
            ) == PackageManager.PERMISSION_GRANTED

        val hasPhoneStatePermission =
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_PHONE_STATE
            ) == PackageManager.PERMISSION_GRANTED

        if (hasCallPermission && hasPhoneStatePermission) {
            startCall(contact)
        } else {
            permissionLauncher.launch(
                arrayOf(
                    Manifest.permission.CALL_PHONE,
                    Manifest.permission.READ_PHONE_STATE
                )
            )
        }

        callRequested = false
    }



    DisposableEffect(Unit) {
        onDispose {
            callStateListener.stop()
        }
    }


    if (currentContact == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "All calls completed",
                color = Color.White
            )
        }
        return
    }

    Scaffold { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color(0xFF141E30),
                            Color(0xFF243B55)
                        )
                    )
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(48.dp))

            Text(
                text = "Calling",
                style = MaterialTheme.typography.titleLarge,
                color = Color.White.copy(alpha = 0.8f)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Contact",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(48.dp))

            Surface(
                shape = CircleShape,
                tonalElevation = 8.dp,
                color = Color.White.copy(alpha = 0.15f),
                modifier = Modifier.size(180.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.PhoneInTalk,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(72.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = currentContact!!.phoneNumber,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Attempt ${currentContact!!.attempts + 1}",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White.copy(alpha = 0.8f)
            )

            Spacer(modifier = Modifier.height(48.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(32.dp)) {

                ControlButton(
                    icon = Icons.Default.Pause,
                    label = "Answered",
                    background = Color(0xFF4CAF50)
                ) {
                    viewModel.markResult(CallStatus.ANSWERED)
                    resultMarked = true
                }

                ControlButton(
                    icon = Icons.Default.Stop,
                    label = "Missed",
                    background = Color(0xFFD32F2F)
                ) {
                    viewModel.markResult(CallStatus.UNANSWERED)
                    resultMarked = true
                }

                ControlButton(
                    icon = Icons.Default.PhoneInTalk,
                    label = "Call",
                    background = Color(0xFF2196F3)
                ) {
                    callRequested = true
                }

            }
        }
    }
}

@Composable
private fun ControlButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    background: Color,
    onClick: () -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {

        Surface(
            shape = CircleShape,
            color = background,
            modifier = Modifier
                .size(72.dp)
                .clickable { onClick() },
            shadowElevation = 8.dp
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = label,
            color = Color.White,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}


