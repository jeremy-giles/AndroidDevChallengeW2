/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.androiddevchallenge

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.androiddevchallenge.ui.theme.MyTheme
import com.example.androiddevchallenge.ui.theme.beige
import com.example.androiddevchallenge.ui.theme.beige500
import com.example.androiddevchallenge.ui.theme.blue100
import com.example.androiddevchallenge.ui.theme.orange500
import com.example.androiddevchallenge.ui.theme.orange900

@ExperimentalAnimationApi
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyTheme {
                MyApp()
            }
        }
    }
}

// Start building your app here!
@ExperimentalAnimationApi
@Composable
fun MyApp() {
    // Timer
    val initialCountDown = 10000
    val progress = remember { mutableStateOf(1F) }
    val timeLeft = remember { mutableStateOf(10) }
    val countdownStart = remember { mutableStateOf(initialCountDown) }
    val timerRunning = remember { mutableStateOf(false) }

    // Banner
    val initialBannerCountDown = 4000
    val progressBanner = remember { mutableStateOf(1F) }
    val timeLeftBanner = remember { mutableStateOf(10) }
    val countdownBannerStart = remember { mutableStateOf(initialBannerCountDown) }
    val timerBannerRunning = remember { mutableStateOf(false) }

    val timerBanner: CountDownTimer =
        object : CountDownTimer(countdownBannerStart.value.toLong(), 10) {

            override fun onTick(millisUntilFinished: Long) {
                val value: Float =
                    millisUntilFinished.toFloat() / (countdownBannerStart.value.toFloat())
                progressBanner.value = value
                timeLeftBanner.value = (millisUntilFinished / 1000).toInt()
                Log.d("onTick", "${(progress.value * 10000) % 1000}")
            }

            override fun onFinish() {
                cancel()
                timerBannerRunning.value = false
                // Reset banner
                progressBanner.value = 1F
                timeLeftBanner.value = (countdownStart.value / 1000)

                // Reset Timer
                progress.value = 1F
                timeLeft.value = (countdownStart.value / 1000)
            }
        }

    val timer: CountDownTimer = object : CountDownTimer(countdownStart.value.toLong(), 10) {

        override fun onTick(millisUntilFinished: Long) {
            val value: Float = millisUntilFinished.toFloat() / (countdownStart.value.toFloat())
            progress.value = value
            timeLeft.value = (millisUntilFinished / 1000).toInt()
        }

        override fun onFinish() {
            cancel()
            timerRunning.value = false

            timerBanner.start()
            timerBannerRunning.value = true

            // progress.value = 1F
            // timeLeft.value = (countdownStart.value / 1000)
        }
    }

    // Animate State
    val borderStateColor by animateColorAsState(
        if (timeLeft.value < 6 && ((progress.value * 10000) % 1000 > 500))
            orange900 else orange500
    )
    val backgroundStateColor by animateColorAsState(
        if (timeLeft.value < 6 && ((progress.value * 10000) % 1000 > 500))
            beige500 else beige
    )
    val borderSizeState by animateDpAsState(
        targetValue = if (timeLeft.value < 6 && ((progress.value * 10000) % 1000 > 500)) 8.dp else 5.dp
    )
    val widthState by animateDpAsState(
        targetValue = if (timeLeft.value < 6 && ((progress.value * 10000) % 1000 > 500)) {
            305.dp
        } else {
            300.dp
        }
    )
    val heightState by animateDpAsState(
        targetValue = if (timeLeft.value < 6 && ((progress.value * 10000) % 1000 > 500)) {
            95.dp
        } else {
            90.dp
        }
    )
    val visibilityState by animateIntAsState(targetValue = if (timeLeft.value <= 0) 1 else 0)

    val offsetBanner by animateDpAsState(
        targetValue = (
            (100 * (progress.value * 10000) / 4000).dp
            )
    )

    // View
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "It's time to play",
                        fontFamily = FontFamily(
                            Font(R.font.hammersmith_one_regular)
                        ),
                        fontSize = 22.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_tent),
                            contentDescription = "Menu"
                        )
                    }
                }

            )
        }
    ) {
        Surface(
            color = MaterialTheme.colors.background,
            modifier = Modifier.fillMaxSize()
        ) {

            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(bottom = 200.dp)
            ) {
                Row(
                    Modifier.width(300.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = {
                            timer.start()
                            timerRunning.value = true
                        },
                        modifier = Modifier
                            .weight(1f)
                            .border(4.dp, orange500, RoundedCornerShape(20.dp))
                            .background(beige, RoundedCornerShape(20.dp)),
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_butterfly),
                            contentDescription = "butterfly",
                            tint = orange500,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    IconButton(
                        onClick = {
                            timer.start()
                            timerRunning.value = true
                        },
                        modifier = Modifier
                            .weight(1f)
                            .border(4.dp, orange500, RoundedCornerShape(20.dp))
                            .background(beige, RoundedCornerShape(20.dp)),
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_fish),
                            tint = orange500,
                            contentDescription = "fish",
                        )
                    }
                }
            }

            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(top = 0.dp)
            ) {
                Row(
                    Modifier
                        .width(widthState)
                        .height(heightState)
                        .border(
                            borderSizeState, borderStateColor,
                            RoundedCornerShape(30.dp)
                        )
                        .background(backgroundStateColor, RoundedCornerShape(30.dp)),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painterResource(id = R.drawable.ic_stop_watch),
                        contentDescription = "Stopwatch",
                        modifier = Modifier
                            .height(70.dp)
                            .width(70.dp),
                    )
                    Text(
                        text = "00",
                        style = TextStyle(
                            // fontSize = if (timeLeft.value >= 3600) 48.sp else 72.sp,
                            fontSize = 50.sp,
                            fontWeight = FontWeight.Bold,
                            color = borderStateColor,
                            /*fontFamily = FontFamily(
                                Font(R.font.hammersmith_one_regular)
                            )*/
                        ),
                    )
                    // Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        text = ":",
                        style = TextStyle(
                            fontSize = 50.sp,
                            fontWeight = FontWeight.Medium,
                            color = borderStateColor,
                            fontFamily = FontFamily(
                                Font(R.font.hammersmith_one_regular)
                            )
                        ),
                        modifier = Modifier.alpha(
                            if ((progress.value * 10000) % 1000 < 500) {
                                1f
                            } else {
                                0f
                            }
                        )
                    )
                    // Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        text = String.format("%02d", timeLeft.value),
                        style = TextStyle(
                            // fontSize = if (timeLeft.value >= 3600) 48.sp else 72.sp,
                            fontSize = 50.sp,
                            fontWeight = FontWeight.Bold,
                            color = borderStateColor,
                            /*fontFamily = FontFamily(
                                Font(R.font.hammersmith_one_regular)
                            )*/
                        ),
                        textAlign = TextAlign.Center,
                    )
                }
            }

            if (visibilityState == 1) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp)
                            .background(beige.copy(alpha = 0.90f)),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Time's up!",
                            style = TextStyle(
                                fontSize = 50.sp,
                                fontWeight = FontWeight.Bold,
                                color = blue100,
                                fontFamily = FontFamily(
                                    Font(R.font.hammersmith_one_regular)
                                )
                            ),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.offset(offsetBanner, 0.dp)
                        )
                    }
                }
            }

            Box(
                modifier = Modifier.fillMaxSize(),
                Alignment.BottomStart
            ) {
                Image(
                    painter = painterResource(id = R.drawable.jacques),
                    contentDescription = "jacques",
                    modifier = Modifier
                        .height(300.dp)
                        .width(200.dp),
                )
            }
        }
    }
}

@ExperimentalAnimationApi
@Preview("Light Theme", widthDp = 360, heightDp = 640)
@Composable
fun LightPreview() {
    MyTheme {
        MyApp()
    }
}

@ExperimentalAnimationApi
@Preview("Dark Theme", widthDp = 360, heightDp = 640)
@Composable
fun DarkPreview() {
    MyTheme(darkTheme = true) {
        MyApp()
    }
}
