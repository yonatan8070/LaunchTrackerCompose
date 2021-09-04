package com.avhar.launchtrackercompose

import android.annotation.SuppressLint
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.VolleyError
import com.android.volley.toolbox.*
import org.json.JSONObject
import java.io.File
import java.text.SimpleDateFormat

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

            var launchList = mutableStateListOf<Launch>()
            val req = JsonObjectRequest(Request.Method.GET, url, null,
                { response: JSONObject ->
                    println("Response received from $url")
                    var launchArray = response.getJSONArray("results")

                    for (i in 0 until launchArray.length()) {
                        var jsonLaunch = launchArray.getJSONObject(i)

                        var name = jsonLaunch.getString("name")
                        var provider = jsonLaunch.getJSONObject("launch_service_provider").getString("name")

                        var formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")

                        var net = formatter.parse(jsonLaunch.getString("net"))
                        var winStart = formatter.parse(jsonLaunch.getString("window_start"))
                        var winEnd = formatter.parse(jsonLaunch.getString("window_end"))

                        launchList.add(Launch(name, provider, net, winStart, winEnd))
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