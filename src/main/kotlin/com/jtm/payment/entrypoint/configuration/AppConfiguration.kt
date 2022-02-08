package com.jtm.payment.entrypoint.configuration

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import com.jtm.payment.core.usecase.stripe.StripeEventDeserializer
import com.stripe.Stripe
import com.stripe.model.Event
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import javax.annotation.PostConstruct

@Configuration
open class AppConfiguration {

    @Value("\${stripe.secret-key}")
    lateinit var secretKey: String

    @PostConstruct
    fun init() {
        Stripe.apiKey = secretKey
    }

    @Bean
    @Primary
    open fun objectMapper(): ObjectMapper {
        val mapper = ObjectMapper()
        val module = SimpleModule()
        module.addDeserializer(Event::class.java, StripeEventDeserializer(mapper))
        return mapper.registerModule(module)
    }
}