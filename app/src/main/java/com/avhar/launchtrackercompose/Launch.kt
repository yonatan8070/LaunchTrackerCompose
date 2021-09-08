package com.avhar.launchtrackercompose

import java.util.*

data class Launch(
    val name: String,
    val provider: String,
    val net: Date?,
    val windowStart: Date?,
    val windowEnd: Date?,
    val description: String,
    val imageURL: String,
    val type: String
) {
    constructor() : this(
        name = "Mission name",
        provider = "Mission provider",
        net = null,
        windowStart = null,
        windowEnd = null,
        description = "Mission description",
        imageURL = "Image URL",
        type = "Mission type"
    )
}
