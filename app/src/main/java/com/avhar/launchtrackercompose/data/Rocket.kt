package com.avhar.launchtrackercompose.data

import java.io.Serializable

data class Rocket(
    val name: String,
    val cost: String,
    val stages: Int,
    val length: Double,
    val diameter: Double,
    val mass: Double,
    val totalLaunches: Int,
    val successfulLaunches: Int,
    val consecutiveSuccessfulLaunches: Int,
    val infoLink: String
) : Serializable {
    constructor() : this(
        name = "Rocket name",
        cost = "Launch cost",
        stages = 2,
        length = 69.5,
        diameter = 3.5,
        mass = 303.2,
        totalLaunches = 420,
        successfulLaunches = 69,
        consecutiveSuccessfulLaunches = 42,
        infoLink = "https://www.youtube.com/watch?v=dQw4w9WgXcQ"
    )
}
