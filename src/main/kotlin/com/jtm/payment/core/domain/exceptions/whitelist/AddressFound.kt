package com.jtm.payment.core.domain.exceptions.whitelist

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(value = HttpStatus.FOUND, reason = "Address already found.")
class AddressFound: RuntimeException()