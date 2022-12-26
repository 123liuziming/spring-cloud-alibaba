/*
 * Copyright 2013-2018 the original author or authors.
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

package com.alibaba.cloud.governance.istio.sds;

import com.alibaba.cloud.governance.istio.XdsChannel;
import io.grpc.stub.StreamObserver;
import istio.v1.auth.Ca;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IstioCertificateService {

	private static final Logger log = LoggerFactory.getLogger(IstioCertificateService.class);

	private XdsChannel channel;

	private CSRGenerator csrGenerator;

	public IstioCertificateService(XdsChannel channel, CSRGenerator csrGenerator) {
		this.channel = channel;
		this.csrGenerator = csrGenerator;
	}

	public void sendCSR() {
		channel.createCertificateSignRequest(buildCSR(10000),
				new StreamObserver<Ca.IstioCertificateResponse>() {
					@Override
					public void onNext(
							Ca.IstioCertificateResponse istioCertificateResponse) {

					}

					@Override
					public void onError(Throwable throwable) {
						log.error("Failed to connect to Istio CA", throwable);
					}

					@Override
					public void onCompleted() {
						log.info("The connection to Istio CA is completed");
					}
				});
	}

	private Ca.IstioCertificateRequest buildCSR(long validityDuration) {
		Ca.IstioCertificateRequest.Builder builder = Ca.IstioCertificateRequest
				.newBuilder();
		return builder.setValidityDuration(validityDuration)
				.setCsr(csrGenerator.getCSR("test")).build();
	}

}
