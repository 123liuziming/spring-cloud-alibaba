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
package com.alibaba.cloud.mtls.ssl.util;

import com.alibaba.cloud.mtls.ssl.hostnameverifier.BasicHostnameVerifier;
import com.alibaba.cloud.mtls.ssl.hostnameverifier.EnhanceableHostnameVerifier;
import com.alibaba.cloud.mtls.ssl.hostnameverifier.FenixHostnameVerifier;
import com.alibaba.cloud.mtls.ssl.hostnameverifier.UnsafeHostnameVerifier;
import com.alibaba.cloud.mtls.ssl.model.HostnameVerifierParameters;

import javax.net.ssl.HostnameVerifier;
import java.util.function.Predicate;

/**
 * @author Hakan Altindag
 */
public final class HostnameVerifierUtils {

    private HostnameVerifierUtils() {}

    /**
     * Creates a basic hostname verifier which validates the hostname against the peer host from the ssl session.
     * This basic hostname verifier provides minimal security. It is recommended to use {@link HostnameVerifierUtils#createFenix()}
     */
    public static HostnameVerifier createBasic() {
        return BasicHostnameVerifier.getInstance();
    }

    /**
     * Creates an unsafe hostname verifier which does not validate the hostname at all.
     * This hostname verifier is unsafe and should be avoided
     */
    public static HostnameVerifier createUnsafe() {
        return UnsafeHostnameVerifier.getInstance();
    }

    /**
     * Creates a fenix hostname verifier which validates the hostname against the SAN field of the peer certificate.
     */
    @Deprecated
    public static HostnameVerifier createFenix() {
        return createDefault();
    }

    /**
     * Creates the default hostname verifier which is able to validate the hostname against the SAN field of the peer certificate.
     */
    public static HostnameVerifier createDefault() {
        return FenixHostnameVerifier.getInstance();
    }

    public static HostnameVerifier createEnhanceable(HostnameVerifier baseHostnameVerifier, Predicate<HostnameVerifierParameters> hostnameVerifierParametersValidator) {
        return new EnhanceableHostnameVerifier(baseHostnameVerifier, hostnameVerifierParametersValidator);
    }

}
