package com.jtm.payment.data.service.whitelist

import com.jtm.payment.core.domain.dto.DomainDto
import com.jtm.payment.core.domain.entity.DomainWhitelist
import com.jtm.payment.core.domain.exceptions.whitelist.DomainFound
import com.jtm.payment.core.domain.exceptions.whitelist.DomainNotFound
import com.jtm.payment.core.usecase.repository.DomainWhitelistRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.*

@Service
class DomainService @Autowired constructor(private val domainRepository: DomainWhitelistRepository) {

    fun addDomain(dto: DomainDto): Mono<DomainWhitelist> {
        return domainRepository.findByDomain(dto.domain)
            .flatMap<DomainWhitelist?> { Mono.error(DomainFound()) }
            .switchIfEmpty(Mono.defer { domainRepository.save(DomainWhitelist(domain = dto.domain)) })
    }

    fun updateDomain(id: UUID, dto: DomainDto): Mono<DomainWhitelist> {
        return domainRepository.findById(id)
            .switchIfEmpty(Mono.defer { Mono.error(DomainNotFound()) })
            .flatMap { domainRepository.save(it.updateDomain(dto.domain)) }
    }

    fun getDomainById(id: UUID): Mono<DomainWhitelist> {
        return domainRepository.findById(id)
            .switchIfEmpty(Mono.defer { Mono.error(DomainNotFound()) })
    }

    fun getDomain(domain: String): Mono<DomainWhitelist> {
        return domainRepository.findByDomain(domain)
            .switchIfEmpty(Mono.defer { Mono.error(DomainNotFound()) })
    }

    fun getDomains(): Flux<DomainWhitelist> {
        return domainRepository.findAll()
    }

    fun deleteDomain(id: UUID): Mono<DomainWhitelist> {
        return domainRepository.findById(id)
            .switchIfEmpty(Mono.defer { Mono.error(DomainNotFound()) })
            .flatMap { domainRepository.delete(it).thenReturn(it) }
    }
}