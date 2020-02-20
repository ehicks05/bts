package net.ehicks.bts.model

import java.util.*

class UserSession {
    var userId: Long = 0
    var logonId = ""
    var sessionId = ""
    var ipAddress = ""
    var lastActivity: Date? = null
    var enteredController: Long? = null
}