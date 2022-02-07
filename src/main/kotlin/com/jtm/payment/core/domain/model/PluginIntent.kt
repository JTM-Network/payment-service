package com.jtm.payment.core.domain.model

import java.util.*

data class PluginIntent(val total: Double, val currency: String = "gbp", val plugins: List<UUID> = listOf())