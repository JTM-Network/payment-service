package com.jtm.payment.core.usecase.repository

import com.jtm.payment.core.domain.entity.DomainWhitelist
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono
import java.util.*

@Repository
interface DomainWhitelistRepository: ReactiveMongoRepository<DomainWhitelist, UUID> {

    fun findByDomain(domain: String): Mono<DomainWhitelist>
}