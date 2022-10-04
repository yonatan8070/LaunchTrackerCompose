package com.avhar.launchtrackercompose.data

import java.io.Serializable
import java.util.*


data class Launch(
    val name: String,
    val provider: String,
    val net: Date?,
    val windowStart: Date?,
    val windowEnd: Date?,
    val description: String,
    val imageURL: String,
    val type: String,
    val rocket: Rocket,
    val ll2id: String
) : Serializable {
    constructor() : this(
        name = "Mission name",
        provider = "Mission provider",
        net = Date(1633796462000),
        windowStart = Date(1633796462000),
        windowEnd = Date(1633796462000),
        description = "Mission description",
        imageURL = "Image URL",
        type = "Mission type",
        rocket = Rocket(),
        ll2id = ""
    )
}
