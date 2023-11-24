/*
 * Copyright 2021 spring-boot-extension the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.livk.sso.commons.util;

import com.nimbusds.jose.jwk.RSAKey;
import lombok.experimental.UtilityClass;
import org.springframework.core.io.Resource;

import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

/**
 * <p>
 * RSAUtils
 * </p>
 *
 * @author livk
 */
@UtilityClass
public class RSAUtils {

	public RSAKey rsaKey(Resource resource, String password, String alise) {
		KeyPair keyPair = new KeyStoreKeyFactory(resource, password.toCharArray()).getKeyPair(alise);
		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
		RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
		return new RSAKey.Builder(publicKey).privateKey(privateKey).build();
	}

}
