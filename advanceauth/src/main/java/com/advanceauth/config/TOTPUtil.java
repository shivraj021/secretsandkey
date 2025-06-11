package com.advanceauth.config;

import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import org.springframework.stereotype.Component;

@Component
public class TOTPUtil {

	private final GoogleAuthenticator gAuth = new GoogleAuthenticator();

	public String generateSecretKey() {
		GoogleAuthenticatorKey key = gAuth.createCredentials();
		return key.getKey();
	}

	public boolean verifyCode(String secret, int code) {
		return gAuth.authorize(secret, code);
	}

	public String getQRBarcodeURL(String user, String secret) {
		return "otpauth://totp/AdvancedAuth:" + user + "?secret=" + secret + "&issuer=AdvancedAuth";
	}
}
