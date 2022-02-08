package com.jtm.payment.data.service.whitelist

import com.jtm.payment.core.domain.dto.DomainDto
import com.jtm.payment.core.domain.entity.DomainWhitelist
import com.jtm.payment.core.domain.exceptions.whitelist.DomainFound
import com.jtm.payment.core.domain.exceptions.whitelist.DomainNotFound
import com.jtm.payment.core.usecase.repository.DomainWhitelistRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import org.mockito.kotlin.anyOrNull
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.springframework.test.context.junit4.SpringRunner
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import java.util.*

@RunWith(SpringRunner::class)
class DomainServiceTest {

    private val domainRepository: DomainWhitelistRepository = mock()
    private val domainService = DomainService(domainRepository)
    private val domain = DomainWhitelist(domain = "www.jtm-network.com")
    private val dto = DomainDto("jty.com")

    @Test
    fun addDomain_thenFound() {
        `when`(domainRepository.findByDomain(anyString())).thenReturn(Mono.just(domain))

        val returned = domainService.addDomain(dto)

        verify(domainRepository, times(1)).findByDomain(anyString())
        verifyNoMoreInteractions(domainRepository)

        StepVerifier.create(returned)
            .expectError(DomainFound::class.java)
            .verify()
    }

    @Test
    fun addDomain() {
        `when`(domainRepository.findByDomain(anyString())).thenReturn(Mono.empty())
        `when`(domainRepository.save(anyOrNull())).thenReturn(Mono.just(domain))

        val returned = domainService.addDomain(dto)

        verify(domainRepository, times(1)).findByDomain(anyString())
        verifyNoMoreInteractions(domainRepository)

        StepVerifier.create(returned)
            .assertNext {
                assertThat(it.domain).isEqualTo("www.jtm-network.com")
                assertThat(it.updated).isEqualTo(domain.updated)
            }
            .verifyComplete()
    }

    @Test
    fun updateDomain_thenNotFound() {
        `when`(domainRepository.findById(any(UUID::class.java))).thenReturn(Mono.empty())

        val returned = domainService.updateDomain(UUID.randomUUID(), dto)

        verify(domainRepository, times(1)).findById(any(UUID::class.java))
        verifyNoMoreInteractions(domainRepository)

        StepVerifier.create(returned)
            .expectError(DomainNotFound::class.java)
            .verify()
    }

    @Test
    fun updateDomain() {
        `when`(domainRepository.findById(any(UUID::class.java))).thenReturn(Mono.just(domain))
        `when`(domainRepository.save(anyOrNull())).thenReturn(Mono.just(domain))

        val returned = domainService.updateDomain(UUID.randomUUID(), dto)

        verify(domainRepository, times(1)).findById(any(UUID::class.java))
        verifyNoMoreInteractions(domainRepository)

        StepVerifier.create(returned)
            .assertNext {
                assertThat(it.domain).isEqualTo("jty.com")
            }
            .verifyComplete()
    }

    @Test
    fun getDomain_thenNotFound() {
        `when`(domainRepository.findById(any(UUID::class.java))).thenReturn(Mono.empty())

        val returned = domainService.getDomainById(UUID.randomUUID())

        verify(domainRepository, times(1)).findById(any(UUID::class.java))
        verifyNoMoreInteractions(domainRepository)

        StepVerifier.create(returned)
            .expectError(DomainNotFound::class.java)
            .verify()
    }

    @Test
    fun getDomain() {
        `when`(domainRepository.findById(any(UUID::class.java))).thenReturn(Mono.just(domain))

        val returned = domainService.getDomainById(UUID.randomUUID())

        verify(domainRepository, times(1)).findById(any(UUID::class.java))
        verifyNoMoreInteractions(domainRepository)

        StepVerifier.create(returned)
            .assertNext { assertThat(it.domain).isEqualTo("www.jtm-network.com") }
            .verifyComplete()
    }

    @Test
    fun getDomainByDomain_thenNotFound() {
        `when`(domainRepository.findByDomain(anyString())).thenReturn(Mono.empty())

        val returned = domainService.getDomain("test")

        verify(domainRepository, times(1)).findByDomain(anyString())
        verifyNoMoreInteractions(domainRepository)

        StepVerifier.create(returned)
            .expectError(DomainNotFound::class.java)
            .verify()
    }

    @Test
    fun getDomainByDomain() {
        `when`(domainRepository.findByDomain(anyString())).thenReturn(Mono.just(domain))

        val returned = domainService.getDomain("test")

        verify(domainRepository, times(1)).findByDomain(anyString())
        verifyNoMoreInteractions(domainRepository)

        StepVerifier.create(returned)
            .assertNext { assertThat(it.domain).isEqualTo("www.jtm-network.com") }
            .verifyComplete()
    }

    @Test
    fun getDomains() {
        `when`(domainRepository.findAll()).thenReturn(Flux.just(domain))

        val returned = domainService.getDomains()

        verify(domainRepository, times(1)).findAll()
        verifyNoMoreInteractions(domainRepository)

        StepVerifier.create(returned)
            .assertNext { assertThat(it.domain).isEqualTo("www.jtm-network.com") }
            .verifyComplete()
    }

    @Test
    fun deleteDomain_thenNotFound() {
        `when`(domainRepository.findById(any(UUID::class.java))).thenReturn(Mono.empty())

        val returned = domainService.deleteDomain(UUID.randomUUID())

        verify(domainRepository, times(1)).findById(any(UUID::class.java))
        verifyNoMoreInteractions(domainRepository)

        StepVerifier.create(returned)
            .expectError(DomainNotFound::class.java)
            .verify()
    }

    @Test
    fun deleteDomain() {
        `when`(domainRepository.findById(any(UUID::class.java))).thenReturn(Mono.just(domain))
        `when`(domainRepository.delete(anyOrNull())).thenReturn(Mono.empty())

        val returned = domainService.deleteDomain(UUID.randomUUID())

        verify(domainRepository, times(1)).findById(any(UUID::class.java))
        verifyNoMoreInteractions(domainRepository)

        StepVerifier.create(returned)
            .assertNext { assertThat(it.domain).isEqualTo("www.jtm-network.com") }
            .verifyComplete()
    }
}