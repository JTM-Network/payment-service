package com.jtm.payment

import com.jtm.payment.data.service.MinecraftIntentServiceTest
import com.jtm.payment.data.service.ProfileServiceTest
import com.jtm.payment.data.service.whitelist.AddressServiceTest
import com.jtm.payment.data.service.whitelist.DomainServiceTest
import com.jtm.payment.entrypoint.controller.MinecraftIntentControllerTest
import com.jtm.payment.entrypoint.controller.ProfileControllerTest
import com.jtm.payment.entrypoint.controller.whitelist.AddressControllerTest
import com.jtm.payment.entrypoint.controller.whitelist.DomainControllerTest
import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(value = [
    MinecraftIntentServiceTest::class,
    MinecraftIntentControllerTest::class,

    ProfileServiceTest::class,
    ProfileControllerTest::class,

    AddressServiceTest::class,
    AddressControllerTest::class,

    DomainServiceTest::class,
    DomainControllerTest::class
])
class PaymentApplicationTestSuite