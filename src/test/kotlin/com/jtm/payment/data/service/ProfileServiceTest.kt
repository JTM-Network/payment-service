package com.jtm.payment.data.service

import com.jtm.payment.core.domain.dto.BasicInfoDto
import com.jtm.payment.core.domain.entity.PaymentProfile
import com.jtm.payment.core.domain.exceptions.ClientIdNotFound
import com.jtm.payment.core.domain.exceptions.FailedCustomerCreation
import com.jtm.payment.core.domain.exceptions.PaymentProfileFound
import com.jtm.payment.core.domain.exceptions.PaymentProfileNotFound
import com.jtm.payment.core.usecase.provider.StripeCustomerProvider
import com.jtm.payment.core.usecase.provider.StripePaymentProvider
import com.jtm.payment.core.usecase.repository.PaymentProfileRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import org.mockito.kotlin.anyOrNull
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.springframework.http.HttpHeaders
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.test.context.junit4.SpringRunner
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

@RunWith(SpringRunner::class)
class ProfileServiceTest {

    private val profileRepository: PaymentProfileRepository = mock()
    private val customerProvider: StripeCustomerProvider = mock()
    private val profileService = ProfileService(profileRepository, customerProvider)

    private val profile = PaymentProfile("test", "stripeId")
    private val infoDto = BasicInfoDto("13 Test road", "", "London", "UK", null, "BR1")
    private val request: ServerHttpRequest = mock()
    private val headers: HttpHeaders = mock()

    @Before
    fun setup() {
        `when`(request.headers).thenReturn(headers)
        `when`(headers.getFirst(anyString())).thenReturn("Client id")
    }

    @Test
    fun createProfile_thenClientIdNotFound() {
        `when`(headers.getFirst(anyString())).thenReturn(null)

        val returned = profileService.createProfile(request, infoDto)

        verify(request, times(1)).headers
        verifyNoMoreInteractions(request)

        verify(headers, times(1)).getFirst(anyString())
        verifyNoMoreInteractions(headers)

        StepVerifier.create(returned)
            .expectError(ClientIdNotFound::class.java)
            .verify()
    }

    @Test
    fun createProfile_thenFound() {
        `when`(profileRepository.findById(anyString())).thenReturn(Mono.just(profile))

        val returned = profileService.createProfile(request, infoDto)

        verify(request, times(1)).headers
        verifyNoMoreInteractions(request)

        verify(headers, times(1)).getFirst(anyString())
        verifyNoMoreInteractions(headers)

        verify(profileRepository, times(1)).findById(anyString())
        verifyNoMoreInteractions(profileRepository)

        StepVerifier.create(returned)
            .expectError(PaymentProfileFound::class.java)
            .verify()
    }

    @Test
    fun createProfile_thenFailedCustomerCreation() {
        `when`(profileRepository.findById(anyString())).thenReturn(Mono.empty())
        `when`(customerProvider.createCustomer(anyOrNull(), anyString())).thenReturn(null)

        val returned = profileService.createProfile(request, infoDto)

        verify(request, times(1)).headers
        verifyNoMoreInteractions(request)

        verify(headers, times(1)).getFirst(anyString())
        verifyNoMoreInteractions(headers)

        verify(profileRepository, times(1)).findById(anyString())
        verifyNoMoreInteractions(profileRepository)

        StepVerifier.create(returned)
            .expectError(FailedCustomerCreation::class.java)
            .verify()
    }

    @Test
    fun createProfile() {
        `when`(profileRepository.findById(anyString())).thenReturn(Mono.empty())
        `when`(customerProvider.createCustomer(anyOrNull(), anyString())).thenReturn("stripeId")
        `when`(profileRepository.save(anyOrNull())).thenReturn(Mono.just(profile))

        val returned = profileService.createProfile(request, infoDto)

        verify(request, times(1)).headers
        verifyNoMoreInteractions(request)

        verify(headers, times(1)).getFirst(anyString())
        verifyNoMoreInteractions(headers)

        verify(profileRepository, times(1)).findById(anyString())
        verifyNoMoreInteractions(profileRepository)

        StepVerifier.create(returned)
            .assertNext {
                assertThat(it.id).isEqualTo(profile.id)
                assertThat(it.stripeId).isEqualTo(profile.stripeId)
            }
            .verifyComplete()
    }

    @Test
    fun getProfile_thenClientIdNotFound() {
        `when`(headers.getFirst(anyString())).thenReturn(null)

        val returned = profileService.getProfile(request)

        verify(request, times(1)).headers
        verifyNoMoreInteractions(request)

        verify(headers, times(1)).getFirst(anyString())
        verifyNoMoreInteractions(headers)

        StepVerifier.create(returned)
            .expectError(ClientIdNotFound::class.java)
            .verify()
    }

    @Test
    fun getProfile_thenNotFound() {
        `when`(profileRepository.findById(anyString())).thenReturn(Mono.empty())

        val returned = profileService.getProfile(request)

        verify(request, times(1)).headers
        verifyNoMoreInteractions(request)

        verify(headers, times(1)).getFirst(anyString())
        verifyNoMoreInteractions(headers)

        verify(profileRepository, times(1)).findById(anyString())
        verifyNoMoreInteractions(profileRepository)

        StepVerifier.create(returned)
            .expectError(PaymentProfileNotFound::class.java)
            .verify()
    }

    @Test
    fun getProfile() {
        `when`(profileRepository.findById(anyString())).thenReturn(Mono.just(profile))

        val returned = profileService.getProfile(request)

        verify(request, times(1)).headers
        verifyNoMoreInteractions(request)

        verify(headers, times(1)).getFirst(anyString())
        verifyNoMoreInteractions(headers)

        verify(profileRepository, times(1)).findById(anyString())
        verifyNoMoreInteractions(profileRepository)

        StepVerifier.create(returned)
            .assertNext {
                assertThat(it.id).isEqualTo(profile.id)
                assertThat(it.stripeId).isEqualTo(profile.stripeId)
            }
            .verifyComplete()
    }
}