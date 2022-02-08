package com.jtm.payment.core.domain.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document("ips_whitelisted")
data class IPWhitelist(@Id val id: UUID = UUID.randomUUID(), var address: String, var updated: Long = System.currentTimeMillis(), val added: Long = System.currentTimeMillis()) {

    fun updateAddress(address: String): IPWhitelist {
        this.address = address
        this.updated = System.currentTimeMillis()
        return this
    }
}
