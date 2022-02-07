package com.jtm.payment.entrypoint.configuration

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import com.jtm.payment.core.usecase.stripe.StripeEventDeserializer
import com.stripe.model.Event
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary

@Configuration
open class AppConfiguration {

    @Bean
    @Primary
    open fun objectMapper(): ObjectMapper {
        val mapper = ObjectMapper()
        val module = SimpleModule()
        module.addDeserializer(Event::class.java, StripeEventDeserializer(mapper))
        return mapper.registerModule(module)
    }
}