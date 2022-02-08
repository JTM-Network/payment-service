package com.jtm.payment.entrypoint.controller.whitelist

import com.jtm.payment.core.domain.dto.AddressDto
import com.jtm.payment.core.domain.entity.IPWhitelist
import com.jtm.payment.data.service.whitelist.AddressService
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.`when`
import org.mockito.Mockito.times
import org.mockito.kotlin.anyOrNull
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import java.util.*

@RunWith(SpringRunner::class)
@WebFluxTest(AddressController::class)
@AutoConfigureWebTestClient
class AddressControllerTest {

    @Autowired
    lateinit var testClient: WebTestClient

    @MockBean
    lateinit var addressService: AddressService

    private val address = IPWhitelist(address = "localhost")
    private val dto = AddressDto("localhost")

    @Test
    fun postAddress() {
        `when`(addressService.addAddress(anyOrNull())).thenReturn(Mono.just(address))

        testClient.post()
            .uri("/address")
            .bodyValue(dto)
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$.address").isEqualTo("localhost")

        verify(addressService, times(1)).addAddress(anyOrNull())
        verifyNoMoreInteractions(addressService)
    }

    @Test
    fun putAddress() {
        `when`(addressService.updateAddress(anyOrNull(), anyOrNull())).thenReturn(Mono.just(address))

        testClient.put()
            .uri("/address/${UUID.randomUUID()}")
            .bodyValue(dto)
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$.address").isEqualTo("localhost")

        verify(addressService, times(1)).updateAddress(anyOrNull(), anyOrNull())
        verifyNoMoreInteractions(addressService)
    }

    @Test
    fun getAddress() {
        `when`(addressService.getAddressById(anyOrNull())).thenReturn(Mono.just(address))

        testClient.get()
            .uri("/address/${UUID.randomUUID()}")
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$.address").isEqualTo("localhost")

        verify(addressService, times(1)).getAddressById(anyOrNull())
        verifyNoMoreInteractions(addressService)
    }

    @Test
    fun getAddressByAddress() {
        `when`(addressService.getAddress(anyString())).thenReturn(Mono.just(address))

        testClient.get()
            .uri("/address?value=test")
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$.address").isEqualTo("localhost")

        verify(addressService, times(1)).getAddress(anyString())
        verifyNoMoreInteractions(addressService)
    }

    @Test
    fun getAddresses() {
        `when`(addressService.getAddresses()).thenReturn(Flux.just(address))

        testClient.get()
            .uri("/address/all")
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$[0].address").isEqualTo("localhost")

        verify(addressService, times(1)).getAddresses()
        verifyNoMoreInteractions(addressService)
    }

    @Test
    fun deleteAddress() {
        `when`(addressService.deleteAddress(anyOrNull())).thenReturn(Mono.just(address))

        testClient.delete()
            .uri("/address/${UUID.randomUUID()}")
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$.address").isEqualTo("localhost")

        verify(addressService, times(1)).deleteAddress(anyOrNull())
        verifyNoMoreInteractions(addressService)
    }
}