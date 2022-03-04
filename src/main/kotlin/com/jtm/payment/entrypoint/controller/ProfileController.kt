package com.jtm.payment.entrypoint.controller

import com.jtm.payment.core.domain.dto.BasicInfoDto
import com.jtm.payment.core.domain.entity.PaymentProfile
import com.jtm.payment.data.service.ProfileService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/profile")
class ProfileController @Autowired constructor(private val profileService: ProfileService) {

    @PostMapping("/create")
    fun postProfile(request: ServerHttpRequest, @RequestBody dto: BasicInfoDto): Mono<PaymentProfile> {
        return profileService.createProfile(request, dto)
    }

    @GetMapping("/me")
    fun getProfile(request: ServerHttpRequest): Mono<PaymentProfile> {
        return profileService.getProfile(request)
    }

    @GetMapping("/all")
    fun getProfiles(): Flux<PaymentProfile> {
        return profileService.getProfiles()
    }

    @DeleteMapping
    fun deleteProfile(@RequestParam("id") id: String): Mono<PaymentProfile> {
        return profileService.removeProfile(id)
    }
}