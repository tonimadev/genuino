package com.genuino.sample

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.genuino.sdk.Genuino
import com.genuino.sdk.GenuinoConfig
import com.genuino.sdk.GenuinoContainer
import com.genuino.sdk.engine.GeminiLlmEngine
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun App() {
    MaterialTheme {
        val viewModel = remember { SampleViewModel() }
        var apiKey by remember { mutableStateOf("") }
        var promptInput by remember { mutableStateOf("Crie um widget de foco para leitura com um botão de iniciar timer") }
        var submittedPrompt by remember { mutableStateOf("") }
        val scope = rememberCoroutineScope()
        val snackbarHostState = remember { SnackbarHostState() }
        
        val themeProvider = SampleThemeProvider(
            primary = MaterialTheme.colorScheme.primary,
            background = MaterialTheme.colorScheme.background,
            onBackground = MaterialTheme.colorScheme.onBackground,
            typography = MaterialTheme.typography
        )

        LaunchedEffect(viewModel) {
            viewModel.actions.collectLatest { message ->
                snackbarHostState.showSnackbar(message)
            }
        }

        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text("Genuino-KMP Demo", style = MaterialTheme.typography.headlineMedium)
                
                OutlinedTextField(
                    value = apiKey,
                    onValueChange = { apiKey = it },
                    label = { Text("Gemini API Key") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = promptInput,
                    onValueChange = { promptInput = it },
                    label = { Text("Prompt para a UI") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3
                )

                Button(
                    onClick = {
                        if (apiKey.isBlank()) {
                            scope.launch { snackbarHostState.showSnackbar("Insira a API Key") }
                            return@Button
                        }
                        submittedPrompt = promptInput
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Gerar Interface")
                }

                HorizontalDivider()

                if (submittedPrompt.isNotBlank()) {
                    val config = remember(apiKey) {
                        GenuinoConfig(
                            llmEngine = GeminiLlmEngine(apiKey),
                            themeProvider = themeProvider,
                            actionHandler = viewModel
                        )
                    }
                    
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp)
                    ) {
                        GenuinoContainer(
                            prompt = submittedPrompt,
                            config = config
                        )
                    }
                } else {
                    Text("A interface gerada aparecerá aqui.")
                }
            }
        }
    }
}
