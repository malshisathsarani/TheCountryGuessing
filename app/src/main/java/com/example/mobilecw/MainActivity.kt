
//https://drive.google.com/drive/folders/17H-yu0z5CNkzjhNChtuW-0sCjF0IIjy6?usp=sharing

package com.example.mobilecw

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.mobilecw.ui.theme.MobileCWTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MobileCWTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainUi()
                }
            }
        }
    }
}

// This is the Main User Interface
@Composable
fun MainUi(){

    //current Context
    var context = LocalContext.current

    var ifON by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ){

        // Switch to enable/disable countdown timer
        Switch(
            checked = ifON,
            //it keyword represents the true/ false
            onCheckedChange = {
                ifON = it
            }
        )


        //Guess The Country Button
        Button(
            onClick = {

                //navigate between activities of the Guess The Country Button
                val intent = Intent(context, GuessTheCountryActivity::class.java)
                intent.putExtra("ifON", ifON)
                context.startActivity(intent)

            },
            modifier = Modifier
                .width(200.dp)
                .height(70.dp)
        ) {
            Text(text = "Guess The Country")

        }

        //Guess The Hints Button
        Button(onClick = {

            //navigate between activities of the Guess the Hints Button
            val intent = Intent(context, GuessTheHintsActivity::class.java)
            context.startActivity(intent)
         },
            modifier = Modifier
            .width(200.dp)
            .height(70.dp)) {
            Text(text = "Guess-Hints")
        }

        //Guess The Flag Button
        Button(onClick = {

            //navigate between activities of the Guess The Flag Button
            val intent = Intent(context, GuessTheFlagActivity::class.java)
            context.startActivity(intent)

         },
            modifier = Modifier
            .width(200.dp)
            .height(70.dp)) {
            Text(text = "Guess the Flag")
        }

        //Advance Level Button
        Button(
            onClick = {

                //navigate between activities of the Advance  Button
                val intent = Intent(context, AdvanceActivity2::class.java)
                context.startActivity(intent)

            },
            modifier = Modifier
                .width(200.dp)
                .height(70.dp)) {
            Text(text = "Advance Level")
        }

    }
}




