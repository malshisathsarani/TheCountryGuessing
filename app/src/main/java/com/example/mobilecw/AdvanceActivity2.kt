package com.example.mobilecw

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mobilecw.ui.theme.MobileCWTheme

class AdvanceActivity2 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MobileCWTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AdvancedLevelGame()
                }
            }
        }
    }
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun AdvancedLevelGame() {
        val context = LocalContext.current
        var randomCountryList by remember { mutableStateOf(countries.shuffled().take(3)) }
        var userGuesses by remember { mutableStateOf(listOf("", "", "")) }
        var userGuessColors by remember { mutableStateOf(listOf(Color.Yellow, Color.Yellow, Color.Yellow)) }
        var gameMode by remember { mutableStateOf("") }
        var buttonText by remember { mutableStateOf("Submit") }
        var attempts by remember { mutableStateOf(0) }
        var userScore by remember { mutableStateOf(0) }


        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            //display user score
            Text(text = "Score ${userScore} / 3")

            //display user attempts
            Text(text = "Attempts ${attempts} / 3")

            // Random Countries
            for(i in 0..2){
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Image(
                        painter = painterResource(id = randomCountryList[i].flagEmoji),
                        contentDescription = randomCountryList[i].name,
                        modifier = Modifier
                            .width(100.dp)
                            .height(100.dp)
                    )
                    TextField(
                        value = userGuesses[i],
                        onValueChange = { newValue -> userGuesses = userGuesses.toMutableList().apply { this[i] = newValue } },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        colors = TextFieldDefaults.textFieldColors(
                            containerColor = userGuessColors[i]
                        ),
                    )
                }
            }

            //Space between image and textfield
            Spacer(modifier = Modifier.height(16.dp))

            //submit button
            Button(
                modifier = Modifier
                    .width(250.dp)
                    .height(80.dp),
                onClick = {
                    if (gameMode == "CORRECT!" || gameMode == "WRONG!") {
                        var intent = Intent(context, MainActivity::class.java)
                        context.startActivity(intent)
                        finish()
                    } else {
                        var correctCount = 0
                        userGuessColors = userGuesses.map { guess ->
                            if (guess == randomCountryList.firstOrNull { it.name == guess }?.name) {
                                correctCount++
                                Color.Green
                            } else {
                                Color.Red
                            }
                        }
                        userScore = correctCount
                        if (correctCount == 3) {
                            gameMode = "CORRECT!"
                            buttonText = "Next"
                        } else {
                            attempts++
                            if (attempts >= 3) {
                                gameMode = "WRONG!"
                                buttonText = "Next"
                            }
                        }
                    }
                }
            ) {
                Text(buttonText)
            }

            //correct and wrong msg below
            val color = when (gameMode) {
                "CORRECT!" -> Color.Green
                "WRONG!" -> Color.Red
                else -> Color.Transparent
            }
            val text = when (gameMode) {
                "CORRECT!" -> gameMode
                "WRONG!" -> "$gameMode\nCorrect answers: ${randomCountryList.joinToString { it.name }}"
                else -> ""
            }
            val fontSize = when (gameMode) {
                "CORRECT!", "WRONG!" -> 20.sp
                else -> 16.sp
            }
            Text(
                text = text,
                color = color,
                style = TextStyle(fontSize = fontSize)
            )
        }
    }
}

