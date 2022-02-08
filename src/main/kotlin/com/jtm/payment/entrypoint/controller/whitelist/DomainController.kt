package com.jtm.payment.entrypoint.controller.whitelist

import com.jtm.payment.core.domain.dto.DomainDto
import com.jtm.payment.core.domain.entity.DomainWhitelist
import com.jtm.payment.data.service.whitelist.DomainService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.*

@RestController
@RequestMapping("/domain")
class DomainController @Autowired constructor(private val domainService: DomainService) {

    @PostMapping
    fun postDomain(@RequestBody dto: DomainDto): Mono<DomainWhitelist> {
        return domainService.addDomain(dto)
    }

    @PutMapping("/{id}")
    fun putDomain(@PathVariable id: UUID, @RequestBody dto: DomainDto): Mono<DomainWhitelist> {
        return domainService.updateDomain(id, dto)
    }

    @GetMapping("/{id}")
    fun getDomain(@PathVariable id: UUID): Mono<DomainWhitelist> {
        return domainService.getDomainById(id)
    }

    @GetMapping
    fun getDomainByDomain(@RequestParam("value") domain: String): Mono<DomainWhitelist> {
        return domainService.getDomain(domain)
    }

    @GetMapping("/all")
    fun getDomains(): Flux<DomainWhitelist> {
        return domainService.getDomains()
    }

    @DeleteMapping("/{id}")
    fun deleteDomain(@PathVariable id: UUID): Mono<DomainWhitelist> {
        return domainService.deleteDomain(id)
    }
}