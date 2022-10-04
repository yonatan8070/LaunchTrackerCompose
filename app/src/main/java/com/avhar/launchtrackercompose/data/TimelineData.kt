package com.avhar.launchtrackercompose.data

data class TimelineData(
    val events: HashMap<Int, String>,
    val telemetry: HashMap<Int, HashMap<Double, TelemetryFrame>>
)

data class TelemetryFrame(
    val velocity: Double,
    val altitude: Double,
    val velocityY: Double,
    val velocityX: Double,
    val acceleration: Double,
    val downrangeDistance: Double,
    val angle: Double,
    val aerodynamicPressure: Double
)