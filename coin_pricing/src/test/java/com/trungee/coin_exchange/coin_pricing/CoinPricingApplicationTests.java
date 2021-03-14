package com.trungee.coin_exchange.coin_pricing;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;

@SpringBootTest
@Import(TestChannelBinderConfiguration.class)
class CoinPricingApplicationTests {

	@Test
	void contextLoads() {
	}

}
