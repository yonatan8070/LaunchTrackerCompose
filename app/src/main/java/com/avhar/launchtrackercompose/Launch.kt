package com.avhar.launchtrackercompose

import java.util.*

data class Launch(
    val name: String,
    val provider: String,
    val net: Date?,
    val windowStart: Date?,
    val windowEnd: Date?
) {
    constructor() : this(
        "Placeholder name",
        "Placeholder provider",
        null,
        null,
        null
    )
}
