package com.jtm.payment

import com.jtm.payment.data.service.MinecraftIntentServiceTest
import com.jtm.payment.entrypoint.controller.MinecraftIntentControllerTest
import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(value = [
    MinecraftIntentServiceTest::class,
    MinecraftIntentControllerTest::class,
])
class PaymentApplicationTestSuite