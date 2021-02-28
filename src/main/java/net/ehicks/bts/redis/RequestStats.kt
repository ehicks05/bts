package net.ehicks.bts.redis

import net.ehicks.bts.NoArg
import java.io.Serializable
import java.util.*

@NoArg
data class RequestStats(
        var requestId: String = "",

        var requestStart: Date = Date(),
        var username: String = "",
        var handlerName: String = "",
        var requestTime: Long = 0,
        var handleTime: Long = 0,
        var postHandleTime: Long = 0,
        var templateTime: Long = 0
) : Serializable