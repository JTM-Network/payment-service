package com.jtm.payment.entrypoint.controller

import com.jtm.payment.data.service.MinecraftHookService
import com.stripe.model.Event
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.`when`
import org.mockito.Mockito.times
import org.mockito.kotlin.anyOrNull
import org.mockito.kotlin.mock
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
@WebFluxTest(MinecraftHookController::class)
@AutoConfigureWebTestClient
class MinecraftHookControllerTest {

    @Autowired
    lateinit var testClient: WebTestClient

    @MockBean
    lateinit var hookService: MinecraftHookService

    private val event: Event = mock()

    @Test
    fun confirmAccess() {
        `when`(hookService.addAccess(event)).thenReturn(Mono.empty())

        testClient.post()
            .uri("/mc/hook/plugin")
            .bodyValue(event)
            .exchange()
            .expectStatus().isOk

        verify(hookService, times(1)).addAccess(anyOrNull())
        verifyNoMoreInteractions(hookService)
    }
}