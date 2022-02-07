package com.jtm.payment.core.domain.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Failed to create payment intent.")
class FailedPaymentIntent: RuntimeException()