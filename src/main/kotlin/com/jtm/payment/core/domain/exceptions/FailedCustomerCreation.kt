package com.jtm.payment.core.domain.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Failed to create your customer profile.")
class FailedCustomerCreation: RuntimeException()