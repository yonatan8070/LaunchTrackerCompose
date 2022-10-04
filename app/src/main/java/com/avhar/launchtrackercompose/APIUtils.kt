package com.avhar.launchtrackercompose

import android.annotation.SuppressLint
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.VolleyError
import com.android.volley.toolbox.*
import com.avhar.launchtrackercompose.data.Launch
import com.avhar.launchtrackercompose.data.Rocket
import com.avhar.launchtrackercompose.data.TelemetryFrame
import com.avhar.launchtrackercompose.data.TimelineData
import org.json.JSONObject
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class APIUtils {
    companion object {
        // Instantiate the cache
        private val cache = DiskBasedCache(File("./cacheDir/"), 1024 * 1024) // 1MB cap

        // Set up the network to use HttpURLConnection as the HTTP client.
        private val network = BasicNetwork(HurlStack())

        // Instantiate the RequestQueue with the cache and network. Start the queue.
        private val requestQueue = RequestQueue(cache, network).apply {
            start()
        }

        @SuppressLint("SimpleDateFormat")
        fun getLL2Data(): SnapshotStateList<Launch> {
            val url = "https://ll.thespacedevs.com/2.2.0/launch/previous/?mode=detailed&hide_recent_previous=true&limit=50"

            val launchList = mutableStateListOf<Launch>()
            val req = JsonObjectRequest(Request.Method.GET, url, null,
                { response: JSONObject ->
                    println("Response received from $url")
                    val launchArray = response.getJSONArray("results")

                    for (i in 0 until launchArray.length()) {
                        val jsonLaunch = launchArray.getJSONObject(i)

                        val name: String = if (!jsonLaunch.isNull("mission")) {
                            jsonLaunch.getJSONObject("mission").getString("name")
                        } else {
                            jsonLaunch.getString("name")
                        }
                        val provider = jsonLaunch.getJSONObject("launch_service_provider").getString("name")
                        val type = jsonLaunch.getJSONObject("launch_service_provider").getString("type")

                        val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
                        formatter.timeZone = TimeZone.getTimeZone("UTC")

                        val net = formatter.parse(jsonLaunch.getString("net"))
                        val winStart = formatter.parse(jsonLaunch.getString("window_start"))
                        val winEnd = formatter.parse(jsonLaunch.getString("window_end"))

                        val description = if (!jsonLaunch.isNull("mission")) {
                            jsonLaunch.getJSONObject("mission").getString("description")
                        } else {
                            "Description unavailable"
                        }

                        val imageURL = jsonLaunch.getString("image")

                        val ll2id = jsonLaunch.getString("id")

                        val jsonRocket = jsonLaunch.getJSONObject("rocket").getJSONObject("configuration")

                        val rocketName = jsonRocket.optString("full_name")
                        val rocketCost = jsonRocket.optString("launch_cost")
                        val rocketStages = jsonRocket.optInt("max_stage")
                        val penisLength = jsonRocket.optDouble("length")
                        val rocketDiameter = jsonRocket.optDouble("diameter")
                        val rocketMass = jsonRocket.optInt("launch_mass", 0)
                        val totalLaunches = jsonRocket.optInt("total_launch_count")
                        val successfulLaunches = jsonRocket.optInt("successful_launches")
                        val consecutiveSuccessfulLaunches = jsonRocket.optInt("consecutive_successful_launches")
                        val infoLink = jsonRocket.optString("wiki_url")

                        val rocket = Rocket(
                            name = rocketName,
                            cost = rocketCost,
                            stages = rocketStages,
                            length = penisLength,
                            diameter = rocketDiameter,
                            mass = rocketMass,
                            totalLaunches = totalLaunches,
                            successfulLaunches = successfulLaunches,
                            consecutiveSuccessfulLaunches = consecutiveSuccessfulLaunches,
                            infoLink = infoLink,
                        )


                        launchList.add(
                            Launch(
                                name = name,
                                provider = provider,
                                net = net,
                                windowStart = winStart,
                                windowEnd = winEnd,
                                description = description,
                                imageURL = imageURL,
                                type = type,
                                rocket = rocket,
                                ll2id = ll2id
                            )
                        )
                    }
                },
                { error: VolleyError ->
                    System.err.println(error.toString())
                }
            )

            requestQueue.add(req)

            return launchList;
        }

        fun getLDAPIData(ll2id: String = "7cea85fa-b373-4896-83ae-2629f4030806"): MutableState<TimelineData?> {
            val url = "http://api.launchdashboard.space/v2/launches?launch_library_2_id=$ll2id"
            val timelineData: TimelineData? = null
            val output = mutableStateOf(timelineData)

            val req = JsonObjectRequest(Request.Method.GET, url, null,
                { response: JSONObject ->
                    println("Got response from $url")
                    val events = response.getJSONArray("events")
                    val eventMap = HashMap<Int, String>()

                    for (i in 0 until events.length()) {
                        val event = events.getJSONObject(i)
                        if (!event.isNull("time")) {
                            eventMap[event.getInt("time")] = event.optString("key", "missing")
                        }
                    }

                    val stages = response.getJSONArray("analysed")
                    val telemetryMap = HashMap<Int, HashMap<Double, TelemetryFrame>>()

                    for (i in 0 until stages.length()) {
                        val stage = stages.getJSONObject(i)
                        val stageNumber = stage.getInt("stage")

                        val telemetryJSON = stage.getJSONArray("telemetry")
                        val stageTelemetry = HashMap<Double, TelemetryFrame>()

                        for (j in 0 until telemetryJSON.length()) {
                            val frameJSON = telemetryJSON.getJSONObject(j)

                            val velocity = frameJSON.getDouble("velocity")
                            val altitude = frameJSON.getDouble("altitude")
                            val velocityY = frameJSON.getDouble("velocity_y")
                            val velocityX = frameJSON.getDouble("velocity_x")
                            val acceleration = frameJSON.getDouble("acceleration")
                            val downrangeDistance = frameJSON.getDouble("downrange_distance")
                            val angle = frameJSON.getDouble("angle")
                            val aerodynamicPressure = frameJSON.getDouble("q")

                            val frame = TelemetryFrame(
                                velocity = velocity,
                                altitude = altitude,
                                velocityY = velocityY,
                                velocityX = velocityX,
                                acceleration = acceleration,
                                downrangeDistance = downrangeDistance,
                                angle = angle,
                                aerodynamicPressure = aerodynamicPressure,
                            )

                            stageTelemetry[frameJSON.getDouble("time")] = frame
                        }

                        telemetryMap[stageNumber] = stageTelemetry
                    }

                    output.value = TimelineData(
                        events = eventMap,
                        telemetry = telemetryMap
                    )
                },
                { error: VolleyError ->
                    System.err.println(error.toString())
                })

            requestQueue.add(req)

            return output
        }
    }
}