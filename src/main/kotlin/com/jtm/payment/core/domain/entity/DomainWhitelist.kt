package com.jtm.payment.core.domain.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document("domains_whitelisted")
data class DomainWhitelist(@Id val id: UUID = UUID.randomUUID(), var domain: String, var updated: Long = System.currentTimeMillis(), val added: Long = System.currentTimeMillis()) {

    fun updateDomain(domain: String): DomainWhitelist {
        this.domain = domain
        this.updated = System.currentTimeMillis()
        return this
    }
}
