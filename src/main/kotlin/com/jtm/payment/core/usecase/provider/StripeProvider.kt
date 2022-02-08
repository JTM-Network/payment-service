package com.jtm.payment.core.usecase.provider

import com.jtm.payment.core.domain.dto.BasicInfoDto
import com.jtm.payment.core.util.UtilString
import com.stripe.Stripe
import com.stripe.exception.StripeException
import com.stripe.model.Customer
import com.stripe.model.PaymentIntent
import com.stripe.param.CustomerCreateParams
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

            val intent = PaymentIntent.create(params)
            intent.clientSecret
        } catch (ex: StripeException) {
            logger.error("Failed to create payment intent.", ex)
            null
        }
    }

    fun createCustomer(dto: BasicInfoDto, clientId: String): String? {
        val addressBuilder = CustomerCreateParams.Address.builder()
            .setLine1(dto.line1)
            .setLine1(dto.line2)
            .setCountry(dto.country)
            .setCity(dto.city)
            .setPostalCode(dto.postalCode)

        if (!dto.state.isNullOrEmpty()) addressBuilder.setState(dto.state)

       return try {
           val params = CustomerCreateParams.builder()
               .setAddress(addressBuilder.build())
               .setMetadata(mapOf("clientId" to clientId))
               .build()

           val customer = Customer.create(params)
           return customer.id
       } catch (ex: StripeException) {
           logger.error("Failed to create customer.", ex)
           null
       }
    }
}