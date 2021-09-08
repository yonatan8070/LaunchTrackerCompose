package com.avhar.launchtrackercompose

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.avhar.launchtrackercompose.ui.theme.LaunchTrackerComposeTheme

class MainActivity : ComponentActivity() {
    var handler: Handler = Handler(Looper.getMainLooper())

    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { MainUI() }
    }
}

@ExperimentalAnimationApi
@Preview(showBackground = true)
@Composable
fun MainUI() {
    var launchList = LLAPIWrapper.getAPIData()

    LaunchTrackerComposeTheme {
        // A surface container using the 'background' color from the theme
        Surface(color = MaterialTheme.colors.background) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LazyColumn(
                    state = rememberLazyListState(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(
                        start = 8.dp,
                        end = 8.dp,
                        top = 8.dp
                    )
                ) {
                    itemsIndexed(launchList) { index, launch ->
                        LaunchCard(launchData = launch, index = index)
                    }
                }
            }
        }
    }
}

@ExperimentalAnimationApi
@Preview
@Composable
fun LaunchCard(launchData: Launch = Launch(), index: Int = 0) {
    Card(
        modifier = Modifier
            .wrapContentHeight(),
        shape = RoundedCornerShape(8.dp),
        elevation = Dp(8F)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            Column(
                modifier = Modifier.absolutePadding(Dp(8F), Dp(8F), Dp(8F), Dp(8F))
            ) {
                Text(text = launchData.name)
                Text(text = launchData.provider)
                Box(modifier = Modifier.fillMaxWidth().padding(0.dp, 8.dp, 0.dp, 0.dp), contentAlignment = Alignment.Center) {
                    CountdownText(target = launchData.net, clockOffset = index * 50)
                }
            }
            Surface(
                color = Color.Blue, modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
            ) {
            }
        }
    }
}