/*
 * Copyright 2013-2023 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.alibaba.cloud.nacos.discovery;

import com.alibaba.cloud.nacos.NacosServiceAutoConfiguration;
import com.alibaba.cloud.nacos.registry.NacosServiceRegistryAutoConfiguration;
import com.alibaba.cloud.nacos.util.UtilIPv6AutoConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.cloud.client.serviceregistry.AutoServiceRegistrationConfiguration;
import org.springframework.cloud.commons.util.UtilAutoConfiguration;
import org.springframework.cloud.netflix.zuul.ZuulProxyMarkerConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

/**
 * @author <a href="mailto:echooy.mxq@gmail.com">echooymxq</a>
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = NacosDiscoveryHeartBeatConfigurationTest.TestConfig.class,
		webEnvironment = RANDOM_PORT)
public class NacosDiscoveryHeartBeatConfigurationTest {

	private ApplicationContextRunner contextRunner = new ApplicationContextRunner()
			.withConfiguration(
					AutoConfigurations.of(AutoServiceRegistrationConfiguration.class,
							NacosServiceRegistryAutoConfiguration.class,
							UtilAutoConfiguration.class, UtilIPv6AutoConfiguration.class,
							NacosServiceAutoConfiguration.class,
							NacosDiscoveryAutoConfiguration.class,
							NacosDiscoveryClientConfiguration.class,
							NacosDiscoveryHeartBeatConfiguration.class, this.getClass()));

	@Test
	public void testDefaultNacosDiscoveryHeartBeatPublisher() {
		contextRunner.run(context -> assertThat(context)
				.doesNotHaveBean(NacosDiscoveryHeartBeatPublisher.class));
	}

	@Test
	public void testNacosDiscoveryHeartBeatPublisherEnabledForGateway() {
		contextRunner
				.withPropertyValues("spring.cloud.gateway.discovery.locator.enabled=true")
				.run(context -> assertThat(context)
						.hasSingleBean(NacosDiscoveryHeartBeatPublisher.class));
	}

	@Test
	public void testNacosDiscoveryHeartBeatPublisherEnabledForProperties() {
		contextRunner
				.withPropertyValues(
						"spring.cloud.nacos.discovery.heart-beat.enabled=true")
				.run(context -> assertThat(context)
						.hasSingleBean(NacosDiscoveryHeartBeatPublisher.class));
	}

	@Test
	public void testZuulGatewayLocatorHeartBeatPublisherEnabled() {
		contextRunner
				.withConfiguration(
						AutoConfigurations.of(ZuulProxyMarkerConfiguration.class))
				.run(context -> assertThat(context)
						.hasSingleBean(NacosDiscoveryHeartBeatPublisher.class));
	}

	@Test
	public void testZuulAndSpringCloudGatewayLocatorHeartBeatPublisherEnabled() {
		contextRunner
				.withPropertyValues("spring.cloud.gateway.discovery.locator.enabled=true")
				.withConfiguration(
						AutoConfigurations.of(ZuulProxyMarkerConfiguration.class))
				.run(context -> assertThat(context)
						.hasSingleBean(NacosDiscoveryHeartBeatPublisher.class));
	}

	@Configuration
	@EnableAutoConfiguration
	@ImportAutoConfiguration({ NacosDiscoveryAutoConfiguration.class,
			NacosDiscoveryHeartBeatConfiguration.class })
	public static class TestConfig {

	}

}