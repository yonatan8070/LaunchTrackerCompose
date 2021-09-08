package com.avhar.launchtrackercompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.avhar.launchtrackercompose.ui.theme.LaunchTrackerComposeTheme

class DetailsActivity : ComponentActivity() {
    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DetailsActivityUI()
        }
    }
}

@ExperimentalAnimationApi
@Preview(showBackground = true, device = Devices.PIXEL_3, showSystemUi = true)
@Composable
fun DetailsActivityUI() {
    LaunchTrackerComposeTheme {
        Box {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .align(Alignment.TopCenter)
                    .padding(start = 0.dp, end = 0.dp, top = 0.dp, bottom = 84.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                DetailCard()
            }
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(84.dp)
                    .align(Alignment.BottomCenter),
                shape = RoundedCornerShape(8.dp, 8.dp, 0.dp, 0.dp),
                elevation = 8.dp,
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    Alignment.Center
                ) {
                    CountdownText()
                }
            }
        }
    }
}

@Preview
@Composable
fun DetailCard(launch: Launch = Launch()) {
    Card(
        modifier = Modifier
            .padding(start = 8.dp, end = 8.dp, top = 8.dp)
            .wrapContentHeight(),
        shape = RoundedCornerShape(8.dp),
        elevation = 8.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            Column(
                modifier = Modifier
                    .absolutePadding(8.dp, 8.dp, 8.dp, 8.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    text = launch.name,
                    fontSize = 28.sp
                )
                Text(text = "${launch.provider} - ${launch.type}")
                Text(text = launch.description)
            }
        }
    }
}
