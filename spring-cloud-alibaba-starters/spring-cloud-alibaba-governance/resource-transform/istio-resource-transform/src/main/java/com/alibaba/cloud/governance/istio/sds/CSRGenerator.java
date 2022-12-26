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

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.bouncycastle.pkcs.jcajce.JcaPKCS10CertificationRequestBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CSRGenerator {

	private static final Logger log = LoggerFactory.getLogger(CSRGenerator.class);

	private PublicKey publicKey;

	private PrivateKey privateKey;

	public String getCSR(String cn) {
		try {
			KeyPairGenerator localKeyPairGenerator = KeyPairGenerator.getInstance("RSA");
			localKeyPairGenerator.initialize(2048);
			KeyPair localKeyPair = localKeyPairGenerator.genKeyPair();
			X500NameBuilder localX500NameBuilder = new X500NameBuilder(BCStyle.INSTANCE);
			localX500NameBuilder.addRDN(BCStyle.CN, cn);
			X500Name localX500Name = localX500NameBuilder.build();
			JcaPKCS10CertificationRequestBuilder p10Builder = new JcaPKCS10CertificationRequestBuilder(
					localX500Name, localKeyPair.getPublic());
			JcaContentSignerBuilder csBuilder = new JcaContentSignerBuilder(
					"SHA256withRSA");
			ContentSigner signer = csBuilder.build(localKeyPair.getPrivate());
			PKCS10CertificationRequest csr = p10Builder.build(signer);
			this.publicKey = localKeyPair.getPublic();
			this.privateKey = localKeyPair.getPrivate();
			return "-----BEGIN CERTIFICATE REQUEST-----\n"
					+ new String(Base64.getEncoder().encode(csr.getEncoded()))
					+ "\n-----END CERTIFICATE REQUEST-----\n";
		}
		catch (Exception e) {
			log.error("Unable to create CSR ", e);
			return "";
		}
	}

	public PrivateKey getPrivateKey() {
		return privateKey;
	}

	public PublicKey getPublicKey() {
		return publicKey;
	}

}
