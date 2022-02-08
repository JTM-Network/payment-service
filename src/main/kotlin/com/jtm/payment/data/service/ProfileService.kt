package com.jtm.payment.data.service

import com.jtm.payment.core.domain.dto.BasicInfoDto
import com.jtm.payment.core.domain.entity.PaymentProfile
import com.jtm.payment.core.domain.exceptions.ClientIdNotFound
import com.jtm.payment.core.domain.exceptions.FailedCustomerCreation
import com.jtm.payment.core.domain.exceptions.PaymentProfileFound
import com.jtm.payment.core.domain.exceptions.PaymentProfileNotFound
import com.jtm.payment.core.usecase.provider.StripeProvider
import com.jtm.payment.core.usecase.repository.PaymentProfileRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class ProfileService @Autowired constructor(private val profileRepository: PaymentProfileRepository, private val stripeProvider: StripeProvider) {

    fun createProfile(request: ServerHttpRequest, dto: BasicInfoDto): Mono<PaymentProfile> {
        val id = request.headers.getFirst("CLIENT_ID") ?: return Mono.error { ClientIdNotFound() }
        return profileRepository.findById(id)
            .flatMap<PaymentProfile?> { Mono.error(PaymentProfileFound()) }
            .switchIfEmpty(Mono.defer {
                val stripeId = stripeProvider.createCustomer(dto, id) ?: return@defer Mono.error(FailedCustomerCreation())
                profileRepository.save(PaymentProfile(id, stripeId))
            })
    }

    fun getProfile(request: ServerHttpRequest): Mono<PaymentProfile> {
        val id = request.headers.getFirst("CLIENT_ID") ?: return Mono.error { ClientIdNotFound() }
        return profileRepository.findById(id)
            .switchIfEmpty(Mono.defer { Mono.error(PaymentProfileNotFound()) })
    }
}