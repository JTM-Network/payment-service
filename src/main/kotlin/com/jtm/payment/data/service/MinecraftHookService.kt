package com.jtm.payment.data.service

import com.jtm.payment.core.usecase.profile.ProfileUpdater
import com.stripe.model.Event
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class MinecraftHookService @Autowired constructor(private val profileUpdater: ProfileUpdater) {

    fun addAccess(event: Event): Mono<Void> {
        return profileUpdater.addAccess(event)
    }
}