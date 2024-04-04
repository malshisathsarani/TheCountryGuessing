package com.example.mobilecw

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mobilecw.ui.theme.MobileCWTheme
import kotlinx.coroutines.delay
import kotlin.random.Random

class GuessTheFlagActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MobileCWTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    GuessTheFlagUI()
                }
            }
        }
    }


@Composable
fun CountdownTimer(isSwitchChecked: Boolean, count: Int) {
    if (isSwitchChecked) {
        var currentCount by remember { mutableStateOf(count) }
        LaunchedEffect(count) {
            for (i in count downTo 1) {
                currentCount = i
                delay(1000) // Wait for 1 second
            }
        }
        Text(
            text = currentCount.toString()
        )
    }
}
}

@Composable
fun GuessTheFlagUI() {
    var randomCountry by remember { mutableStateOf<Country?>(null) }
    var shuffledListCountries by remember { mutableStateOf(listOf<Country>()) }
    var SelectedCountry by remember { mutableStateOf("") }
    var guessResult by remember { mutableStateOf("") }
    var guessResultColor by remember { mutableStateOf(Color.Transparent) }
    var ButtonLabel by remember { mutableStateOf("Submit") }

    var count by remember { mutableStateOf(10) }

    // Generate flags and select a random country when the activity starts
    LaunchedEffect(Unit) {
        val threeRandomCountries = countries.shuffled().take(3)
        shuffledListCountries = threeRandomCountries
        randomCountry = shuffledListCountries[Random.nextInt(shuffledListCountries.size)]
    }

    Column(
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(text = "Guess The Flag")

        // Display the countdown timer
        CountdownTimer(true, count)

        //Generate three shuffled country Flags from the list
        for (country in shuffledListCountries) {
            Image(
                modifier = Modifier
                    .size(200.dp)
                    .clickable {
                        SelectedCountry = country.name
                    },
                painter = painterResource(id = country.flagEmoji),
                contentDescription = country.name,
            )
        }
        randomCountry?.let {
            Text(
                style = TextStyle(fontSize = 30.sp),
                text = it.name,
            )
        }

        Button(onClick = {

            //when user click the Submit Button
            if (ButtonLabel == "Submit") {

                count = 10
                if (SelectedCountry == randomCountry!!.name) {
                    guessResult = "CORRECT !"
                    guessResultColor = Color.Green
                    ButtonLabel = "Next"
                } else {
                    guessResult = "WRONG ! Answer is ${randomCountry!!.name} "
                    guessResultColor = Color.Red
                    ButtonLabel = "Next"
                }
            } else {

                // Reset game state
                SelectedCountry = ""
                guessResult = ""
                guessResultColor = Color.Transparent
                ButtonLabel = "Submit"

                // Regenerate flags
                val threeRandomCountries = countries.shuffled().take(3)
                shuffledListCountries = threeRandomCountries
                randomCountry = shuffledListCountries[Random.nextInt(shuffledListCountries.size)]
            }
        }) {
            Text(text = ButtonLabel)
        }
        Text(text = guessResult, color = guessResultColor)
    }
}
