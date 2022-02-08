package com.jtm.payment.core.domain.dto

import java.util.*

data class PluginAccessDto(val clientId: String, val plugins: List<UUID>)