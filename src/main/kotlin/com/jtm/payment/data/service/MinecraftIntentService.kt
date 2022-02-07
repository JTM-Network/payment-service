package com.jtm.payment.data.service

import com.jtm.payment.core.domain.exceptions.ClientIdNotFound
import com.jtm.payment.core.domain.exceptions.FailedPaymentIntent
import com.jtm.payment.core.domain.model.PluginIntent
import com.jtm.payment.core.usecase.provider.StripeProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class MinecraftIntentService @Autowired constructor(private val stripeProvider: StripeProvider) {

    fun createPluginIntent(request: ServerHttpRequest, intent: PluginIntent): Mono<String> {
        val id = request.headers.getFirst("CLIENT_ID") ?: return Mono.error { ClientIdNotFound() }
        val secret = stripeProvider.createPaymentIntent(intent.total, intent.currency, id, intent.plugins.toTypedArray()) ?: return Mono.error { FailedPaymentIntent() }
        return Mono.just(secret)
    }
}