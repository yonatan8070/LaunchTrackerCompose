package com.avhar.launchtrackercompose

import android.os.Handler
import android.os.Looper
import androidx.compose.animation.*
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.*

@Preview(showBackground = true)
@ExperimentalAnimationApi
@Composable
fun CountdownText(target: Date? = Date(1633796462000), clockOffset: Int = 0) {
    if (target != null) {
        var timeUntilLaunch by remember { mutableStateOf((target.time - System.currentTimeMillis()) / 1000L) }

        val outputText: String = String.format(
            "T- %02d : %02d : %02d : %02d",
            timeUntilLaunch / 86400,
            (timeUntilLaunch / 3600) % 24,
            (timeUntilLaunch % 3600) / 60,
            (timeUntilLaunch % 60)
        )

        val days = timeUntilLaunch / 86400
        val hours = (timeUntilLaunch / 3600) % 24
        val minutes = (timeUntilLaunch % 3600) / 60
        val seconds = (timeUntilLaunch % 60)

        val handler = Handler(Looper.getMainLooper())
        val runnable = object : Runnable {
            override fun run() {
                timeUntilLaunch = (target.time - System.currentTimeMillis() + clockOffset) / 1000L
                handler.postDelayed(this, 1000 - (System.currentTimeMillis() + clockOffset % 1000))
            }
        }

        handler.postDelayed(runnable, 1000 - (System.currentTimeMillis() + clockOffset % 1000))

        Row {
            NumberWithLabel(
                value = String.format("%02d", days),
                label = "Days",
                width = 72.dp
            )
            NumberWithLabel(
                value = String.format("%02d", hours),
                label = "Hours",
                width = 72.dp
            )
            NumberWithLabel(
                value = String.format("%02d", minutes),
                label = "Minutes",
                width = 72.dp
            )
            NumberWithLabel(
                value = String.format("%02d", seconds),
                label = "Seconds",
                width = 72.dp
            )
        }
    } else {
        Text(text = "Launch net unavailable")
        System.err.println("Got null for target date, cannot compute countdown")
    }
}

@ExperimentalAnimationApi
@Composable
fun AnimatedNumber(value: String) {
    Row(modifier = Modifier.width(48.dp)) {
        value.forEach { char ->
            AnimatedContent(
                targetState = char,
                transitionSpec = {
                    slideInHorizontally({ height -> -height }) + fadeIn() with
                            slideOutHorizontally({ height -> height }) + fadeOut()
                }
            ) { outputChar ->
                Text(
                    text = outputChar.toString(),
                    fontSize = 36.sp
                )
            }
        }
    }
}

@Preview(showBackground = true)
@ExperimentalAnimationApi
@Composable
fun NumberWithLabel(value: String = "69", label: String = "Seconds", width: Dp = 72.dp) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(width)
    ) {
        Row {
            value.forEach { char ->
                AnimatedContent(
                    targetState = char,
                    transitionSpec = {
                        slideInHorizontally({ height -> -height }) + fadeIn() with
                                slideOutHorizontally({ height -> height }) + fadeOut()
                    }
                ) { outputChar ->
                    Text(
                        text = outputChar.toString(),
                        fontSize = 36.sp
                    )
                }
            }
        }
        Text(text = label)
    }
}