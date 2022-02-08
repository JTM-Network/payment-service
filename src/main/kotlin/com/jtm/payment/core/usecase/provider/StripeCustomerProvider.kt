package com.jtm.payment.core.usecase.provider

import com.jtm.payment.core.domain.dto.BasicInfoDto
import com.stripe.exception.StripeException
import com.stripe.model.Customer
import com.stripe.param.CustomerCreateParams
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class StripeCustomerProvider {

    private val logger = LoggerFactory.getLogger(StripeCustomerProvider::class.java)

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