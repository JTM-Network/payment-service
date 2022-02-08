package com.jtm.payment.entrypoint.controller

import com.jtm.payment.data.service.MinecraftHookService
import com.stripe.model.Event
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/mc/hook")
class MinecraftHookController @Autowired constructor(private val hookService: MinecraftHookService) {

    @PostMapping("/plugin")
    fun confirmAccess(@RequestBody event: Event): Mono<Void> = hookService.addAccess(event)
}