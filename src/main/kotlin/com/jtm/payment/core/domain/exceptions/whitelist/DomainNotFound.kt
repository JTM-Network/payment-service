package com.jtm.payment.core.domain.exceptions.whitelist

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Domain not found.")
class DomainNotFound: RuntimeException()