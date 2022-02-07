package com.jtm.payment.core.domain.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Client identifier not found.")
class ClientIdNotFound: RuntimeException()