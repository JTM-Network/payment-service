package com.jtm.payment.data.service

import com.jtm.payment.core.domain.exceptions.ClientIdNotFound
import com.jtm.payment.core.domain.exceptions.FailedPaymentIntent
import com.jtm.payment.core.domain.model.PluginIntent
import com.jtm.payment.core.usecase.provider.StripePaymentProvider
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import org.mockito.kotlin.anyOrNull
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions
import org.springframework.http.HttpHeaders
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.test.context.junit4.SpringRunner
import reactor.test.StepVerifier
import java.util.*

@RunWith(SpringRunner::class)
class MinecraftIntentServiceTest {

    private val stripeProvider: StripePaymentProvider = mock()
    private val intentService = MinecraftIntentService(stripeProvider)

    private val intent = PluginIntent(25.0, "gbp", listOf(UUID.randomUUID()))
    private val request: ServerHttpRequest = mock()
    private val headers: HttpHeaders = mock()

    @Before
    fun setup() {
        `when`(request.headers).thenReturn(headers)
        `when`(headers.getFirst(anyString())).thenReturn("Client Id")
    }

    @Test
    fun createPluginIntent_thenClientIdNotFound() {
        `when`(headers.getFirst(anyString())).thenReturn(null)

        val returned = intentService.createPluginIntent(request, intent)

        verify(request, times(1)).headers
        verifyNoMoreInteractions(request)

        verify(headers, times(1)).getFirst(anyString())
        verifyNoMoreInteractions(headers)

        StepVerifier.create(returned)
            .expectError(ClientIdNotFound::class.java)
            .verify()
    }

    @Test
    fun createPluginIntent_thenFailedPaymentIntent() {
        `when`(stripeProvider.createPaymentIntent(anyDouble(), anyString(), anyString(), anyOrNull())).thenReturn(null)

        val returned = intentService.createPluginIntent(request, intent)

        verify(request, times(1)).headers
        verifyNoMoreInteractions(request)

        verify(headers, times(1)).getFirst(anyString())
        verifyNoMoreInteractions(headers)

        verify(stripeProvider, times(1)).createPaymentIntent(anyDouble(), anyString(), anyString(), anyOrNull())
        verifyNoMoreInteractions(stripeProvider)

        StepVerifier.create(returned)
            .expectError(FailedPaymentIntent::class.java)
            .verify()
    }

    @Test
    fun createPluginIntent() {
        `when`(stripeProvider.createPaymentIntent(anyDouble(), anyString(), anyString(), anyOrNull())).thenReturn("secret")

        val returned = intentService.createPluginIntent(request, intent)

        verify(request, times(1)).headers
        verifyNoMoreInteractions(request)

        verify(headers, times(1)).getFirst(anyString())
        verifyNoMoreInteractions(headers)

        verify(stripeProvider, times(1)).createPaymentIntent(anyDouble(), anyString(), anyString(), anyOrNull())
        verifyNoMoreInteractions(stripeProvider)

        StepVerifier.create(returned)
            .assertNext { assertThat(it).isEqualTo("secret") }
            .verifyComplete()
    }
}