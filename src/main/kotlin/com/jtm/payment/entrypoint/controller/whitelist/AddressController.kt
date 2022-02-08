package com.jtm.payment.entrypoint.controller.whitelist

import com.jtm.payment.core.domain.dto.AddressDto
import com.jtm.payment.core.domain.entity.IPWhitelist
import com.jtm.payment.data.service.whitelist.AddressService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.*

@RestController
@RequestMapping("/address")
class AddressController @Autowired constructor(private val addressService: AddressService) {

    @PostMapping
    fun postAddress(@RequestBody dto: AddressDto): Mono<IPWhitelist> {
        return addressService.addAddress(dto)
    }

    @PutMapping("/{id}")
    fun putAddress(@PathVariable id: UUID, @RequestBody dto: AddressDto): Mono<IPWhitelist> {
        return addressService.updateAddress(id, dto)
    }

    @GetMapping("/{id}")
    fun getAddress(@PathVariable id: UUID): Mono<IPWhitelist> {
        return addressService.getAddressById(id)
    }

    @GetMapping
    fun getAddressByAddress(@RequestParam("value") address: String): Mono<IPWhitelist> {
        return addressService.getAddress(address)
    }

    @GetMapping("/all")
    fun getAddresses(): Flux<IPWhitelist> {
        return addressService.getAddresses()
    }

    @DeleteMapping("/{id}")
    fun deleteAddress(@PathVariable id: UUID): Mono<IPWhitelist> {
        return addressService.deleteAddress(id)
    }
}