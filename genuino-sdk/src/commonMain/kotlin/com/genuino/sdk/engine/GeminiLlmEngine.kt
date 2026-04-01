package com.genuino.sdk.engine

import com.genuino.sdk.contracts.LlmEngine
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

/**
 * Engine that uses Google's Gemini LLM to generate UI components.
 * 
 * @param apiKey The Gemini API Key.
 * @param model The Gemini model to use (default: gemini-1.5-flash).
 */
class GeminiLlmEngine(
    private val apiKey: String,
    private val model: String = "gemini-1.5-flash"
) : LlmEngine {

    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                prettyPrint = true
                isLenient = true
            })
        }
        install(Logging) {
            level = LogLevel.INFO
        }
        defaultRequest {
            url("https://generativelanguage.googleapis.com/v1beta/models/$model:generateContent?key=$apiKey")
            contentType(ContentType.Application.Json)
        }
    }

    override suspend fun generateUi(prompt: String): String {
        val systemInstruction = """
            You are a Genuino SDK renderer. You must output ONLY valid JSON that matches the following structure:
            {
              "root": {
                "type": "Column" | "Row" | "Text" | "Button" | "Spacer",
                ... (properties based on the type)
              }
            }
            Component types and properties:
            - Text: { "text": string, "style": string?, "color": hexString? }
            - Button: { "text": string, "actionId": string, "payload": { key: value } }
            - Column/Row: { "children": [components], "spacing": int }
            - Spacer: { "size": int }
            
            Do not include markdown or explanations. Return ONLY the JSON object.
        """.trimIndent()

        val response: GeminiResponse = client.post {
            setBody(
                GeminiRequest(
                    contents = listOf(
                        Content(
                            parts = listOf(
                                Part(text = "$systemInstruction\n\nPrompt: $prompt")
                            )
                        )
                    ),
                    generationConfig = GenerationConfig(
                        responseMimeType = "application/json"
                    )
                )
            )
        }.body()

        return response.candidates.firstOrNull()?.content?.parts?.firstOrNull()?.text 
            ?: throw Exception("No content generated")
    }
}

@Serializable
data class GeminiRequest(
    val contents: List<Content>,
    val generationConfig: GenerationConfig? = null
)

@Serializable
data class Content(val parts: List<Part>)

@Serializable
data class Part(val text: String)

@Serializable
data class GenerationConfig(val responseMimeType: String)

@Serializable
data class GeminiResponse(val candidates: List<Candidate>)

@Serializable
data class Candidate(val content: Content)
