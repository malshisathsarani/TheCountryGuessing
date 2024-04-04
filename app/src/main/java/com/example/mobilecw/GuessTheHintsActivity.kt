package com.example.mobilecw

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mobilecw.ui.theme.MobileCWTheme
import kotlinx.coroutines.delay
import kotlin.random.Random

class GuessTheHintsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MobileCWTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    GuessTheHintsUI()
                }
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

@Composable
fun GuessTheHintsUI() {
    val context = LocalContext.current
    var numberOfChances by remember { mutableIntStateOf(3) }
    var randomNumber by remember { mutableIntStateOf(Random.nextInt(countries.size)) }
    var randomCountry by remember { mutableStateOf(countries[randomNumber]) }
    var guessedLetters by remember { mutableStateOf(listOf<Char>()) }
    var currentGuess by remember { mutableStateOf("") }
    var gameResult by remember { mutableStateOf("") }
    var gameResultColor by remember { mutableStateOf(Color.Transparent) }
    var buttonLabel by remember { mutableStateOf("Submit") }
    val focusManager = LocalFocusManager.current

    var count by remember { mutableStateOf(10) }

    Column(
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {

        // Display the countdown timer
        CountdownTimer(true, count)


        //Display the number of chances
        Surface (
            color = Color.Yellow
        ){
            Text(
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 30.sp
                ),
                text = "Attempts: $numberOfChances"
            )
        }

        //Random image
        Image(
            painter = painterResource(id = randomCountry.flagEmoji),
            contentDescription = "Flag of ${randomCountry.name}",
            modifier = Modifier
                .width(350.dp)
                .height(350.dp),
        )

        // Add a vertical space
        Spacer(modifier = Modifier.height(20.dp))

        // Generate a display name for the country with guessed letters
        val displayName = randomCountry.name.map { char ->
            if (char in guessedLetters) char else '-'
        }.joinToString("")

        Text(
            text = displayName,
            style = TextStyle(fontSize = 60.sp),
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Update user Guess with the new value
        TextField(
            value = currentGuess,
            onValueChange = { newValue ->
                currentGuess = newValue
            },
            label = { Text("Guess a letter") },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = {
                focusManager.clearFocus()
            }),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        //Submit Button
        Button(
            onClick = {
                if (buttonLabel == "Submit") {

                    count = 10

                    // Get the single character from the current guess
                    val guessedChar = currentGuess.singleOrNull() ?: return@Button

                    //check the condition
                    if (guessedChar in randomCountry.name) {
                        guessedLetters = guessedLetters + guessedChar
                        // Debug log
                        println("Guessed letters: $guessedLetters")
                        println("Country name: ${randomCountry.name}")
                        println("All letters guessed: ${guessedLetters.toSet().size == randomCountry.name.length}")
                        if (guessedLetters.toSet().size == randomCountry.name.length) {

                            //Update the game result and button label
                            gameResult = "CORRECT!"
                            gameResultColor = Color.Green
                            buttonLabel = "Next"
                        }
                    } else {
                        numberOfChances -= 1
                        if (numberOfChances == 0) {
                            gameResult = "WRONG! The correct country is ${randomCountry.name}."
                            gameResultColor = Color.Red
                            buttonLabel = "Next"

                        }

                    }
                    currentGuess = ""
                    focusManager.clearFocus()
                }else{

                    //Starting a new activity and optionally finishing the current one
                    var intent = Intent(context, GuessTheHintsActivity::class.java)
                    context.startActivity(intent)
                    (context as? Activity)?.finish()
                }
            },
            modifier = Modifier
                .width(250.dp)
                .height(80.dp),
        ) {
            Text(buttonLabel)
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = gameResult,
            color = gameResultColor,
            style = TextStyle(fontSize = 20.sp)
        )
    }
}
