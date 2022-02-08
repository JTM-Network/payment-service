package com.jtm.payment.data.service.whitelist

import com.jtm.payment.core.domain.dto.AddressDto
import com.jtm.payment.core.domain.entity.IPWhitelist
import com.jtm.payment.core.domain.exceptions.whitelist.AddressFound
import com.jtm.payment.core.domain.exceptions.whitelist.AddressNotFound
import com.jtm.payment.core.usecase.repository.IPWhitelistRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import org.mockito.kotlin.anyOrNull
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.test.context.junit4.SpringRunner
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import java.util.*

@RunWith(SpringRunner::class)
class AddressServiceTest {

    private val ipRepository: IPWhitelistRepository = mock()
    private val addressService = AddressService(ipRepository)
    private val ip = IPWhitelist(address = "127.0.0.1")
    private val dto = AddressDto(address = "localhost")

    @Test
    fun addAddress_thenFound() {
        `when`(ipRepository.findByAddress(anyString())).thenReturn(Mono.just(ip))

        val returned = addressService.addAddress(dto)

        verify(ipRepository, times(1)).findByAddress(anyString())
        verifyNoMoreInteractions(ipRepository)

        StepVerifier.create(returned)
            .expectError(AddressFound::class.java)
            .verify()
    }

    @Test
    fun addAddress() {
        `when`(ipRepository.findByAddress(anyString())).thenReturn(Mono.empty())
        `when`(ipRepository.save(anyOrNull())).thenReturn(Mono.just(ip))

        val returned = addressService.addAddress(dto)

        verify(ipRepository, times(1)).findByAddress(anyString())
        verifyNoMoreInteractions(ipRepository)

        StepVerifier.create(returned)
            .assertNext { assertThat(it.address).isEqualTo("127.0.0.1") }
            .verifyComplete()
    }

    @Test
    fun updateAddress_thenNotFound() {
        `when`(ipRepository.findById(any(UUID::class.java))).thenReturn(Mono.empty())

        val returned = addressService.updateAddress(UUID.randomUUID(), dto)

        verify(ipRepository, times(1)).findById(any(UUID::class.java))
        verifyNoMoreInteractions(ipRepository)

        StepVerifier.create(returned)
            .expectError(AddressNotFound::class.java)
            .verify()
    }

    @Test
    fun updateAddress() {
        `when`(ipRepository.findById(any(UUID::class.java))).thenReturn(Mono.just(ip))
        `when`(ipRepository.save(anyOrNull())).thenReturn(Mono.just(ip))

        val returned = addressService.updateAddress(UUID.randomUUID(), dto)

        verify(ipRepository, times(1)).findById(any(UUID::class.java))
        verifyNoMoreInteractions(ipRepository)

        StepVerifier.create(returned)
            .assertNext { assertThat(it.address).isEqualTo("localhost") }
            .verifyComplete()
    }

    @Test
    fun getAddressById_thenNotFound() {
        `when`(ipRepository.findById(any(UUID::class.java))).thenReturn(Mono.empty())

        val returned = addressService.getAddressById(UUID.randomUUID())

        verify(ipRepository, times(1)).findById(any(UUID::class.java))
        verifyNoMoreInteractions(ipRepository)

        StepVerifier.create(returned)
            .expectError(AddressNotFound::class.java)
            .verify()
    }

    @Test
    fun getAddressById() {
        `when`(ipRepository.findById(any(UUID::class.java))).thenReturn(Mono.just(ip))

        val returned = addressService.getAddressById(UUID.randomUUID())

        verify(ipRepository, times(1)).findById(any(UUID::class.java))
        verifyNoMoreInteractions(ipRepository)

        StepVerifier.create(returned)
            .assertNext { assertThat(it.address).isEqualTo("127.0.0.1") }
            .verifyComplete()
    }

    @Test
    fun getAddressByAddress_thenNotFound() {
        `when`(ipRepository.findByAddress(anyString())).thenReturn(Mono.empty())

        val returned = addressService.getAddress("test")

        verify(ipRepository, times(1)).findByAddress(anyString())
        verifyNoMoreInteractions(ipRepository)

        StepVerifier.create(returned)
            .expectError(AddressNotFound::class.java)
            .verify()
    }

    @Test
    fun getAddressByAddress() {
        `when`(ipRepository.findByAddress(anyString())).thenReturn(Mono.just(ip))

        val returned = addressService.getAddress("test")

        verify(ipRepository, times(1)).findByAddress(anyString())
        verifyNoMoreInteractions(ipRepository)

        StepVerifier.create(returned)
            .assertNext { assertThat(it.address).isEqualTo("127.0.0.1") }
            .verifyComplete()
    }

    @Test
    fun getAddresses() {
        `when`(ipRepository.findAll()).thenReturn(Flux.just(ip))

        val returned = addressService.getAddresses()

        verify(ipRepository, times(1)).findAll()
        verifyNoMoreInteractions(ipRepository)

        StepVerifier.create(returned)
            .assertNext {
                assertThat(it.address).isEqualTo("127.0.0.1")
            }
            .verifyComplete()
    }

    @Test
    fun deleteAddress_thenNotFound() {}

    @Test
    fun deleteAddress() {}
}