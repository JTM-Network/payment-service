package com.jtm.payment.core.domain.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(value = HttpStatus.FOUND, reason = "Payment profile already found.")
class PaymentProfileFound: RuntimeException()