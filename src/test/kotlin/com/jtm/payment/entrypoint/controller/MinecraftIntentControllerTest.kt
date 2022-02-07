package com.jtm.payment.entrypoint.controller

import com.jtm.payment.core.domain.model.PluginIntent
import com.jtm.payment.data.service.MinecraftIntentService
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
import java.util.*

@RunWith(SpringRunner::class)
@WebFluxTest(MinecraftIntentController::class)
@AutoConfigureWebTestClient
class MinecraftIntentControllerTest {

    @Autowired
    lateinit var testClient: WebTestClient

    @MockBean
    lateinit var intentService: MinecraftIntentService

    @Test
    fun postPluginIntent() {
        `when`(intentService.createPluginIntent(anyOrNull(), anyOrNull())).thenReturn(Mono.just("test"))

        testClient.post()
            .uri("/mc/intent/plugin")
            .bodyValue(PluginIntent(25.0, "gbp", listOf(UUID.randomUUID())))
            .exchange()
            .expectStatus().isOk

        verify(intentService, times(1)).createPluginIntent(anyOrNull(), anyOrNull())
        verifyNoMoreInteractions(intentService)
    }
}