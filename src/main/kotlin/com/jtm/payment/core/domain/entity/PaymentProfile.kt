package com.jtm.payment.core.domain.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document("payment_profiles")
data class PaymentProfile(@Id val id: String, val email: String?, var stripeId: String, var paypalId: String = "") {

    fun updateStripeId(stripeId: String): PaymentProfile {
        this.stripeId = stripeId
        return this
    }
}
