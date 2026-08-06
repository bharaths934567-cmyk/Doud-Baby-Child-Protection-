package com.example.data.telegram

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

class TelegramService {

    private val client = OkHttpClient()

    suspend fun validateBotToken(token: String): Result<String> = withContext(Dispatchers.IO) {
        try {
            val url = "https://api.telegram.org/bot$token/getMe"
            val request = Request.Builder().url(url).get().build()
            client.newCall(request).execute().use { response ->
                val bodyStr = response.body?.string() ?: ""
                if (response.isSuccessful) {
                    val json = JSONObject(bodyStr)
                    if (json.optBoolean("ok")) {
                        val result = json.getJSONObject("result")
                        val botName = result.optString("first_name", "Telegram Bot")
                        val username = result.optString("username", "bot")
                        Result.success("@$username ($botName)")
                    } else {
                        Result.failure(Exception("Telegram API Error: ${json.optString("description")}"))
                    }
                } else {
                    Result.failure(Exception("HTTP ${response.code}: Invalid Bot Token format or server unreachable"))
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun sendMessage(token: String, chatId: String, messageText: String): Result<Boolean> = withContext(Dispatchers.IO) {
        try {
            val url = "https://api.telegram.org/bot$token/sendMessage"
            val jsonBody = JSONObject().apply {
                put("chat_id", chatId)
                put("text", messageText)
                put("parse_mode", "HTML")
            }
            val mediaType = "application/json; charset=utf-8".toMediaType()
            val body = jsonBody.toString().toRequestBody(mediaType)
            val request = Request.Builder().url(url).post(body).build()

            client.newCall(request).execute().use { response ->
                if (response.isSuccessful) {
                    Result.success(true)
                } else {
                    Result.failure(Exception("Failed to dispatch to Telegram. Code: ${response.code}"))
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
