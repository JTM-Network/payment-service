package com.jtm.payment.core.domain.exceptions.profile

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Failed to deserialize event.")
class FailedDeserialization: RuntimeException()