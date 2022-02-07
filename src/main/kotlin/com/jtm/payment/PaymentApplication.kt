package com.jtm.payment

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient

@EnableDiscoveryClient
@SpringBootApplication
open class PaymentApplication

fun main(args: Array<String>) {
    runApplication<PaymentApplication>(*args)
}