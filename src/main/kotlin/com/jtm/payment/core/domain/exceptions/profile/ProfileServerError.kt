package com.jtm.payment.core.domain.exceptions.profile

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Profile server failed to process request.")
class ProfileServerError: RuntimeException()