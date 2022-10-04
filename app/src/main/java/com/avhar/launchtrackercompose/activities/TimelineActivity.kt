package com.avhar.launchtrackercompose.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.avhar.launchtrackercompose.APIUtils.Companion.getLDAPIData
import com.avhar.launchtrackercompose.activities.ui.theme.LaunchTrackerComposeTheme
import com.avhar.launchtrackercompose.data.Launch
import com.avhar.launchtrackercompose.data.TimelineData
import kotlin.math.max

class TimelineActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val launch: Launch = this.intent.getSerializableExtra("launch") as Launch
        val data = getLDAPIData(launch.ll2id)

        setContent {
            MainTimelineUI(data.value)
        }
    }
}

@Preview(showBackground = true, device = Devices.PIXEL_3)
@Composable
fun MainTimelineUI(data: TimelineData? = null) {
    LaunchTrackerComposeTheme {
        var endTime: Int = Int.MIN_VALUE

        data?.events?.forEach { event ->
            endTime = max(event.key, endTime)
        }

        val timeToSizeRatio = remember { mutableStateOf(1.0) }

        Surface(color = MaterialTheme.colors.background) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 24.dp, top = 24.dp, bottom = 24.dp)
                    .onGloballyPositioned { coordinates ->
                        timeToSizeRatio.value = coordinates.size.height.toDouble() / endTime.toDouble()
                    }
            ) {
                Timeline()
                data?.events?.forEach { event ->
                    TimePoint(
                        label = event.value,
                        positionPx = (event.key * timeToSizeRatio.value).toInt()
                    )
                }
            }
        }
    }
}

@Composable
fun Timeline() {
    val color = Color.Black
    val width = 8.dp

    Box(
        modifier = Modifier
            .fillMaxHeight()
            .width(width)
            .background(color)
    ) {
        Box(
            modifier = Modifier
                .size(width)
                .offset(y = (-4).dp)
                .background(color, RoundedCornerShape(50))
        )
        Box(
            modifier = Modifier
                .size(width)
                .offset(y = 4.dp)
                .align(Alignment.BottomCenter)
                .background(color, RoundedCornerShape(50))
        )
    }
}

@Composable
fun TimePoint(label: String = "", positionPx: Int = 0) {
    val yOffset = remember { mutableStateOf(0) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .offset { IntOffset(x = 0, y = yOffset.value) }
            .onGloballyPositioned { coordinates ->
                // This calculates how far up the row needs to move to have it position by the center, with
                yOffset.value = ((coordinates.size.height / 2) * -1) + positionPx
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(16.dp)
                .offset(x = (-4).dp)
                .background(Color.Blue, RoundedCornerShape(50))
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = label)
    }
}