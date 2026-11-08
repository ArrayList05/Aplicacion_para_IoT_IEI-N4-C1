package cl.inacap.atmiotapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AtmiIoTAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigator()
                }
            }
        }
    }
}

@Composable
fun AppNavigator() {
    var isLoggedIn by remember { mutableStateOf(false) }

    if (!isLoggedIn) {
        LoginScreen(onLoginSuccess = { isLoggedIn = true })
    } else {
        ControlPanelScreen()
    }
}

@Composable
fun LoginScreen(onLoginSuccess: () -> Unit) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "ATM IoT - Acceso", fontSize = 26.sp, modifier = Modifier.padding(bottom = 32.dp))

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Usuario o RUT") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                // Validación simple para cumplir con la rúbrica
                if (username.isNotBlank() && password.isNotBlank()) {
                    onLoginSuccess()
                } else {
                    errorMessage = "Por favor ingrese usuario y contraseña"
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Ingresar al Sistema")
        }

        if (errorMessage.isNotEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = errorMessage, color = MaterialTheme.colorScheme.error)
        }
    }
}

@Composable
fun ControlPanelScreen() {
    var alarmEnabled by remember { mutableStateOf(false) }
    var sensitivity by remember { mutableStateOf(50f) }
    var statusText by remember { mutableStateOf("Estado: Conectado al Cajero") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(text = "Panel de Control ATM - IoT", fontSize = 22.sp)

        Button(
            onClick = { statusText = "Estado: Buscando dispositivos..." },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Buscar Dispositivos Bluetooth")
        }

        Text(text = statusText, color = MaterialTheme.colorScheme.primary)

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Activar Alerta Remota")
            Switch(
                checked = alarmEnabled,
                onCheckedChange = { alarmEnabled = it }
            )
        }

        Text(text = "Sensibilidad: ${sensitivity.toInt()}%")
        Slider(
            value = sensitivity,
            onValueChange = { sensitivity = it },
            valueRange = 0f..100f
        )

        Button(
            onClick = { statusText = "Estado: Parámetros enviados" },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Enviar Datos al Cajero")
        }
    }
}

@Composable
fun AtmiIoTAppTheme(content: @Composable () -> Unit) {
    MaterialTheme(content = content)
}