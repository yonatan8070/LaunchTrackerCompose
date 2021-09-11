package com.avhar.launchtrackercompose.activities

import android.content.Intent
import android.os.Bundle
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.avhar.launchtrackercompose.CountdownText
import com.avhar.launchtrackercompose.APIUtils
import com.avhar.launchtrackercompose.data.Launch
import com.avhar.launchtrackercompose.ui.theme.LaunchTrackerComposeTheme

class MainActivity : ComponentActivity() {
    @ExperimentalMaterialApi
    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { MainUI() }
    }
}

@ExperimentalMaterialApi
@ExperimentalAnimationApi
@Preview(showBackground = true)
@Composable
fun MainUI() {
    var launchList = APIUtils.getLL2Data()

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
                        LaunchCard(launch = launch, index = index)
                    }
                }
            }
        }
    }
}

@ExperimentalMaterialApi
@ExperimentalAnimationApi
@Composable
fun LaunchCard(launch: Launch = Launch(), index: Int = 0) {
    val context = LocalContext.current

    Card(
        modifier = Modifier
            .wrapContentHeight(),
        shape = RoundedCornerShape(8.dp),
        elevation = 8.dp,
        onClick = {
            val i = Intent(context, DetailsActivity::class.java)
            i.putExtra("launch", launch)

            context.startActivity(i)
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            Column(
                modifier = Modifier.absolutePadding(8.dp, 8.dp, 8.dp, 8.dp)
            ) {
                Text(text = launch.name)
                Text(text = "${launch.provider} - ${launch.type}")
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(0.dp, 8.dp, 0.dp, 0.dp), contentAlignment = Alignment.Center
                ) {
                    CountdownText(target = launch.net, clockOffset = index * 50)
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