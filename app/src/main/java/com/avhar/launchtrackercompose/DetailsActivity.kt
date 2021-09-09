package com.avhar.launchtrackercompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.avhar.launchtrackercompose.data.Launch
import com.avhar.launchtrackercompose.data.Rocket
import com.avhar.launchtrackercompose.ui.theme.LaunchTrackerComposeTheme
import java.text.SimpleDateFormat
import java.util.*

class DetailsActivity : ComponentActivity() {
    @ExperimentalCoilApi
    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DetailsActivityUI()
        }
    }
}

@ExperimentalCoilApi
@ExperimentalAnimationApi
// @Preview(showBackground = true, device = Devices.PIXEL_3, showSystemUi = true)
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
//                TitleCard()
                ImageCard()
                TimeCard()
                RocketCard()
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

@Composable
fun TitleCard(launch: Launch = Launch()) {
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
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(text = "${launch.provider} - ${launch.type}")
                Text(text = launch.description)
            }
        }
    }
}

@ExperimentalCoilApi
@Composable
fun ImageCard(imageURL: String = "https://spacelaunchnow-prod-east.nyc3.digitaloceanspaces.com/media/agency_images/spacex_image_20190207032501.jpeg") {
    Card(
        modifier = Modifier
            .padding(start = 8.dp, end = 8.dp, top = 8.dp)
            .wrapContentSize(),
        shape = RoundedCornerShape(8.dp),
        elevation = 8.dp
    ) {
        println("Got here")
        Image(
            painter = rememberImagePainter(imageURL),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(64.dp, 384.dp)
        )

    }
}

@Preview
@Composable
fun TimeCard(launch: Launch = Launch()) {
    val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    formatter.timeZone = TimeZone.getDefault()

    Card(
        modifier = Modifier
            .padding(start = 8.dp, end = 8.dp, top = 8.dp)
            .wrapContentHeight(),
        shape = RoundedCornerShape(8.dp),
        elevation = 8.dp
    ) {
        Column(
            modifier = Modifier
                .absolutePadding(8.dp, 8.dp, 8.dp, 8.dp)
                .fillMaxWidth()
        ) {
            Text(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                text = "Timetable",
                fontSize = 24.sp,
                fontWeight = FontWeight.Medium
            )
            Text(text = "NET: ${formatter.format(launch.net)}")
            Text(text = "Window start: ${formatter.format(launch.windowStart)}")
            Text(text = "Window end: ${formatter.format(launch.windowEnd)}")
            Button(
                onClick = { /*TODO*/ }, modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {
                Text(
                    text = "More details",
                )
            }
        }
    }
}

@Composable
fun RocketCard(rocket: Rocket = Rocket()) {
    Card(
        modifier = Modifier
            .padding(start = 8.dp, end = 8.dp, top = 8.dp)
            .wrapContentHeight(),
        shape = RoundedCornerShape(8.dp),
        elevation = 8.dp
    ) {
        Column(
            modifier = Modifier
                .absolutePadding(8.dp, 8.dp, 8.dp, 8.dp)
                .fillMaxWidth()
        ) {
            Text(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                text = rocket.name,
                fontSize = 24.sp,
                fontWeight = FontWeight.Medium
            )
            RocketInfoLine("Total launches", rocket.totalLaunches.toString())
            RocketInfoLine("Successful launches", rocket.successfulLaunches.toString())
            RocketInfoLine("Consecutive successful launches", rocket.consecutiveSuccessfulLaunches.toString())
            RocketInfoLine("Total launches", rocket.totalLaunches.toString())
            RocketInfoLine("Total launches", rocket.totalLaunches.toString())

        }
    }
}

@Composable
fun RocketInfoLine(label: String = "LABEL", value: String = "DATA") {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.6f)
                .wrapContentHeight()
        ) {
            Text(text = label, fontWeight = FontWeight.Medium)
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            Text(text = value)
        }
    }
}