package com.jtm.payment.entrypoint.controller

import com.jtm.payment.core.domain.dto.BasicInfoDto
import com.jtm.payment.core.domain.entity.PaymentProfile
import com.jtm.payment.data.service.ProfileService
import org.junit.Test
import org.junit.runner.RunWith
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
import reactor.core.publisher.Mono

@RunWith(SpringRunner::class)
@WebFluxTest(ProfileController::class)
@AutoConfigureWebTestClient
class ProfileControllerTest {

    @Autowired
    lateinit var testClient: WebTestClient

    @MockBean
    lateinit var profileService: ProfileService

    private val profile = PaymentProfile("id", "stripeId")
    private val dto = BasicInfoDto("13 Test road", "", "London", "UK", null, "BR1")

    @Test
    fun postProfile() {
        `when`(profileService.createProfile(anyOrNull(), anyOrNull())).thenReturn(Mono.just(profile))

        testClient.post()
            .uri("/profile/create")
            .bodyValue(dto)
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$.id").isEqualTo("id")
            .jsonPath("$.stripeId").isEqualTo("stripeId")

        verify(profileService, times(1)).createProfile(anyOrNull(), anyOrNull())
        verifyNoMoreInteractions(profileService)
    }

    @Test
    fun getProfile() {
        `when`(profileService.getProfile(anyOrNull())).thenReturn(Mono.just(profile))

        testClient.get()
            .uri("/profile/me")
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$.id").isEqualTo("id")
            .jsonPath("$.stripeId").isEqualTo("stripeId")

        verify(profileService, times(1)).getProfile(anyOrNull())
        verifyNoMoreInteractions(profileService)
    }
}