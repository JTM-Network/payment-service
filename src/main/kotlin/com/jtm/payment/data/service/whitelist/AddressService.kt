package com.jtm.payment.data.service.whitelist

import com.jtm.payment.core.domain.dto.AddressDto
import com.jtm.payment.core.domain.entity.IPWhitelist
import com.jtm.payment.core.domain.exceptions.whitelist.AddressFound
import com.jtm.payment.core.domain.exceptions.whitelist.AddressNotFound
import com.jtm.payment.core.usecase.repository.IPWhitelistRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.*

@Service
class AddressService @Autowired constructor(private val ipRepository: IPWhitelistRepository) {

    fun addAddress(dto: AddressDto): Mono<IPWhitelist> {
        return ipRepository.findByAddress(dto.address)
            .flatMap<IPWhitelist> { Mono.error(AddressFound()) }
            .switchIfEmpty(Mono.defer { ipRepository.save(IPWhitelist(address = dto.address)) })
    }

    fun updateAddress(id: UUID, dto: AddressDto): Mono<IPWhitelist> {
        return ipRepository.findById(id)
            .switchIfEmpty(Mono.defer { Mono.error(AddressNotFound()) })
            .flatMap { ipRepository.save(it.updateAddress(dto.address)) }
    }

    fun getAddressById(id: UUID): Mono<IPWhitelist> {
        return ipRepository.findById(id)
            .switchIfEmpty(Mono.defer { Mono.error(AddressNotFound()) })
    }

    fun getAddress(address: String): Mono<IPWhitelist> {
        return ipRepository.findByAddress(address)
            .switchIfEmpty(Mono.defer { Mono.error(AddressNotFound()) })
    }

    fun getAddresses(): Flux<IPWhitelist> {
        return ipRepository.findAll()
    }

    fun deleteAddress(id: UUID): Mono<IPWhitelist> {
        return ipRepository.findById(id)
            .switchIfEmpty(Mono.defer { Mono.error(AddressNotFound()) })
            .flatMap { ipRepository.delete(it).thenReturn(it) }
    }
}