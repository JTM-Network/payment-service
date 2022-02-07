package com.jtm.payment.core.usecase.provider

import com.jtm.payment.core.util.UtilString
import com.stripe.Stripe
import com.stripe.exception.StripeException
import com.stripe.model.PaymentIntent
import com.stripe.param.PaymentIntentCreateParams
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.*
import javax.annotation.PostConstruct

@Component
class StripeProvider @Autowired constructor(@Value("\${stripe.secret-key}") val secretKey: String) {

    private val logger = LoggerFactory.getLogger(StripeProvider::class.java)

    @PostConstruct
    fun init() {
        Stripe.apiKey = secretKey
    }

    fun createPaymentIntent(amount: Double, currency: String, clientId: String, plugins: Array<UUID>): String? {
        return try {
            val longAmount = (amount * 100).toLong()
            val params = PaymentIntentCreateParams.builder()
                .setAmount(longAmount)
                .setCurrency(currency)
                .putAllMetadata(mapOf("clientId" to clientId, "plugins" to UtilString.pluginsToString(plugins)))
                .build()

            PaymentIntent.create(params).clientSecret
        } catch (ex: StripeException) {
            logger.error("Failed to create payment intent.", ex)
            null
        }
    }
}