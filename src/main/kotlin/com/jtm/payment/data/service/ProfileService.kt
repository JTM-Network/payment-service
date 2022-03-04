package com.jtm.payment.data.service

import com.google.gson.GsonBuilder
import com.jtm.payment.core.domain.dto.BasicInfoDto
import com.jtm.payment.core.domain.entity.PaymentProfile
import com.jtm.payment.core.domain.exceptions.*
import com.jtm.payment.core.domain.model.BasicInfo
import com.jtm.payment.core.usecase.provider.StripeCustomerProvider
import com.jtm.payment.core.usecase.repository.PaymentProfileRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class ProfileService @Autowired constructor(private val profileRepository: PaymentProfileRepository, private val stripeProvider: StripeCustomerProvider) {

    private val gson = GsonBuilder().create()

    fun createProfile(request: ServerHttpRequest, dto: BasicInfoDto): Mono<PaymentProfile> {
        val id = request.headers.getFirst("CLIENT_ID") ?: return Mono.error { ClientIdNotFound() }
        val json = request.headers.getFirst("BASIC_INFO") ?: return Mono.error { ClientInformationNotFound() }
        val info = gson.fromJson(json, BasicInfo::class.java)
        return profileRepository.findById(id)
            .flatMap<PaymentProfile?> { Mono.error(PaymentProfileFound()) }
            .switchIfEmpty(Mono.defer {
                val stripeId = stripeProvider.createCustomer(info, dto, id) ?: return@defer Mono.error(FailedCustomerCreation())
                profileRepository.save(PaymentProfile(id, info.email, stripeId))
            })
    }

    fun getProfile(request: ServerHttpRequest): Mono<PaymentProfile> {
        val id = request.headers.getFirst("CLIENT_ID") ?: return Mono.error { ClientIdNotFound() }
        return profileRepository.findById(id)
            .switchIfEmpty(Mono.defer { Mono.error(PaymentProfileNotFound()) })
    }

    fun getProfiles(): Flux<PaymentProfile> {
        return profileRepository.findAll()
    }

    fun removeProfile(id: String): Mono<PaymentProfile> {
        val replaced = id.replace("%40", "|")
        return profileRepository.findById(replaced)
            .switchIfEmpty(Mono.defer { Mono.error(PaymentProfileNotFound()) })
            .flatMap { profileRepository.delete(it).thenReturn(it) }
    }
}