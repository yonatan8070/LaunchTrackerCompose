package com.avhar.launchtrackercompose

import android.annotation.SuppressLint
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.VolleyError
import com.android.volley.toolbox.*
import com.avhar.launchtrackercompose.data.Launch
import com.avhar.launchtrackercompose.data.Rocket
import org.json.JSONObject
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class LLAPIWrapper {
    companion object {
        @SuppressLint("SimpleDateFormat")
        fun getAPIData(): SnapshotStateList<Launch> {
            // Instantiate the cache
            val cache = DiskBasedCache(File("./cacheDir/"), 1024 * 1024) // 1MB cap

            // Set up the network to use HttpURLConnection as the HTTP client.
            val network = BasicNetwork(HurlStack())

            // Instantiate the RequestQueue with the cache and network. Start the queue.
            val requestQueue = RequestQueue(cache, network).apply {
                start()
            }

            val url = "https://lldev.thespacedevs.com/2.2.0/launch/upcoming/?mode=detailed&hide_recent_previous=true"

            val launchList = mutableStateListOf<Launch>()
            val req = JsonObjectRequest(Request.Method.GET, url, null,
                { response: JSONObject ->
                    println("Response received from $url")
                    val launchArray = response.getJSONArray("results")

                    for (i in 0 until launchArray.length()) {
                        println(i)
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

                        val imageURL = jsonLaunch.getString("image")

                        launchList.add(
                            Launch(
                                name = name,
                                provider = provider,
                                net = net,
                                windowStart = winStart,
                                windowEnd = winEnd,
                                description = "",
                                imageURL = imageURL,
                                type = type,
                                rocket = Rocket()
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
    }
}