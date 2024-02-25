package com.example.mentorhakka

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mentorhakka.ui.theme.MentorHakkaTheme
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.BlockThreshold
import com.google.ai.client.generativeai.type.HarmCategory
import com.google.ai.client.generativeai.type.SafetySetting
import com.google.ai.client.generativeai.type.asTextOrNull
import com.google.ai.client.generativeai.type.generationConfig
import kotlinx.coroutines.MainScope
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.launch

import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.TopAppBar
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    private val chatViewModel: ChatViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MentorHakkaTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "home") {
                    composable("home") {
                        ScreenHome(navController = navController, chatViewModel = chatViewModel)
                    }
                    composable("history") {
                        // Pass history responses to the history screen
                        HistoryScreen(historyResponses = chatViewModel.chatHistory, navController = navController)
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenHome(modifier: Modifier = Modifier, navController: NavController, chatViewModel: ChatViewModel) {

    var journalEntry by remember { mutableStateOf("") }
    var userAim by remember { mutableStateOf("") }
    var responseShow by remember { mutableStateOf("Your Personal Guide Will Write here!") }
    var isLoading by remember { mutableStateOf(false) } // State to track loading state

    Column{
        TopAppBar(title = { Text("Home") }, modifier = Modifier.fillMaxWidth(),
            navigationIcon = {
                IconButton(onClick = {
                    // Navigate to the history screen when the icon button is clicked
                    navController.navigate("history")
                }) {
                    Icon(Icons.Filled.DateRange, contentDescription = "History")
                }
            }
        )
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = journalEntry,
                onValueChange = { journalEntry = it },
                label = { Text("Journal Entry") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )
            OutlinedTextField(
                value = userAim,
                onValueChange = { userAim = it },
                label = { Text("Aim") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )
            if (userAim.isNotEmpty() && journalEntry.isNotEmpty()) {
                Button(onClick = {
                    val formattedInput =
                        "$journalEntry The preceding is my Journal entry from yesterday. The aim of my life right now is to anyhow achieve $userAim. Mentor me regarding the same. Also tell me what i should do tomorrow, to get better"
                    val scope = MainScope() // Or any other appropriate coroutine scope
                    isLoading = true // Set loading state to true

                    scope.launch {
                        val chat =
                            model.startChat() //INSIDE THE PARENTHESIS, CONTENT TYPE DATA I.E. CHAT HISTORY CAN BE ADDED SO THAT THE MODEL CAN BE TRAINED ON THE SAME and future responses are related
                        val response = chat.sendMessage(formattedInput)
                        // Get the first text part of the first candidate
                        responseShow = response.text.toString()
                        print(response.candidates.first().content.parts.first().asTextOrNull())
                        chatViewModel.addResponse(responseShow)
                        isLoading = false // Set loading state to false after data is fetched
                    }
                    // Call the API with formattedInput
                    // Update the UI with the response from the API
                }) {
                    Text(text = "Get Guidance")
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .size(24.dp)
                                .offset(x = 8.dp),
                            color = Color.White
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .background(Color.Black)
                    .clip(RoundedCornerShape(18.dp))
            ) {
                Text(
                    text = responseShow,
                    style = TextStyle(fontSize = 16.sp, color = Color.White),
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(modifier: Modifier = Modifier, historyResponses: List<Pair<String, String>> ,  navController: NavController) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color.LightGray, RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        TopAppBar(title = { Text("History") },
            navigationIcon = {
                IconButton(onClick = {
                    // Navigate back when the navigation icon is clicked
                    navController.popBackStack()
                }) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                }
            }
        )
        Text(
            text = "History Responses",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(Color.Gray)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            historyResponses.forEachIndexed { index, (response, date) ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                ) {
                    Text(
                        text = "Date: $date",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        text = response,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    if (index < historyResponses.size - 1) {
                        Divider(color = Color.LightGray)
                    }
                }
            }
        }
    }
}





val model = GenerativeModel(
    "gemini-1.0-pro-001",
    // Retrieve API key as an environmental variable defined in a Build Configuration
    // see https://github.com/google/secrets-gradle-plugin for further instructions
    "AIzaSyDeh_2cVlwjg3HGhXB0BfxAjdb78ZRE_UQ",
    generationConfig = generationConfig {
        temperature = 0.9f
        topK = 1
        topP = 1f
        maxOutputTokens = 2048
    },
    safetySettings = listOf(
        SafetySetting(HarmCategory.HARASSMENT, BlockThreshold.ONLY_HIGH),
        SafetySetting(HarmCategory.HATE_SPEECH, BlockThreshold.ONLY_HIGH),
        SafetySetting(HarmCategory.SEXUALLY_EXPLICIT, BlockThreshold.ONLY_HIGH),
        SafetySetting(HarmCategory.DANGEROUS_CONTENT, BlockThreshold.ONLY_HIGH),
    ),
)