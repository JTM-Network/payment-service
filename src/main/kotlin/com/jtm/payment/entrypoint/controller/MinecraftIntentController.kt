package com.jtm.payment.entrypoint.controller

import com.jtm.payment.core.domain.model.PluginIntent
import com.jtm.payment.data.service.MinecraftIntentService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/mc/intent")
class MinecraftIntentController @Autowired constructor(private val intentService: MinecraftIntentService) {

    @PostMapping("/plugin")
    fun postPluginIntent(request: ServerHttpRequest, @RequestBody intent: PluginIntent): Mono<String> {
        return intentService.createPluginIntent(request, intent)
    }
}