package com.example.mobilecw

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.mobilecw.ui.theme.MobileCWTheme
import kotlinx.coroutines.delay
import kotlin.random.Random

class GuessTheCountryActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MobileCWTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    GuessTheCountryUI()
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
    fun GuessTheCountryUI() {

        //Current Context
        var context = LocalContext.current

        var randomCountry = remember {
            mutableStateOf(countries[Random.nextInt(countries.size)])
        }
        var SelectedCountry by remember { mutableStateOf("") }
        var guessResult by remember { mutableStateOf("") }
        var guessResultColor by remember { mutableStateOf(Color.Transparent) }
        var ButtonLabel by remember { mutableStateOf("Submit") }

        val isSwitchChecked = intent.getBooleanExtra("ifON", false)
        Log.d("GuessTheCountryActivity", "isSwitchChecked: $isSwitchChecked")

        var count by remember { mutableStateOf(10) }

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {


            // Call the Timer composable here
            if (isSwitchChecked) {
                CountdownTimer(isSwitchChecked, count)
            }

            //This is the Button That navigate to the Main Screen
            Button(onClick = {
                val intent = Intent(context, MainActivity::class.java)
                context.startActivity(intent)

            }) {
                Text(text = "Back To Home")
            }

            Image(
                modifier = Modifier
                    .width(350.dp)
                    .height(350.dp),
                painter = painterResource(id = randomCountry.value.flagEmoji),
                contentDescription = "##"
            )

            //Lazy Column to display the country list
            LazyColumn(
                modifier = Modifier
                    .width(300.dp)
                    .height(300.dp)
            ) {
                items(countries) { country ->
                    Text(
                        modifier = Modifier
                            .clickable { SelectedCountry = country.name },
                        text = country.name,

                        //This line display the selected one in green
                        color = if (SelectedCountry == country.name) Color.Green else Color.Black
                    )
                }
            }

            //Submit Button
            Button(onClick = {
                if (ButtonLabel == "Submit") {
                    count = 10
                    if (SelectedCountry == randomCountry.value.name) {
                        guessResult = "CORRECT !"
                        guessResultColor = Color.Green
                        ButtonLabel = "Next"

                    } else {
                        guessResult = "WRONG !  Answer is ${randomCountry.value.name}"
                        guessResultColor = Color.Red
                        ButtonLabel = "Next"
                    }
                } else {

                    //Starting a new activity and optionally finishing the current one
                    var intent = Intent(context, GuessTheCountryActivity::class.java)
                    context.startActivity(intent)
                    (context as? Activity)?.finish()
                }

            }) {
                Text(text = ButtonLabel)
            }

            Text(text = guessResult, color = guessResultColor)
        }
    }

}


