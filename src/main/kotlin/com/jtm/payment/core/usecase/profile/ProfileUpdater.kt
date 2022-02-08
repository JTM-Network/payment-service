package com.jtm.payment.core.usecase.profile

import com.jtm.payment.core.domain.dto.PluginAccessDto
import com.jtm.payment.core.domain.exceptions.profile.FailedAccess
import com.jtm.payment.core.domain.exceptions.profile.FailedDeserialization
import com.jtm.payment.core.domain.exceptions.profile.InvalidPaymentIntent
import com.jtm.payment.core.domain.exceptions.profile.ProfileServerError
import com.jtm.payment.core.util.UtilString
import com.stripe.model.Event
import com.stripe.model.PaymentIntent
import com.stripe.model.StripeObject
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import reactor.core.publisher.Mono

@Component
class ProfileUpdater(@Value("\${hosts.profile.url}") var host: String, @Value("\${hosts.profile.port}") var port: Int) {

    private val client = WebClient.create("${host}:${port}")

    fun addAccess(event: Event): Mono<Void> {
        val dataObjectDeserializer = event.dataObjectDeserializer
        if (!dataObjectDeserializer.`object`.isPresent) return Mono.error { FailedDeserialization() }
        val stripeObject: StripeObject = dataObjectDeserializer.`object`.get()
        val intent: PaymentIntent = stripeObject as PaymentIntent
        val clientId = intent.metadata["clientId"] ?: return Mono.error { InvalidPaymentIntent() }
        val plugins = intent.metadata["plugins"] ?: return Mono.error { InvalidPaymentIntent() }
        val pluginIds = UtilString.stringToPlugins(plugins)
        return client.post()
            .uri("/access")
            .bodyValue(PluginAccessDto(clientId, pluginIds.toList()))
            .exchangeToMono<Void?> {
                if (it.statusCode().is4xxClientError) return@exchangeToMono Mono.error { FailedAccess() }
                if (it.statusCode().is5xxServerError) return@exchangeToMono Mono.error { ProfileServerError() }
                it.bodyToMono()
            }
            .then()
    }
}