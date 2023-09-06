/*
 * Copyright 2019 Thunderberry.
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
package com.alibaba.cloud.mtls.ssl.trustmanager.trustoptions;

import javax.net.ssl.CertPathTrustManagerParameters;
import java.security.cert.TrustAnchor;
import java.util.Set;

/**
 * @author Hakan Altindag
 */
@FunctionalInterface
public interface TrustAnchorTrustOptions<R extends CertPathTrustManagerParameters> extends TrustOptions<Set<TrustAnchor>, R> {

    @Override
    R apply(Set<TrustAnchor> trustAnchors) throws Exception;

}
