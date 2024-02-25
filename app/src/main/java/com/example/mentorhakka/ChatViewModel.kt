package com.example.mentorhakka

import androidx.lifecycle.ViewModel
import java.text.SimpleDateFormat
import java.util.*

class ChatViewModel : ViewModel() {
    val chatHistory = mutableListOf<Pair<String, String>>() // Pair of response and date

    fun addResponse(response: String) {
        val currentDate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
        chatHistory.add(Pair(response, currentDate))
    }
}

