package com.jtm.payment.core.usecase.provider

import com.stripe.exception.StripeException
import com.stripe.model.Customer
import com.stripe.model.PaymentMethod
import com.stripe.model.PaymentMethodCollection
import com.stripe.param.PaymentMethodCreateParams
import com.stripe.param.PaymentMethodListParams
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class StripePaymentMethodProvider {

    private val logger = LoggerFactory.getLogger(StripePaymentMethodProvider::class.java)

    fun getPaymentMethod(methodId: String): PaymentMethod? {
        return try {
            PaymentMethod.retrieve(methodId)
        } catch (ex: StripeException) {
            logger.error("Failed to retrieve payment method.", ex)
            null
        }
    }

    fun getPaymentMethods(customerId: String): PaymentMethodCollection? {
        return try {
            val params = PaymentMethodListParams.builder()
                .setCustomer(customerId)
                .setType(PaymentMethodListParams.Type.CARD)
                .build()

            PaymentMethod.list(params)
        } catch (ex: StripeException) {
            logger.error("Failed to retrieve payment methods.", ex)
            null
        }
    }
}