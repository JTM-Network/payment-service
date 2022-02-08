package com.jtm.payment.entrypoint.controller.whitelist

import com.jtm.payment.core.domain.dto.DomainDto
import com.jtm.payment.core.domain.entity.DomainWhitelist
import com.jtm.payment.data.service.whitelist.DomainService
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
import java.util.*

@RunWith(SpringRunner::class)
@WebFluxTest(DomainController::class)
@AutoConfigureWebTestClient
class DomainControllerTest {

    @Autowired
    lateinit var testClient: WebTestClient

    @MockBean
    lateinit var domainService: DomainService

    private val domain = DomainWhitelist(domain = "www.jtm-network.com")
    private val dto = DomainDto("www.jtm-network.com")

    @Test
    fun postDomain() {
        `when`(domainService.addDomain(anyOrNull())).thenReturn(Mono.just(domain))

        testClient.post()
            .uri("/domain")
            .bodyValue(dto)
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$.domain").isEqualTo("www.jtm-network.com")

        verify(domainService, times(1)).addDomain(anyOrNull())
        verifyNoMoreInteractions(domainService)
    }

    @Test
    fun putDomain() {
        `when`(domainService.updateDomain(anyOrNull(), anyOrNull())).thenReturn(Mono.just(domain))

        testClient.put()
            .uri("/domain/${UUID.randomUUID()}")
            .bodyValue(dto)
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$.domain").isEqualTo("www.jtm-network.com")

        verify(domainService, times(1)).updateDomain(anyOrNull(), anyOrNull())
        verifyNoMoreInteractions(domainService)
    }

    @Test
    fun getDomain() {
        `when`(domainService.getDomainById(anyOrNull())).thenReturn(Mono.just(domain))

        testClient.get()
            .uri("/domain/${UUID.randomUUID()}")
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$.domain").isEqualTo("www.jtm-network.com")

        verify(domainService, times(1)).getDomainById(anyOrNull())
        verifyNoMoreInteractions(domainService)
    }

    @Test
    fun getDomainByDomain() {
        `when`(domainService.getDomain(anyString())).thenReturn(Mono.just(domain))

        testClient.get()
            .uri("/domain?value=test")
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$.domain").isEqualTo("www.jtm-network.com")

        verify(domainService, times(1)).getDomain(anyString())
        verifyNoMoreInteractions(domainService)
    }

    @Test
    fun getDomains() {
        `when`(domainService.getDomains()).thenReturn(Flux.just(domain))

        testClient.get()
            .uri("/domain/all")
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$[0].domain").isEqualTo("www.jtm-network.com")

        verify(domainService, times(1)).getDomains()
        verifyNoMoreInteractions(domainService)
    }

    @Test
    fun deleteDomain() {
        `when`(domainService.deleteDomain(anyOrNull())).thenReturn(Mono.just(domain))

        testClient.delete()
            .uri("/domain/${UUID.randomUUID()}")
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$.domain").isEqualTo("www.jtm-network.com")

        verify(domainService, times(1)).deleteDomain(anyOrNull())
        verifyNoMoreInteractions(domainService)
    }
}