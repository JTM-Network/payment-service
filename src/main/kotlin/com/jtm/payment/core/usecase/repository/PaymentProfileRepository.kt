package com.jtm.payment.core.usecase.repository

import com.jtm.payment.core.domain.entity.PaymentProfile
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono

@Repository
interface PaymentProfileRepository: ReactiveMongoRepository<PaymentProfile, String> {

    fun findByStripeId(stripeId: String): Mono<PaymentProfile>
}