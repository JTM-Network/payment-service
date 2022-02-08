package com.jtm.payment.core.usecase.repository

import com.jtm.payment.core.domain.entity.IPWhitelist
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono
import java.util.*

@Repository
interface IPWhitelistRepository: ReactiveMongoRepository<IPWhitelist, UUID> {

    fun findByAddress(address: String): Mono<IPWhitelist>
}